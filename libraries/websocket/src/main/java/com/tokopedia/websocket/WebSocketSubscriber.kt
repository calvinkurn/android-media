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
            webSocketInfo.isOnOpen!! -> {
                hasOpened = true
                onOpen(webSocketInfo.webSocket!!)
            }
            webSocketInfo.string != null -> onMessage(webSocketInfo.string!!)
            webSocketInfo.byteString != null -> onMessage(webSocketInfo.byteString!!)
            webSocketInfo.isOnReconnect -> onReconnect()
        }

        if (webSocketInfo.response != null) {
            onMessage(webSocketInfo.response!!)
        }
    }

    protected open fun onOpen(webSocket: WebSocket) {}

    protected open fun onMessage(text: String) {}

    protected open fun onMessage(webSocketResponse: WebSocketResponse) {}

    protected open fun onMessage(byteString: ByteString) {}

    protected open fun onReconnect() {}

    protected open fun onClose() {

    }

    override fun onCompleted() {
        if (hasOpened) {
            onClose()
        }
    }

    override fun onError(e: Throwable) {
        onClose()
    }

}
