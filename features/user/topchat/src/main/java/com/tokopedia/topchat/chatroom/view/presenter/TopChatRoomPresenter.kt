package com.tokopedia.topchat.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.attachcommon.data.ResultProduct
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
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.subscriber.DeleteMessageAllSubscriber
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.WebSocket
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author : Steven 11/12/18
 */

open class TopChatRoomPresenter @Inject constructor(
        tkpdAuthInterceptor: TkpdAuthInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        userSession: UserSessionInterface,
        protected val webSocketUtil: RxWebSocketUtil,
        private var getChatUseCase: GetChatUseCase,
        private var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
        private var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
        private var replyChatUseCase: ReplyChatUseCase,
        private var getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
        private var deleteMessageListUseCase: DeleteMessageListUseCase,
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
        private val chatToggleBlockChat: ChatToggleBlockChatUseCase,
        private val chatBackgroundUseCase: ChatBackgroundUseCase,
        private val chatSrwUseCase: SmartReplyQuestionUseCase,
        private val sharedPref: SharedPreferences,
        private val dispatchers: CoroutineDispatchers,
        private val remoteConfig: RemoteConfig
) : BaseChatPresenter<TopChatContract.View>(userSession, topChatRoomWebSocketMessageMapper),
        TopChatContract.Presenter, CoroutineScope {

    var autoRetryConnectWs = true
    var newUnreadMessage = 0
    var thisMessageId: String = ""
    val attachments: ArrayMap<String, Attachment> = ArrayMap()
    val onGoingStockUpdate: ArrayMap<String, UpdateProductStockResult> = ArrayMap()
    private var userLocationInfo = LocalCacheModel()

    private lateinit var webSocketUrl: String
    private var attachmentsPreview: ArrayList<SendablePreview> = arrayListOf()
    private var mSubscription: CompositeSubscription
    private var compressImageSubscription: CompositeSubscription
    private var listInterceptor: ArrayList<Interceptor>
    private var dummyList: ArrayList<Visitable<*>>

    private val _srw = MutableLiveData<Resource<ChatSmartReplyQuestionResponse>>()
    val srw: LiveData<Resource<ChatSmartReplyQuestionResponse>>
        get() = _srw

    init {
        mSubscription = CompositeSubscription()
        compressImageSubscription = CompositeSubscription()
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
        dummyList = arrayListOf()
    }

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

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

    override fun initUserLocation(userLocation: LocalCacheModel?) {
        userLocation ?: return
        this.userLocationInfo = userLocation
    }

    override fun getProductIdPreview(): List<String> {
        return attachmentsPreview.filterIsInstance<SendableProductPreview>()
                .map { it.productId }
    }

    override fun getAttachmentsPreview(): List<SendablePreview> {
        return attachmentsPreview
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
                    view?.onSendAndReceiveMessage()
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
        getExistingMessageIdUseCase.getMessageId(
                toShopId, toUserId, source, onSuccessGetMessageId, onError
        )
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
        val isValidImage = ImageUtil.validateImageAttachment(it.imageUrl)
        if (isValidImage.first) {
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
        } else {
            when (isValidImage.second) {
                ImageUtil.IMAGE_UNDERSIZE -> showErrorSnackbar(R.string.undersize_image)
                ImageUtil.IMAGE_EXCEED_SIZE_LIMIT -> showErrorSnackbar(R.string.oversize_image)
            }
        }
    }

    override fun startUploadImages(image: ImageUploadViewModel) {
        if (isEnableUploadImageService()) {
            addDummyToService(image)
            startUploadImageWithService(image)
        } else {
            processDummyMessage(image)
            uploadImageUseCase.upload(image, ::onSuccessUploadImage, ::onErrorUploadImage)
        }
    }

    private fun addDummyToService(image: ImageUploadViewModel) {
        view?.addDummyMessage(image)
        val uploadImageDummy = UploadImageDummy(messageId = thisMessageId, visitable = image)
        UploadImageChatService.dummyMap.add(uploadImageDummy)
    }

    private fun startUploadImageWithService(image: ImageUploadViewModel) {
        UploadImageChatService.enqueueWork(view.context, ImageUploadMapper.mapToImageUploadServer(image), thisMessageId)
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

    override fun isUploading(): Boolean {
        return uploadImageUseCase.isUploading
    }

    private fun processDummyMessage(it: Visitable<*>) {
        view?.addDummyMessage(it)
        dummyList.add(it)
    }

    private fun getDummyOnList(visitable: Visitable<*>): Visitable<*>? {
        dummyList.isNotEmpty().let {
            for (i in 0 until dummyList.size) {
                val temp = (dummyList[i] as SendableViewModel)
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

    protected open fun sendMessageWebSocket(messageText: String) {
        RxWebSocket.send(messageText, listInterceptor)
    }

    protected open fun sendMessageJsonObjWebSocket(msgObj: JsonObject) {
        RxWebSocket.send(msgObj, listInterceptor)
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

    override fun sendAttachmentsAndSrw(
            messageId: String,
            question: QuestionUiModel,
            startTime: String,
            opponentId: String,
            onSendingMessage: () -> Unit
    ) {
        sendAttachments(messageId, opponentId, question.content)
        sendMessage(messageId, question.content, startTime, opponentId, question.intent)
        view?.clearAttachmentPreviews()
    }

    /**
     * sendMessage but with param [intention]
     * make sure the [sendMessage] is valid before sending msg
     */
    private fun sendMessage(
            messageId: String,
            sendMessage: String,
            startTime: String,
            opponentId: String,
            intention: String? = null
    ) {
        if (networkMode == MODE_WEBSOCKET) {
            topchatSendMessageWithWebsocket(
                    messageId, sendMessage, startTime, opponentId, intention
            )
        }
    }

    override fun sendSrwBubble(
            messageId: String, question: QuestionUiModel,
            products: List<SendablePreview>, opponentId: String,
            onSendingMessage: () -> Unit
    ) {
        if (networkMode == MODE_WEBSOCKET) {
            val startTime = SendableViewModel.generateStartTime()
            topchatSendMessageWithWebsocket(
                    messageId, question.content, startTime,
                    opponentId, question.intent, products
            )
        }
    }


    /**
     * send with websocket but with param [intention]
     */
    private fun topchatSendMessageWithWebsocket(
            messageId: String, sendMessage: String,
            startTime: String, opponentId: String,
            intention: String?
    ) {
        processDummyMessage(mapToDummyMessage(thisMessageId, sendMessage, startTime))
        sendMessageWebSocket(
                TopChatWebSocketParam.generateParamSendMessage(
                        messageId, sendMessage, startTime,
                        attachmentsPreview, intention, userLocationInfo
                )
        )
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(messageId))
    }

    private fun topchatSendMessageWithWebsocket(
            messageId: String, sendMessage: String,
            startTime: String, opponentId: String,
            intention: String?, products: List<SendablePreview>
    ) {
        processDummyMessage(mapToDummyMessage(thisMessageId, sendMessage, startTime))
        sendMessageWebSocket(
                TopChatWebSocketParam.generateParamSendMessage(
                        messageId, sendMessage, startTime, products, intention
                )
        )
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(messageId))
    }

    /**
     * recommended to use [topchatSendMessageWithWebsocket] instead
     */
    override fun sendMessageWithWebsocket(
            messageId: String, sendMessage: String,
            startTime: String, opponentId: String
    ) {
        processDummyMessage(mapToDummyMessage(thisMessageId, sendMessage, startTime))
        sendMessageWebSocket(TopChatWebSocketParam.generateParamSendMessage(
                messageId, sendMessage, startTime, attachmentsPreview)
        )
        sendMessageWebSocket(TopChatWebSocketParam.generateParamStopTyping(messageId))
    }

    private fun mapToDummyMessage(
            messageId: String, messageText: String, startTime: String
    ): Visitable<*> {
        return topChatRoomWebSocketMessageMapper.mapToDummyMessage(
                messageId, userSession.userId, userSession.name, startTime, messageText
        )
    }

    override fun sendMessageWithApi(messageId: String, sendMessage: String, startTime: String) {
        val dummyMessage = mapToDummyMessage(thisMessageId, sendMessage, startTime)
        processDummyMessage(dummyMessage)
        sendByApi(ReplyChatUseCase.generateParam(messageId, sendMessage), dummyMessage)
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

    private fun mapToDummySticker(
            messageId: String, sticker: Sticker, startTime: String
    ): Visitable<*> {
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
        sendMessageWebSocket(stringContract)
    }

    private fun sendAttachments(messageId: String, opponentId: String, message: String) {
        if (attachmentsPreview.isEmpty()) return
        attachmentsPreview.forEach { attachment ->
            val wsMsgPayload = attachment.generateMsgObj(
                    messageId, opponentId, message, listInterceptor, userLocationInfo
            )
            sendWebSocketAttachmentPayload(wsMsgPayload)
            view?.sendAnalyticAttachmentSent(attachment)
        }
    }

    private fun sendWebSocketAttachmentPayload(wsMsgPayload: Any) {
        when (wsMsgPayload) {
            is String -> sendMessageWebSocket(wsMsgPayload)
            is JsonObject -> sendMessageJsonObjWebSocket(wsMsgPayload)
        }
    }

    override fun deleteChat(messageId: String, onError: (Throwable) -> Unit, onSuccessDeleteConversation: () -> Unit) {
        deleteMessageListUseCase.execute(DeleteMessageListUseCase.generateParam(messageId),
                DeleteMessageAllSubscriber(onError, onSuccessDeleteConversation))
    }

    override fun getShopFollowingStatus(
            shopId: Long,
            onError: (Throwable) -> Unit,
            onSuccessGetShopFollowingStatus: (Boolean) -> Unit
    ) {
        getShopFollowingUseCase.getStatus(shopId, onError, onSuccessGetShopFollowingStatus)
    }

    override fun detachView() {
        destroyWebSocket()
        getChatUseCase.unsubscribe()
        getTemplateChatRoomUseCase.unsubscribe()
        replyChatUseCase.unsubscribe()
        deleteMessageListUseCase.unsubscribe()
        getShopFollowingUseCase.safeCancel()
        addToCartUseCase.unsubscribe()
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

    override fun followUnfollowShop(
            shopId: String,
            onError: (Throwable) -> Unit,
            onSuccess: (isSuccess: Boolean) -> Unit,
            action: ToggleFavouriteShopUseCase.Action?
    ) {
        val param = if (action != null) {
            ToggleFavouriteShopUseCase.createRequestParam(shopId, action)
        } else {
            ToggleFavouriteShopUseCase.createRequestParam(shopId)
        }
        toggleFavouriteShopUseCase.execute(
                param,
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        onError(e)
                    }

                    override fun onNext(success: Boolean) {
                        onSuccess(success)
                    }
                })
    }

    override fun addAttachmentPreview(sendablePreview: SendablePreview) {
        attachmentsPreview.add(sendablePreview)
    }

    override fun hasEmptyAttachmentPreview(): Boolean {
        return attachmentsPreview.isEmpty()
    }

    override fun initAttachmentPreview() {
        if (attachmentsPreview.isEmpty()) return
        view?.showAttachmentPreview(attachmentsPreview)
        view?.updateSrwState()
        if (view?.hasProductPreviewShown() == false) {
            view?.focusOnReply()
        }
    }

    override fun clearAttachmentPreview() {
        attachmentsPreview.clear()
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

    override fun removeFromWishList(
            productId: String, userId: String, wishListActionListener: WishListActionListener
    ) {
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

    override fun loadAttachmentData(msgId: Long, chatRoom: ChatroomViewModel) {
        if (chatRoom.hasAttachment() && msgId != 0L) {
            chatAttachmentUseCase.getAttachments(
                    msgId, chatRoom.attachmentIds, userLocationInfo,
                    ::onSuccessGetAttachments, ::onErrorGetAttachments
            )
        }
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

    override fun requestBlockPromo(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit) {
        chatToggleBlockChat.blockPromo(messageId, onSuccess, onError)
    }

    override fun requestAllowPromo(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit) {
        chatToggleBlockChat.allowPromo(messageId, onSuccess, onError)
    }

    override fun blockChat(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit) {
        chatToggleBlockChat.blockChat(messageId, onSuccess, onError)
    }

    override fun unBlockChat(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit) {
        chatToggleBlockChat.unBlockChat(messageId, onSuccess, onError)
    }

    override fun getBackground() {
        chatBackgroundUseCase.getBackground(
                ::onLoadBackgroundFromCache, ::onSuccessLoadBackground, ::onErrorLoadBackground
        )
    }

    override fun addProductToCart(
            requestParams: RequestParams,
            onSuccessAddToCart: (data: DataModel) -> Unit,
            onError: (msg: String) -> Unit
    ) {
        launchCatchError(
                dispatchers.io,
                block = {
                    val atcResponse = addToCartUseCase.createObservable(requestParams)
                            .toBlocking()
                            .single().data
                    withContext(dispatchers.main) {
                        if (atcResponse.success == 1) {
                            onSuccessAddToCart(atcResponse)
                        } else {
                            onError(atcResponse.message.getOrNull(0) ?: "")
                        }
                    }
                },
                onError = {
                    withContext(dispatchers.main) {
                        it.message?.let { errorMsg ->
                            onError(errorMsg)
                        }
                    }
                }
        )
    }

    override fun addOngoingUpdateProductStock(
            productId: String,
            product: ProductAttachmentViewModel, adapterPosition: Int,
            parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    ) {
        val result = UpdateProductStockResult(product, adapterPosition, parentMetaData)
        onGoingStockUpdate[productId] = result
    }

    override fun getSmartReplyWidget(msgId: String) {
        launchCatchError(dispatchers.io,
                {
                    chatSrwUseCase.getSrwList(msgId).collect {
                        _srw.postValue(it)
                    }
                },
                {
                    _srw.postValue(Resource.error(it, null))
                }
        )
    }

    private fun onLoadBackgroundFromCache(url: String) {
        view?.renderBackground(url)
    }

    private fun onSuccessLoadBackground(url: String, needToUpdate: Boolean) {
        if (needToUpdate) {
            view?.renderBackground(url)
        }
    }

    private fun onErrorLoadBackground(throwable: Throwable) {
        throwable.printStackTrace()
    }

    private fun onSuccessGetAttachments(attachments: ArrayMap<String, Attachment>) {
        this.attachments.putAll(attachments.toMap())
        view?.updateAttachmentsView(this.attachments)
    }

    private fun onErrorGetAttachments(
            throwable: Throwable, errorAttachment: ArrayMap<String, Attachment>
    ) {
        this.attachments.putAll(errorAttachment.toMap())
        view?.updateAttachmentsView(this.attachments)
        throwable.printStackTrace()
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

    private fun onSuccessGetOrderProgress(orderProgressResponse: OrderProgressResponse) {
        view?.renderOrderProgress(orderProgressResponse.chatOrderProgress)
    }

    private fun isEnableUploadImageService(): Boolean {
        return try {
            remoteConfig.getBoolean(ENABLE_UPLOAD_IMAGE_SERVICE, false)
        } catch (ex: Exception) {
            false
        }
    }

    private fun onErrorGetOrderProgress(throwable: Throwable) {}

    private fun onErrorGetStickerGroup(throwable: Throwable) {}

    companion object {
        const val ENABLE_UPLOAD_IMAGE_SERVICE = "android_enable_topchat_upload_image_service"
    }
}