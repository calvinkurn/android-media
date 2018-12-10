package com.tokopedia.chat_common.presenter

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.network.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chat_common.view.viewmodel.ChatRoomViewModel
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.WebSocket
import okio.ByteString
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author : Steven 29/11/18
 */

open class BaseChatPresenter @Inject constructor(
        open var userSession: UserSessionInterface,
        open var getChatUseCase: GetChatUseCase)
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

    private lateinit var mSubscription: CompositeSubscription

    override fun attachView(view: BaseChatContract.View?) {
        super.attachView(view)
        mSubscription = CompositeSubscription()
    }

    fun connectWebSocket(messageId: String) {
        var webSocketUrl = CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
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

    override fun mappingEvent(websocketResponse: WebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo = Gson().fromJson(websocketResponse.getData(), ChatSocketPojo::class.java)

        if (pojo.msgId.toString() != messageId) return

        when (websocketResponse.getCode()) {
            EVENT_TOPCHAT_TYPING -> view.onReceiveStartTypingEvent()
            EVENT_TOPCHAT_END_TYPING -> view.onReceiveStopTypingEvent()
            EVENT_TOPCHAT_READ_MESSAGE -> view.onReceiveReadEvent()
            EVENT_TOPCHAT_REPLY_MESSAGE -> {
                view.onReceiveMessageEvent(mapToVisitable(pojo))
            }
        }
    }

    private fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return MessageViewModel(pojo.msgId.toString(), pojo.fromUid, pojo.from, pojo.fromRole, ""
                , "", "", pojo.startTime, "", false, false, pojo.isOpposite)
    }


    override fun detachView() {
        super.detachView()
        destroyWebSocket()
    }

    override fun getChatUseCase(messageId: String) {
        getChatUseCase(messageId, 1)
    }

    override fun getChatUseCase(messageId: String, page: Int) {
        getChatUseCase.execute(GetChatUseCase.generateParam(messageId, page)
                , object : Subscriber<ChatRoomViewModel>() {
            override fun onNext(model: ChatRoomViewModel?) {
                view.developmentView()
                model?.listChat.let { view.onSuccessGetChat(it!!) }
            }

            override fun onCompleted() {
                return
            }

            override fun onError(e: Throwable?) {
                return
            }

        })
    }

    fun destroyWebSocket() {
        mSubscription.clear()
        mSubscription.unsubscribe()
    }
}
