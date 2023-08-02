package com.tokopedia.tokochat.view.chatroom

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.extensions.ExtensionMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.domain.cache.TokoChatBubblesCache
import com.tokopedia.tokochat.domain.response.extension.TokoChatExtensionPayload
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.domain.response.orderprogress.param.TokoChatOrderProgressParam
import com.tokopedia.tokochat.domain.response.ticker.TokochatRoomTickerResponse
import com.tokopedia.tokochat.domain.usecase.GetTokoChatBackgroundUseCase
import com.tokopedia.tokochat.domain.usecase.GetTokoChatRoomTickerUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetChatHistoryUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetImageUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetTypingUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatMarkAsReadUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatOrderProgressUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatRegistrationChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatSendMessageUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatUploadImageUseCase
import com.tokopedia.tokochat.util.TokoChatValueUtil.BUBBLES_PREF
import com.tokopedia.tokochat.util.TokoChatValueUtil.IMAGE_EXTENSION
import com.tokopedia.tokochat.util.TokoChatValueUtil.PICTURE
import com.tokopedia.tokochat.util.TokoChatViewUtil
import com.tokopedia.tokochat.util.TokoChatViewUtil.Companion.getTokoChatPhotoPath
import com.tokopedia.tokochat.view.chatroom.uimodel.TokoChatImageAttachmentExtensionProvider
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.tokochat_common.util.TokoChatCacheManagerImpl.Companion.TOKOCHAT_IMAGE_ATTACHMENT_MAP
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.IMAGE_ATTACHMENT_MSG
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.userconsent.domain.collection.GetNeedConsentUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

class TokoChatViewModel @Inject constructor(
    private val chatChannelUseCase: TokoChatChannelUseCase,
    private val getChatHistoryUseCase: TokoChatGetChatHistoryUseCase,
    private val markAsReadUseCase: TokoChatMarkAsReadUseCase,
    private val registrationChannelUseCase: TokoChatRegistrationChannelUseCase,
    private val sendMessageUseCase: TokoChatSendMessageUseCase,
    private val getTypingUseCase: TokoChatGetTypingUseCase,
    private val getTokoChatBackgroundUseCase: GetTokoChatBackgroundUseCase,
    private val getTokoChatRoomTickerUseCase: GetTokoChatRoomTickerUseCase,
    private val getTokoChatOrderProgressUseCase: TokoChatOrderProgressUseCase,
    private val getImageUrlUseCase: TokoChatGetImageUseCase,
    private val uploadImageUseCase: TokoChatUploadImageUseCase,
    private val getNeedConsentUseCase: GetNeedConsentUseCase,
    private val viewUtil: TokoChatViewUtil,
    private val imageAttachmentExtensionProvider: TokoChatImageAttachmentExtensionProvider,
    private val cacheManager: TokoChatCacheManager,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main), DefaultLifecycleObserver {

    private val _channelDetail = MutableLiveData<Result<GroupBookingChannelDetails>>()
    val channelDetail: LiveData<Result<GroupBookingChannelDetails>>
        get() = _channelDetail

    private val _isChatConnected = MutableLiveData<Boolean>()
    val isChatConnected: LiveData<Boolean>
        get() = _isChatConnected

    private val _chatBackground = MutableLiveData<Result<String>>()
    val chatBackground: MutableLiveData<Result<String>>
        get() = _chatBackground

    private val _chatRoomTicker = MutableLiveData<Result<TokochatRoomTickerResponse>>()
    val chatRoomTicker: LiveData<Result<TokochatRoomTickerResponse>>
        get() = _chatRoomTicker

    private val _orderTransactionStatus = MutableLiveData<Result<TokoChatOrderProgressResponse>>()
    val orderTransactionStatus: LiveData<Result<TokoChatOrderProgressResponse>>
        get() = _orderTransactionStatus

    private val _isNeedConsent = MutableLiveData<Result<Boolean>>()
    val isNeedConsent: LiveData<Result<Boolean>>
        get() = _isNeedConsent

    private val _updateOrderTransactionStatus =
        MutableSharedFlow<Result<TokoChatOrderProgressResponse>>(replay = Int.ONE)

    val updateOrderTransactionStatus: SharedFlow<Result<TokoChatOrderProgressResponse>> =
        _updateOrderTransactionStatus

    private val _imageUploadError = MutableLiveData<Pair<String, Throwable>>()
    val imageUploadError: LiveData<Pair<String, Throwable>>
        get() = _imageUploadError

    private val _error = MutableLiveData<Pair<Throwable, String>>()
    val error: LiveData<Pair<Throwable, String>>
        get() = _error

    var gojekOrderId: String = ""
    var source: String = ""
    var tkpdOrderId: String = ""
    var isFromTokoFoodPostPurchase = false
    var pushNotifTemplateKey = ""
    var channelId = ""
    var isFromBubble = false

    @Volatile
    var imageAttachmentMap = mutableMapOf<String, String>()

    @VisibleForTesting
    var connectionCheckJob: Job? = null
    val orderStatusParamFlow = MutableSharedFlow<Pair<String, String>>(Int.ONE)

    init {
        viewModelScope.launch {
            orderStatusParamFlow
                .debounce(DELAY_UPDATE_ORDER_STATE)
                .flatMapLatest { (orderId, serviceType) ->
                    if (orderId.isNotBlank() && serviceType.isNotBlank()) {
                        fetchOrderStatusUseCase(orderId, serviceType).catch {
                            emit(Fail(it))
                        }
                    } else {
                        emptyFlow()
                    }
                }
                .flowOn(dispatcher.io)
                .collectLatest {
                    _updateOrderTransactionStatus.emit(it)
                }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        chatChannelUseCase.registerExtensionProvider(imageAttachmentExtensionProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        val cacheImageAttachmentMap = cacheManager.loadCache(
            TOKOCHAT_IMAGE_ATTACHMENT_MAP,
            Map::class.java
        ) as? Map<String, String>
        imageAttachmentMap.putAll(cacheImageAttachmentMap ?: mapOf())
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (imageAttachmentMap.isNotEmpty()) {
            cacheManager.saveCache(TOKOCHAT_IMAGE_ATTACHMENT_MAP, imageAttachmentMap)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        chatChannelUseCase.unRegisterExtensionProvider(imageAttachmentExtensionProvider)
    }

    fun sendMessage(channelId: String, text: String) {
        try {
            val messageMetaData = SendMessageMetaData()
            sendMessageUseCase.sendTextMessage(
                channelUrl = channelId,
                text = text,
                messageMetaData
            )
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::sendMessage.name)
        }
    }

    fun initGroupBooking(
        orderId: String,
        serviceType: Int = TOKOFOOD_SERVICE_TYPE,
        groupBookingListener: ConversationsGroupBookingListener,
        orderChatType: OrderChatType = OrderChatType.Unknown
    ) {
        try {
            chatChannelUseCase.initGroupBookingChat(
                orderId = orderId,
                serviceType = serviceType,
                groupBookingListener = groupBookingListener,
                orderChatType = orderChatType
            )
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::initGroupBooking.name)
        }
    }

    fun getGroupBookingChannel(channelId: String) {
        try {
            chatChannelUseCase.getRemoteGroupBookingChannel(channelId, onSuccess = {
                _channelDetail.postValue(Success(it))
            }, onError = {
                    _channelDetail.postValue(Fail(it))
                    // Call from local group booking
                })
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::getGroupBookingChannel.name)
        }
    }

    fun getChatHistory(channelId: String): LiveData<List<ConversationsMessage>>? {
        return try {
            getChatHistoryUseCase(channelId)
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::getChatHistory.name)
            MutableLiveData()
        }
    }

    fun loadPreviousMessages() {
        try {
            getChatHistoryUseCase.loadPreviousMessage()
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::loadPreviousMessages.name)
        }
    }

    fun markChatAsRead(channelId: String) {
        try {
            markAsReadUseCase(channelId)
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::markChatAsRead.name)
        }
    }

    fun registerActiveChannel(channelId: String) {
        try {
            registrationChannelUseCase.registerActiveChannel(channelId)
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::registerActiveChannel.name)
        }
    }

    fun deRegisterActiveChannel(channelId: String) {
        try {
            registrationChannelUseCase.deRegisterActiveChannel(channelId)
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::deRegisterActiveChannel.name)
        }
    }

    fun getTypingStatus(): LiveData<List<String>>? {
        return try {
            getTypingUseCase.getTypingStatus()
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::getTypingStatus.name)
            MutableLiveData()
        }
    }

    fun setTypingStatus(status: Boolean) {
        try {
            getTypingUseCase.setTypingStatus(status)
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::setTypingStatus.name)
        }
    }

    fun resetTypingStatus() {
        try {
            getTypingUseCase.resetTypingStatus()
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::resetTypingStatus.name)
        }
    }

    fun doCheckChatConnection() {
        cancelCheckConnection()
        connectionCheckJob = launch {
            withContext(dispatcher.io) {
                try {
                    while (true) {
                        delay(DELAY_UPDATE_ORDER_STATE)
                        _isChatConnected.postValue(chatChannelUseCase.isChatConnected())
                    }
                } catch (throwable: Throwable) {
                    Timber.d(throwable)
                    _isChatConnected.postValue(false)
                }
            }
        }
    }

    fun cancelCheckConnection() {
        connectionCheckJob?.cancel()
        connectionCheckJob = null
    }

    fun getTokoChatBackground() {
        launch {
            try {
                getTokoChatBackgroundUseCase(Unit).collect {
                    _chatBackground.value = Success(it)
                }
            } catch (throwable: Throwable) {
                _chatBackground.value = Fail(throwable)
            }
        }
    }

    fun loadChatRoomTicker() {
        launch {
            withContext(dispatcher.io) {
                try {
                    val result = getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
                    _chatRoomTicker.postValue(Success(result))
                } catch (throwable: Throwable) {
                    _chatRoomTicker.postValue(Fail(throwable))
                }
            }
        }
    }

    fun getUserId(): String {
        return registrationChannelUseCase.getUserId()
    }

    fun getMemberLeft(): MutableLiveData<String>? {
        return try {
            chatChannelUseCase.getMemberLeftLiveData()
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::getMemberLeft.name)
            MutableLiveData()
        }
    }

    fun resetMemberLeft() {
        chatChannelUseCase.resetMemberLeftLiveData()
    }

    /*
    * Order Transaction Section
     */
    fun loadOrderCompletedStatus(orderId: String, serviceType: String) {
        launch {
            try {
                val result = withContext(dispatcher.io) {
                    getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(orderId, serviceType))
                }
                _orderTransactionStatus.value = Success(result)
            } catch (throwable: Throwable) {
                _orderTransactionStatus.value = Fail(throwable)
            }
        }
    }

    fun updateOrderStatusParam(orderStatusParam: Pair<String, String>) {
        orderStatusParamFlow.tryEmit(orderStatusParam)
    }

    private fun fetchOrderStatusUseCase(
        orderId: String,
        serviceType: String
    ): Flow<Result<TokoChatOrderProgressResponse>> {
        return flow {
            val result = getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(orderId, serviceType))
            emit(Success(result))
        }
    }

    fun getLiveChannel(channelId: String): LiveData<ConversationsChannel?>? {
        return try {
            chatChannelUseCase.getLiveChannel(channelId)
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::getLiveChannel.name)
            MutableLiveData()
        }
    }

    fun getImageWithId(
        imageId: String,
        channelId: String,
        onImageReady: (File?) -> Unit,
        onError: () -> Unit,
        onDirectLoad: () -> Unit,
        imageView: ImageView? = null,
        isFromRetry: Boolean = false
    ) {
        launch {
            withContext(dispatcher.io) {
                try {
                    val cachedImage = getTokoChatPhotoPath(imageId)
                    // If image has never been downloaded, then download
                    if (!cachedImage.exists() || isFromRetry) {
                        delay(DELAY_FETCH_IMAGE)
                        val imageUrlResponse = getImageUrlUseCase(
                            TokoChatGetImageUseCase.Param(imageId, channelId)
                        )
                        if (imageUrlResponse.success == true) {
                            imageUrlResponse.data?.url?.let {
                                viewUtil.downloadAndSaveByteArrayImage(
                                    imageId,
                                    getImageUrlUseCase.getImage(it).byteStream(),
                                    onImageReady,
                                    onError,
                                    onDirectLoad,
                                    imageView
                                )
                            }
                        } else {
                            _error.postValue(
                                Pair(
                                    Throwable(imageUrlResponse.error?.firstOrNull()?.message),
                                    ::getImageWithId.name
                                )
                            )
                            onError()
                        }
                    } else { // Else use the downloaded image
                        onImageReady(cachedImage)
                    }
                } catch (throwable: Throwable) {
                    _error.postValue(Pair(throwable, ::getImageWithId.name))
                    onError()
                }
            }
        }
    }

    fun uploadImage(filePath: String, onDummyAdded: ((String) -> Unit)) {
        viewModelScope.launch {
            withContext(dispatcher.io) {
                val localUUID = UUID.randomUUID().toString()
                try {
                    // Compress & Rename Image to local UUID
                    val localImage = preprocessingImage(filePath = filePath, newFileName = localUUID)
                    // Add transient (dummy) message
                    sendMessageUseCase.addTransientMessage(
                        channel = channelId,
                        extensionMessage = createExtensionMessage(localUUID = localUUID)
                    )
                    // Handle loading dummy
                    onDummyAdded(localUUID)
                    // Save the combination of imageId (localUUID for transient) and localUUID
                    imageAttachmentMap[localUUID] = localUUID
                    // Image id is still local UUID
                    uploadImageToServer(filePath = localImage, localUUID = localUUID)
                } catch (throwable: Throwable) {
                    handleImageUploadError(localUUID = localUUID, throwable = throwable)
                }
            }
        }
    }

    private fun handleImageUploadError(localUUID: String, throwable: Throwable) {
        sendMessageUseCase.setTransientMessageFailed(localUUID)
        _imageUploadError.postValue(Pair(localUUID, throwable))
    }

    fun resendImage(imageId: String) {
        viewModelScope.launch {
            withContext(dispatcher.io) {
                try {
                    val cachedImage = getTokoChatPhotoPath(imageId)
                    uploadImageToServer(
                        filePath = cachedImage.absolutePath,
                        localUUID = imageAttachmentMap[imageId]!! // Expected error when null
                    )
                } catch (throwable: Throwable) {
                    handleImageUploadError(
                        localUUID = imageAttachmentMap[imageId] ?: "",
                        throwable = throwable
                    )
                }
            }
        }
    }

    private fun replaceCacheImageData(newImageId: String, localUUID: String) {
        // Replace local image element with newImageId
        imageAttachmentMap.remove(localUUID)
        imageAttachmentMap[newImageId] = localUUID
    }

    private fun preprocessingImage(filePath: String, newFileName: String): String {
        val compressedImage = viewUtil.compressImageToTokoChatPath(originalFilePath = filePath)
            ?: throw MessageErrorException(ERROR_COMPRESSED_IMAGE_NULL)
        return renameImage(originalFileUri = compressedImage, newFileName = newFileName)
    }

    private fun renameImage(
        originalFileUri: Uri,
        newFileName: String
    ): String {
        return viewUtil.renameAndMoveFileToTokoChatDir(
            originalFileUri = originalFileUri,
            newFileName = newFileName
        ) ?: throw MessageErrorException(ERROR_RENAMED_IMAGE_NULL)
    }

    private suspend fun uploadImageToServer(filePath: String, localUUID: String) {
        val params = TokoChatUploadImageUseCase.Param(
            filePath = filePath,
            channelId = channelId
        )
        val uploadResult = uploadImageUseCase(params)
        when {
            (!uploadResult.error.isNullOrEmpty()) -> {
                // Set transient to failed
                handleImageUploadError(
                    localUUID = localUUID,
                    // Checked in the first checker
                    throwable = MessageErrorException(uploadResult.error!!.joinToString())
                )
            }
            (uploadResult.data == null) -> { // Error is empty / null, but data is also null
                // Set transient to failed
                handleImageUploadError(
                    localUUID = localUUID,
                    throwable = MessageErrorException(
                        TokoChatUploadImageUseCase.ERROR_PAYLOAD_NOT_EXPECTED
                    )
                )
            }
            else -> { // Success
                val newImageId = uploadResult.data!!.imageId!! // Expected error when null
                // Rename Image to imageId
                renameImage(
                    originalFileUri = Uri.parse(filePath),
                    newFileName = newImageId
                )
                replaceCacheImageData(newImageId, localUUID)
                // Send transient message and change the payload from UUID to image Id
                sendMessageUseCase.sendTransientMessage(
                    channel = channelId,
                    extensionMessage = createExtensionMessage(
                        localUUID = localUUID,
                        extensionId = newImageId
                    )
                )
            }
        }
    }

    private fun createExtensionMessage(
        localUUID: String,
        extensionId: String? = null
    ): ExtensionMessage {
        return ExtensionMessage(
            extensionId = PICTURE,
            extensionMessageId = PICTURE,
            extensionVersion = Int.ONE,
            transientId = localUUID,
            messageId = localUUID,
            message = IMAGE_ATTACHMENT_MSG,
            isCanned = false,
            cannedMessagePayload = null,
            payload = createExtensionPayloadImageAttachment(extensionId = extensionId ?: localUUID)
        )
    }

    private fun createExtensionPayloadImageAttachment(
        extensionId: String
    ): String {
        val extensionPayload = TokoChatExtensionPayload(
            extension = IMAGE_EXTENSION,
            id = extensionId
        )
        return Gson().toJson(extensionPayload)
    }

    fun getUserConsent() {
        launch {
            try {
                val result = getNeedConsentUseCase(TokoChatValueUtil.consentParam)
                _isNeedConsent.value = result
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::getUserConsent.name)
            }
        }
    }

    fun shouldShowTickerBubblesCache(): Boolean {
        val cacheResult = cacheManager.loadCache(BUBBLES_PREF, TokoChatBubblesCache::class.java)
        // If no cache from Awareness bottom sheet, return false
        return if (cacheResult != null) {
            // True only if channel id is the same & ticker is not closed
            cacheResult.channelId == channelId && cacheResult.hasShownTicker == false
        } else {
            false
        }
    }

    fun shouldShowBottomsheetBubblesCache(): Boolean {
        val cacheResult = cacheManager.loadCache(BUBBLES_PREF, TokoChatBubblesCache::class.java)
        return cacheResult == null
    }

    fun setBubblesPref(
        hasShownBottomSheet: Boolean = true,
        hasShownTicker: Boolean
    ) {
        cacheManager.saveCache(
            BUBBLES_PREF,
            TokoChatBubblesCache(
                channelId = channelId,
                hasShownBottomSheet = hasShownBottomSheet, // Need true to show ticker
                hasShownTicker = hasShownTicker // If true, ticker won't show again
            )
        )
    }

    companion object {
        const val TOKOFOOD_SERVICE_TYPE = 5
        const val DELAY_UPDATE_ORDER_STATE = 5000L
        private const val DELAY_FETCH_IMAGE = 500L
        private const val ERROR_COMPRESSED_IMAGE_NULL = "Compressed image null"
        private const val ERROR_RENAMED_IMAGE_NULL = "Renamed image null"
    }
}
