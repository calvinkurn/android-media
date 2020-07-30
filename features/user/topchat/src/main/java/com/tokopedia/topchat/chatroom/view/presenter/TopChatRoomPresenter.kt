package com.tokopedia.topchat.chatroom.view.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_API
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.network.ChatUrl.Companion.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.purchase_platform.common.constant.ATC_AND_BUY
import com.tokopedia.purchase_platform.common.constant.ATC_ONLY
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.subscriber.ChangeChatBlockSettingSubscriber
import com.tokopedia.topchat.chatroom.domain.subscriber.DeleteMessageAllSubscriber
import com.tokopedia.topchat.chatroom.domain.subscriber.GetExistingMessageIdSubscriber
import com.tokopedia.topchat.chatroom.domain.subscriber.GetShopFollowingStatusSubscriber
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableVoucherPreview
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import okhttp3.Interceptor
import okhttp3.WebSocket
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.io.File
import javax.inject.Inject

/**
 * @author : Steven 11/12/18
 */

class TopChatRoomPresenter @Inject constructor(
        tkpdAuthInterceptor: TkpdAuthInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        userSession: UserSessionInterface,
        private val webSocketUtil: RxWebSocketUtil,
        private var getChatUseCase: GetChatUseCase,
        private var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
        private var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
        private var replyChatUseCase: ReplyChatUseCase,
        private var getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
        private var deleteMessageListUseCase: DeleteMessageListUseCase,
        private var changeChatBlockSettingUseCase: ChangeChatBlockSettingUseCase,
        private var getShopFollowingUseCase: GetShopFollowingUseCase,
        private var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
        private var addToCartUseCase: AddToCartUseCase,
        private var compressImageUseCase: CompressImageUseCase,
        private var seamlessLoginUsecase: SeamlessLoginUsecase,
        private var getChatRoomSettingUseCase: GetChatRoomSettingUseCase,
        private var addWishListUseCase: AddWishListUseCase,
        private var removeWishListUseCase: RemoveWishListUseCase,
        private var uploadImageUseCase: TopchatUploadImageUseCase,
        private var orderProgressUseCase: OrderProgressUseCase,
        private val groupStickerUseCase: ChatListGroupStickerUseCase,
        private val chatAttachmentUseCase: ChatAttachmentUseCase,
        private val sharedPref: SharedPreferences
) : BaseChatPresenter<TopChatContract.View>(userSession, topChatRoomWebSocketMessageMapper),
        TopChatContract.Presenter {

    var autoRetryConnectWs = true
    var newUnreadMessage = 0
    var thisMessageId: String = ""
    val attachments: ArrayMap<String, Attachment> = ArrayMap()

    private lateinit var webSocketUrl: String
    private lateinit var addToCardSubscriber: Subscriber<AddToCartDataModel>
    private var attachmentsPreview: ArrayList<SendablePreview> = arrayListOf()
    private var mSubscription: CompositeSubscription
    private var compressImageSubscription: CompositeSubscription
    private var listInterceptor: ArrayList<Interceptor>
    private var dummyList: ArrayList<Visitable<*>>

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
                networkMode = MODE_WEBSOCKET
                view?.showErrorWebSocket(false)
                readMessage()
            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                mappingEvent(webSocketResponse, messageId)
            }

            override fun onReconnect() {
                networkMode = MODE_API
                view?.showErrorWebSocket(true)
            }

            override fun onClose() {
                networkMode = MODE_API
                destroyWebSocket()
                view?.showErrorWebSocket(true)
                if (autoRetryConnectWs) {
                    connectWebSocket(messageId)
                }
            }

        }

        val rxWebSocket = webSocketUtil.getWebSocketInfo(webSocketUrl, userSession.accessToken)
        val subscription = rxWebSocket?.subscribe(subscriber)

        mSubscription?.add(subscription)
    }

    override fun destroyWebSocket() {
        mSubscription.clear()
        mSubscription.unsubscribe()
    }

    override fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo = topChatRoomWebSocketMessageMapper.parseResponse(webSocketResponse)
        if (pojo.msgId.toString() != messageId) return
        when (webSocketResponse.code) {
            EVENT_TOPCHAT_TYPING -> {
                if (!isInTheMiddleOfThePage()) {
                    view?.onReceiveStartTypingEvent()
                }
            }
            EVENT_TOPCHAT_END_TYPING -> {
                if (!isInTheMiddleOfThePage()) {
                    view?.onReceiveStopTypingEvent()
                }
            }
            EVENT_TOPCHAT_READ_MESSAGE -> {
                if (!isInTheMiddleOfThePage()) {
                    view?.onReceiveReadEvent()
                }
            }
            EVENT_TOPCHAT_REPLY_MESSAGE -> {
                if (!isInTheMiddleOfThePage()) {
                    onReplyMessage(pojo)
                    newUnreadMessage = 0
                    view?.hideUnreadMessage()
                } else {
                    if (pojo.isOpposite) {
                        newUnreadMessage++
                        view?.showUnreadMessage(newUnreadMessage)
                    }
                }
            }
        }
    }

    private fun onReplyMessage(pojo: ChatSocketPojo) {
        val temp = mapToVisitable(pojo)
        view?.onReceiveMessageEvent(temp)
        if (!pojo.isOpposite) {
            checkDummyAndRemove(temp)
        } else {
            readMessage()
        }
    }

    override fun getExistingChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccessGetExistingMessage: (ChatroomViewModel, ChatReplies) -> Unit) {
        if (messageId.isNotEmpty()) {
            getChatUseCase.getFirstPageChat(
                    messageId = messageId,
                    onSuccess = onSuccessGetExistingMessage,
                    onErrorGetChat = onError
            )
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

    override fun loadTopChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccessGetPreviousChat: (ChatroomViewModel, ChatReplies) -> Unit
    ) {
        if (messageId.isNotEmpty()) {
            getChatUseCase.getTopChat(
                    messageId = messageId,
                    onSuccess = onSuccessGetPreviousChat,
                    onErrorGetChat = onError
            )
        }
    }

    override fun loadBottomChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onsuccess: (ChatroomViewModel, ChatReplies) -> Unit
    ) {
        if (messageId.isNotEmpty()) {
            getChatUseCase.getBottomChat(messageId, onsuccess, onError)
        }
    }

    fun getTemplate(isSeller: Boolean) {
        getTemplateChatRoomUseCase.execute(
                GetTemplateChatRoomUseCase.generateParam(isSeller),
                object : Subscriber<GetTemplateUiModel>() {
                    override fun onNext(t: GetTemplateUiModel?) {
                        val templateList = arrayListOf<Visitable<Any>>()
                        t?.let {
                            if (t.isEnabled) {
                                t.listTemplate?.let {
                                    templateList.addAll(it)
                                }
                            }
                        }
                        view?.onSuccessGetTemplate(templateList)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        view?.onErrorGetTemplate()
                    }

                }
        )
    }

    override fun readMessage() {
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
                                showErrorSnackbar(R.string.error_compress_image)
                            }
                        })
                compressImageSubscription?.clear()
                compressImageSubscription?.add(subscription)
            }
        }
    }

    override fun startUploadImages(image: ImageUploadViewModel) {
        processDummyMessage(image)
        uploadImageUseCase.upload(image, ::onSuccessUploadImage, ::onErrorUploadImage)
    }

    private fun onSuccessUploadImage(uploadId: String, image: ImageUploadViewModel) {
        when (networkMode) {
            MODE_WEBSOCKET -> sendImageByWebSocket(uploadId, image)
            MODE_API -> sendImageByApi(uploadId, image)
        }
    }

    private fun onErrorUploadImage(throwable: Throwable, image: ImageUploadViewModel) {
        view?.onErrorUploadImage(ErrorHandler.getErrorMessage(view?.context, throwable), image)
    }

    private fun sendImageByWebSocket(uploadId: String, image: ImageUploadViewModel) {
        val requestParams = TopChatWebSocketParam.generateParamSendImage(thisMessageId, uploadId, image.startTime)
        sendMessageWebSocket(requestParams)
    }

    private fun sendImageByApi(uploadId: String, image: ImageUploadViewModel) {
        val requestParams = ReplyChatUseCase.generateParamAttachImage(thisMessageId, uploadId)
        sendByApi(requestParams, image)
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
            showErrorSnackbar(R.string.undersize_image)
            false
        } else if (fileSize >= MAX_FILE_SIZE) {
            showErrorSnackbar(R.string.oversize_image)
            false
        } else {
            true
        }
    }

    override fun isUploading(): Boolean {
        return uploadImageUseCase.isUploading
    }

    private fun processDummyMessage(it: Visitable<*>) {
        view?.addDummyMessage(it)
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
        sendMessageWebSocket(TopChatWebSocketParam.generateParamSendMessage(messageId, sendMessage, startTime, attachmentsPreview))
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
                    view?.onReceiveMessageEvent(response.chat)
                    checkDummyAndRemove(dummyMessage);
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view?.showSnackbarError(ErrorHandler.getErrorMessage(view?.context, e))
            }

        })
    }

    private fun checkDummyAndRemove(dummyMessage: Visitable<*>) {
        getDummyOnList(dummyMessage)?.let {
            view?.removeDummy(it)
        }
    }

    override fun showErrorSnackbar(@StringRes stringId: Int) {
        view?.let {
            it.showSnackbarError(it.getStringResource(stringId))
        }
    }

    private fun sendMessageWebSocket(messageText: String) {
        RxWebSocket.send(messageText, listInterceptor)
    }

    override fun sendAttachmentsAndMessage(
            messageId: String,
            sendMessage: String,
            startTime: String,
            opponentId: String,
            onSendingMessage: () -> Unit
    ) {
        if (isValidReply(sendMessage)) {
            sendAttachments(messageId, opponentId, sendMessage)
            sendMessage(messageId, sendMessage, startTime, opponentId, onSendingMessage)
            view?.clearAttachmentPreviews()
        }
    }

    override fun sendAttachmentsAndSticker(
            messageId: String,
            sticker: Sticker,
            startTime: String,
            opponentId: String,
            onSendingMessage: () -> Unit
    ) {
        sendAttachments(messageId, opponentId, sticker.intention)
        sendSticker(messageId, sticker, startTime, opponentId, onSendingMessage)
        view?.clearAttachmentPreviews()
    }

    private fun sendSticker(
            messageId: String,
            sticker: Sticker,
            startTime: String,
            opponentId: String,
            onSendingMessage: () -> Unit
    ) {
        onSendingMessage()
        processDummyMessage(mapToDummySticker(messageId, sticker, startTime))
        sendStickerWithWebSocket(messageId, sticker, opponentId, startTime)
    }

    private fun mapToDummySticker(messageId: String, sticker: Sticker, startTime: String): Visitable<*> {
        return StickerUiModel(
                messageId, userSession.userId, userSession.name, startTime, sticker.generateStickerProfile()
        )
    }

    private fun sendStickerWithWebSocket(
            messageId: String,
            sticker: Sticker,
            opponentId: String,
            startTime: String
    ) {
        val stickerContract = sticker.generateWebSocketPayload(messageId, opponentId, startTime, attachmentsPreview)
        val stringContract = CommonUtil.toJson(stickerContract)
        RxWebSocket.send(stringContract, listInterceptor)
    }

    private fun sendAttachments(messageId: String, opponentId: String, message: String) {
        if (attachmentsPreview.isEmpty()) return
        attachmentsPreview.forEach { attachment ->
            attachment.sendTo(messageId, opponentId, message, listInterceptor)
            view?.sendAnalyticAttachmentSent(attachment)
        }
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
        groupStickerUseCase.safeCancel()
        chatAttachmentUseCase.safeCancel()
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
        val stringProductPreviews = view?.getStringArgument(ApplinkConst.Chat.PRODUCT_PREVIEWS, savedInstanceState)

        if (stringProductPreviews == null || stringProductPreviews.isEmpty()) return

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
        val id = view?.getStringArgument(ApplinkConst.Chat.INVOICE_ID, savedInstanceState) ?: ""
        val invoiceCode = view?.getStringArgument(ApplinkConst.Chat.INVOICE_CODE, savedInstanceState)
                ?: ""
        val productName = view?.getStringArgument(ApplinkConst.Chat.INVOICE_TITLE, savedInstanceState)
                ?: ""
        val date = view?.getStringArgument(ApplinkConst.Chat.INVOICE_DATE, savedInstanceState) ?: ""
        val imageUrl = view?.getStringArgument(ApplinkConst.Chat.INVOICE_IMAGE_URL, savedInstanceState)
                ?: ""
        val invoiceUrl = view?.getStringArgument(ApplinkConst.Chat.INVOICE_URL, savedInstanceState)
                ?: ""
        val statusId = view?.getStringArgument(ApplinkConst.Chat.INVOICE_STATUS_ID, savedInstanceState)
                ?: ""
        val status = view?.getStringArgument(ApplinkConst.Chat.INVOICE_STATUS, savedInstanceState)
                ?: ""
        val totalPriceAmount = view?.getStringArgument(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, savedInstanceState)
                ?: ""

        val invoiceViewModel = InvoicePreviewUiModel(
                id.toIntOrNull() ?: InvoicePreviewUiModel.INVALID_ID,
                invoiceCode,
                productName,
                date,
                imageUrl,
                invoiceUrl,
                statusId.toIntOrNull() ?: InvoicePreviewUiModel.INVALID_ID,
                status,
                totalPriceAmount
        )

        if (invoiceViewModel.enoughRequiredData()) {
            clearAttachmentPreview()
            attachmentsPreview.add(invoiceViewModel)
        }
    }

    override fun initAttachmentPreview() {
        if (attachmentsPreview.isEmpty()) return
        view?.showAttachmentPreview(attachmentsPreview)
        view?.focusOnReply()
    }

    override fun clearAttachmentPreview() {
        attachmentsPreview.clear()
    }

    override fun getAtcPageIntent(
            context: Context?,
            element: ProductAttachmentViewModel,
            sourcePage: String
    ): Intent {
        val quantity = element.minOrder
        val atcOnly = ATC_ONLY
        val needRefresh = true
        val shopName = view?.getShopName()
        return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, element.shopId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, element.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
            putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, element.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcOnly)
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopName)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
            putExtra(ApplinkConst.Transaction.EXTRA_REFERENCE, ApplinkConst.TOPCHAT)
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, element.categoryId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_LABEL, element.getAtcEventLabel())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, element.getAtcEventAction())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_DIMENSION40, element.getAtcDimension40(sourcePage))
            putExtra(ApplinkConst.Transaction.EXTRA_ATC_EXTERNAL_SOURCE, AddToCartRequestParams.ATC_FROM_TOPCHAT)
        }
    }

    override fun getBuyPageIntent(
            context: Context?,
            element: ProductAttachmentViewModel,
            sourcePage: String
    ): Intent {
        val quantity = element.minOrder
        val atcAndBuyAction = ATC_AND_BUY
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
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, element.categoryId)
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, element.category)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, element.productName)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, element.priceInt.toFloat())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_LABEL, element.getAtcEventLabel())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, element.getBuyEventAction())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_DIMENSION40, element.getAtcDimension40(sourcePage))
            putExtra(ApplinkConst.Transaction.EXTRA_ATC_EXTERNAL_SOURCE, AddToCartRequestParams.ATC_FROM_TOPCHAT)
        }
    }

    override fun initProductPreviewFromAttachProduct(resultProducts: ArrayList<ResultProduct>) {
        if (resultProducts.isNotEmpty()) clearAttachmentPreview()
        for (resultProduct in resultProducts) {
            val productPreview = ProductPreview(
                    id = resultProduct.productId.toString(),
                    imageUrl = resultProduct.productImageThumbnail,
                    name = resultProduct.name,
                    price = resultProduct.price,
                    url = resultProduct.productUrl
            )
            if (productPreview.notEnoughRequiredData()) continue
            val sendAbleProductPreview = SendableProductPreview(productPreview)
            attachmentsPreview.add(sendAbleProductPreview)
        }
        initAttachmentPreview()
    }

    override fun initVoucherPreview(extras: Bundle?) {
        val stringVoucherPreview = view?.getStringArgument(ApplinkConst.AttachVoucher.PARAM_VOUCHER_PREVIEW, extras)

        if (stringVoucherPreview == null || stringVoucherPreview.isEmpty()) return

        val voucherPreview = CommonUtil.fromJson<VoucherPreview>(stringVoucherPreview, VoucherPreview::class.java)
        val sendableVoucher = SendableVoucherPreview(voucherPreview)
        if (attachmentsPreview.isNotEmpty()) clearAttachmentPreview()
        attachmentsPreview.add(sendableVoucher)
    }

    override fun onClickBannedProduct(liteUrl: String) {
        val seamlessLoginSubscriber = createSeamlessLoginSubscriber(liteUrl)
        seamlessLoginUsecase.generateSeamlessUrl(liteUrl, seamlessLoginSubscriber)
    }

    private fun createSeamlessLoginSubscriber(liteUrl: String): SeamlessLoginSubscriber {
        return object : SeamlessLoginSubscriber {
            override fun onUrlGenerated(url: String) {
                view?.redirectToBrowser(url)
            }

            override fun onError(msg: String) {
                view?.redirectToBrowser(liteUrl)
            }
        }
    }

    override fun loadChatRoomSettings(
            messageId: String,
            onSuccess: (List<Visitable<TopChatTypeFactory>>) -> Unit
    ) {
        getChatRoomSettingUseCase.execute(messageId, onSuccess)
    }

    override fun addToWishList(
            productId: String,
            userId: String,
            wishlistActionListener: WishListActionListener
    ) {
        addWishListUseCase.createObservable(productId, userId, wishlistActionListener)
    }

    override fun removeFromWishList(productId: String, userId: String, wishListActionListener: WishListActionListener) {
        removeWishListUseCase.createObservable(productId, userId, wishListActionListener)
    }

    override fun clearText() {}

    override fun getOrderProgress(messageId: String) {
        orderProgressUseCase.getOrderProgress(
                messageId,
                ::onSuccessGetOrderProgress,
                ::onErrorGetOrderProgress
        )
    }

    override fun getStickerGroupList(chatRoom: ChatroomViewModel) {
        groupStickerUseCase.getStickerGroup(
                chatRoom.isSeller(), ::onLoadingStickerGroup, ::onSuccessGetStickerGroup,
                ::onErrorGetStickerGroup
        )
    }

    override fun loadAttachmentData(msgId: Int, chatRoom: ChatroomViewModel) {
        if (chatRoom.hasAttachment() && msgId != 0) {
            chatAttachmentUseCase.getAttachments(
                    msgId, chatRoom.attachmentIds, ::onSuccessGetAttachments, ::onErrorGetAttachments
            )
        }
    }

    override fun isStickerTooltipAlreadyShow(): Boolean {
        return sharedPref.getBoolean(STICKER_TOOLTIP_ONBOARDING, false)
    }

    override fun toolTipOnBoardingShown() {
        sharedPref.edit().putBoolean(STICKER_TOOLTIP_ONBOARDING, true).apply()
    }

    override fun setBeforeReplyTime(createTime: String) {
        getChatUseCase.minReplyTime = createTime
    }

    override fun isInTheMiddleOfThePage(): Boolean {
        return getChatUseCase.isInTheMiddleOfThePage()
    }

    override fun resetChatUseCase() {
        getChatUseCase.reset()
    }

    override fun resetUnreadMessage() {
        newUnreadMessage = 0
    }

    private fun onSuccessGetAttachments(attachments: ArrayMap<String, Attachment>) {
        this.attachments.putAll(attachments.toMap())
        view?.updateAttachmentsView(this.attachments)
    }

    private fun onErrorGetAttachments(throwable: Throwable, errorAttachment: ArrayMap<String, Attachment>) {
        this.attachments.putAll(errorAttachment.toMap())
        view?.updateAttachmentsView(this.attachments)
        println(throwable.message)
    }

    private fun onLoadingStickerGroup(response: ChatListGroupStickerResponse) {
        view?.getChatMenuView()?.stickerMenu
                ?.updateStickers(response.chatListGroupSticker.list)
    }

    private fun onSuccessGetStickerGroup(
            response: ChatListGroupStickerResponse,
            needToUpdate: List<StickerGroup>
    ) {
        view?.getChatMenuView()?.stickerMenu
                ?.updateStickers(response.chatListGroupSticker.list, needToUpdate)
    }

    private fun onErrorGetStickerGroup(throwable: Throwable) {

    }

    private fun onSuccessGetOrderProgress(orderProgressResponse: OrderProgressResponse) {
        view?.renderOrderProgress(orderProgressResponse.chatOrderProgress)
    }

    private fun onErrorGetOrderProgress(throwable: Throwable) {}

    companion object {
        const val STICKER_TOOLTIP_ONBOARDING = "sticker_tooltip_onboarding"
    }
}