package com.tokopedia.topchat.stub.chatroom.websocket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.domain.pojo.*
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductProfile
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.chatlist.data.ChatWebSocketConstant
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.time.RfcDateTimeParser
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.websocket.WebSocketResponse
import okhttp3.WebSocket
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject
import java.util.*

class RxWebSocketUtilStub constructor(
    private val mapper: TopChatRoomGetExistingChatMapper,
    private val session: UserSessionInterface,
) : RxWebSocketUtil(
    emptyList(), 60, 5, 5
) {

    private val websocketInfoObservable = PublishSubject.create<WebSocketInfo>()
    private val websocket: WebSocket = WebSocketStub()
    private val gson: Gson = GsonBuilder().create()
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
        startTimeQueue.add(msg)
    }

    fun simulateResponse(wsResponseText: WebSocketResponse) {
        val responseString = CommonUtil.toJson(wsResponseText)
        val wsInfo = WebSocketInfo(
            websocket, responseString
        )
        websocketInfoObservable.onNext(wsInfo)
    }

    /**
     * simulate just start_time only
     */
    fun simulateResponseMatchRequestStartTime(
        wsResponseText: WebSocketResponse
    ) {
        val requestMsg = startTimeQueue.remove() ?: return
        val requestObj: WebSocketResponse = CommonUtil.fromJson(
            requestMsg, WebSocketResponse::class.java
        )
        val startTime = requestObj.jsonObject?.get("start_time")?.asString ?: return
        wsResponseText.jsonObject?.addProperty("start_time", startTime)
        val responseString = CommonUtil.toJson(wsResponseText)
        val wsInfo = WebSocketInfo(
            websocket, responseString
        )
        websocketInfoObservable.onNext(wsInfo)
    }

    fun simulateResponseFromRequestQueue(room: GetExistingChatPojo) {
        while (startTimeQueue.peek() != null) {
            val requestMsg = startTimeQueue.remove()
            val requestObj: WebSocketResponse = CommonUtil.fromJson(
                requestMsg, WebSocketResponse::class.java
            )
            when {
                isProductAttachment(requestObj) -> simulateAttachmentResponse(requestObj, room)
                isTextOnly(requestObj) -> simulateMessageResponse(requestObj, room)
                else -> Log.d("NOT_HANDLED_WS_RESPONSE", requestMsg)
            }
        }
    }

    private fun simulateAttachmentResponse(
        request: WebSocketResponse,
        room: GetExistingChatPojo
    ) {
        val requestStartTime = request.jsonObject?.get("start_time")?.asString ?: ""
        val requestMsg = request.jsonObject?.get("message")?.asString ?: ""
        val requestAttachment = request.jsonObject?.get("product_profile")?.asJsonObject
        val requestProductProfile: ProductProfile = CommonUtil.fromJson(
            requestAttachment.toString(), ProductProfile::class.java
        )
        val requestProductId = request.jsonObject?.get("product_id")?.asString ?: ""
        val requestTimeStamp = RfcDateTimeParser.parseDateString(
            requestStartTime, arrayOf(START_TIME_FORMAT)
        ).time
        val requestAttributes = ProductAttachmentAttributes(
            requestProductId, requestProductProfile
        )

        val uiModel = mapper.map(room)
        val attachment = AttachmentPojo(
            id = requestProductId,
            type = AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT,
            attributes = JsonParser.parseString(gson.toJson(requestAttributes)).asJsonObject,
            fallbackAttachment = Fallback()
        )
        val message = MessagePojo(
            censoredReply = requestMsg,
            originalReply = requestMsg,
            timestamp = requestStartTime,
            timestampFmt = requestStartTime,
            timeStampUnixNano = (requestTimeStamp * 1_000_000).toString(),
            timeStampUnix = requestTimeStamp.toString()
        )
        val chat = ChatSocketPojo(
            msgId = TopchatRoomTest.MSG_ID.toLong(),
            fromUid = session.userId,
            from = uiModel.shopName,
            fromRole = uiModel.role,
            toUid = uiModel.headerModel.senderId.toLong(),
            message = message,
            startTime = requestStartTime,
            imageUri = "",
            attachment = attachment,
            showRating = false,
            ratingStatus = 0,
            isOpposite = false,
            blastId = 0,
            source = "inbox",
            label = ""
        )
        val chatElement = gson.toJsonTree(chat)
        val response = WebSocketResponse(
            "", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE, chatElement
        )
        simulateResponse(response)
    }

    private fun simulateMessageResponse(
        request: WebSocketResponse,
        room: GetExistingChatPojo
    ) {
        val requestStartTime = request.jsonObject?.get("start_time")?.asString ?: ""
        val requestMsg = request.jsonObject?.get("message")?.asString ?: ""
        val uiModel = mapper.map(room)
        val timestamp = RfcDateTimeParser.parseDateString(
            requestStartTime, arrayOf(START_TIME_FORMAT)
        ).time
        val message = MessagePojo(
            censoredReply = requestMsg,
            originalReply = requestMsg,
            timestamp = requestStartTime,
            timestampFmt = requestStartTime,
            timeStampUnixNano = (timestamp * 1_000_000).toString(),
            timeStampUnix = timestamp.toString()
        )
        val chat = ChatSocketPojo(
            msgId = TopchatRoomTest.MSG_ID.toLong(),
            fromUid = session.userId,
            from = uiModel.shopName,
            fromRole = uiModel.role,
            toUid = uiModel.headerModel.senderId.toLong(),
            message = message,
            startTime = requestStartTime,
            imageUri = "",
            attachment = null,
            showRating = false,
            ratingStatus = 0,
            isOpposite = false,
            blastId = 0,
            source = "inbox",
            label = ""
        )
        val chatElement = gson.toJsonTree(chat)
        val response = WebSocketResponse(
            "", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE, chatElement
        )
        simulateResponse(response)
    }

    private fun isProductAttachment(requestObj: WebSocketResponse): Boolean {
        return hasAttachment(requestObj) && requestObj.jsonObject
            ?.get("attachment_type")
            ?.asString == AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
    }

    private fun hasAttachment(requestObj: WebSocketResponse): Boolean {
        return isReplyMessage(requestObj) && requestObj.jsonObject
            ?.has("attachment_type") == true
    }

    private fun isTextOnly(requestObj: WebSocketResponse): Boolean {
        return isReplyMessage(requestObj) && !hasAttachment(requestObj)
    }

    private fun isReplyMessage(requestObj: WebSocketResponse): Boolean {
        return requestObj.code == ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE
    }

    companion object {
        const val START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    }
}

