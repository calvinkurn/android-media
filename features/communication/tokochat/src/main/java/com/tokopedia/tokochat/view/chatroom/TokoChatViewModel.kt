package com.tokopedia.tokochat.view.chatroom

import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
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
import com.tokopedia.tokochat.util.TokoChatViewUtil
import com.tokopedia.tokochat.util.TokoChatViewUtil.Companion.getTokoChatPhotoPath
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
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
    private val viewUtil: TokoChatViewUtil,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

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

    private val _updateOrderTransactionStatus =
        MutableSharedFlow<Result<TokoChatOrderProgressResponse>>(replay = Int.ONE)

    val updateOrderTransactionStatus: SharedFlow<Result<TokoChatOrderProgressResponse>> =
        _updateOrderTransactionStatus

    private val _error = MutableLiveData<Pair<Throwable, String>>()
    val error: LiveData<Pair<Throwable, String>>
        get() = _error

    var gojekOrderId: String = ""
    var source: String = ""
    var tkpdOrderId: String = ""
    var isFromTokoFoodPostPurchase = false
    var pushNotifTemplateKey = ""
    var channelId = ""

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

    fun getChatHistory(channelId: String): LiveData<List<ConversationsMessage>> {
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

    fun getTypingStatus(): LiveData<List<String>> {
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
        connectionCheckJob = launchCatchError(context = dispatcher.io, block = {
            while (true) {
                delay(DELAY_UPDATE_ORDER_STATE)
                _isChatConnected.postValue(chatChannelUseCase.isChatConnected())
            }
        }, onError = {
                _isChatConnected.postValue(false)
            })
    }

    fun cancelCheckConnection() {
        connectionCheckJob?.cancel()
        connectionCheckJob = null
    }

    fun getTokoChatBackground() {
        launchCatchError(block = {
            getTokoChatBackgroundUseCase(Unit).collect {
                _chatBackground.value = Success(it)
            }
        }, onError = {
                _chatBackground.value = Fail(it)
            })
    }

    fun loadChatRoomTicker() {
        launchCatchError(context = dispatcher.io, block = {
            val result = getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            _chatRoomTicker.value = Success(result)
        }, onError = {
                _chatRoomTicker.value = Fail(it)
            })
    }

    fun getUserId(): String {
        return registrationChannelUseCase.getUserId()
    }

    fun getMemberLeft(): MutableLiveData<String> {
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
        launchCatchError(block = {
            val result = withContext(dispatcher.io) {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(orderId, serviceType))
            }
            _orderTransactionStatus.value = Success(result)
        }, onError = {
                _orderTransactionStatus.value = Fail(it)
            })
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

    fun getLiveChannel(channelId: String): LiveData<ConversationsChannel?> {
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
        launchCatchError(context = dispatcher.io, block = {
            val cachedImage = getTokoChatPhotoPath(generateImageName(imageId, channelId))
            // If image has never been downloaded, then download
            if (!cachedImage.exists() || isFromRetry) {
                delay(DELAY_FETCH_IMAGE)
                val imageUrlResponse = getImageUrlUseCase(
                    TokoChatGetImageUseCase.Param(imageId, channelId)
                )
                if (imageUrlResponse.success == true) {
                    imageUrlResponse.data?.url?.let {
                        viewUtil.downloadAndSaveByteArrayImage(
                            generateImageName(imageId, channelId),
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
        }, onError = {
                _error.postValue(Pair(it, ::getImageWithId.name))
                onError()
            })
    }

    private fun generateImageName(imageId: String, channelId: String): String {
        return "${imageId}_$channelId"
    }

    companion object {
        const val TOKOFOOD_SERVICE_TYPE = 5
        const val DELAY_UPDATE_ORDER_STATE = 5000L
        private const val DELAY_FETCH_IMAGE = 500L
    }
}
