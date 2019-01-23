package com.tokopedia.topchat.chatroom.view.presenter

import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_API
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.network.ChatUrl.Companion.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatImageUploadPojo
import com.tokopedia.topchat.chatroom.domain.subscriber.*
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel
import com.tokopedia.topchat.common.TopChatRouter
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.WebSocket
import okio.ByteString
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.io.File
import javax.inject.Inject

/**
 * @author : Steven 11/12/18
 */

class TopChatRoomPresenter @Inject constructor(
        private var getChatUseCase: GetChatUseCase,
        override var userSession: UserSessionInterface,
        private val tkpdAuthInterceptor: TkpdAuthInterceptor,
        private val fingerprintInterceptor: FingerprintInterceptor,
        private var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
        private var uploadImageUseCase: UploadImageUseCase<TopChatImageUploadPojo>,
        private var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
        private var replyChatUseCase: ReplyChatUseCase,
        private var getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
        private var deleteMessageListUseCase: DeleteMessageListUseCase,
        private var changeChatBlockSettingUseCase: ChangeChatBlockSettingUseCase,
        private var getShopFollowingUseCase: GetShopFollowingUseCase)
    : BaseChatPresenter<TopChatContract.View>(userSession, topChatRoomWebSocketMessageMapper), TopChatContract.Presenter {

    private var mSubscription: CompositeSubscription
    private var listInterceptor: ArrayList<Interceptor>

    private lateinit var webSocketUrl: String
    private lateinit var subject: PublishSubject<Boolean>
    private var isUploading: Boolean = false
    private var dummyList: ArrayList<Visitable<*>>
    var thisMessageId: String = ""

    init {
        mSubscription = CompositeSubscription()
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
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
                view.showErrorWebSocket(false)
                networkMode = WebsocketEvent.Mode.MODE_WEBSOCKET
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
                networkMode = WebsocketEvent.Mode.MODE_API
                view.showErrorWebSocket(true)
            }

            override fun onClose() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onClose")
                }
                networkMode = WebsocketEvent.Mode.MODE_API
                destroyWebSocket()
                view.showErrorWebSocket(true)
                connectWebSocket(messageId)
            }

        }

        var rxWebSocket = RxWebSocket[webSocketUrl, userSession.accessToken]
        val subscription = rxWebSocket?.subscribe(subscriber)

        mSubscription?.add(subscription)
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
                var temp = mapToVisitable(pojo)
                view.onReceiveMessageEvent(temp)
                if (!pojo.isOpposite) {
                    checkDummyAndRemove(temp)
                } else {
                    readMessage()
                }
            }
        }
    }

    override fun getExistingChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccessGetExistingMessage: (ChatroomViewModel) -> Unit) {
        if (messageId.isNotEmpty()) {
            getChatUseCase.execute(GetChatUseCase.generateParamFirstTime(messageId),
                    GetChatSubscriber(onError, onSuccessGetExistingMessage))
        }
    }


    override fun getChatCache(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccessGetExistingMessage: (ChatroomViewModel) -> Unit) {
        if (messageId.isNotEmpty()) {
            var temp = true
            getChatUseCase.getCache(GetChatUseCase.generateParamFirstTime(messageId),
                    GetChatSubscriber(onError, onSuccessGetExistingMessage))
        }
    }

    override fun getMessageId(toUserId: String,
                              toShopId: String,
                              source: String,
                              onError: (Throwable) -> Unit,
                              onSuccessGetMessageId: (String) -> Unit) {
        getExistingMessageIdUseCase.execute(GetExistingMessageIdUseCase.generateParam(
                toShopId, toUserId, source), GetExistingMessageIdSubscriber(
                onError, onSuccessGetMessageId
        ))
    }


    override fun loadPreviousChat(
            messageId: String,
            page: Int,
            onError: (Throwable) -> Unit,
            onSuccessGetPreviousChat: (ChatroomViewModel) -> Unit
    ) {
        if (messageId.isNotEmpty()) {
            getChatUseCase.execute(GetChatUseCase.generateParam(messageId, page),
                    GetChatSubscriber(onError, onSuccessGetPreviousChat))
        }
    }

    fun getTemplate() {
        getTemplateChatRoomUseCase.execute(object : Subscriber<GetTemplateViewModel>() {
            override fun onNext(t: GetTemplateViewModel?) {
                val templateList = arrayListOf<Visitable<Any>>()
                t?.let {
                    if (t.isEnabled) {
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

    override fun startUploadImages(it: ImageUploadViewModel) {
        if (validateImageAttachment(it.imageUrl)) {
            processDummyMessage(it)
            isUploading = true
            uploadImageUseCase.unsubscribe()
            val reqParam = HashMap<String, RequestBody>()
            RequestBody.create(MediaType.parse("text/plain"), "1")
            reqParam["web_service"] = createRequestBody("1")
            reqParam["id"] = createRequestBody(String.format("%s%s", userSession.userId, it.imageUrl))
            val params = uploadImageUseCase.createRequestParam(it.imageUrl, "/upload/attachment", "fileToUpload\"; filename=\"image.jpg", reqParam)
            uploadImageUseCase.execute(params, object : Subscriber<ImageUploadDomainModel<TopChatImageUploadPojo>>() {
                override fun onNext(t: ImageUploadDomainModel<TopChatImageUploadPojo>) {
                    t.dataResultImageUpload.data?.run {
                        when (networkMode) {
                            MODE_API -> sendByApi(
                                    ReplyChatUseCase.generateParamAttachImage(thisMessageId, this.picSrc),
                                    it
                            )
                            MODE_WEBSOCKET -> sendMessageWebSocket(TopChatWebSocketParam.generateParamSendImage(thisMessageId,
                                    this.picSrc, it.startTime))
                        }
                    }
                    isUploading = false
                }


                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    isUploading = false
                    view.onErrorUploadImage(ErrorHandler.getErrorMessage(view.context, e), it)
                }

            })
        }
    }

    private fun validateImageAttachment(uri: String?): Boolean {
        var MAX_FILE_SIZE = 5120
        val MINIMUM_HEIGHT = 100
        val MINIMUM_WIDTH = 300
        val DEFAULT_ONE_MEGABYTE: Long = 1024
        if (uri == null) return false
        val file = File(uri)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        val fileSize = Integer.parseInt((file.length() / DEFAULT_ONE_MEGABYTE).toString())

        return if (imageHeight < MINIMUM_HEIGHT || imageWidth < MINIMUM_WIDTH) {
            view.showSnackbarError(view.getStringResource(R.string.undersize_image))
            false
        } else if (fileSize >= MAX_FILE_SIZE) {
            view.showSnackbarError(view.getStringResource(R.string.oversize_image))
            false
        } else {
            true
        }
    }

    override fun isUploading(): Boolean {
        return isUploading
    }

    private fun createRequestBody(content: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), content)
    }

    private fun processDummyMessage(it: Visitable<*>) {
        view.addDummyMessage(it)
        dummyList.add(it)
    }

    private fun mapToDummyMessage(messageId: String, messageText: String, startTime: String): Visitable<*> {
        return MessageViewModel(messageId, userSession.userId, userSession.name, startTime, messageText)
    }

    private fun getDummyOnList(visitable: Visitable<*>): Visitable<*>? {
        dummyList.isNotEmpty().let {
            for (i in 0 until dummyList.size) {
                var temp = (dummyList[i] as SendableViewModel)
                if (temp.startTime == (visitable as SendableViewModel).startTime
                        && temp.messageId == (visitable as SendableViewModel).messageId) {
                    return dummyList[i]
                }
            }
        }

        return null
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return topChatRoomWebSocketMessageMapper.map(pojo)
    }

    override fun sendMessageWithWebsocket(messageId: String, sendMessage: String, startTime: String, opponentId: String) {
        processDummyMessage(mapToDummyMessage(thisMessageId, sendMessage, startTime))
        sendMessageWebSocket(TopChatWebSocketParam.generateParamSendMessage(messageId, sendMessage, startTime))
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(messageId))
    }

    override fun sendMessageWithApi(messageId: String, sendMessage: String, startTime: String) {
        var dummyMessage = mapToDummyMessage(thisMessageId, sendMessage, startTime)
        processDummyMessage(dummyMessage)
        sendByApi(ReplyChatUseCase.generateParam(messageId, sendMessage), dummyMessage)
    }

    private fun sendByApi(requestParams: RequestParams, dummyMessage: Visitable<*>) {
        replyChatUseCase.execute(requestParams, object : Subscriber<ReplyChatViewModel>() {
            override fun onNext(response: ReplyChatViewModel) {
                if (response.isSuccessReplyChat) {
                    view.onReceiveMessageEvent(response.chat)
                    checkDummyAndRemove(dummyMessage);
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view.showSnackbarError(ErrorHandler.getErrorMessage(view.context, e))
            }

        })
    }

    private fun checkDummyAndRemove(dummyMessage: Visitable<*>) {
        getDummyOnList(dummyMessage)?.let {
            view.removeDummy(it)
        }
    }

    override fun showErrorSnackbar(stringId: Int) {
        view.showSnackbarError(view.getStringResource(stringId))
    }

    private fun sendMessageWebSocket(messageText: String) {
        RxWebSocket.send(messageText, listInterceptor)
    }


    private fun isValidReply(message: String): Boolean {
        if (message.trim { it <= ' ' }.isEmpty()) {
            view.showSnackbarError(view.getStringResource(R.string.error_empty_product))
            return false
        }
        return true
    }

    override fun addProductToCart(router: TopChatRouter, element: ProductAttachmentViewModel, onError: (Throwable) -> Unit, onSuccess: () -> Unit) {
        router.addToCartProduct(
                AddToCartRequest.Builder()
                        .productId(Integer.parseInt(element.productId.toString()))
                        .notes("")
                        .quantity(1)
                        .shopId(Integer.parseInt(element.fromUid))
                        .build(),
                false).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addToCartSubscriber())
    }

    private fun addToCartSubscriber(): Subscriber<AddToCartResult> {
        return object : Subscriber<AddToCartResult>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                Log.d("onError", e.toString())
            }

            override fun onNext(addToCartResult: AddToCartResult) {
//                view.dismissProgressDialog()
//                if (addToCartResult.cartId.isEmpty()) {
//                    wishListView.showAddToCartErrorMessage(addToCartResult.message)
//                } else {
//                    wishListView.showAddToCartMessage(addToCartResult.message)
//                }
//                wishListView.sendAddToCartAnalytics(dataDetail, addToCartResult)
                Log.d("onNext", addToCartResult.toString())
            }
        }
    }

    override fun sendProductAttachment(messageId: String, item: ResultProduct,
                                       startTime: String, opponentId: String) {

        RxWebSocket.send(SendWebsocketParam.generateParamSendProductAttachment(messageId, item, startTime,
                        opponentId), listInterceptor
        )
    }

    override fun deleteChat(messageId: String, onError: (Throwable) -> Unit, onSuccessDeleteConversation: () -> Unit) {
        deleteMessageListUseCase.execute(DeleteMessageListUseCase.generateParam(messageId),
                DeleteMessageAllSubscriber(onError, onSuccessDeleteConversation))
    }

    override fun unblockChat(messageId: String,
                             opponentRole: String,
                             onError: (Throwable) -> Unit,
                             onSuccessUnblockChat: (BlockedStatus) -> Unit) {
        changeChatBlockSettingUseCase.execute(
                ChangeChatBlockSettingUseCase.generateParam(
                        messageId,
                        ChangeChatBlockSettingUseCase.getBlockType(opponentRole),
                        false
                ), ChangeChatBlockSettingSubscriber(onError, onSuccessUnblockChat)
        )
    }

    override fun getShopFollowingStatus(shopId: Int, onError: (Throwable) -> Unit, onSuccessGetShopFollowingStatus: (Boolean) -> Unit) {
        getShopFollowingUseCase.execute(GetShopFollowingUseCase.generateParam(shopId),
                GetShopFollowingStatusSubscriber(onError, onSuccessGetShopFollowingStatus))
    }

    override fun detachView() {
        destroyWebSocket()
        getChatUseCase.unsubscribe()
        uploadImageUseCase.unsubscribe()
        getExistingMessageIdUseCase.unsubscribe()
        getTemplateChatRoomUseCase.unsubscribe()
        deleteMessageListUseCase.unsubscribe()
        changeChatBlockSettingUseCase.unsubscribe()
        getShopFollowingUseCase.unsubscribe()
        super.detachView()
    }

    override fun startTyping() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStartTyping(thisMessageId))
    }

    override fun stopTyping() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(thisMessageId))
    }

}