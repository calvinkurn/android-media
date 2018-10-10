package com.tokopedia.groupchat.chatroom.websocket;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author : Steven 07/10/18
 */
public final class WebSocketOnSubscribe implements Observable.OnSubscribe<WebSocketInfo> {
    private final Map<String, WebSocket> webSocketMap;
    private String url;
    private String accessToken;

    private WebSocket webSocket;

    private boolean showLog = true;
    private String logTag = "RxWebSocket";

    private OkHttpClient client;

    WebSocketOnSubscribe(OkHttpClient client, String url, String accessToken, Map<String, WebSocket> webSocketMap) {
        this.client = client;
        this.url = url;
        this.accessToken = accessToken;
        this.webSocketMap = webSocketMap;
    }

    @Override
    public void call(final Subscriber<? super WebSocketInfo> subscriber) {
//        if (webSocket != null) {
//            if (!"main".equals(Thread.currentThread().getName())) {
//                long ms = TimeUnit.SECONDS.toMillis(1);
//                SystemClock.sleep(ms);
//                subscriber.onNext(WebSocketInfo.createReconnect());
//            }
//        }
        initWebSocket(subscriber, accessToken);
    }

    private Request getRequest(String url, String accessToken) {
        return new Request.Builder().get().url(url)
                .header("Origin", "https://www.tokopedia.com/")
                .header("Accounts-Authorization",
                        "Bearer " + accessToken)
                .build();
    }

    private void initWebSocket(final Subscriber<? super WebSocketInfo> subscriber, String accessToken) {
        webSocket = client.newWebSocket(getRequest(url, accessToken), new WebSocketListener() {
            boolean receivePong;
            Subscription subscription;
            @Override
            public void onOpen(final WebSocket webSocket, Response response) {
                if (showLog) {
                    Log.d(logTag, url + " --> onOpen");
                }
                receivePong = true;
                webSocketMap.put(url, webSocket);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new WebSocketInfo(webSocket, true));
                }

                subscription = Observable.interval(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            if (receivePong) {
                                Log.d(logTag, "sending ping");
                                webSocket.send("ping");
                                receivePong = false;
                            } else {
                                Log.i(logTag, "call: "+aLong);
                                Log.i(logTag, "cancel");
                                try {
                                    webSocket.close(1000, "asdfad");
                                }catch (Exception e){
                                    Log.i(logTag, "crash: "+e.toString());
                                }
                                subscriber.onCompleted();
                            }
                        });
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if(text.equals("ping")){
                    receivePong = true;
                    Log.d(logTag, "pong received");
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new WebSocketInfo(webSocket, text));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new WebSocketInfo(webSocket, bytes));
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                if (showLog) {
                    Log.e(logTag, t.toString() + webSocket.request().url().uri().getPath());
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }

                if(subscription != null && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, "onClosing");
                if(subscription != null && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                if (showLog) {
                    Log.d(logTag, url + " --> onClosed:code = " + code + ", reason = " + reason);
                    subscriber.onNext(WebSocketInfo.createReconnect());
                }
            }

        });

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                webSocket.close(3000, "close WebSocket");
                if (showLog) {
                    Log.d(logTag, url + " --> onUnsubscribe ");
                }
            }
        });
    }
}