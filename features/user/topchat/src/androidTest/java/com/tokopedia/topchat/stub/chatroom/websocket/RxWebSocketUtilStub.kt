package com.tokopedia.topchat.stub.chatroom.websocket

import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.websocket.WebSocketResponse
import okhttp3.WebSocket
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject
import java.util.*

class RxWebSocketUtilStub constructor() : RxWebSocketUtil(
    emptyList(), 60, 5, 5
) {

    val websocketInfoObservable = PublishSubject.create<WebSocketInfo>()
    val websocket: WebSocket = WebSocketStub()
    private val startTimeQueue = LinkedList<String>()

    override fun getWebSocketInfo(url: String, accessToken: String): Observable<WebSocketInfo>? {
        return websocketInfoObservable
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .also {
                websocketInfoObservable.onNext(WebSocketInfo(websocket, true))
            }
    }

    override fun send(msg: String) {
        val requestObj: WebSocketResponse = CommonUtil.fromJson(
            msg, WebSocketResponse::class.java
        )
        val startTime = requestObj.jsonObject
            ?.get("start_time")
            ?.asString ?: return
        startTimeQueue.add(startTime)
    }

    fun simulateResponse(wsResponseText: WebSocketResponse) {
        val responseString = CommonUtil.toJson(wsResponseText)
        val wsInfo = WebSocketInfo(
            websocket, responseString
        )
        websocketInfoObservable.onNext(wsInfo)
    }

    fun simulateResponseMatchRequestStartTime(
        wsResponseText: WebSocketResponse
    ) {
        val startTime = startTimeQueue.remove() ?: return
        wsResponseText.jsonObject?.addProperty("start_time", startTime)
        val responseString = CommonUtil.toJson(wsResponseText)
        val wsInfo = WebSocketInfo(
            websocket, responseString
        )
        websocketInfoObservable.onNext(wsInfo)
    }
}