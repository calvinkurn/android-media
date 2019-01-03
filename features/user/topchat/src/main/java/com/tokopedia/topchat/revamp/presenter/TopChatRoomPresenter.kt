package com.tokopedia.topchat.revamp.presenter

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.network.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel
import com.tokopedia.topchat.revamp.domain.subscriber.GetChatSubscriber
import com.tokopedia.topchat.revamp.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.revamp.domain.usecase.GetTemplateChatRoomUseCase
import com.tokopedia.topchat.revamp.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.revamp.listener.TopChatContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.WebSocket
import okio.ByteString
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author : Steven 11/12/18
 */

class TopChatRoomPresenter @Inject constructor(
        var getChatUseCase: GetChatUseCase,
        override var userSession: UserSessionInterface,
        private var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
        private var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase)
    : BaseChatPresenter<TopChatContract.View>(userSession, topChatRoomWebSocketMessageMapper), TopChatContract.Presenter {

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
                readMessage()
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

    override fun destroyWebSocket() {
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
                if(!pojo.isOpposite){
                    getDummyOnList(pojo)?.let {
                        view.removeDummy(it)
                    }
                }else {
                    readMessage()
                }
            }
        }
    }

    override fun getExistingChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccess: (ChatroomViewModel) -> Unit) {
        if (messageId.isNotEmpty()) {
            getChatUseCase.execute(GetChatUseCase.generateParamFirstTime(messageId),
                    GetChatSubscriber(onError, onSuccess))
        }
    }

    fun getTemplate() {
        getTemplateChatRoomUseCase.execute(object : Subscriber<GetTemplateViewModel>(){
            override fun onNext(t: GetTemplateViewModel?) {
                var templateList = arrayListOf<Visitable<Any>>()
                t?.let {
                    if(t.isEnabled){
                        t.listTemplate?.let {
                            templateList.addAll(it)
                        }
                    }
                }
                templateList.add(TemplateChatModel(false) as Visitable<Any>)
                view.onSuccessGetTemplate(templateList)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view.onErrorGetTemplate()
            }

        })
    }

    private fun readMessage() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamRead(thisMessageId))
    }

//    override fun startUploadImages(it: ImageUploadViewModel) {
//
//        uploadImageUseCase.unsubscribe()
//        var reqParam = HashMap<String, RequestBody>()
//        val webService = RequestBody.create(MediaType.parse("text/plain"), "1")
//        reqParam.put("web_service", createRequestBody("1"))
//        reqParam.put("id", createRequestBody(String.format("%s%s", userSession.userId, it.imageUrl)))
//        var params = uploadImageUseCase.createRequestParam(it.imageUrl, "/upload/attachment", "fileToUpload\"; filename=\"image.jpg", reqParam)
//        uploadImageUseCase.execute(params, object : Subscriber<ImageUploadDomainModel<TopChatImageUploadPojo>>(){
//            override fun onNext(t: ImageUploadDomainModel<TopChatImageUploadPojo>?) {
//                t
//            }
//
//
//            override fun onCompleted() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onError(e: Throwable?) {
//                e
//            }
//
//        })
//    }
//
    private fun processDummyMessage(messageText: String, startTime: String) {
        var dummyMessage = mapToDummyMessage(thisMessageId, messageText, startTime)
        view.addDummyMessage(dummyMessage)
        dummyList.add(dummyMessage)
    }


    private fun mapToDummyMessage(messageId: String, messageText: String, startTime: String): Visitable<*> {
        return MessageViewModel(messageId, userSession.userId, userSession.name, startTime, messageText)
    }

    private fun getDummyOnList(pojo: ChatSocketPojo): Visitable<*>? {
        dummyList.isNotEmpty().let {
            for (i in 0 until dummyList.size){
                var temp = (dummyList[i] as MessageViewModel)
                 if(temp.startTime == pojo.startTime
                        && temp.messageId == pojo.msgId.toString()){
                    return temp
                }
            }
        }

        return null
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return topChatRoomWebSocketMessageMapper.map(pojo)
    }

//    override fun sendMessage(messageId: String, messageText: String) {
//        if (isValidReply(messageText)) {
//            val startTime = SendableViewModel.generateStartTime()
//            view.clearEditText()
////            view.disableAction()
//            processDummyMessage(messageText, startTime)
////            when (networkMode) {
////                MODE_WEBSOCKET ->
//            sendMessageWebSocket(TopChatWebSocketParam.generateParamSendMessage(thisMessageId, messageText, startTime))
////            }
//        }
//    }

    override fun sendMessageWithWebsocket(messageId: String, sendMessage: String, startTime: String, opponentId: String) {
        if (isValidReply(sendMessage)) {
            view.clearEditText()
//            view.disableAction()
            processDummyMessage(sendMessage, startTime)
            sendMessageWebSocket(TopChatWebSocketParam.generateParamSendMessage(messageId, sendMessage, startTime))
            sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(messageId))
        }
    }

    override fun sendMessageWithApi(messageId: String, sendMessage: String, startTime: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun sendMessageWebSocket(messageText: String) {
        RxWebSocket.send(
                msg = messageText,
                tkpdAuthInterceptor = null,
                fingerprintInterceptor = null
        )
    }


    private fun isValidReply(message: String): Boolean {
        if (message.trim { it <= ' ' }.isEmpty()) {
            view.showSnackbarError(view.getStringResource(R.string.error_empty_product))
            return false
        }
        return true
    }

    override fun detachView() {
        destroyWebSocket()
        super.detachView()
    }

    override fun startTyping() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStartTyping(thisMessageId))
    }

    override fun stopTyping() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(thisMessageId))
    }
}