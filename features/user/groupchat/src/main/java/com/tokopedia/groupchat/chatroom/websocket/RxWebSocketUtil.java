package com.tokopedia.groupchat.chatroom.websocket;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by dhh on 2017/9/21.
 * WebSocketUtil based on okhttp and RxJava
 * Core Feature : WebSocket will be auto reconnection onFailed.
 */
public class RxWebSocketUtil {
    private static final int DEFAULT_PING = 10000;
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final int DEFAULT_DELAY = 5000;
    private static RxWebSocketUtil instance;
    private int delay;
    private int maxRetries;

    private OkHttpClient client;

    private Map<String, Observable<WebSocketInfo>> observableMap;
    private Map<String, WebSocket> webSocketMap;
    private boolean showLog = true;
    private String logTag = "MainActivity RxWebSocket";

    private RxWebSocketUtil(int delay, int maxRetries, int pingInterval) {
        observableMap = new ArrayMap<>();
        webSocketMap = new ArrayMap<>();
        client = new OkHttpClient.Builder().pingInterval(pingInterval, TimeUnit.MILLISECONDS).build();
        this.delay = delay;
        this.maxRetries = maxRetries;
    }

    public static RxWebSocketUtil getInstance(int delay, int maxRetries, int pingInterval) {
        if (instance == null) {
            synchronized (RxWebSocketUtil.class) {
                if (instance == null) {
                    instance = new RxWebSocketUtil(delay, maxRetries, pingInterval);
                }
            }
        }
        return instance;
    }

    public static RxWebSocketUtil getInstance() {
        return getInstance(DEFAULT_DELAY, DEFAULT_MAX_RETRIES, DEFAULT_PING);
    }

    public Observable<WebSocketInfo> getWebSocketInfo(final String url, String accessToken, String groupChatToken) {
        String urlWithGCToken = String.format("%s%s%s", url, "&token=", groupChatToken);
        Observable<WebSocketInfo> observable = observableMap.get(url);
        if (observable == null && observableMap.isEmpty()) {
            RetryObservable retryObservable = new RetryObservable(maxRetries, delay);
            observable = Observable.create(new WebSocketOnSubscribe(client, urlWithGCToken,
                    accessToken,
                    webSocketMap))
                    .retryWhen(retryObservable)
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
                                retryObservable.resetMaxRetries();
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

    public Observable<WebSocket> getWebSocket(String url, String accessToken, String groupChatToken) {
        return getWebSocketInfo(url, accessToken, groupChatToken)
                .map(new Func1<WebSocketInfo, WebSocket>() {
                    @Override
                    public WebSocket call(WebSocketInfo webSocketInfo) {
                        return webSocketInfo.getWebSocket();
                    }
                });
    }

    public void send(String url, String msg) {
        WebSocket webSocket = webSocketMap.get(url);
        if (webSocket != null) {
            webSocket.send(msg);
        } else {
            throw new WebSocketException("The WebSokcet not open");
        }
    }

    public void asyncSend(String url, final String msg, String groupChatToken) {
        getWebSocket(url, "", groupChatToken)
                .first()
                .subscribe(new Action1<WebSocket>() {
                    @Override
                    public void call(WebSocket webSocket) {
                        webSocket.send(msg);
                    }
                });

    }
}
