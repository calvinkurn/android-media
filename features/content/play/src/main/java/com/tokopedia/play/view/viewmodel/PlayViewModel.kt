package com.tokopedia.play.view.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.domain.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateListener
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.util.video.state.PlayViewerVideoStateListener
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.mapper.PlayUiMapper
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @Inject constructor(
        private val playVideoManager: PlayVideoManager,
        videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory,
        channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory,
        videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory,
        private val getChannelInfoUseCase: GetChannelDetailUseCase,
        private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
        private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val getCartCountUseCase: GetCartCountUseCase,
        private val getProductTagItemsUseCase: GetProductTagItemsUseCase,
        private val trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase,
        private val playSocket: PlaySocket,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider,
        private val pageMonitoring: PlayPltPerformanceCallback,
        private val remoteConfig: RemoteConfig
) : PlayBaseViewModel(dispatchers.main) {

    private val _observableLikeInfo = MutableLiveData<PlayLikeInfoUiModel>() /**Added**/
    val observableLikeStatusInfo: LiveData<PlayLikeStatusInfoUiModel> = Transformations.map(_observableLikeInfo) { it.status } /**Added**/

    /**
     * Not Used for Event to component
     */
    val observableGetChannelInfo: LiveData<NetworkResult<ChannelInfoUiModel>>
        get() = _observableGetChannelInfo

    val observableChannelErrorEvent: LiveData<Event<Boolean>>
        get() = _observableChannelErrorEvent
    val observableLatestChannelInfo: LiveData<PlayCompleteInfoUiModel>
        get() = _observableLatestChannelInfo
    val observableVideoMeta: LiveData<VideoMetaUiModel>
        get() = _observableVideoMeta
    val observableSocketInfo: LiveData<PlaySocketInfo>
        get() = _observableSocketInfo
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableTotalLikes: LiveData<TotalLikeUiModel>
        get() = _observableTotalLikes
    val observableLikeState: LiveData<NetworkResult<LikeStateUiModel>>
        get() = _observableLikeState
    val observableTotalViews: LiveData<TotalViewUiModel>
        get() = _observableTotalViews
    val observablePartnerInfo: LiveData<PlayPartnerInfoUiModel> /**Changed**/
        get() = _observablePartnerInfo
    val observableQuickReply: LiveData<PlayQuickReplyInfoUiModel> /**Changed**/
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
    val observableCartInfo: LiveData<PlayCartInfoUiModel> /**Changed**/
        get() = _observableCartInfo
    val observableShareInfo: LiveData<PlayShareInfoUiModel> /**Added**/
        get() = _observableShareInfo
    val observableEventPiP: LiveData<Event<PiPMode>>
        get() = _observableEventPiP

    val videoOrientation: VideoOrientation
        get() {
            val videoStream = _observableLatestChannelInfo.value?.videoStream
            return videoStream?.orientation ?: VideoOrientation.Unknown
        }
    val channelType: PlayChannelType
        get() {
            val videoStream = _observableLatestChannelInfo.value?.videoStream
            return videoStream?.channelType ?: PlayChannelType.Unknown
        }
    val videoPlayer: VideoPlayerUiModel
        get() {
            val videoPlayer = _observableVideoMeta.value?.videoPlayer
            return videoPlayer ?: Unknown
        }
    val viewerVideoState: PlayViewerVideoState
        get() {
            val videoState = _observableVideoProperty.value?.state
            return videoState ?: PlayViewerVideoState.Unknown
        }
    val feedInfoUiModel: FeedInfoUiModel?
        get() {
            val channelInfo = _observableLatestChannelInfo.value?.channelInfo
            return channelInfo?.feedInfo
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

    private var channelId: String = ""

    val latestCompleteChannelData: PlayChannelData.Complete
        get() {
            return PlayChannelData.Complete(
                    id = channelId,
                    partnerInfo = _observablePartnerInfo.value ?: error("Partner Info should not be null"),
                    likeInfo = _observableLikeInfo.value ?: error("Like Info should not be null"),
                    shareInfo = _observableShareInfo.value ?: error("Share Info should not be null"),
                    cartInfo = _observableCartInfo.value ?: error("Cart Info should not be null"),
                    pinnedInfo = PlayPinnedInfoUiModel(
                            pinnedMessage = _observablePinnedMessage.value,
                            pinnedProduct = _observablePinnedProduct.value
                    ),
                    quickReplyInfo = _observableQuickReply.value ?: error("Quick Reply should not be null")
            )
        }

    val pipMode: PiPMode
        get() = _observableEventPiP.value?.peekContent() ?: PiPMode.StopPip

    val isInPiPMode: Boolean
        get() {
            val pipValue = _observableEventPiP.value
            return pipValue != null && pipValue.peekContent() != PiPMode.StopPip
        }
    val isPiPAllowed: Boolean
        get() {
            return remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_PIP, true)
                    && !videoPlayer.isYouTube
        }

    val userId: String
        get() = userSession.userId

    private val isProductSheetInitialized: Boolean
        get() = _observableProductSheetContent.value != null

    private val _observableLatestChannelInfo = MutableLiveData<PlayCompleteInfoUiModel>()
    private val _observableChannelErrorEvent = MutableLiveData<Event<Boolean>>()
    private val _observableGetChannelInfo = MutableLiveData<NetworkResult<ChannelInfoUiModel>>()
    private val _observableSocketInfo = MutableLiveData<PlaySocketInfo>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableTotalLikes = MutableLiveData<TotalLikeUiModel>()
    private val _observableLikeState = MutableLiveData<NetworkResult<LikeStateUiModel>>()
    private val _observableTotalViews = MutableLiveData<TotalViewUiModel>()
    private val _observablePartnerInfo = MutableLiveData<PlayPartnerInfoUiModel>() /**Changed**/
    private val _observableQuickReply = MutableLiveData<PlayQuickReplyInfoUiModel>() /**Changed**/
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observablePinnedProduct = MutableLiveData<PinnedProductUiModel>()
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val _observableVideoMeta = MutableLiveData<VideoMetaUiModel>()
    private val _observableProductSheetContent = MutableLiveData<PlayResult<ProductSheetUiModel>>()
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observablePinned = MediatorLiveData<PinnedUiModel>()
    private val _observableCartInfo = MutableLiveData<PlayCartInfoUiModel>() /**Changed**/
    private val _observableShareInfo = MutableLiveData<PlayShareInfoUiModel>() /**Added**/
    private val _observableEventPiP = MutableLiveData<Event<PiPMode>>()
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
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

    private val videoStateListener = object : PlayViewerVideoStateListener {
        override fun onStateChanged(state: PlayViewerVideoState) {
            scope.launch(dispatchers.immediate) {
                _observableVideoProperty.value = VideoPropertyUiModel(state)
            }
        }
    }

    private val channelStateListener = object : PlayViewerChannelStateListener {
        override fun onChannelFreezeStateChanged(shouldFreeze: Boolean) {
            scope.launch(dispatchers.immediate) {
                val value = _observableEvent.value
                value?.let { _observableEvent.value = it.copy(isFreeze = shouldFreeze) }
            }
        }
    }

    private val videoManagerListener = object : PlayVideoManager.Listener {
        override fun onVideoPlayerChanged(player: ExoPlayer) {
            scope.launch(dispatchers.immediate) {
                if (!videoPlayer.isYouTube) {
                    val videoPlayer = General(player)
                    val currentMetaValue = _observableVideoMeta.value
                    _observableVideoMeta.value = currentMetaValue?.copy(videoPlayer = videoPlayer) ?: VideoMetaUiModel(videoPlayer)
                }
            }
        }
    }

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val stateHandlerObserver = object : Observer<Unit> {
        override fun onChanged(t: Unit?) {}
    }

    private val videoStateProcessor = videoStateProcessorFactory.create(scope) { channelType }
    private val channelStateProcessor = channelStateProcessorFactory.create(scope) { channelType }
    private val videoBufferGovernor = videoBufferGovernorFactory.create(scope)

    init {
        playVideoManager.addListener(videoManagerListener)
        videoStateProcessor.addStateListener(videoStateListener)
        channelStateProcessor.addStateListener(channelStateListener)
        videoBufferGovernor.startBufferGovernance()

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
        if (!isInPiPMode) stopPlayer()
        playVideoManager.removeListener(videoManagerListener)
        videoStateProcessor.removeStateListener(videoStateListener)
        channelStateProcessor.removeStateListener(channelStateListener)
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

    fun watchInPiP() {
        _observableEventPiP.value = Event(PiPMode.WatchInPip)
    }

    fun openPiPBrowsingPage() {
        _observableEventPiP.value = Event(PiPMode.BrowsingOtherPage)
    }

    fun stopPiP() {
        _observableEventPiP.value = Event(PiPMode.StopPip)
    }

    private fun initiateVideo(video: Video) {
        startVideoWithUrlString(
                video.streamSource,
                bufferControl = video.bufferControl?.let { mapBufferControl(it) }
                        ?: PlayBufferControl()
        )
        playVideoManager.setRepeatMode(false)
    }

    private fun startVideoWithUrlString(urlString: String, bufferControl: PlayBufferControl) {
        try {
            playVideoManager.playUri(uri = Uri.parse(urlString), bufferControl = bufferControl)
        } catch (e: Exception) {}
    }

    private fun playGeneralVideoStream(channel: Channel) {
        if (channel.configuration.active) initiateVideo(channel.video)
    }

    private fun stopPlayer() {
        if (playVideoManager.isVideoLive() || channelType.isLive || isFreezeOrBanned) playVideoManager.release()
        else playVideoManager.stop()
    }
    //endregion

    fun processChannelInfo(channelData: PlayChannelData) {
        when (channelData) {
            PlayChannelData.Empty -> {}
            is PlayChannelData.Placeholder -> {
                channelId = channelData.id
                handlePartnerInfo(channelData.partnerInfo)
                handleShareInfo(channelData.shareInfo)
                handleCartInfo(channelData.cartInfo)
                handlePinnedInfo(channelData.pinnedInfo)
                handleQuickReplyInfo(channelData.quickReplyInfo)
            }
            is PlayChannelData.Complete -> {
                channelId = channelData.id
                handlePartnerInfo(channelData.partnerInfo)
                handleLikeInfo(channelData.likeInfo)
                handleShareInfo(channelData.shareInfo)
                handleCartInfo(channelData.cartInfo)
                handlePinnedInfo(channelData.pinnedInfo)
                handleQuickReplyInfo(channelData.quickReplyInfo)
            }
        }
    }

    fun updateChannelInfo(channelData: PlayChannelData) {
        when (channelData) {
            PlayChannelData.Empty -> {}
            is PlayChannelData.Placeholder -> {
                updatePartnerInfo(channelData.partnerInfo)
                updateLikeInfo(channelData.likeParamInfo, channelData.id)
                updateCartInfo(channelData.cartInfo)
            }
            is PlayChannelData.Complete -> {
                updatePartnerInfo(channelData.partnerInfo)
                updateLikeInfo(channelData.likeInfo.param, channelData.id)
                updateCartInfo(channelData.cartInfo)
            }
        }
    }

    fun getChannelInfo(channelId: String) {

        pageMonitoring.startNetworkRequestPerformanceMonitoring()
        var retryCount = 0

        fun getChannelInfoResponse(channelId: String){
            channelInfoJob = scope.launchCatchError(block = {
                val channel = withContext(dispatchers.io) {
                    getChannelInfoUseCase.params = GetChannelDetailUseCase.createParams(channelId)
                    return@withContext getChannelInfoUseCase.executeOnBackground()
                }

                val completeInfoUiModel = PlayUiMapper.createCompleteInfoModel(
                        channel = channel,
                        isBanned = _observableEvent.value?.isBanned ?: false,
                        exoPlayer = playVideoManager.videoPlayer
                )
                _observableLatestChannelInfo.value = completeInfoUiModel

                _observableGetChannelInfo.value = NetworkResult.Success(completeInfoUiModel.channelInfo)
//                _observablePartnerInfo.value = completeInfoUiModel.channelInfo.partnerInfo
                _observableTotalViews.value = completeInfoUiModel.totalView
                _observablePinnedMessage.value = completeInfoUiModel.pinnedMessage
                _observablePinnedProduct.value = completeInfoUiModel.pinnedProduct
//                _observableQuickReply.value = completeInfoUiModel.quickReply
                _observableVideoMeta.value = VideoMetaUiModel(completeInfoUiModel.videoPlayer, completeInfoUiModel.videoStream)
                _observableEvent.value = completeInfoUiModel.event

                if (!isActive) return@launchCatchError

                launch { getTotalLikes(completeInfoUiModel.channelInfo.id) }
                launch { getIsLike(completeInfoUiModel.channelInfo.feedInfo) }
//                launch { getBadgeCart(channel.configuration.showCart) }
                launch { if (completeInfoUiModel.channelInfo.showPinnedProduct) getProductTagItems(completeInfoUiModel.channelInfo) }

                startWebSocket(channelId)

                if (completeInfoUiModel.videoPlayer.isGeneral) playGeneralVideoStream(channel)
                else playVideoManager.release()

//                if (completeInfoUiModel.channelInfo.partnerInfo.type == PartnerType.Shop) {
//                    getFollowStatus(completeInfoUiModel.channelInfo)
//                }

            }) {
                if (retryCount == 0) _observableChannelErrorEvent.value = Event(false)
                if (retryCount++ < MAX_RETRY_CHANNEL_INFO) getChannelInfoResponse(channelId)
                else if (it !is CancellationException) {
                    if (_observableLatestChannelInfo.value == null) doOnForbidden()
                    _observableGetChannelInfo.value = NetworkResult.Fail(it)
                }
            }
        }

        if (!isFreezeOrBanned) {
            _observableGetChannelInfo.value = NetworkResult.Loading
            if (channelInfoJob?.isActive != true) getChannelInfoResponse(channelId)
        }
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
        _observableLikeState.value = NetworkResult.Success(LikeStateUiModel(isLiked = shouldLike, fromNetwork = false))
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
        val cartInfo = _observableCartInfo.value
        if (cartInfo?.shouldShow == true) {
            viewModelScope.launch {
                val newCount = getCartCount()
                _observableCartInfo.value = cartInfo.copy(count = newCount)
            }
        }
    }

    private fun startWebSocket(channelId: String) {
        scope.launchCatchError(block = {
            val socketCredential = withContext(dispatchers.io) {
                return@withContext getSocketCredentialUseCase.executeOnBackground()
            }
            connectWebSocket(
                    channelId = channelId,
                    socketCredential = socketCredential
            )
        }) {
            connectWebSocket(
                    channelId = channelId,
                    socketCredential = SocketCredential()
            )
        }
    }

    private fun connectWebSocket(channelId: String, socketCredential: SocketCredential) {
        playSocket.channelId = channelId
        playSocket.gcToken = socketCredential.gcToken
        playSocket.settings = socketCredential.setting
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
                        //TODO("Use separate mapper")
//                        _observableQuickReply.value = PlayUiMapper.mapQuickReply(result)
                    }
                    is BannedFreeze -> {
                        if (result.channelId.isNotEmpty() && result.channelId.equals(channelId, true)) {
                            _observableEvent.value = _observableEvent.value?.copy(
                                    isBanned = result.isBanned && result.userId.isNotEmpty()
                                            && result.userId.equals(userSession.userId, true))

                            channelStateProcessor.setIsFreeze(result.isFreeze)
                        }
                    }
                    is ProductTag -> {
                        val productSheet = _observableProductSheetContent.value
                        val currentProduct = if (productSheet is PlayResult.Success) productSheet.data else ProductSheetUiModel.empty()
                        val productList = PlayUiMapper.mapItemProducts(result.listOfProducts)
                        _observableProductSheetContent.value = PlayResult.Success(
                                data = currentProduct.copy(productList = productList)
                        )
                        trackProductTag(
                                channelId = channelId,
                                productList = productList
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
            connectWebSocket(channelId, socketCredential)
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
     * Handle existing channel data
     */
    private fun handlePartnerInfo(partnerInfo: PlayPartnerInfoUiModel) {
        _observablePartnerInfo.value = partnerInfo
    }

    private fun handleLikeInfo(likeInfoUiModel: PlayLikeInfoUiModel) {
        _observableLikeInfo.value = likeInfoUiModel
    }

    private fun handleShareInfo(shareInfo: PlayShareInfoUiModel) {
        _observableShareInfo.value = shareInfo
    }

    private fun handleCartInfo(cartInfo: PlayCartInfoUiModel) {
        _observableCartInfo.value = cartInfo
    }

    private fun handlePinnedInfo(pinnedInfo: PlayPinnedInfoUiModel) {
        _observablePinnedMessage.value = pinnedInfo.pinnedMessage
        _observablePinnedProduct.value = pinnedInfo.pinnedProduct
    }

    private fun handleQuickReplyInfo(quickReplyInfo: PlayQuickReplyInfoUiModel) {
        _observableQuickReply.value = quickReplyInfo
    }

    /**
     * Update channel data
     */
    private fun updatePartnerInfo(partnerInfo: PlayPartnerInfoUiModel) {
        if (partnerInfo.type == PartnerType.Shop) {
            viewModelScope.launch {
                val shopInfo = getShopInfo(partnerInfo)

                val newPartnerInfo = partnerInfo.copy(isFollowable = userSession.shopId != shopInfo.shopCore.shopId)
                newPartnerInfo.isFollowed = shopInfo.favoriteData.alreadyFavorited == 1

                _observablePartnerInfo.value = newPartnerInfo
            }
        }
    }

    private fun updateLikeInfo(likeParamInfo: PlayLikeParamInfoUiModel, channelId: String) {
        viewModelScope.launch {
            val deferredTotalLike = async { getTotalLikes(channelId) }
            val deferredIsLiked = async { getIsLiked(likeParamInfo) }
            val newLikeStatus = PlayLikeStatusInfoUiModel(
                    totalLike = deferredTotalLike.await().totalLike,
                    totalLikeFormatted = deferredTotalLike.await().totalLikeFormatted,
                    isLiked = deferredIsLiked.await()
            )
            _observableLikeInfo.value = likeParamInfo + newLikeStatus
        }
    }

    private fun updateCartInfo(cartInfo: PlayCartInfoUiModel) {
        if (cartInfo.shouldShow) {
            viewModelScope.launch {
                val cartCount = getCartCount()
                _observableCartInfo.value = cartInfo.copy(count = cartCount)
            }
        }
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

    private suspend fun getTotalLikes(channelId: String): TotalLike = withContext(dispatchers.io) {
        getTotalLikeUseCase.params = GetTotalLikeUseCase.createParam(channelId)
        getTotalLikeUseCase.executeOnBackground()
    }

    private suspend fun getIsLiked(likeParamInfo: PlayLikeParamInfoUiModel) = withContext(dispatchers.io) {
        getIsLikeUseCase.params = GetIsLikeUseCase.createParam(
                contentId = likeParamInfo.contentId.toIntOrZero(),
                contentType = likeParamInfo.contentType
        )
        getIsLikeUseCase.executeOnBackground()
    }

    private suspend fun getIsLike(feedInfoUiModel: FeedInfoUiModel) {
        try {
            val isLiked = withContext(dispatchers.io) {
                getIsLikeUseCase.params = GetIsLikeUseCase.createParam(
                        contentId = feedInfoUiModel.contentId.toIntOrZero(),
                        contentType = feedInfoUiModel.contentType
                )
                getIsLikeUseCase.executeOnBackground()
            }
            _observableLikeState.value = NetworkResult.Success(LikeStateUiModel(isLiked, fromNetwork = true))
        } catch (e: Exception) {
            _observableLikeState.value = NetworkResult.Fail(e)
        }
    }

    private suspend fun getShopInfo(partnerInfo: PlayPartnerInfoUiModel): ShopInfo = withContext(dispatchers.io) {
            getPartnerInfoUseCase.params = GetPartnerInfoUseCase.createParam(partnerInfo.id.toInt(), partnerInfo.type)
            getPartnerInfoUseCase.executeOnBackground()
    }

    private suspend fun getCartCount(): Int = withContext(dispatchers.io) {
        try {
            getCartCountUseCase.executeOnBackground()
        } catch (e: Exception) {
            0
        }
    }

    private suspend fun getProductTagItems(channel: ChannelInfoUiModel) {
        if (!isProductSheetInitialized) _observableProductSheetContent.value = PlayResult.Loading(
                showPlaceholder = true
        )

        try {
            val productTagsItems = withContext(dispatchers.io) {
                getProductTagItemsUseCase.params = GetProductTagItemsUseCase.createParam(channel.id)
                getProductTagItemsUseCase.executeOnBackground()
            }
            val partnerId = partnerId ?: 0L
            val productSheet = PlayUiMapper.mapProductSheet(
                    channel.titleBottomSheet,
                    partnerId,
                    productTagsItems)
            _observableProductSheetContent.value = PlayResult.Success(productSheet)
            trackProductTag(
                    channelId = channel.id,
                    productList = productSheet.productList
            )
        } catch (e: Exception) {
            _observableProductSheetContent.value = PlayResult.Failure(e) {
                scope.launch { if (channel.showPinnedProduct) getProductTagItems(channel) }
            }
        }
    }

    private fun trackProductTag(channelId: String, productList: List<PlayProductUiModel>) {
        scope.launchCatchError(block = {
            withContext(dispatchers.io) {
                val productIds = productList.mapNotNull { product -> if (product is ProductLineUiModel) product.id else null }
                trackProductTagBroadcasterUseCase.params = TrackProductTagBroadcasterUseCase.createParams(channelId, productIds)
                trackProductTagBroadcasterUseCase.executeOnBackground()
            }
        }) {
        }
    }

    private fun mapBufferControl(bufferControl: Video.BufferControl) = PlayBufferControl(
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

        private const val FIREBASE_REMOTE_CONFIG_KEY_PIP = "android_mainapp_enable_pip"
    }
}