package com.tokopedia.chat_common.presenter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.subscriber.GetChatRepliesSubscriber
import com.tokopedia.chat_common.network.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.WebSocket
import okio.ByteString
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author : Steven 29/11/18
 */

open class BaseChatPresenter @Inject constructor(
        open var userSession: UserSessionInterface,
        open var getChatUseCase: GetChatUseCase,
        open var websocketMessageMapper: WebsocketMessageMapper
        )
    : BaseDaggerPresenter<BaseChatContract.View>(), BaseChatContract.Presenter {

    var thisMessageId: String = ""
    var EVENT_REPLY_MESSAGE = 102
    val EVENT_TOPCHAT_REPLY_MESSAGE = 103
    var EVENT_TYPING = 201
    var EVENT_END_TYPING = 202
    val EVENT_TOPCHAT_TYPING = 203
    val EVENT_TOPCHAT_END_TYPING = 204
    val EVENT_TOPCHAT_READ_MESSAGE = 301
    var MONITORING = 900
    var CLOSE_CONNECTION = 999

    val MODE_WEBSOCKET = 1
    val MODE_API = 2

    var networkMode: Int = 0

    lateinit var webSocketUrl: String

    lateinit var dummyList: ArrayList<Visitable<*>>

    private lateinit var mSubscription: CompositeSubscription

    override fun attachView(view: BaseChatContract.View?) {
        super.attachView(view)
        thisMessageId = this.view.getMsgId()
        networkMode = this.view.getNetworkMode()
        mSubscription = CompositeSubscription()
        dummyList = arrayListOf()
        connectWebSocket(thisMessageId)
    }

    fun connectWebSocket(messageId: String) {
        webSocketUrl = CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + userSession.deviceId +
                "&user_id=" + userSession.userId

        destroyWebSocket()

        if (mSubscription == null || mSubscription.isUnsubscribed) {
            mSubscription = CompositeSubscription()
        }

        val subscriber = object : WebSocketSubscriber() {
            override fun onOpen(webSocket: WebSocket) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", " on WebSocket open")
                }
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

        val subscription = RxWebSocket.get(webSocketUrl, userSession.accessToken)?.subscribe(subscriber)


        mSubscription.add(subscription)

    }

    private fun mappingEvent(response: WebSocketResponse, messageId: String) {
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
//        return MessageViewModel(pojo.msgId.toString(), pojo.fromUid, pojo.from, pojo.fromRole, ""
//                , "", pojo.message.timeStampUnix, pojo.startTime, pojo.message.censoredReply, false, false, !pojo.isOpposite)
        return websocketMessageMapper.map(pojo)
    }

    private fun mapToDummyMessage(messageId: String, messageText: String, startTime: String): Visitable<*> {
        return MessageViewModel(messageId, userSession.userId, userSession.name, startTime, messageText)
    }



    override fun detachView() {
        super.detachView()
        destroyWebSocket()
    }

    //TODO MOVE THIS TO TOPCHAT PRESENTER
    override fun getChatUseCase(messageId: String, onError : (Exception) -> Unit ) {
        getChatUseCase.execute(GetChatUseCase.generateParam(messageId),
                GetChatRepliesSubscriber(onError))
    }

    fun destroyWebSocket() {
        mSubscription.clear()
        mSubscription.unsubscribe()
    }

    override fun sendMessage(messageText: String) {
        if (isValidReply(messageText)) {
            val startTime = SendableViewModel.generateStartTime()
            view.clearEditText()
//            view.disableAction()
            processDummyMessage(messageText, startTime)
            when (networkMode) {
                MODE_WEBSOCKET -> sendMessageWebSocket(messageText, startTime)
            }
        }
    }

    private fun processDummyMessage(messageText: String, startTime: String) {
        var dummyMessage = mapToDummyMessage(thisMessageId, messageText, startTime)
        view.addDummyMessage(dummyMessage)
        dummyList.add(dummyMessage)
    }

    private fun sendMessageWebSocket(messageText: String, startTime: String) {
        RxWebSocket.send(webSocketUrl, msg = generateParamSendMessage(messageText, startTime))
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

    private fun isValidReply(message: String): Boolean {
        if (message.trim { it <= ' ' }.isEmpty()) {
            view.showSnackbarError(view.getStringResource(R.string.error_empty_product))
            return false
        }
        return true
    }
}
