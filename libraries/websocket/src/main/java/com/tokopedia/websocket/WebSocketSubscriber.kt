package com.tokopedia.websocket

import okhttp3.WebSocket
import okio.ByteString
import rx.Subscriber

/**
 * Created by dhh on 2017/11/2.
 * override the method of you want to use
 */

abstract class WebSocketSubscriber : Subscriber<WebSocketInfo>() {

    private var hasOpened: Boolean = false

    override fun onNext(webSocketInfo: WebSocketInfo) {
        when {
            webSocketInfo.isOnOpen -> {
                webSocketInfo.webSocket?.let {
                    hasOpened = true
                    onOpen(it)
                }
            }
            webSocketInfo.isOnReconnect -> onReconnect()
        }

        webSocketInfo.string?.let {
            onMessage(it)
        }

        webSocketInfo.byteString?.let {
            onMessage(it)
        }

        webSocketInfo.response?.let {
            onMessage(it)
        }
    }

    protected open fun onOpen(webSocket: WebSocket) {}

    protected open fun onMessage(text: String) {}

    protected open fun onMessage(webSocketResponse: WebSocketResponse) {}

    protected open fun onMessage(byteString: ByteString) {}

    protected open fun onReconnect() {}

    protected open fun onClose() {}

    override fun onCompleted() {
        if (hasOpened) {
            onClose()
        }
    }

    override fun onError(e: Throwable) {
        onClose()
    }

}
