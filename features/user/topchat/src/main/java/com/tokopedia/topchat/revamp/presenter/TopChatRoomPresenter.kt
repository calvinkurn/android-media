package com.tokopedia.topchat.revamp.presenter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.network.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.topchat.revamp.listener.TopChatContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.WebSocket
import okio.ByteString
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author : Steven 11/12/18
 */

class TopChatRoomPresenter @Inject constructor(
        var getChatUseCase: GetChatUseCase,
        var userSession: UserSessionInterface,
        private var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper)
    : BaseDaggerPresenter<TopChatContract.View>(), TopChatContract.Presenter {

    private var mSubscription: CompositeSubscription
    private lateinit var webSocketUrl: String
    lateinit var dummyList: ArrayList<Visitable<*>>
    var thisMessageId: String = ""

    init {
        mSubscription = CompositeSubscription()
        dummyList = arrayListOf()
    }

    override fun connectWebSocket(messageId: String) {
        thisMessageId = messageId
        webSocketUrl = CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + userSession.deviceId +
                "&user_id=" + userSession.userId

        destroyWebSocket()

        if (mSubscription.isUnsubscribed) {
            mSubscription = CompositeSubscription()
        }

        val subscriber = object : WebSocketSubscriber() {
            override fun onOpen(webSocket: WebSocket) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", " on WebSocket open")
                }
                view.developmentView()
            }

            override fun onMessage(text: String) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", text)
                }
            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "item")
                }
                val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.getData(), ChatSocketPojo::class.java)
                if (pojo.msgId.toString() != messageId) return
                mappingEvent(webSocketResponse, messageId)
            }


            override fun onMessage(byteString: ByteString) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", byteString.toString())
                }
            }

            override fun onReconnect() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onReconnect")
                }
            }

            override fun onClose() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onClose")
                }
                destroyWebSocket()

            }

        }

        val subscription = RxWebSocket[webSocketUrl, userSession.accessToken, null, null]?.subscribe(subscriber)


        mSubscription.add(subscription)
    }


    fun destroyWebSocket() {
        mSubscription.clear()
        mSubscription.unsubscribe()
    }

    override fun mappingEvent(response: WebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo = Gson().fromJson(response.getData(), ChatSocketPojo::class.java)

        if (pojo.msgId.toString() != messageId) return
        when (response.getCode()) {
            EVENT_TOPCHAT_TYPING -> view.onReceiveStartTypingEvent()
            EVENT_TOPCHAT_END_TYPING -> view.onReceiveStopTypingEvent()
            EVENT_TOPCHAT_READ_MESSAGE -> view.onReceiveReadEvent()
            EVENT_TOPCHAT_REPLY_MESSAGE -> {
                view.onReceiveMessageEvent(mapToVisitable(pojo))
                getDummyOnList(pojo)?.let {
                    view.removeDummy(it)
                }
            }
        }
    }


    private fun processDummyMessage(messageText: String, startTime: String) {
        var dummyMessage = mapToDummyMessage(thisMessageId, messageText, startTime)
        view.addDummyMessage(dummyMessage)
        dummyList.add(dummyMessage)
    }


    private fun mapToDummyMessage(messageId: String, messageText: String, startTime: String): Visitable<*> {
        return MessageViewModel(messageId, userSession.userId, userSession.name, startTime, messageText)
    }

    private fun getDummyOnList(pojo: ChatSocketPojo): Visitable<*>? {
        for (i in 0.. dummyList.size){
            var temp = (dummyList[i] as MessageViewModel)
            if(temp.startTime == pojo.startTime
                    && temp.messageId == pojo.msgId.toString()
                    && temp.message.equals(pojo.message.originalReply)){
                return temp
            }
        }
        return null
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return topChatRoomWebSocketMessageMapper.map(pojo)
    }

    override fun sendMessage(messageId: String, messageText: String) {
        if (isValidReply(messageText)) {
            val startTime = SendableViewModel.generateStartTime()
            view.clearEditText()
//            view.disableAction()
            processDummyMessage(messageText, startTime)
//            when (networkMode) {
//                MODE_WEBSOCKET ->
            sendMessageWebSocket(messageText, startTime)
//            }
        }
    }

    private fun sendMessageWebSocket(messageText: String, startTime: String) {
        RxWebSocket.send(
                msg = generateParamSendMessage(messageText, startTime),
                tkpdAuthInterceptor = null,
                fingerprintInterceptor = null
        )
    }


    private fun isValidReply(message: String): Boolean {
        if (message.trim { it <= ' ' }.isEmpty()) {
//            view.showSnackbarError(view.getStringResource(R.string.error_empty_product))
            return false
        }
        return true
    }




    override fun detachView() {
        destroyWebSocket()
        super.detachView()
    }


    private fun generateParamSendMessage(messageText: String, startTime: String): String {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(thisMessageId))
        data.addProperty("message", messageText)
        data.addProperty("start_time", startTime)
        json.add("data", data)
        return json.toString()
    }

    private fun generateParamStartTyping(): String {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(thisMessageId))
        json.add("data", data)
        return json.toString()
    }

    private fun generateParamStopTyping(): String {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_END_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(thisMessageId))
        json.add("data", data)
        return json.toString()
    }

    private fun generateParamRead(): String {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(thisMessageId))
        json.add("data", data)
        return json.toString()
    }
}