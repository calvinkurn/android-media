package com.tokopedia.topchat.stub.chatroom.websocket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.*
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductProfile
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatlist.data.ChatWebSocketConstant
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerAttributesResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.common.alterResponseOf
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

    val defaultImageResponsePath = "ws/image_response.json"

    val changeAddressResponse: WebSocketResponse
        get() {
            return AndroidFileUtil.parse(
                "buyer/ws_opposite_with_label.json",
                WebSocketResponse::class.java
            )
        }

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

    fun generateUploadImageResposne(roomMetaData: RoomMetaData): WebSocketResponse {
        return alterResponseOf(defaultImageResponsePath) {
            val data = it.getAsJsonObject(data)
            data.addProperty(msg_id, roomMetaData.msgId)
            data.addProperty(from_uid, roomMetaData.receiver.uid)
            data.addProperty(to_uid, roomMetaData.sender.uid)
            data.addProperty(is_opposite, true)
        }
    }

    private val data = "data"
    private val msg_id = "msg_id"
    private val from_uid = "from_uid"
    private val to_uid = "to_uid"
    private val is_opposite = "is_opposite"

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

    // TODO: handle invoice and image attachment
    fun simulateResponseFromRequestQueue(room: GetExistingChatPojo) {
        while (startTimeQueue.peek() != null) {
            val requestMsg = startTimeQueue.remove()
            val requestObj: WebSocketResponse = CommonUtil.fromJson(
                requestMsg, WebSocketResponse::class.java
            )
            when {
                isProductAttachment(requestObj) -> simulateProductAttachmentResponse(
                    requestObj, room
                )
                isStickerAttachment(requestObj) -> simulateStickerAttachmentResponse(
                    requestObj, room
                )
                isTextOnly(requestObj) -> simulateMessageResponse(requestObj, room)
                else -> Log.d("NOT_HANDLED_WS_RESPONSE", requestMsg)
            }
        }
    }

    private fun simulateProductAttachmentResponse(
        request: WebSocketResponse,
        room: GetExistingChatPojo
    ) {
        val requestAttachment = request.jsonObject?.get("product_profile")?.asJsonObject
        val requestProductProfile: ProductProfile = CommonUtil.fromJson(
            requestAttachment.toString(), ProductProfile::class.java
        )
        val requestProductId = request.jsonObject?.get("product_id")?.asString ?: ""
        val requestAttributes = ProductAttachmentAttributes(
            requestProductId, requestProductProfile
        )
        val attachment = AttachmentPojo(
            id = requestProductId,
            type = AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT,
            attributes = JsonParser.parseString(gson.toJson(requestAttributes)).asJsonObject,
            fallbackAttachment = Fallback()
        )
        simulateWsResponse(attachment, request, room)
    }

    private fun simulateStickerAttachmentResponse(
        request: WebSocketResponse,
        room: GetExistingChatPojo
    ) {
        val stickerPayload = request.jsonObject?.get("payload")?.asJsonObject
        val groupId = stickerPayload?.get("group_id")?.asString ?: ""
        val imageUrl = stickerPayload?.get("image_url")?.asString ?: ""
        val intention = stickerPayload?.get("intention")?.asString ?: ""
        val stickerId = stickerPayload?.get("sticker_id")?.asString ?: ""
        val requestAttributes = StickerAttributesResponse(
            stickerProfile = StickerProfile(
                groupId = groupId,
                imageUrl = imageUrl,
                intention = intention,
                stickerId = stickerId
            )
        )
        val attachment = AttachmentPojo(
            id = "1",
            type = AttachmentType.Companion.TYPE_STICKER.toString(),
            attributes = JsonParser.parseString(gson.toJson(requestAttributes)).asJsonObject,
            fallbackAttachment = Fallback()
        )
        simulateWsResponse(attachment, request, room)
    }

    private fun simulateWsResponse(
        attachment: AttachmentPojo?,
        request: WebSocketResponse,
        room: GetExistingChatPojo
    ) {
        val requestStartTime = request.jsonObject?.get("start_time")?.asString ?: ""
        val requestMsg = request.jsonObject?.get("message")?.asString ?: ""
        val localId = request.jsonObject?.get("local_id")?.asString ?: ""
        val parentReply = generateParentReplyFrom(request)
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
            attachment = attachment,
            showRating = false,
            ratingStatus = 0,
            isOpposite = false,
            blastId = 0,
            source = "inbox",
            label = "",
            localId = localId,
            parentReply = parentReply
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
        simulateWsResponse(null, request, room)
    }

    private fun generateParentReplyFrom(request: WebSocketResponse): ParentReply? {
        val parentReply = request.jsonObject?.get("parent_reply")?.asJsonObject ?: return null
        val attachmentId = parentReply.get("attachment_id")
        val attachmentType = parentReply.get("attachment_type")
        val senderId = parentReply.get("sender_id")
        val replyTime = parentReply.get("reply_time")
        val mainText = parentReply.get("main_text")
        val subText = parentReply.get("sub_text")
        val imageUrl = parentReply.get("image_url")
        val localId = parentReply.get("local_id")
        val source = parentReply.get("source")
        return ParentReply(
            attachmentId = attachmentId.asString,
            attachmentType = attachmentType.asString,
            senderId = senderId.asString,
            replyTime = replyTime.asString,
            mainText = mainText.asString,
            subText = subText.asString,
            imageUrl = imageUrl.asString,
            localId = localId.asString,
            source = source.asString
        )
    }

    private fun isStickerAttachment(requestObj: WebSocketResponse): Boolean {
        return hasAttachment(requestObj) && getAttachmentType(
            requestObj) == AttachmentType.Companion.TYPE_STICKER.toString()
    }

    private fun isProductAttachment(requestObj: WebSocketResponse): Boolean {
        return hasAttachment(requestObj) && getAttachmentType(
            requestObj) == AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
    }

    private fun getAttachmentType(requestObj: WebSocketResponse): String? {
        return requestObj.jsonObject
            ?.get("attachment_type")
            ?.asString
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

