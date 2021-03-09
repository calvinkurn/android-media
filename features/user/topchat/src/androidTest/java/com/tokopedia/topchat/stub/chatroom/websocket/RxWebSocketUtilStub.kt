package com.tokopedia.topchat.stub.chatroom.websocket

import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.websocket.WebSocketResponse
import okhttp3.WebSocket
import rx.Observable
import rx.schedulers.TestScheduler
import rx.subjects.PublishSubject

class RxWebSocketUtilStub constructor() : RxWebSocketUtil(
        emptyList(), 60, 5, 5
) {

    val websocketInfoObservable = PublishSubject.create<WebSocketInfo>()
    val websocket: WebSocket = WebSocketStub()

    override fun getWebSocketInfo(url: String, accessToken: String): Observable<WebSocketInfo>? {
        return websocketInfoObservable
                .subscribeOn(TestScheduler())
                .observeOn(TestScheduler()).also {
                    websocketInfoObservable.onNext(WebSocketInfo(websocket, true))
                }
    }

    override fun send(msg: String) {
        // Do nothing on sent
    }

    fun simulateResponse(wsResponseText: WebSocketResponse) {
        val responseString = CommonUtil.toJson(wsResponseText)
        val wsInfo = WebSocketInfo(
                websocket, responseString
        )
        websocketInfoObservable.onNext(wsInfo)
    }
}