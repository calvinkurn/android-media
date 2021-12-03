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
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.BaseChatUiModel.Builder.Companion.generateCurrentReplyTime
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_API
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.network.ChatUrl.Companion.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.data.Status
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import com.tokopedia.topchat.common.util.AddressUtil
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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
    private var compressImageUseCase: CompressImageUseCase,
    private var addWishListUseCase: AddWishListUseCase,
    private var removeWishListUseCase: RemoveWishListUseCase,
    private var uploadImageUseCase: TopchatUploadImageUseCase,
    private val groupStickerUseCase: ChatListGroupStickerUseCase,
    private val chatAttachmentUseCase: ChatAttachmentUseCase,
    private val chatToggleBlockChat: ChatToggleBlockChatUseCase,
    private val chatBackgroundUseCase: ChatBackgroundUseCase,
    private val chatSrwUseCase: SmartReplyQuestionUseCase,
    private val tokoNowWHUsecase: ChatTokoNowWarehouseUseCase,
    private val moveChatToTrashUseCase: MutationMoveChatToTrashUseCase,
    private val sharedPref: SharedPreferences,
    private val dispatchers: CoroutineDispatchers,
    private val remoteConfig: RemoteConfig
) : BaseChatPresenter<TopChatContract.View>(userSession, topChatRoomWebSocketMessageMapper),
    TopChatContract.Presenter, CoroutineScope {

    var autoRetryConnectWs = true
    var newUnreadMessage = 0
        private set
    var thisMessageId: String = ""
        private set
    val attachments: ArrayMap<String, Attachment> = ArrayMap()
    val onGoingStockUpdate: ArrayMap<String, UpdateProductStockResult> = ArrayMap()
    var attachProductWarehouseId = "0"
        private set
    private var userLocationInfo = LocalCacheModel()

    private lateinit var webSocketUrl: String
    private var attachmentsPreview: ArrayList<SendablePreview> = arrayListOf()
    private var mSubscription: CompositeSubscription
    private var compressImageSubscription: CompositeSubscription
    private var listInterceptor: ArrayList<Interceptor>
    private var dummyList: ArrayList<Visitable<*>>
    var roomMetaData: RoomMetaData = RoomMetaData()
        private set

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
        roomMetaData.updateMessageId(messageId)
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
        this.attachProductWarehouseId = userLocation.warehouse_id
    }

    override fun getProductIdPreview(): List<String> {
        return attachmentsPreview.filterIsInstance<SendableProductPreview>()
            .map { it.productId }
    }

    override fun getAttachmentsPreview(): List<SendablePreview> {
        return attachmentsPreview
    }

    override fun adjustInterlocutorWarehouseId(msgId: String) {
        attachProductWarehouseId = "0"
        launchCatchError(
            block = {
                tokoNowWHUsecase.getWarehouseId(msgId)
                    .flowOn(dispatchers.io)
                    .collect {
                        if (it.status == Status.SUCCESS) {
                            attachProductWarehouseId = it.data
                                ?.chatTokoNowWarehouse
                                ?.warehouseId ?: "0"
                        }
                    }
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    override fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo = topChatRoomWebSocketMessageMapper.parseResponse(
            webSocketResponse
        )
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
        val uiModel = mapToVisitable(pojo)
        view?.onReceiveMessageEvent(uiModel)
        handleSrwBubbleState(pojo, uiModel)
        if (!pojo.isOpposite) {
            checkDummyAndRemove(uiModel)
        } else {
            readMessage()
        }
    }

    private fun handleSrwBubbleState(pojo: ChatSocketPojo, uiModel: Visitable<*>) {
        when (pojo.attachment?.type) {
            AttachmentType.Companion.TYPE_INVOICE_SEND,
            AttachmentType.Companion.TYPE_IMAGE_UPLOAD,
            AttachmentType.Companion.TYPE_VOUCHER -> view?.removeSrwBubble()
            AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT -> {
                if (uiModel is ProductAttachmentUiModel) {
                    view?.removeSrwBubble(uiModel.productId)
                }
            }
        }
    }

    private fun handleSrwBubbleState(previewToSent: SendablePreview) {
        when (previewToSent) {
            is InvoicePreviewUiModel -> view?.removeSrwBubble()
        }
    }

    override fun getExistingChat(
        messageId: String,
        onError: (Throwable) -> Unit,
        onSuccessGetExistingMessage: (ChatroomViewModel, ChatReplies) -> Unit
    ) {
        if (messageId.isNotEmpty()) {
            getChatUseCase.getFirstPageChat(
                messageId = messageId,
                onSuccess = onSuccessGetExistingMessage,
                onErrorGetChat = onError
            ) {
                updateRoomMetaData(it)
            }
        }
    }

    private fun updateRoomMetaData(roomMetaData: RoomMetaData) {
        this.roomMetaData = roomMetaData
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
                override fun onNext(templateUiModel: GetTemplateUiModel?) {
                    val templateList = arrayListOf<Visitable<Any>>()
                    if (templateUiModel != null) {
                        if (templateUiModel.isEnabled) {
                            templateUiModel.listTemplate?.let {
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

    override fun startCompressImages(it: ImageUploadUiModel) {
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

    override fun startUploadImages(image: ImageUploadUiModel) {
        view?.removeSrwBubble()
        if (isEnableUploadImageService()) {
            addDummyToService(image)
            startUploadImageWithService(image)
        } else {
            processDummyMessage(image)
            uploadImageUseCase.upload(
                image = image,
                onSuccess = ::onSuccessUploadImage,
                onError = ::onErrorUploadImage
            )
        }
    }

    protected open fun addDummyToService(image: ImageUploadUiModel) {
        val dummyPosition = UploadImageChatService.findDummy(image)
        if (dummyPosition == null) {
            val uploadImageDummy = UploadImageDummy(messageId = thisMessageId, visitable = image)
            UploadImageChatService.dummyMap.add(uploadImageDummy)
        }
        view?.addDummyMessage(image)

    }

    protected open fun startUploadImageWithService(image: ImageUploadUiModel) {
        UploadImageChatService.enqueueWork(
            view.context,
            ImageUploadMapper.mapToImageUploadServer(image),
            thisMessageId
        )
    }

    private fun onSuccessUploadImage(uploadId: String, image: ImageUploadUiModel) {
        when (networkMode) {
            MODE_WEBSOCKET -> sendImageByWebSocket(uploadId, image)
            MODE_API -> sendImageByApi(uploadId, image)
        }
    }

    private fun onErrorUploadImage(throwable: Throwable, image: ImageUploadUiModel) {
        view?.onErrorUploadImage(ErrorHandler.getErrorMessage(view?.context, throwable), image)
    }

    private fun sendImageByWebSocket(uploadId: String, image: ImageUploadUiModel) {
        val requestParams = TopChatWebSocketParam.generateParamSendImage(
            thisMessageId, uploadId, image
        )
        sendMessageWebSocket(requestParams)
    }

    private fun sendImageByApi(uploadId: String, image: ImageUploadUiModel) {
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
                val temp = (dummyList[i] as SendableUiModel)
                if (temp.startTime == (visitable as SendableUiModel).startTime
                    && temp.messageId == (visitable as SendableUiModel).messageId
                ) {
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
        sendMessage: String, referredMsg: ParentReply?
    ) {
        if (isValidReply(sendMessage)) {
            sendAttachments(sendMessage)
            topchatSendMessageWithWebsocket(
                sendMessage = sendMessage,
                referredMsg = referredMsg
            )
            view?.clearAttachmentPreviews()
            view?.clearReferredMsg()
        }
    }

    override fun sendAttachmentsAndSticker(
        sticker: Sticker, referredMsg: ParentReply?
    ) {
        sendAttachments(sticker.intention)
        sendSticker(sticker, referredMsg)
        view?.clearAttachmentPreviews()
        view?.clearReferredMsg()
    }

    override fun sendAttachmentsAndSrw(
        question: QuestionUiModel, referredMsg: ParentReply?
    ) {
        sendAttachments(question.content)
        topchatSendMessageWithWebsocket(
            sendMessage = question.content,
            intention = question.intent,
            referredMsg = referredMsg
        )
        view?.clearAttachmentPreviews()
        view?.clearReferredMsg()
    }

    override fun sendSrwFrom(
        attachment: HeaderCtaButtonAttachment
    ) {
        val addressMasking = AddressUtil.getAddressMasking(userLocationInfo.label)
        val ctaButton = attachment.ctaButton
        val productName = ctaButton.productName
        val srwMessage = "Ubah alamat pengiriman \"$productName\" ke $addressMasking"
        val question = QuestionUiModel(srwMessage, ctaButton.extras.intent)
        val products = ctaButton.generateSendableProductPreview()
        topchatSendMessageWithWebsocket(
            sendMessage = question.content,
            intention = question.intent,
            products = products
        )
    }

    override fun sendSrwBubble(
        question: QuestionUiModel, products: List<SendablePreview>,
    ) {
        topchatSendMessageWithWebsocket(
            sendMessage = question.content,
            intention = question.intent,
            products = products
        )
    }

    /**
     * send with websocket but with param [intention]
     */
    private fun topchatSendMessageWithWebsocket(
        sendMessage: String,
        intention: String? = null,
        products: List<SendablePreview>? = null,
        referredMsg: ParentReply? = null
    ) {
        val startTime = SendableUiModel.generateStartTime()
        val previewMsg = generatePreviewMessage(
            messageText = sendMessage,
            startTime = startTime,
            referredMsg = referredMsg
        )
        val requestParams = TopChatWebSocketParam.generateParamSendMessage(
            roomeMetaData = roomMetaData,
            messageText = sendMessage,
            startTime = startTime,
            attachments = products ?: attachmentsPreview,
            localId = previewMsg.localId,
            intention = intention,
            userLocationInfo = userLocationInfo,
            referredMsg = referredMsg
        )
        sendWs(requestParams, previewMsg)
    }

    private fun sendWs(
        request: String,
        preview: SendableUiModel
    ) {
        processPreviewMessage(preview)
        sendMessageWebSocket(request)
        sendWsStopTyping()
    }

    private fun sendWsStopTyping() {
        val request = TopChatWebSocketParam.generateParamStopTyping(roomMetaData.msgId)
        sendMessageWebSocket(request)
    }

    private fun processPreviewMessage(previewMsg: SendableUiModel) {
        view?.showPreviewMsg(previewMsg)
    }

    private fun generatePreviewMessage(
        messageText: String,
        startTime: String,
        referredMsg: ParentReply?
    ): SendableUiModel {
        val localId = IdentifierUtil.generateLocalId()
        return MessageUiModel.Builder()
            .withMsgId(roomMetaData.msgId)
            .withFromUid(userSession.userId)
            .withFrom(userSession.name)
            .withReplyTime(generateCurrentReplyTime())
            .withStartTime(startTime)
            .withMsg(messageText)
            .withLocalId(localId)
            .withParentReply(referredMsg)
            .withIsDummy(true)
            .withIsSender(true)
            .withIsRead(false)
            .build()
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
        sticker: Sticker,
        referredMsg: ParentReply?
    ) {
        val startTime = SendableUiModel.generateStartTime()
        val previewSticker = StickerUiModel.generatePreviewMessage(
            roomMetaData = roomMetaData,
            sticker = sticker,
            referredMsg = referredMsg
        )
        val stickerContract = sticker.generateWebSocketPayload(
            messageId = roomMetaData.msgId,
            startTime = startTime,
            attachments = attachmentsPreview,
            localId = previewSticker.localId,
            referredMsg = referredMsg
        )
        val request = CommonUtil.toJson(stickerContract)
        sendWs(request, previewSticker)
    }

    private fun sendAttachments(message: String) {
        if (attachmentsPreview.isEmpty()) return
        attachmentsPreview.forEach { attachment ->
            handleSrwBubbleState(attachment)
            val previewMsg = attachment.generatePreviewMessage(
                roomMetaData, message
            )
            val wsMsgPayload = attachment.generateMsgObj(
                roomMetaData, message, userLocationInfo, previewMsg.localId
            )
            processPreviewMessage(previewMsg)
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

    override fun deleteChat(
        messageId: String,
        onError: (Throwable) -> Unit,
        onSuccessDeleteConversation: () -> Unit
    ) {
        launch {
            try {
                val result = moveChatToTrashUseCase.execute(messageId)
                if (result.chatMoveToTrash.list.isNotEmpty()) {
                    val deletedChat = result.chatMoveToTrash.list.first()
                    if (deletedChat.isSuccess == Constant.INT_STATUS_TRUE) {
                        onSuccessDeleteConversation()
                    } else {
                        onError(Throwable(deletedChat.detailResponse))
                    }
                }
            } catch (throwable: Throwable) {
                onError(throwable)
            }
        }
    }

    override fun detachView() {
        destroyWebSocket()
        getChatUseCase.unsubscribe()
        getTemplateChatRoomUseCase.unsubscribe()
        replyChatUseCase.unsubscribe()
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

    override fun addAttachmentPreview(sendablePreview: SendablePreview) {
        attachmentsPreview.add(sendablePreview)
    }

    override fun hasEmptyAttachmentPreview(): Boolean {
        return attachmentsPreview.isEmpty()
    }

    override fun initAttachmentPreview() {
        if (attachmentsPreview.isEmpty()) return
        view?.let {
            it.showAttachmentPreview(attachmentsPreview)
            it.updateSrwPreviewState()
            if (!it.hasProductPreviewShown()) {
                it.focusOnReply()
            }
        }
    }

    override fun clearAttachmentPreview() {
        attachmentsPreview.clear()
    }

    override fun initProductPreviewFromAttachProduct(resultProducts: ArrayList<ResultProduct>) {
        if (resultProducts.isNotEmpty()) clearAttachmentPreview()
        for (resultProduct in resultProducts) {
            val productPreview = ProductPreview(
                id = resultProduct.productId,
                imageUrl = resultProduct.productImageThumbnail,
                name = resultProduct.name,
                price = resultProduct.price,
                url = resultProduct.productUrl,
                priceBefore = resultProduct.priceBefore,
                dropPercentage = resultProduct.dropPercentage,
                productFsIsActive = resultProduct.isFreeOngkirActive,
                productFsImageUrl = resultProduct.imgUrlFreeOngkir,
                remainingStock = resultProduct.stock,
                isSupportVariant = resultProduct.isSupportVariant,
                campaignId = resultProduct.campaignId,
                isPreorder = resultProduct.isPreorder,
                priceInt = resultProduct.priceInt,
                categoryId = resultProduct.categoryId
            )
            if (productPreview.notEnoughRequiredData()) continue
            val sendAbleProductPreview = SendableProductPreview(productPreview)
            attachmentsPreview.add(sendAbleProductPreview)
        }
        initAttachmentPreview()
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

    override fun getStickerGroupList(chatRoom: ChatroomViewModel) {
        groupStickerUseCase.getStickerGroup(
            chatRoom.isSeller(), ::onLoadingStickerGroup, ::onSuccessGetStickerGroup,
        ) {}
    }

    override fun loadAttachmentData(msgId: Long, chatRoom: ChatroomViewModel) {
        if (chatRoom.hasAttachment() && msgId != 0L) {
            chatAttachmentUseCase.getAttachments(
                msgId, chatRoom.replyIDs, userLocationInfo,
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

    override fun requestBlockPromo(
        messageId: String,
        onSuccess: (ChatSettingsResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        chatToggleBlockChat.blockPromo(messageId, onSuccess, onError)
    }

    override fun requestAllowPromo(
        messageId: String,
        onSuccess: (ChatSettingsResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        chatToggleBlockChat.allowPromo(messageId, onSuccess, onError)
    }

    override fun blockChat(
        messageId: String,
        onSuccess: (ChatSettingsResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        chatToggleBlockChat.blockChat(messageId, onSuccess, onError)
    }

    override fun unBlockChat(
        messageId: String,
        onSuccess: (ChatSettingsResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        chatToggleBlockChat.unBlockChat(messageId, onSuccess, onError)
    }

    override fun getBackground() {
        chatBackgroundUseCase.getBackground(
            ::onLoadBackgroundFromCache, ::onSuccessLoadBackground
        ) {}
    }

    override fun addOngoingUpdateProductStock(
        productId: String,
        product: ProductAttachmentUiModel, adapterPosition: Int,
        parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    ) {
        val result = UpdateProductStockResult(product, adapterPosition, parentMetaData)
        onGoingStockUpdate[productId] = result
    }

    override fun getSmartReplyWidget(msgId: String, productIds: String) {
        launchCatchError(dispatchers.io,
            {
                chatSrwUseCase.getSrwList(
                    msgId = msgId,
                    productIds = productIds,
                    addressId = userLocationInfo.address_id,
                    districtId = userLocationInfo.district_id,
                    postalCode = userLocationInfo.postal_code,
                    latLon = userLocationInfo.latLong
                ).collect {
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

    protected open fun isEnableUploadImageService(): Boolean {
        return try {
            remoteConfig.getBoolean(ENABLE_UPLOAD_IMAGE_SERVICE, false) && !isProblematicDevice()
        } catch (ex: Exception) {
            false
        }
    }

    private fun isProblematicDevice(): Boolean {
        return PROBLEMATIC_DEVICE.contains(DeviceInfo.getModelName().toLowerCase())
    }

    override fun clearText() {}

    override fun sendMessageWithWebsocket(
        messageId: String, sendMessage: String,
        startTime: String, opponentId: String
    ) { }

    companion object {
        const val ENABLE_UPLOAD_IMAGE_SERVICE = "android_enable_topchat_upload_image_service"
        private val PROBLEMATIC_DEVICE = listOf("iris88", "iris88_lite", "lenovo k9")
    }
}