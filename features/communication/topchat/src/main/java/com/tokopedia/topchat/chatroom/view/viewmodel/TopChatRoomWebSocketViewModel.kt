package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.usecase.TopchatUploadImageUseCase
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.uimodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import com.tokopedia.topchat.common.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.common.websocket.TopchatWebSocket
import com.tokopedia.topchat.common.websocket.WebSocketParser
import com.tokopedia.topchat.common.websocket.WebSocketStateHandler
import com.tokopedia.topchat.common.websocket.WebsocketPayloadGenerator
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import java.util.*
import javax.inject.Inject

open class TopChatRoomWebSocketViewModel @Inject constructor(
    private val chatWebSocket: TopchatWebSocket,
    private val webSocketStateHandler: WebSocketStateHandler,
    private val webSocketParser: WebSocketParser,
    private val topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
    private var uploadImageUseCase: TopchatUploadImageUseCase,
    private var payloadGenerator: WebsocketPayloadGenerator,
    private val remoteConfig: RemoteConfig,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main), DefaultLifecycleObserver {

    private val _isWebsocketError = MutableLiveData<Boolean>()
    val isWebsocketError: LiveData<Boolean>
        get() = _isWebsocketError

    private val _unreadMsg = MutableLiveData<Int>()
    val unreadMsg: LiveData<Int>
        get() = _unreadMsg

    private val _isTyping = MutableLiveData<Boolean>()
    val isTyping: LiveData<Boolean>
        get() = _isTyping

    private val _msgRead = MutableLiveData<Unit>()
    val msgRead: LiveData<Unit>
        get() = _msgRead

    private val _newMsg = MutableLiveData<Visitable<*>>()
    val newMsg: LiveData<Visitable<*>>
        get() = _newMsg

    private val _msgDeleted = MutableLiveData<String>()
    val msgDeleted: LiveData<String>
        get() = _msgDeleted

    private val _removeSrwBubble = MutableLiveData<String?>()
    val removeSrwBubble: LiveData<String?>
        get() = _removeSrwBubble

    private val _errorSnackbar = MutableLiveData<Throwable>()
    val errorSnackbar: LiveData<Throwable>
        get() = _errorSnackbar

    private val _uploadImageService = MutableLiveData<ImageUploadServiceModel>()
    val uploadImageService: LiveData<ImageUploadServiceModel>
        get() = _uploadImageService

    private val _previewMsg = MutableLiveData<SendableUiModel>()
    val previewMsg: LiveData<SendableUiModel>
        get() = _previewMsg

    private val _failUploadImage = MutableLiveData<ImageUploadUiModel>()
    val failUploadImage: LiveData<ImageUploadUiModel>
        get() = _failUploadImage

    private val _attachmentSent = MutableLiveData<SendablePreview>()
    val attachmentSent: LiveData<SendablePreview>
        get() = _attachmentSent

    private var autoRetryJob: Job? = null

    var roomMetaData: RoomMetaData = RoomMetaData()
    var userLocationInfo: LocalCacheModel = LocalCacheModel()
    var attachmentsPreview: ArrayList<SendablePreview> = arrayListOf()
    var isInTheMiddleOfThePage: Boolean = false

    /**
     * these flags use to handle messages in order to unread when from the bubble and on stop
     */
    var isOnStop = false
    var isFromBubble = false

    override fun onDestroy(owner: LifecycleOwner) {
        chatWebSocket.close()
        chatWebSocket.destroy()
        cancel()
    }

    override fun onStop(owner: LifecycleOwner) {
        isOnStop = true
    }

    override fun onResume(owner: LifecycleOwner) {
        isOnStop = false
    }

    /**
     * Websocket handler
     */

    fun connectWebSocket() {
        chatWebSocket.connectWebSocket(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("$TAG - onOpen")
                handleOnOpenWebSocket()
                markAsRead()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val response = webSocketParser.parseResponse(text)
                handleOnMessageWebSocket(response)
                Timber.d("$TAG - onMessage - ${response.code}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("$TAG - onClosing - $code - $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("$TAG - onClosed - $code - $reason")
                handleOnClosedWebSocket(code)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Timber.d("$TAG - onFailure - ${t.message}")
                handleOnFailureWebSocket()
            }
        })
    }

    /**
     * Websocket open & close handler
     */
    private fun handleOnOpenWebSocket() {
        _isWebsocketError.postValue(false)
        webSocketStateHandler.retrySucceed()
    }

    private fun handleOnClosedWebSocket(code: Int) {
        if (code != DefaultTopChatWebSocket.CODE_NORMAL_CLOSURE) {
            retryConnectWebSocket()
        }
    }

    /**
     * Websocket failure / error handler
     */
    private fun handleOnFailureWebSocket() {
        retryConnectWebSocket()
    }

    private fun retryConnectWebSocket() {
        chatWebSocket.reset()
        chatWebSocket.close()
        _isWebsocketError.postValue(true)
        autoRetryJob = launchCatchError(
            dispatcher.io,
            {
                Timber.d("$TAG - scheduleForRetry")
                webSocketStateHandler.scheduleForRetry {
                    withContext(dispatcher.main) {
                        Timber.d("$TAG - reconnecting websocket")
                        connectWebSocket()
                    }
                }
            },
            {
                Timber.d("$TAG - ${it.message}")
            }
        )
    }

    /**
     * Websocket message mapper
     */
    private fun handleOnMessageWebSocket(response: WebSocketResponse) {
        val incomingChatEvent = topChatRoomWebSocketMessageMapper.parseResponse(response)
        if (incomingChatEvent.msgId != roomMetaData.msgId) return
        when (response.code) {
            WebsocketEvent.Event.EVENT_TOPCHAT_TYPING -> onReceiveTypingEvent()
            WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING -> onReceiveEndTypingEvent()
            WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE -> onReceiveReadMsgEvent()
            WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE -> onReceiveReplyEvent(
                incomingChatEvent
            )
            WebsocketEvent.Event.EVENT_DELETE_MSG -> onReceiveDeleteMsgEvent(
                incomingChatEvent
            )
        }
    }

    private fun onReceiveTypingEvent() {
        updateLiveDataOnMainThread(_isTyping, true)
    }

    private fun onReceiveEndTypingEvent() {
        updateLiveDataOnMainThread(_isTyping, false)
    }

    private fun onReceiveReadMsgEvent() {
        if (!isInTheMiddleOfThePage) {
            updateLiveDataOnMainThread(_msgRead, Unit)
        }
    }

    private fun onReceiveReplyEvent(chat: ChatSocketPojo) {
        if (!isInTheMiddleOfThePage) {
            renderChatItem(chat)
            if (isFromBubble && isOnStop) return
            updateLiveDataOnMainThread(_unreadMsg, 0)
        } else {
            if (chat.isOpposite) {
                incrementUnreadMsg()
            }
        }
    }

    private fun renderChatItem(chat: ChatSocketPojo) {
        val chatUiModel = topChatRoomWebSocketMessageMapper.map(chat)
        updateLiveDataOnMainThread(_newMsg, chatUiModel)
        handleSrwBubbleState(chat, chatUiModel)
        if (chat.isOpposite) {
            markAsRead()
        }
    }

    private fun handleSrwBubbleState(pojo: ChatSocketPojo, uiModel: Visitable<*>) {
        when (pojo.attachment?.type) {
            AttachmentType.Companion.TYPE_INVOICE_SEND,
            AttachmentType.Companion.TYPE_IMAGE_UPLOAD,
            AttachmentType.Companion.TYPE_VOUCHER -> updateLiveDataOnMainThread(
                _removeSrwBubble,
                null
            )
            AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT -> {
                if (uiModel is ProductAttachmentUiModel) {
                    updateLiveDataOnMainThread(_removeSrwBubble, uiModel.productId)
                }
            }
        }
    }

    private fun handleSrwBubbleState(previewToSent: SendablePreview) {
        when (previewToSent) {
            is InvoicePreviewUiModel -> _removeSrwBubble.value = null
        }
    }

    private fun onReceiveDeleteMsgEvent(chat: ChatSocketPojo) {
        updateLiveDataOnMainThread(_msgDeleted, chat.replyTime)
    }

    /**
     * Websocket send events
     */

    fun sendWsStartTyping() {
        val wsPayload = payloadGenerator.generateWsPayloadStartTyping(
            roomMetaData.msgId
        )
        sendWsPayload(wsPayload)
    }

    fun sendWsStopTyping() {
        val wsPayload = payloadGenerator.generateWsPayloadStopTyping(
            roomMetaData.msgId
        )
        sendWsPayload(wsPayload)
    }

    fun sendAttachments(message: String) {
        if (attachmentsPreview.isEmpty()) return
        attachmentsPreview.forEach { attachment ->
            handleSrwBubbleState(attachment)
            val previewMsg = payloadGenerator.generateAttachmentPreviewMsg(
                sendablePreview = attachment,
                roomMetaData = roomMetaData,
                message = message
            )
            val wsPayload = payloadGenerator.generateAttachmentWsPayload(
                sendablePreview = attachment,
                roomMetaData = roomMetaData,
                message = message,
                userLocationInfo = userLocationInfo,
                localId = previewMsg.localId
            )
            showPreviewMsg(previewMsg)
            sendWsPayload(wsPayload)
            _attachmentSent.value = attachment
        }
    }

    fun sendMsg(
        message: String,
        intention: String?,
        referredMsg: ParentReply?,
        products: List<SendablePreview>? = null
    ) {
        val previewMsg = payloadGenerator.generatePreviewMsg(
            message = message,
            intention = intention,
            roomMetaData = roomMetaData,
            referredMsg = referredMsg
        )
        val wsPayload = payloadGenerator.generateWsPayload(
            message = message,
            intention = intention,
            roomMetaData = roomMetaData,
            previewMsg = previewMsg,
            attachments = products ?: attachmentsPreview,
            userLocationInfo = userLocationInfo,
            referredMsg = referredMsg
        )
        showPreviewMsg(previewMsg)
        sendWsPayload(wsPayload)
        sendWsStopTyping()
    }

    fun sendSticker(sticker: Sticker, referredMsg: ParentReply?) {
        val previewMsg = payloadGenerator.generateStickerPreview(
            roomMetaData = roomMetaData,
            sticker = sticker,
            referredMsg = referredMsg
        )
        val wsPayload = payloadGenerator.generateStickerWsPayload(
            sticker = sticker,
            roomMetaData = roomMetaData,
            attachments = attachmentsPreview,
            localId = previewMsg.localId,
            referredMsg = referredMsg
        )
        showPreviewMsg(previewMsg)
        sendWsPayload(wsPayload)
        sendWsStopTyping()
    }

    /**
     * isSecure param is used when users don't use service
     * otherwise it will be sent in service param
     */
    fun startUploadImages(
        image: ImageUploadUiModel,
        isSecure: Boolean
    ) {
        _removeSrwBubble.value = null
        if (isEnableUploadImageService()) {
            showPreviewMsg(image)
            addDummyToService(image)
            startUploadImageWithService(image)
        } else {
            showPreviewMsg(image)
            uploadImageUseCase.upload(
                image = image,
                onSuccess = ::onSuccessUploadImage,
                onError = ::onErrorUploadImage,
                isSecure = isSecure
            )
        }
    }

    private fun addDummyToService(image: ImageUploadUiModel) {
        val dummyPosition = UploadImageChatService.findDummy(image)
        if (dummyPosition == null) {
            val uploadImageDummy = UploadImageDummy(
                messageId = roomMetaData.msgId,
                visitable = image
            )
            UploadImageChatService.dummyMap.add(uploadImageDummy)
        }
    }

    private fun startUploadImageWithService(image: ImageUploadUiModel) {
        _uploadImageService.value = ImageUploadMapper.mapToImageUploadServer(image)
    }

    private fun onSuccessUploadImage(
        uploadId: String,
        imageUploadUiModel: ImageUploadUiModel,
        isSecure: Boolean
    ) {
        val wsPayload = payloadGenerator.generateImageWsPayload(
            roomMetaData,
            uploadId,
            imageUploadUiModel,
            isSecure
        )
        sendWsPayload(wsPayload)
    }

    private fun onErrorUploadImage(
        throwable: Throwable,
        imageUploadUiModel: ImageUploadUiModel
    ) {
        _errorSnackbar.value = throwable
        _failUploadImage.value = imageUploadUiModel
    }

    fun isUploading(): Boolean {
        return uploadImageUseCase.isUploading
    }

    private fun showPreviewMsg(previewMsg: SendableUiModel) {
        _previewMsg.value = previewMsg
    }

    private fun sendWsPayload(wsPayload: String) {
        chatWebSocket.sendPayload(wsPayload)
    }

    /**
     * Read / Unread message
     */

    fun markAsRead() {
        if (isFromBubble && isOnStop) {
            incrementUnreadMsg()
            return
        }
        val wsPayload = payloadGenerator.generateMarkAsReadPayload(roomMetaData)
        sendWsPayload(wsPayload)
    }

    private fun incrementUnreadMsg() {
        val currentValue = _unreadMsg.value ?: 0
        updateLiveDataOnMainThread(_unreadMsg, currentValue + 1)
    }

    fun resetUnreadMessage() {
        _unreadMsg.value = 0
    }

    fun resetMessageLiveData() {
        // WS
        _isWebsocketError.value = null

        // Messages
        _unreadMsg.value = null
        _msgRead.value = null
        _previewMsg.value = null
        _msgDeleted.value = null
        _newMsg.value = null

        // Typing
        _isTyping.value = null

        // Attachment
        _attachmentSent.value = null
        _removeSrwBubble.value = null

        // Images
        _uploadImageService.value = null
        _failUploadImage.value = null

        // Common
        _errorSnackbar.value = null
    }

    /**
     * Common
     */
    private fun <T> updateLiveDataOnMainThread(liveData: MutableLiveData<T>, value: T) {
        viewModelScope.launch(dispatcher.main) {
            liveData.value = value
        }
    }

    private fun isEnableUploadImageService(): Boolean {
        return try {
            remoteConfig.getBoolean(
                TopChatViewModel.ENABLE_UPLOAD_IMAGE_SERVICE,
                false
            ) && !isProblematicDevice()
        } catch (ex: Throwable) {
            false
        }
    }

    private fun isProblematicDevice(): Boolean {
        return PROBLEMATIC_DEVICE.contains(
            DeviceInfo.getModelName().lowercase(Locale.getDefault())
        )
    }

    companion object {
        private const val TAG = "DEBUG_TOPCHAT_WEBSOCKET"
        private val PROBLEMATIC_DEVICE = listOf("iris88", "iris88_lite", "lenovo k9")
    }
}
