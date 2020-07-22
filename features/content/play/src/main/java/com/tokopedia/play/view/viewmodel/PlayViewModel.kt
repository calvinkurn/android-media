package com.tokopedia.play.view.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.play.ERR_SERVER_ERROR
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.domain.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.mapper.PlayUiMapper
import com.tokopedia.play.view.wrapper.GlobalErrorCodeWrapper
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @Inject constructor(
        private val playVideoManager: PlayVideoManager,
        private val getChannelInfoUseCase: GetChannelInfoUseCase,
        private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val getCartCountUseCase: GetCartCountUseCase,
        private val getProductTagItemsUseCase: GetProductTagItemsUseCase,
        private val playSocket: PlaySocket,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : PlayBaseViewModel(dispatchers.main) {

    /**
     * Not Used for Event to component
     */
    val observableGetChannelInfo: LiveData<Result<ChannelInfoUiModel>>
        get() = _observableGetChannelInfo

    val observableVideoPlayer: LiveData<VideoPlayerUiModel>
        get() = _observableVideoPlayer
    val observableSocketInfo: LiveData<PlaySocketInfo>
        get() = _observableSocketInfo
    val observableVideoStream: LiveData<VideoStreamUiModel>
        get() = _observableVideoStream
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableTotalLikes: LiveData<TotalLikeUiModel>
        get() = _observableTotalLikes
    val observableLikeState: LiveData<LikeStateUiModel>
        get() = _observableLikeState
    val observableTotalViews: LiveData<TotalViewUiModel>
        get() = _observableTotalViews
    val observablePartnerInfo: LiveData<PartnerInfoUiModel>
        get() = _observablePartnerInfo
    val observableQuickReply: LiveData<QuickReplyUiModel>
        get() = _observableQuickReply
    val observableEvent: LiveData<EventUiModel>
        get() = _observableEvent
    val observableBottomInsetsState: LiveData<Map<BottomInsetsType, BottomInsetsState>>
        get() = _observableBottomInsetsState
    val observablePinned: LiveData<PinnedUiModel>
        get() = _observablePinned
    val observableVideoProperty: LiveData<VideoPropertyUiModel>
        get() = _observableVideoProperty
    val observableProductSheetContent: LiveData<PlayResult<ProductSheetUiModel>>
        get() = _observableProductSheetContent
    val observableBadgeCart: LiveData<CartUiModel>
        get() = _observableBadgeCart

    val videoOrientation: VideoOrientation
        get() {
            val videoStream = _observableCompleteInfo.value?.videoStream
            return videoStream?.orientation ?: VideoOrientation.Unknown
        }
    val channelType: PlayChannelType
        get() {
            val videoStream = _observableCompleteInfo.value?.videoStream
            return videoStream?.channelType ?: PlayChannelType.Unknown
        }
    val videoPlayer: VideoPlayerUiModel
        get() {
            val videoPlayer = _observableCompleteInfo.value?.videoPlayer
            return videoPlayer ?: Unknown
        }
    val contentId: Int
        get() {
            val channelInfo = _observableCompleteInfo.value?.channelInfo
            return channelInfo?.contentId ?: 0
        }
    val contentType: Int
        get() {
            val channelInfo = _observableCompleteInfo.value?.channelInfo
            return channelInfo?.contentType ?: 0
        }
    val likeType: Int
        get() {
            val channelInfo = _observableCompleteInfo.value?.channelInfo
            return channelInfo?.likeType ?: 0
        }
    val bottomInsets: Map<BottomInsetsType, BottomInsetsState>
        get() {
            val value = _observableBottomInsetsState.value
            return value ?: getDefaultBottomInsetsMapState()
        }
    val isFreezeOrBanned: Boolean
        get() {
            val event = _observableEvent.value
            return event?.isFreeze == true || event?.isBanned == true
        }
    val partnerId: Long?
        get() = _observablePartnerInfo.value?.id
    val totalView: String?
        get() = _observableTotalViews.value?.totalView

    val userId: String
        get() = userSession.userId

    private val isProductSheetInitialized: Boolean
        get() = _observableProductSheetContent.value != null

    private val _observableCompleteInfo = MutableLiveData<PlayCompleteInfoUiModel>()
    private val _observableGetChannelInfo = MutableLiveData<Result<ChannelInfoUiModel>>()
    private val _observableSocketInfo = MutableLiveData<PlaySocketInfo>()
    private val _observableVideoStream = MutableLiveData<VideoStreamUiModel>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableTotalLikes = MutableLiveData<TotalLikeUiModel>()
    private val _observableLikeState = MutableLiveData<LikeStateUiModel>()
    private val _observableTotalViews = MutableLiveData<TotalViewUiModel>()
    private val _observablePartnerInfo = MutableLiveData<PartnerInfoUiModel>()
    private val _observableQuickReply = MutableLiveData<QuickReplyUiModel>()
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observablePinnedProduct = MutableLiveData<PinnedProductUiModel>()
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val _observableProductSheetContent = MutableLiveData<PlayResult<ProductSheetUiModel>>()
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observablePinned = MediatorLiveData<PinnedUiModel>()
    private val _observableVideoPlayer = MediatorLiveData<VideoPlayerUiModel>().apply {
        addSource(playVideoManager.getObservableVideoPlayer()) {
            if (!videoPlayer.isYouTube) value = General(it)
        }
    }
    private val _observableBadgeCart = MutableLiveData<CartUiModel>()
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(playVideoManager.getObservablePlayVideoState()) {
            _observableVideoProperty.value = VideoPropertyUiModel(it)
        }
        addSource(observablePartnerInfo) {
            val currentMessageValue = _observablePinnedMessage.value
            if (currentMessageValue != null) {
                _observablePinnedMessage.value = currentMessageValue.copy(
                        partnerName = it.name
                )
            }

            val currentProductValue = _observablePinnedProduct.value
            if (currentProductValue != null) {
                _observablePinnedProduct.value = currentProductValue.copy(
                        partnerName = it.name
                )
            }
        }
        addSource(observableProductSheetContent) {
            _observablePinned.value = getPinnedModel(
                    pinnedMessage = _observablePinnedMessage.value,
                    pinnedProduct = _observablePinnedProduct.value,
                    productSheetResult = it
            )
        }
        addSource(observableEvent) {
            if (it.isFreeze || it.isBanned) doOnForbidden()
        }
    }

    //region helper
    private val hasWordsOrDotsRegex = Regex("(\\.+|[a-z]+)")
    private val amountStringStepArray = arrayOf("k", "m")
    private fun String.trimMultipleNewlines() = trim().replace(Regex("(\\n+)"), "\n")
    //endregion

    private var channelInfoJob: Job? = null

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val stateHandlerObserver = object : Observer<Unit> {
        override fun onChanged(t: Unit?) {}
    }

    init {
        stateHandler.observeForever(stateHandlerObserver)

        _observablePinned.addSource(_observablePinnedMessage) {
            _observablePinned.value = getPinnedModel(
                    pinnedMessage = it,
                    pinnedProduct = _observablePinnedProduct.value,
                    productSheetResult = _observableProductSheetContent.value
            )
        }
        _observablePinned.addSource(_observablePinnedProduct) {
            _observablePinned.value = getPinnedModel(
                    pinnedMessage = _observablePinnedMessage.value,
                    pinnedProduct = it,
                    productSheetResult = _observableProductSheetContent.value
            )
        }

        _observableChatList.value = mutableListOf()
    }

    //region lifecycle
    override fun onCleared() {
        super.onCleared()
        stateHandler.removeObserver(stateHandlerObserver)
        destroy()
        stopPlayer()
    }
    //endregion

    //region bottom insets
    fun onKeyboardShown(estimatedKeyboardHeight: Int) {
        val isLive = channelType.isLive
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.Keyboard] =
                if (isLive) BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedKeyboardHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isShown == true
                ) else BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onKeyboardHidden() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.Keyboard] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowProductSheet(estimatedProductSheetHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.ProductSheet] =
                BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedProductSheetHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.ProductSheet]?.isShown == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onHideProductSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.ProductSheet] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.ProductSheet]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowVariantSheet(estimatedProductSheetHeight: Int, product: ProductLineUiModel, action: ProductAction) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.VariantSheet] =
                BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedProductSheetHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.VariantSheet]?.isShown == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onHideVariantSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.VariantSheet] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.VariantSheet]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun hideInsets(isKeyboardHandled: Boolean) {
        val defaultBottomInsets = getDefaultBottomInsetsMapState()
        _observableBottomInsetsState.value = if (isKeyboardHandled) {
            defaultBottomInsets.toMutableMap().apply {
                this[BottomInsetsType.Keyboard] = BottomInsetsState.Hidden(true)
            }
        } else defaultBottomInsets
    }

    private fun getLatestBottomInsetsMapState(): Map<BottomInsetsType, BottomInsetsState> {
        val currentValue = _observableBottomInsetsState.value
                ?: return getDefaultBottomInsetsMapState()
        currentValue.values.forEach {
            it.isPreviousStateSame = true
            if (it is BottomInsetsState.Shown) it.deepLevel += 1
        }

        return currentValue
    }

    private fun getDefaultBottomInsetsMapState(): Map<BottomInsetsType, BottomInsetsState> {
        val currentBottomInsetsMap = _observableBottomInsetsState.value
        val defaultKeyboardState = currentBottomInsetsMap?.get(BottomInsetsType.Keyboard)?.isHidden ?: false
        val defaultProductSheetState = currentBottomInsetsMap?.get(BottomInsetsType.ProductSheet)?.isHidden ?: false
        val defaultVariantSheetState = currentBottomInsetsMap?.get(BottomInsetsType.VariantSheet)?.isHidden ?: false
        return mapOf(
                BottomInsetsType.Keyboard to BottomInsetsState.Hidden(defaultKeyboardState),
                BottomInsetsType.ProductSheet to BottomInsetsState.Hidden(defaultProductSheetState),
                BottomInsetsType.VariantSheet to BottomInsetsState.Hidden(defaultVariantSheetState)
        )
    }
    //endregion

    //region video player
    fun startCurrentVideo() {
        if (!videoPlayer.isYouTube) playVideoManager.resume()
    }

    fun getVideoDuration(): Long {
        return playVideoManager.getVideoDuration()
    }

    private fun initiateVideo(channel: Channel) {
        startVideoWithUrlString(
                channel.videoStream.config.streamUrl,
                bufferControl = channel.videoStream.bufferControl?.let { mapBufferControl(it) }
                        ?: PlayBufferControl()
        )
//        startVideoWithUrlString(
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
////                "https://assets.mixkit.co/videos/preview/mixkit-womans-feet-splashing-in-the-pool-1261-large.mp4",
//                bufferControl = channel.videoStream.bufferControl?.let { mapBufferControl(it) }
//                        ?: PlayBufferControl()
//        )
        playVideoManager.setRepeatMode(false)
    }

    private fun startVideoWithUrlString(urlString: String, bufferControl: PlayBufferControl) {
        try {
            playVideoManager.playUri(uri = Uri.parse(urlString), bufferControl = bufferControl)
        } catch (e: Exception) {}
    }

    private fun playGeneralVideoStream(channel: Channel) {
        if (channel.isActive) initiateVideo(channel)
    }

    private fun stopPlayer() {
        playVideoManager.stop()
    }
    //endregion

    fun getChannelInfo(channelId: String) {

        var retryCount = 0

        fun getChannelInfoResponse(channelId: String){
            channelInfoJob = scope.launchCatchError(block = {
                val channel = withContext(dispatchers.io) {
                    getChannelInfoUseCase.channelId = channelId
                    return@withContext getChannelInfoUseCase.executeOnBackground()
                }

                launch { getTotalLikes(channel.contentId, channel.contentType, channel.likeType) }
                launch { getIsLike(channel.contentId, channel.contentType) }
                launch { getBadgeCart(channel.isShowCart) }
                launch { if (channel.isShowProductTagging) getProductTagItems(channel) }

                startWebSocket(channelId, channel.gcToken, channel.settings)

                val completeInfoUiModel = PlayUiMapper.createCompleteInfoModel(
                        channel = channel,
                        partnerName = _observablePartnerInfo.value?.name.orEmpty(),
                        isBanned = _observableEvent.value?.isBanned ?: false,
                        exoPlayer = playVideoManager.videoPlayer
                )
                _observableCompleteInfo.value = completeInfoUiModel

                _observableGetChannelInfo.value = Success(completeInfoUiModel.channelInfo)
                _observableTotalViews.value = completeInfoUiModel.totalView
                _observablePinnedMessage.value = completeInfoUiModel.pinnedMessage
                _observablePinnedProduct.value = completeInfoUiModel.pinnedProduct
                _observableQuickReply.value = completeInfoUiModel.quickReply
                _observableVideoPlayer.value = completeInfoUiModel.videoPlayer
                _observableVideoStream.value = completeInfoUiModel.videoStream
                _observableEvent.value = completeInfoUiModel.event

                yield()
                if (completeInfoUiModel.videoPlayer.isGeneral) playGeneralVideoStream(channel)
                else playVideoManager.release()

                _observablePartnerInfo.value = getPartnerInfo(completeInfoUiModel.channelInfo)
            }) {
                if (retryCount++ < MAX_RETRY_CHANNEL_INFO) getChannelInfoResponse(channelId)
                else if (it !is CancellationException) {
                    val error = if (_observableGetChannelInfo.value == null && GlobalErrorCodeWrapper.wrap(it.message.orEmpty()) == GlobalErrorCodeWrapper.Unknown)
                        Throwable(ERR_SERVER_ERROR)
                    else it

                    if (GlobalErrorCodeWrapper.wrap(error.message.orEmpty()) != GlobalErrorCodeWrapper.Unknown) doOnForbidden()

                    _observableGetChannelInfo.value = Fail(error)
                }
            }
        }

        getChannelInfoResponse(channelId)
    }

    fun sendChat(message: String) {
        if (!userSession.isLoggedIn)
            return

        val cleanMessage = message.trimMultipleNewlines()
        playSocket.send(cleanMessage)
        setNewChat(PlayUiMapper.mapPlayChat(userSession.userId,
                PlayChat(
                        message = cleanMessage,
                        user = PlayChat.UserData(
                                id = userSession.userId,
                                name = userSession.name,
                                image = userSession.profilePicture)
                )
        ))
    }

    fun changeLikeCount(shouldLike: Boolean) {
        _observableLikeState.value = LikeStateUiModel(isLiked = shouldLike, fromNetwork = false)
        val currentTotalLike = _observableTotalLikes.value ?: TotalLikeUiModel.empty()
        if (!hasWordsOrDotsRegex.containsMatchIn(currentTotalLike.totalLikeFormatted)) {
            var finalTotalLike = currentTotalLike.totalLike + (if (shouldLike) 1 else -1)
            if (finalTotalLike < 0) finalTotalLike = 0
            _observableTotalLikes.value = TotalLikeUiModel(
                    finalTotalLike,
                    finalTotalLike.toAmountString(amountStringStepArray, separator = ".")
            )
        }
    }

    fun onBackPressed(): Boolean {
        val shownBottomSheets = observableBottomInsetsState.value
                ?.filter { it.value.isShown }
                ?.mapValues { it.value as BottomInsetsState.Shown }
                .orEmpty()
        val entry = shownBottomSheets.minBy { it.value.deepLevel }
        when (entry?.key) {
            BottomInsetsType.Keyboard -> onKeyboardHidden()
            BottomInsetsType.ProductSheet -> onHideProductSheet()
            BottomInsetsType.VariantSheet -> onHideVariantSheet()
        }
        return shownBottomSheets.isNotEmpty()
    }

    fun updateBadgeCart() {
        val channelInfo = _observableGetChannelInfo.value
        if (channelInfo != null && channelInfo is Success) {
            scope.launch { getBadgeCart(channelInfo.data.isShowCart) }
        }
    }

    private fun startWebSocket(channelId: String, gcToken: String, settings: Channel.Settings) {
        playSocket.channelId = channelId
        playSocket.gcToken = gcToken
        playSocket.settings = settings
        playSocket.connect(onMessageReceived = { response ->
            scope.launch {
                val result = withContext(dispatchers.io) {
                    val socketMapper = PlaySocketMapper(response)
                    socketMapper.mapping()
                }
                when (result) {
                    is TotalLike -> {
                        _observableTotalLikes.value = PlayUiMapper.mapTotalLikes(result)
                    }
                    is TotalView -> {
                        _observableTotalViews.value = PlayUiMapper.mapTotalViews(result)
                    }
                    is PlayChat -> {
                        setNewChat(PlayUiMapper.mapPlayChat(userSession.userId, result))
                    }
                    is PinnedMessage -> {
                        val partnerName = _observablePartnerInfo.value?.name.orEmpty()
                        _observablePinnedMessage.value = PlayUiMapper.mapPinnedMessage(partnerName, result)
                    }
                    is QuickReply -> {
                        _observableQuickReply.value = PlayUiMapper.mapQuickReply(result)
                    }
                    is BannedFreeze -> {
                        if (result.channelId.isNotEmpty() && result.channelId.equals(channelId, true)) {
                            _observableEvent.value = _observableEvent.value?.copy(
                                    isFreeze = result.isFreeze,
                                    isBanned = result.isBanned && result.userId.isNotEmpty()
                                            && result.userId.equals(userSession.userId, true))
                        }
                    }
                    is ProductTag -> {
                        val productSheet = _observableProductSheetContent.value
                        val currentProduct = if (productSheet is PlayResult.Success) productSheet.data else ProductSheetUiModel.empty()
                        _observableProductSheetContent.value = PlayResult.Success(
                                data = currentProduct.copy(productList = PlayUiMapper.mapItemProducts(result.listOfProducts))
                        )
                    }
                    is MerchantVoucher -> {
                        val productSheet = _observableProductSheetContent.value
                        val currentProduct = if (productSheet is PlayResult.Success) productSheet.data else ProductSheetUiModel.empty()
                        _observableProductSheetContent.value = PlayResult.Success(
                                data = currentProduct.copy(voucherList = PlayUiMapper.mapItemVouchers(result.listOfVouchers))
                        )
                    }
                }
            }
        }, onReconnect = {
            _observableSocketInfo.value = PlaySocketInfo.Reconnect
        }, onError = {
            _observableSocketInfo.value = PlaySocketInfo.Error(it)
            startWebSocket(channelId, gcToken, settings)
        })
    }

    fun getStateHelper(orientation: ScreenOrientation): StateHelperUiModel {
        val pinned = _observablePinned.value
        val bottomInsets = _observableBottomInsetsState.value
        return StateHelperUiModel(
                shouldShowPinned = pinned is PinnedMessageUiModel || pinned is PinnedProductUiModel,
                channelType = channelType,
                videoPlayer = videoPlayer,
                bottomInsets = bottomInsets ?: getDefaultBottomInsetsMapState(),
                screenOrientation = orientation,
                videoOrientation = videoOrientation,
                videoState = playVideoManager.getVideoState()
        )
    }

    fun stopJob() {
        channelInfoJob?.cancel()
    }

    /**
     * Private Method
     */
    private fun destroy() {
        playSocket.destroy()
    }

    private fun setNewChat(chat: PlayChatUiModel) {
        val currentChatList = _observableChatList.value ?: mutableListOf()
        currentChatList.add(chat)
        _observableChatList.value = currentChatList
    }

    private suspend fun getTotalLikes(contentId: Int, contentType: Int, likeType: Int) {
        try {
            val totalLike = withContext(dispatchers.io) {
                getTotalLikeUseCase.params = GetTotalLikeUseCase.createParam(contentId, contentType, likeType)
                getTotalLikeUseCase.executeOnBackground()
            }
            _observableTotalLikes.value = PlayUiMapper.mapTotalLikes(totalLike)
        } catch (e: Exception) {
        }
    }

    private suspend fun getIsLike(contentId: Int, contentType: Int) {
        try {
            val isLiked = withContext(dispatchers.io) {
                getIsLikeUseCase.params = GetIsLikeUseCase.createParam(contentId, contentType)
                getIsLikeUseCase.executeOnBackground()
            }
            _observableLikeState.value = LikeStateUiModel(isLiked, fromNetwork = true)
        } catch (e: Exception) {
        }
    }

    private suspend fun getPartnerInfo(channel: ChannelInfoUiModel): PartnerInfoUiModel {
        val partnerId = channel.partnerId
        return if (channel.partnerType == PartnerType.Admin) {
            PartnerInfoUiModel(
                    id = partnerId,
                    name = channel.moderatorName,
                    type = channel.partnerType,
                    isFollowed = true,
                    isFollowable = false
            )
        } else {
            val shopInfo = getPartnerInfo(partnerId, channel.partnerType)
            PlayUiMapper.mapPartnerInfoFromShop(userSession.shopId, shopInfo)
        }
    }

    private suspend fun getPartnerInfo(partnerId: Long, partnerType: PartnerType) = withContext(dispatchers.io) {
        getPartnerInfoUseCase.params = GetPartnerInfoUseCase.createParam(partnerId.toInt(), partnerType)
        getPartnerInfoUseCase.executeOnBackground()
    }

    private suspend fun getBadgeCart(isShowCart: Boolean) {
        if (isShowCart) {
            try {
                val countCart = withContext(dispatchers.io) {
                    getCartCountUseCase.executeOnBackground()
                }
                _observableBadgeCart.value = CartUiModel(isShowCart, countCart)
            } catch (e: Exception) {
            }
        }
    }

    private suspend fun getProductTagItems(channel: Channel) {
        if (!isProductSheetInitialized) _observableProductSheetContent.value = PlayResult.Loading(
                showPlaceholder = true
        )

        try {
            val productTagsItems = withContext(dispatchers.io) {
                getProductTagItemsUseCase.params = GetProductTagItemsUseCase.createParam(channel.channelId)
                getProductTagItemsUseCase.executeOnBackground()
            }
            val partnerId = partnerId ?: 0L
            _observableProductSheetContent.value = PlayResult.Success(
                    PlayUiMapper.mapProductSheet(
                            channel.pinnedProduct.titleBottomSheet,
                            partnerId,
                            productTagsItems)
            )

        } catch (e: Exception) {
            _observableProductSheetContent.value = PlayResult.Failure(e) {
                scope.launch { if (channel.isShowProductTagging) getProductTagItems(channel) }
            }
        }
    }

    private fun mapBufferControl(bufferControl: VideoStream.BufferControl) = PlayBufferControl(
            minBufferMs = bufferControl.minBufferingSecond * MS_PER_SECOND,
            maxBufferMs = bufferControl.maxBufferingSecond * MS_PER_SECOND,
            bufferForPlaybackMs = bufferControl.bufferForPlayback * MS_PER_SECOND,
            bufferForPlaybackAfterRebufferMs = bufferControl.bufferForPlaybackAfterRebuffer * MS_PER_SECOND
    )

    private fun doOnForbidden() {
        destroy()
        stopPlayer()
        hideInsets(isKeyboardHandled = false)
    }

    private fun getPinnedModel(
            pinnedMessage: PinnedMessageUiModel?,
            pinnedProduct: PinnedProductUiModel?,
            productSheetResult: PlayResult<ProductSheetUiModel>?
    ): PinnedUiModel {
        return if (pinnedProduct != null && productSheetResult is PlayResult.Success && !productSheetResult.data.productList.isNullOrEmpty()) pinnedProduct
        else pinnedMessage ?: PinnedRemoveUiModel
    }
    //endregion

    companion object {
        private const val MAX_RETRY_CHANNEL_INFO = 3

        private const val MS_PER_SECOND = 1000
    }
}