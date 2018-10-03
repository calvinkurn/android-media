package com.tokopedia.groupchat.chatroom.websocket;

import android.os.SystemClock;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public abstract class WSUseCase {


    private OkHttpClient client;

    private Map<String, Observable<WebSocketInfo>> observableMap;
    private Map<String, WebSocket> webSocketMap;
    private boolean showLog;
    private String logTag = "RxWebSocket";
    private long interval = 1;
    private TimeUnit reconnectIntervalTimeUnit = TimeUnit.SECONDS;

    private Request getRequest(String url) {
        return new Request.Builder().get().url(url).build();
    }

    public Observable<WebSocketInfo> getWebSocketInfo(final String url, final long timeout, final TimeUnit timeUnit) {
        Observable<WebSocketInfo> observable = observableMap.get(url);
        if (observable == null) {
            observable = Observable.create(new WebSocketOnSubscribe(url))
                    //自动重连
                    .timeout(timeout, timeUnit)
                    .retry()
                    //共享
                    .doOnUnsubscribe(new Action0() {
                        @Override
                        public void call() {
                            observableMap.remove(url);
                            webSocketMap.remove(url);
                            if (showLog) {
                                Log.d(logTag, "unsubscribe");
                            }
                        }
                    })
                    .doOnNext(new Action1<WebSocketInfo>() {
                        @Override
                        public void call(WebSocketInfo webSocketInfo) {
                            if (webSocketInfo.isOnOpen()) {
                                webSocketMap.put(url, webSocketInfo.getWebSocket());
                            }
                        }
                    })
                    .share()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            observableMap.put(url, observable);
        } else {
            WebSocket webSocket = webSocketMap.get(url);
            if (webSocket != null) {
                observable = observable.startWith(new WebSocketInfo(webSocket, true));
            }
        }
        return observable;
    }

    private final class WebSocketOnSubscribe implements Observable.OnSubscribe<WebSocketInfo> {
        private String url;

        private WebSocket webSocket;

        public WebSocketOnSubscribe(String url) {
            this.url = url;
        }

        @Override
        public void call(final Subscriber<? super WebSocketInfo> subscriber) {
            if (webSocket != null) {
                //降低重连频率
                if (!"main".equals(Thread.currentThread().getName())) {
                    long ms = reconnectIntervalTimeUnit.toMillis(interval);
                    if (ms == 0) {
                        ms = 1000;
                    }
                    SystemClock.sleep(ms);
                    subscriber.onNext(WebSocketInfo.createReconnect());
                }
            }
            initWebSocket(subscriber);
        }

        private void initWebSocket(final Subscriber<? super WebSocketInfo> subscriber) {
            webSocket = client.newWebSocket(getRequest(url), new WebSocketListener() {
                @Override
                public void onOpen(final WebSocket webSocket, Response response) {
                    if (showLog) {
                        Log.d(logTag, url + " --> onOpen");
                    }
                    webSocketMap.put(url, webSocket);
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(new WebSocketInfo(webSocket, true));
                    }
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
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
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    webSocket.close(1000, null);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    if (showLog) {
                        Log.d(logTag, url + " --> onClosed:code = " + code + ", reason = " + reason);
                    }
                }
            });
        }

    }



}