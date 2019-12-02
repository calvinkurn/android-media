package com.tokopedia.topchat.chatroom.view.presenter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_API
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.network.ChatUrl.Companion.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatImageUploadPojo
import com.tokopedia.topchat.chatroom.domain.subscriber.*
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel
import com.tokopedia.topchat.common.TopChatRouter
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
        private var getShopFollowingUseCase: GetShopFollowingUseCase,
        private var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
        private var addToCartUseCase: AddToCartUseCase,
        private var compressImageUseCase: CompressImageUseCase)
    : BaseChatPresenter<TopChatContract.View>(userSession, topChatRoomWebSocketMessageMapper), TopChatContract.Presenter {

    override fun clearText() {
    }

    private var mSubscription: CompositeSubscription
    private var compressImageSubscription: CompositeSubscription
    private var listInterceptor: ArrayList<Interceptor>

    private lateinit var webSocketUrl: String
    private lateinit var subject: PublishSubject<Boolean>
    private var isUploading: Boolean = false
    private var dummyList: ArrayList<Visitable<*>>
    var thisMessageId: String = ""
    private lateinit var addToCardSubscriber: Subscriber<AddToCartDataModel>

    private var attachmentsPreview: ArrayList<SendablePreview> = arrayListOf()

    init {
        mSubscription = CompositeSubscription()
        compressImageSubscription = CompositeSubscription()
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

    fun getTemplate(isSeller: Boolean) {
        getTemplateChatRoomUseCase.execute(
                GetTemplateChatRoomUseCase.generateParam(isSeller),
                object : Subscriber<GetTemplateViewModel>() {
                    override fun onNext(t: GetTemplateViewModel?) {
                        val templateList = arrayListOf<Visitable<Any>>()
                        t?.let {
                            if (t.isEnabled) {
                                t.listTemplate?.let {
                                    templateList.addAll(it)
                                }
                            }
                        }
                        view.onSuccessGetTemplate(templateList)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        view.onErrorGetTemplate()
                    }

                }
        )
    }

    private fun readMessage() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamRead(thisMessageId))
    }


    override fun startCompressImages(it: ImageUploadViewModel) {
        if (validateImageAttachment(it.imageUrl)) {
            it.imageUrl?.let { it1 ->
                val subscription = compressImageUseCase.compressImage(it1)
                        .subscribe(object : Subscriber<String>() {
                            override fun onNext(compressedImageUrl: String?) {
                                it.imageUrl = compressedImageUrl
                                startUploadImages(it)
                            }

                            override fun onCompleted() {
                            }

                            override fun onError(e: Throwable?) {
                                view.showSnackbarError(view.getStringResource(R.string.error_compress_image))
                            }
                        })
                compressImageSubscription?.clear()
                compressImageSubscription?.add(subscription)
            }
        }
    }

    override fun startUploadImages(it: ImageUploadViewModel) {
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

    private fun validateImageAttachment(uri: String?): Boolean {
        var MAX_FILE_SIZE = 15360
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

    override fun addProductToCart(
            router: TopChatRouter,
            element: ProductAttachmentViewModel,
            onError: (Throwable) -> Unit,
            onSuccess: (addToCartResult: AddToCartDataModel) -> Unit,
            shopId: Int
    ) {
        addToCardSubscriber = addToCartSubscriber(onError, onSuccess)

        val addToCartRequestParams = AddToCartRequestParams()
        addToCartRequestParams.productId = Integer.parseInt(element.productId.toString()).toLong()
        addToCartRequestParams.shopId = shopId
        addToCartRequestParams.quantity = 1
        addToCartRequestParams.notes = ""

        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addToCardSubscriber)
    }

    private fun addToCartSubscriber(
            onError: (Throwable) -> Unit,
            onSuccess: (addToCartResult: AddToCartDataModel) -> Unit
    ): Subscriber<AddToCartDataModel> {
        return object : Subscriber<AddToCartDataModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                onError(e)
            }

            override fun onNext(addToCartResult: AddToCartDataModel) {
                onSuccess(addToCartResult)
            }
        }
    }

    override fun sendAttachmentsAndMessage(
            messageId: String,
            sendMessage: String,
            startTime: String,
            opponentId: String,
            onSendingMessage: () -> Unit
    ) {
        if (isValidReply(sendMessage)) {
            sendAttachments(messageId, opponentId)
            sendMessage(messageId, sendMessage, startTime, opponentId, onSendingMessage)
        }
    }

    private fun sendAttachments(messageId: String, opponentId: String) {
        if (attachmentsPreview.isEmpty()) return
        attachmentsPreview.forEach { attachment ->
            attachment.sendTo(messageId, opponentId, listInterceptor)
            view.sendAnalyticAttachmentSent(attachment)
        }
        view.notifyAttachmentsSent()
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
        getTemplateChatRoomUseCase.unsubscribe()
        replyChatUseCase.unsubscribe()
        getExistingMessageIdUseCase.unsubscribe()
        deleteMessageListUseCase.unsubscribe()
        changeChatBlockSettingUseCase.unsubscribe()
        getShopFollowingUseCase.unsubscribe()
        addToCartUseCase.unsubscribe()
        if (::addToCardSubscriber.isInitialized) {
            addToCardSubscriber.unsubscribe()
        }
        compressImageSubscription.unsubscribe()
        super.detachView()
    }

    override fun startTyping() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStartTyping(thisMessageId))
    }

    override fun stopTyping() {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(thisMessageId))
    }

    override fun copyVoucherCode(fromUid: String?, replyId: String, blastId: String, attachmentId: String, replyTime: String?) {
        sendMessageWebSocket(TopChatWebSocketParam.generateParamCopyVoucherCode(thisMessageId, replyId, blastId, attachmentId, replyTime, fromUid))
    }

    override fun followUnfollowShop(shopId: String,
                                    onError: (Throwable) -> Unit,
                                    onSuccess: (isSuccess: Boolean) -> Unit) {
        toggleFavouriteShopUseCase.execute(
                ToggleFavouriteShopUseCase.createRequestParam(shopId), object : Subscriber<Boolean>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                onError(e)
            }

            override fun onNext(success: Boolean) {
                onSuccess(success)
            }
        })
    }

    override fun initProductPreview(savedInstanceState: Bundle?) {
        val stringProductPreviews = view.getStringArgument(ApplinkConst.Chat.PRODUCT_PREVIEWS, savedInstanceState)

        if (stringProductPreviews.isEmpty()) return

        val listType = object : TypeToken<List<ProductPreview>>() {}.type
        val productPreviews = CommonUtil.fromJson<List<ProductPreview>>(
                stringProductPreviews,
                listType
        )

        for (productPreview in productPreviews) {
            if (productPreview.notEnoughRequiredData()) continue
            val sendAbleProductPreview = SendableProductPreview(productPreview)
            attachmentsPreview.add(sendAbleProductPreview)
        }

    }

    override fun initInvoicePreview(savedInstanceState: Bundle?) {
        val id = view.getStringArgument(ApplinkConst.Chat.INVOICE_ID, savedInstanceState)
        val invoiceCode = view.getStringArgument(ApplinkConst.Chat.INVOICE_CODE, savedInstanceState)
        val productName = view.getStringArgument(ApplinkConst.Chat.INVOICE_TITLE, savedInstanceState)
        val date = view.getStringArgument(ApplinkConst.Chat.INVOICE_DATE, savedInstanceState)
        val imageUrl = view.getStringArgument(ApplinkConst.Chat.INVOICE_IMAGE_URL, savedInstanceState)
        val invoiceUrl = view.getStringArgument(ApplinkConst.Chat.INVOICE_URL, savedInstanceState)
        val statusId = view.getStringArgument(ApplinkConst.Chat.INVOICE_STATUS_ID, savedInstanceState)
        val status = view.getStringArgument(ApplinkConst.Chat.INVOICE_STATUS, savedInstanceState)
        val totalPriceAmount = view.getStringArgument(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, savedInstanceState)

        val invoiceViewModel = InvoicePreviewViewModel(
                id.toIntOrNull() ?: InvoicePreviewViewModel.INVALID_ID,
                invoiceCode,
                productName,
                date,
                imageUrl,
                invoiceUrl,
                statusId.toIntOrNull() ?: InvoicePreviewViewModel.INVALID_ID,
                status,
                totalPriceAmount
        )

        attachmentsPreview.add(invoiceViewModel)

        if (invoiceViewModel.notEnoughRequiredData()) {
            attachmentsPreview.remove(invoiceViewModel)
        }
    }

    override fun initAttachmentPreview() {
        if (attachmentsPreview.isEmpty()) return
        view.showAttachmentPreview(attachmentsPreview)
        view.focusOnReply()
    }

    override fun clearAttachmentPreview() {
        attachmentsPreview.clear()
    }

    override fun getAtcPageIntent(context: Context?, element: ProductAttachmentViewModel): Intent {
        val quantity = "1"
        val atcAndBuyAction = "1"
        val needRefresh = true
        val shopName = view?.getShopName()
        return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, element.shopId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, element.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
            putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, element.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcAndBuyAction)
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopName)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
            putExtra(ApplinkConst.Transaction.EXTRA_REFERENCE, ApplinkConst.TOPCHAT)
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, element.categoryId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_LABEL, element.getAtcEventLabel())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, element.getAtcEventAction())
        }
    }
}