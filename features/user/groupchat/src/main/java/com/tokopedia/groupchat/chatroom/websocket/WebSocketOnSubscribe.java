package com.tokopedia.groupchat.chatroom.websocket;

import android.util.Log;

import com.tokopedia.network.constant.TkpdBaseURL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import rx.Observable;
import rx.Subscriber;

/**
 * @author : Steven 07/10/18
 */
public final class WebSocketOnSubscribe implements Observable.OnSubscribe<WebSocketInfo> {
    private String url;
    private String accessToken;

    private WebSocket webSocket;

    private boolean showLog = true;
    private String logTag = "RxWebSocket";

    private OkHttpClient client;

    WebSocketOnSubscribe(OkHttpClient client, String url, String accessToken, WebSocket webSocketMap) {
        this.client = client;
        this.url = url;
        this.accessToken = accessToken;
        this.webSocket = webSocketMap;
    }

    @Override
    public void call(final Subscriber<? super WebSocketInfo> subscriber) {
        initWebSocket(subscriber, accessToken);
    }

    private Request getRequest(String url, String accessToken) {
        return new Request.Builder().get().url(url)
                .header("Origin", TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL)
                .header("Accounts-Authorization",
                        "Bearer " + accessToken)
                .build();
    }

    private void initWebSocket(final Subscriber<? super WebSocketInfo> subscriber, String accessToken) {
        webSocket = client.newWebSocket(getRequest(url, accessToken), new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                if (showLog) {
                    Log.d(logTag, url + " --> onOpen");
                }

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
                    subscriber.onNext(WebSocketInfo.createReconnect());
                    subscriber.onError(t);
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, "onClosing");
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