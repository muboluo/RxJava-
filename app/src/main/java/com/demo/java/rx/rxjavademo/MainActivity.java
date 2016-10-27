package com.demo.java.rx.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //框架中集成了 RetroLambda 。
        //这个库真是个好东西啊

        //被观察者
        //RxJava最核心的两个东西是Observables（被观察者，事件源）和Subscribers（观察者）。
        // Observables发出一系列事件，
        // Subscribers处理这些事件。
        // 这里的事件可以是任何你感兴趣的东西（触摸事件，web接口调用返回的数据。。。）
        //现在我们有一个 ovservable  要做一个onnext的方法，做这个方法需要一个 T类型的参数。
        //不过 O 自己不能做这件事情，他需要召唤 一个 subscriber 来做这件事。
        //通过什么办法召唤呢？
        //最简单的就是通过 通灵术：O.subscribe(subscriber)，不过还有其他重载方法。
        //通过这个方法，将它们两者绑定到一起

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("test");
                subscriber.onCompleted();
            }
        });
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("TAG", "onNext: " + s);

            }
        };
        observable.subscribe(subscriber);

        button = (Button) findViewById(R.id.button);


    }

    int count = 0;
    Button button;

    public void button(View v) {

        count++;
        switch (count) {

            case 1: //基本的简化
                button.setText("第二次点击");

                //第一次点击， 只创建 发出一个事件 就结束的 observable 对象
                Observable<String> ob = Observable.just("hi rxJava");
                //只有 onnext 的 时候
                Action1<String> action1 = new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("TAG", "call: " + s);
                    }
                };

                ob.subscribe(action1);

                break;

            case 2: //  1 的链式
                button.setText("第三次点击");

                Observable.just("hi rxJava").subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("TAG", "call-1: " + s);


                    }
                });

                break;
            case 3: //  2的java8 lambda 语言
                button.setText("第四次点击");
                Observable.just("hi rxjava").subscribe(s -> Log.e("TAG", "call0: " + s + "3"));

                break;

            case 4: //中途变幻 observable

                button.setText("the fifth click");
                Observable.just("hi rxjava").
                        map(new Func1<String, Integer>() {
                            @Override
                            public Integer call(String s) {

                                return 9527;
                            }
                        }). //这里 i 是指上一个map中 func1 返回的数据类型，即上一个 map中传递给下一个链式方法的return参数。
                        subscribe(i -> Log.e("TAG", "call1: " + i));
                break;

            case 5: //中途变化简化
                button.setText("the sixth click");
                Observable.just("hi rxjava").
                        map(s -> 9527).
                        map(i -> i + "").
                        subscribe(s1 -> Log.e("TAG", "call2: " + s1 ));

                break;

            case 6:

                break;


        }


    }
}
