package com.tokopedia.topchat.common.websocket

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.stub.chatroom.websocket.WebSocketStub
import com.tokopedia.websocket.WebSocketResponse
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.*
import javax.inject.Inject

class FakeTopchatWebSocket @Inject constructor() : TopchatWebSocket {

    private val websocket: WebSocket = WebSocketStub()
    private var listener: WebSocketListener? = null
    private val gson: Gson = GsonBuilder().create()
    private val startTimeQueue = LinkedList<String>()

    val defaultImageResponsePath = "ws/image_response.json"
    val deleteImageResponsePath = "ws/delete_image.json"
    val changeAddressResponse: WebSocketResponse
        get() = alterResponseOf("buyer/ws_opposite_with_label.json") {}
    val deleteImageResponse: WebSocketResponse
        get() = alterResponseOf(deleteImageResponsePath) {}

    override fun connectWebSocket(listener: WebSocketListener) {
        this.listener = listener
    }

    override fun close() {

    }

    override fun destroy() {
        this.listener = null
    }

    override fun sendPayload(wsPayload: String) {

    }

    fun simulateResponse(wsResponseText: WebSocketResponse) {
        val responseString = CommonUtil.toJson(wsResponseText)
        listener?.onMessage(websocket, responseString)
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

    companion object {
        private val data = "data"
        private val msg_id = "msg_id"
        private val from_uid = "from_uid"
        private val to_uid = "to_uid"
        private val is_opposite = "is_opposite"
    }
}