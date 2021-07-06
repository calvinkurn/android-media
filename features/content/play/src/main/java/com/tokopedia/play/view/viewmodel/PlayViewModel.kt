package com.tokopedia.play.view.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.R
import com.tokopedia.play.data.*
import com.tokopedia.play.data.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play.data.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play.data.dto.interactive.isScheduled
import com.tokopedia.play.data.interactive.ChannelInteractive
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.data.websocket.revamp.WebSocketAction
import com.tokopedia.play.data.websocket.revamp.WebSocketClosedReason
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateListener
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.*
import com.tokopedia.play.view.monitoring.PlayVideoLatencyPerformanceMonitoring
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play.view.uimodel.state.PlayFollowStatusUiState
import com.tokopedia.play.view.uimodel.state.PlayInteractiveUiState
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @Inject constructor(
        playVideoBuilder: PlayVideoWrapper.Builder,
        videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory,
        channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory,
        videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory,
        private val getChannelStatusUseCase: GetChannelStatusUseCase,
        private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
        private val getReportSummariesUseCase: GetReportSummariesUseCase,
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val getCartCountUseCase: GetCartCountUseCase,
        private val getProductTagItemsUseCase: GetProductTagItemsUseCase,
        private val trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase,
        private val trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
        private val playSocketToModelMapper: PlaySocketToModelMapper,
        private val playUiModelMapper: PlayUiModelMapper,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers,
        private val remoteConfig: RemoteConfig,
        private val playPreference: PlayPreference,
        private val videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring,
        private val playChannelWebSocket: PlayChannelWebSocket,
        private val interactiveRepo: PlayViewerInteractiveRepository,
        private val partnerRepo: PlayViewerPartnerRepository,
) : ViewModel() {

    val observableChannelInfo: LiveData<PlayChannelInfoUiModel> /**Added**/
        get() = _observableChannelInfo
    val observableVideoMeta: LiveData<PlayVideoMetaInfoUiModel> /**Changed**/
        get() = _observableVideoMeta
    val observableSocketInfo: LiveData<PlaySocketInfo>
        get() = _observableSocketInfo
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableTotalViews: LiveData<PlayTotalViewUiModel> /**Changed**/
        get() = _observableTotalViews
    val observableQuickReply: LiveData<PlayQuickReplyInfoUiModel> /**Changed**/
        get() = _observableQuickReply
    val observableStatusInfo: LiveData<PlayStatusInfoUiModel> /**Changed**/
        get() = _observableStatusInfo
    val observableBottomInsetsState: LiveData<Map<BottomInsetsType, BottomInsetsState>>
        get() = _observableBottomInsetsState
    val observablePinnedMessage: LiveData<PinnedMessageUiModel>
        get() = _observablePinnedMessage
    val observablePinnedProduct: LiveData<PinnedProductUiModel>
        get() = _observablePinnedProduct
    val observableVideoProperty: LiveData<VideoPropertyUiModel>
        get() = _observableVideoProperty
    val observableProductSheetContent: LiveData<PlayResult<PlayProductTagsUiModel.Complete>>
        get() = _observableProductSheetContent
    val observableCartInfo: LiveData<PlayCartInfoUiModel> /**Changed**/
        get() = _observableCartInfo
    val observableShareInfo: LiveData<PlayShareInfoUiModel> /**Added**/
        get() = _observableShareInfo
    val observableLikeStatusInfo: LiveData<PlayLikeStatusInfoUiModel> /**Added**/
        get() = _observableLikeStatusInfo
    val observableEventPiPState: LiveData<Event<PiPState>>
        get() = _observableEventPiPState
    val observableOnboarding: LiveData<Event<Unit>>
        get() = _observableOnboarding

    val uiState: LiveData<PlayViewerNewUiState>
        get() = _uiState
    val uiEvent: Flow<PlayViewerNewUiEvent>
        get() = _uiEvent

    val videoOrientation: VideoOrientation
        get() {
            val videoStream = _observableVideoMeta.value?.videoStream
            return videoStream?.orientation ?: VideoOrientation.Unknown
        }
    val statusType: PlayStatusType
        get() {
            val channelStatus = _observableStatusInfo.value
            return channelStatus?.statusType ?: error("Not Possible")
        }
    val channelType: PlayChannelType
        get() {
            val channelInfo = _observableChannelInfo.value
            return channelInfo?.channelType ?: PlayChannelType.Unknown
        }
    val videoPlayer: PlayVideoPlayerUiModel
        get() {
            val videoPlayer = _observableVideoMeta.value?.videoPlayer
            return videoPlayer ?: PlayVideoPlayerUiModel.Unknown
        }
    val viewerVideoState: PlayViewerVideoState
        get() {
            val videoState = _observableVideoProperty.value?.state
            return videoState ?: PlayViewerVideoState.Unknown
        }
    val likeParamInfo: PlayLikeParamInfoUiModel
        get() {
            val likeParamInfo = _observableLikeInfo.value?.param
            return likeParamInfo ?: error("Not Possible")
        }
    val bottomInsets: Map<BottomInsetsType, BottomInsetsState>
        get() {
            val value = _observableBottomInsetsState.value
            return value ?: getDefaultBottomInsetsMapState()
        }
    val isFreezeOrBanned: Boolean
        get() {
            val event = _observableStatusInfo.value
            return event?.statusType?.isFreeze == true || event?.statusType?.isBanned == true
        }
    val partnerId: Long?
        get() = mChannelData?.partnerInfo?.id

    val totalView: String?
        get() = _observableTotalViews.value?.totalViewFmt

    val videoLatency: Long
        get() = videoLatencyPerformanceMonitoring.totalDuration

    private var mChannelData: PlayChannelData? = null

    val latestCompleteChannelData: PlayChannelData
        get() {
            val channelData = mChannelData ?: error("Channel Data should not be null")

            val videoMetaInfo = _observableVideoMeta.value ?: channelData.videoMetaInfo
            val newVideoPlayer = when (val videoPlayer = videoMetaInfo.videoPlayer) {
                is PlayVideoPlayerUiModel.General -> videoPlayer.updateParams(
                        newParams = videoPlayer.params.copy(
                                lastMillis = playVideoPlayer.getCurrentPosition()
                        )
                )
                else -> videoPlayer
            }
            val newVideoMeta = videoMetaInfo.copy(videoPlayer = newVideoPlayer)

            val pinnedMessage = _observablePinnedMessage.value ?: channelData.pinnedInfo.pinnedMessage
            val pinnedProduct = _observablePinnedProduct.value ?: channelData.pinnedInfo.pinnedProduct

            return channelData.copy(
                    channelInfo = _observableChannelInfo.value ?: channelData.channelInfo,
                    partnerInfo = channelData.partnerInfo,
                    likeInfo = _observableLikeInfo.value ?: channelData.likeInfo,
                    totalViewInfo = _observableTotalViews.value ?: channelData.totalViewInfo,
                    cartInfo = _observableCartInfo.value ?: channelData.cartInfo,
                    pinnedInfo = PlayPinnedInfoUiModel(
                            pinnedMessage = pinnedMessage,
                            pinnedProduct = pinnedProduct,
                    ),
                    quickReplyInfo = _observableQuickReply.value ?: channelData.quickReplyInfo,
                    videoMetaInfo = newVideoMeta,
                    statusInfo = _observableStatusInfo.value ?: channelData.statusInfo,
                    lastUiState = state
            )
        }

    val pipState: PiPState
        get() = _observableEventPiPState.value?.peekContent() ?: PiPState.Stop

    val isPiPAllowed: Boolean
        get() {
            return remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_PIP, true)
                    && !videoPlayer.isYouTube
        }

    val userId: String
        get() = userSession.userId

    private val isProductSheetInitialized: Boolean
        get() = _observableProductSheetContent.value != null

    private var socketJob: Job? = null

    private val _observableChannelInfo = MutableLiveData<PlayChannelInfoUiModel>()
    private val _observableSocketInfo = MutableLiveData<PlaySocketInfo>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableTotalViews = MutableLiveData<PlayTotalViewUiModel>() /**Changed**/
    private val _observableQuickReply = MutableLiveData<PlayQuickReplyInfoUiModel>() /**Changed**/
    private val _observableStatusInfo = MutableLiveData<PlayStatusInfoUiModel>() /**Changed**/
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observablePinnedProduct = MutableLiveData<PinnedProductUiModel>() /**Changed**/
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val _observableVideoMeta = MutableLiveData<PlayVideoMetaInfoUiModel>() /**Changed**/
    private val _observableProductSheetContent = MutableLiveData<PlayResult<PlayProductTagsUiModel.Complete>>() /**Changed**/
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observableLikeInfo = MutableLiveData<PlayLikeInfoUiModel>() /**Added**/
    private val _observableLikeStatusInfo = MediatorLiveData<PlayLikeStatusInfoUiModel>().apply {
        addSource(_observableLikeInfo) { likeInfo ->
            if (likeInfo is PlayLikeInfoUiModel.Complete) value = likeInfo.status
        }
    }
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableCartInfo = MutableLiveData<PlayCartInfoUiModel>() /**Changed**/
    private val _observableShareInfo = MutableLiveData<PlayShareInfoUiModel>() /**Added**/
    private val _observableEventPiPState = MutableLiveData<Event<PiPState>>()
    private val _observableOnboarding = MutableLiveData<Event<Unit>>() /**Added**/
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(observableProductSheetContent) {
            if (it is PlayResult.Success) {
                val pinnedProduct = _observablePinnedProduct.value
                if (pinnedProduct != null) {
                    val newPinnedProduct = pinnedProduct.copy(
                            productTags = pinnedProduct.productTags.setContent(
                                    basicInfo = it.data.basicInfo,
                                    productList = it.data.productList,
                                    voucherList = it.data.voucherList
                            )
                    )
                    _observablePinnedProduct.value = newPinnedProduct
                }
            }
        }
        addSource(observableStatusInfo) {
            if (it.statusType.isFreeze || it.statusType.isBanned) doOnForbidden()
        }
        addSource(_observableBottomInsetsState) { insets ->
            viewModelScope.launch {
                setUiState { copy(bottomInsets = insets) }
                if (insets.isAnyShown) _uiEvent.emit(HideCoachMarkWinnerEvent)
            }
        }
    }

    private val _uiState = MutableLiveData(PlayViewerNewUiState())
    private val _uiEvent = MutableSharedFlow<PlayViewerNewUiEvent>(extraBufferCapacity = 5)

    private val state: PlayViewerNewUiState
        get() = _uiState.value ?: error("State is impossible to be null")

    //region helper
    private val hasWordsOrDotsRegex = Regex("(\\.+|[a-z]+)")
    private val amountStringStepArray = arrayOf("k", "m")
    private fun String.trimMultipleNewlines() = trim().replace(Regex("(\\n+)"), "\n")
    //endregion

    private var channelInfoJob: Job? = null

    private val videoStateListener = object : PlayViewerVideoStateListener {
        override fun onStateChanged(state: PlayViewerVideoState) {
            viewModelScope.launch(dispatchers.immediate) {
                _observableVideoProperty.value = VideoPropertyUiModel(state)
            }
        }
    }

    private val channelStateListener = object : PlayViewerChannelStateListener {
        override fun onChannelFreezeStateChanged(shouldFreeze: Boolean) {
            viewModelScope.launch(dispatchers.immediate) {
                val value = _observableStatusInfo.value
                if (value != null && !statusType.isFreeze) {
                    _observableStatusInfo.value = if (shouldFreeze) value.copy(statusType = PlayStatusType.Freeze) else value
                }
            }
        }
    }

    private val videoManagerListener = object : PlayVideoWrapper.Listener {
        override fun onVideoPlayerChanged(player: ExoPlayer) {
            viewModelScope.launch(dispatchers.immediate) {
                val newVidPlayer = when (val vidPlayer = videoPlayer) {
                    is PlayVideoPlayerUiModel.General.Incomplete -> vidPlayer.setPlayer(player)
                    is PlayVideoPlayerUiModel.General.Complete -> vidPlayer.copy(exoPlayer = player)
                    else -> vidPlayer
                }
                val currentMetaValue = _observableVideoMeta.value
                _observableVideoMeta.value = currentMetaValue?.copy(videoPlayer = newVidPlayer)
            }
        }
    }

    private val videoPerformanceListener = object : PlayViewerVideoPerformanceListener {
        override fun onPlaying() {
            if (videoLatencyPerformanceMonitoring.hasStarted) videoLatencyPerformanceMonitoring.stop()
        }

        override fun onError() {
            videoLatencyPerformanceMonitoring.reset()
        }
    }

    private val playVideoPlayer = playVideoBuilder.build()

    /**
     * Ui State
     */
    private val mutex = Mutex()

    /**
     * Interactive
     */
    private val interactiveFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 5)

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val stateHandlerObserver = object : Observer<Unit> {
        override fun onChanged(t: Unit?) {}
    }

    private val videoStateProcessor = videoStateProcessorFactory.create(playVideoPlayer, viewModelScope) { channelType }
    private val channelStateProcessor = channelStateProcessorFactory.create(playVideoPlayer, viewModelScope, { channelType }, { videoPlayer })
    private val videoBufferGovernor = videoBufferGovernorFactory.create(playVideoPlayer, viewModelScope)

    init {
        videoStateProcessor.addStateListener(videoStateListener)
        videoStateProcessor.addStateListener(videoPerformanceListener)
        channelStateProcessor.addStateListener(channelStateListener)
        videoBufferGovernor.startBufferGovernance()

        stateHandler.observeForever(stateHandlerObserver)

        _observableChatList.value = mutableListOf()

        viewModelScope.launch {
            interactiveFlow.collect(::onReceivedInteractiveAction)
        }
    }

    //region lifecycle
    override fun onCleared() {
        super.onCleared()
        stateHandler.removeObserver(stateHandlerObserver)
        stopWebSocket()
        if (!pipState.isInPiP) stopPlayer()
        playVideoPlayer.removeListener(videoManagerListener)
        videoStateProcessor.removeStateListener(videoStateListener)
        videoStateProcessor.removeStateListener(videoPerformanceListener)
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

    fun onShowVariantSheet(estimatedProductSheetHeight: Int, product: PlayProductUiModel.Product, action: ProductAction) {
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

    private fun showLeaderboardSheet(estimatedHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.LeaderboardSheet] =
                BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.LeaderboardSheet]?.isShown == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    private fun hideLeaderboardSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.LeaderboardSheet] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.LeaderboardSheet]?.isHidden == true
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
        val defaultKeyboardState = currentBottomInsetsMap?.get(BottomInsetsType.Keyboard)?.isHidden ?: true
        val defaultProductSheetState = currentBottomInsetsMap?.get(BottomInsetsType.ProductSheet)?.isHidden ?: true
        val defaultVariantSheetState = currentBottomInsetsMap?.get(BottomInsetsType.VariantSheet)?.isHidden ?: true
        val defaultLeaderboardSheetState = currentBottomInsetsMap?.get(BottomInsetsType.LeaderboardSheet)?.isHidden ?: true
        return mapOf(
                BottomInsetsType.Keyboard to BottomInsetsState.Hidden(defaultKeyboardState),
                BottomInsetsType.ProductSheet to BottomInsetsState.Hidden(defaultProductSheetState),
                BottomInsetsType.VariantSheet to BottomInsetsState.Hidden(defaultVariantSheetState),
                BottomInsetsType.LeaderboardSheet to BottomInsetsState.Hidden(defaultLeaderboardSheetState),
        )
    }
    //endregion

    //region video player
    fun startCurrentVideo() {
        if (!videoPlayer.isYouTube) playVideoPlayer.resume()
    }

    fun getVideoDuration(): Long {
        return playVideoPlayer.getVideoDuration()
    }

    fun requestWatchInPiP() {
        _observableEventPiPState.value = Event(PiPState.Requesting(PiPMode.WatchInPiP))
    }

    fun requestPiPBrowsingPage(applinkModel: OpenApplinkUiModel) {
        _observableEventPiPState.value = Event(
                PiPState.Requesting(
                        PiPMode.BrowsingOtherPage(applinkModel)
                )
        )
    }

    fun stopPiP() {
        _observableEventPiPState.value = Event(PiPState.Stop)
    }

    fun goPiP() {
        when (val state = pipState) {
            is PiPState.Requesting -> {
                _observableEventPiPState.value = Event(PiPState.InPiP(state.pipMode))
            }
        }
    }

    fun submitAction(action: PlayViewerNewAction) {
        when (action) {
            InteractivePreStartFinishedAction -> handleInteractivePreStartFinished()
            InteractiveOngoingFinishedAction -> handleInteractiveOngoingFinished()
            is InteractiveWinnerBadgeClickedAction -> handleWinnerBadgeClicked(action.height)
            InteractiveTapTapAction -> handleTapTapAction()
            ClickCloseLeaderboardSheetAction -> handleCloseLeaderboardSheet()
            ClickFollowAction -> handleClickFollow()
            ClickPartnerNameAction -> handleClickPartnerName()
            is OpenPageResultAction -> handleOpenPageResult(action.isSuccess, action.requestCode)
        }
    }

    private fun initiateVideo(videoPlayer: PlayVideoPlayerUiModel.General) {
        startVideoWithUrlString(
                urlString = videoPlayer.params.videoUrl,
                bufferControl = videoPlayer.params.buffer,
                lastPosition = if (channelType.isVod) videoPlayer.params.lastMillis else null
        )
        playVideoPlayer.setRepeatMode(shouldRepeat = false)
    }

    private fun startVideoWithUrlString(urlString: String, bufferControl: PlayBufferControl, lastPosition: Long?) {
        try {
            videoLatencyPerformanceMonitoring.start()
            playVideoPlayer.playUri(uri = Uri.parse(urlString), bufferControl = bufferControl, startPosition = lastPosition)
        } catch (e: Exception) {}
    }

    private fun playGeneralVideo(videoPlayer: PlayVideoPlayerUiModel.General) {
        if (statusType.isActive) initiateVideo(videoPlayer)
    }

    private fun stopPlayer() {
        if (playVideoPlayer.isVideoLive() || channelType.isLive || isFreezeOrBanned) playVideoPlayer.release()
        else playVideoPlayer.stop()
    }
    //endregion

    fun createPage(channelData: PlayChannelData) {
        viewModelScope.launch {
            setUiState { channelData.lastUiState }
        }

        mChannelData = channelData
        handleStatusInfo(channelData.statusInfo)
        handleChannelInfo(channelData.channelInfo)
        handleOnboarding(channelData.videoMetaInfo)
        handleVideoMetaInfo(channelData.videoMetaInfo)
        handleShareInfo(channelData.shareInfo)
        handleTotalViewInfo(channelData.totalViewInfo)
        handleLikeInfo(channelData.likeInfo)
        handleCartInfo(channelData.cartInfo)
        handlePinnedInfo(channelData.pinnedInfo)
        handleQuickReplyInfo(channelData.quickReplyInfo)
    }

    fun focusPage(channelData: PlayChannelData) {
        focusVideoPlayer(channelData)
        updateChannelInfo(channelData)
        startWebSocket(channelData.id)
        trackVisitChannel(channelData.id)

        //TODO("This is mock")
        if (channelData.interactiveInfo.hasInteractive) checkInteractive(channelData.id)
    }

    fun defocusPage(shouldPauseVideo: Boolean) {
        stopJob()
        defocusVideoPlayer(shouldPauseVideo)
        stopWebSocket()

        stopInteractive()
    }

    private fun focusVideoPlayer(channelData: PlayChannelData) {
        if (channelData.statusInfo.statusType.isFreeze) return

        playVideoPlayer.addListener(videoManagerListener)
        playVideoPlayer.resume()
    }

    private fun defocusVideoPlayer(shouldPauseVideo: Boolean) {
        if (shouldPauseVideo) {
            if (playVideoPlayer.isVideoLive() || viewerVideoState.hasNoData) playVideoPlayer.stop()
            else playVideoPlayer.pause(preventLoadingBuffer = true)
        }
        playVideoPlayer.removeListener(videoManagerListener)
    }

    private fun updateChannelInfo(channelData: PlayChannelData) {
        updateStatusInfo(channelData.id)
        updatePartnerInfo(channelData.partnerInfo)
        updateCartInfo(channelData.cartInfo)
        if (!channelData.statusInfo.statusType.isFreeze) {
            updateVideoMetaInfo(channelData.videoMetaInfo)
            updateLikeAndTotalViewInfo(channelData.likeInfo.param, channelData.id)
            updateProductTagsInfo(channelData.pinnedInfo.pinnedProduct.productTags, channelData.pinnedInfo, channelData.id)
        }
    }

    fun sendChat(message: String) {
        if (!userSession.isLoggedIn) return

        val channelId = mChannelData?.id ?: return

        val cleanMessage = message.trimMultipleNewlines()
        playChannelWebSocket.send(
                playSocketToModelMapper.mapSendChat(cleanMessage, channelId)
        )
        setNewChat(
                playUiModelMapper.mapChat(
                        PlayChat(
                                message = cleanMessage,
                                user = PlayChat.UserData(
                                        id = userSession.userId,
                                        name = userSession.name,
                                        image = userSession.profilePicture)
                        )
                )
        )
    }

    fun changeLikeCount(shouldLike: Boolean) {
        val likeInfo = _observableLikeInfo.value
        if (likeInfo !is PlayLikeInfoUiModel.Complete) return

        val currentTotalLike = likeInfo.status.totalLike
        val currentTotalLikeFmt = likeInfo.status.totalLikeFormatted
        if (!hasWordsOrDotsRegex.containsMatchIn(currentTotalLikeFmt)) {
            val finalTotalLike = (currentTotalLike + (if (shouldLike) 1 else -1)).coerceAtLeast(0)
            _observableLikeInfo.value = likeInfo.copy(
                    status = PlayLikeStatusInfoUiModel(
                            totalLike = finalTotalLike,
                            totalLikeFormatted = finalTotalLike.toAmountString(amountStringStepArray, separator = "."),
                            isLiked = shouldLike,
                            source = LikeSource.UserAction
                    )
            )
        } else {
            _observableLikeInfo.value = likeInfo.copy(
                    status = PlayLikeStatusInfoUiModel(
                            totalLike = likeInfo.status.totalLike,
                            totalLikeFormatted = likeInfo.status.totalLikeFormatted,
                            isLiked = shouldLike,
                            source = LikeSource.UserAction
                    )
            )
        }
    }

    /**
     * @return true means that back has been consumed/handled
     * false means that back is allowed
     */
    fun goBack(): Boolean {
        val shownBottomSheets = observableBottomInsetsState.value
                ?.filter { it.value.isShown }
                ?.mapValues { it.value as BottomInsetsState.Shown }
                .orEmpty()
        val entry = shownBottomSheets.minBy { it.value.deepLevel }
        when (entry?.key) {
            BottomInsetsType.Keyboard -> onKeyboardHidden()
            BottomInsetsType.ProductSheet -> onHideProductSheet()
            BottomInsetsType.VariantSheet -> onHideVariantSheet()
            BottomInsetsType.LeaderboardSheet -> hideLeaderboardSheet()
        }
        return shownBottomSheets.isNotEmpty()
    }

    fun updateBadgeCart() {
        val cartInfo = _observableCartInfo.value
        if (cartInfo != null) updateCartInfo(cartInfo)
    }

    fun getVideoPlayer() = playVideoPlayer

    private fun startWebSocket(channelId: String) {
        viewModelScope.launch {
            val socketCredential = try {
                withContext(dispatchers.io) {
                    return@withContext getSocketCredentialUseCase.executeOnBackground()
                }
            } catch (e: Throwable) {
                SocketCredential()
            }

            socketJob = launch {
                playChannelWebSocket.listenAsFlow()
                        .collect {
                            handleWebSocketResponse(it, channelId, socketCredential)
                        }
            }

            connectWebSocket(
                    channelId = channelId,
                    socketCredential = socketCredential
            )
        }
    }

    private fun connectWebSocket(channelId: String, socketCredential: SocketCredential) {
        playChannelWebSocket.connectSocket(channelId, socketCredential.gcToken)
    }

    private fun stopWebSocket() {
        playChannelWebSocket.close()
    }

    private fun stopInteractive() {
        viewModelScope.launch {
            setUiState {
                copy(interactive = PlayInteractiveUiState.NoInteractive)
            }
        }
    }

    private fun stopJob() {
        channelInfoJob?.cancel()
        socketJob?.cancel()
    }

    /**
     * Handle existing channel data
     */
    private fun handleStatusInfo(statusInfo: PlayStatusInfoUiModel) {
        _observableStatusInfo.value = statusInfo
    }

    private fun handleChannelInfo(channelInfo: PlayChannelInfoUiModel) {
        _observableChannelInfo.value = channelInfo
    }

    private fun handleVideoMetaInfo(videoMetaInfo: PlayVideoMetaInfoUiModel) {
        val newVidPlayer = when (val vidPlayer = videoMetaInfo.videoPlayer) {
            is PlayVideoPlayerUiModel.General.Incomplete -> vidPlayer.setPlayer(playVideoPlayer.videoPlayer)
            else -> vidPlayer
        }
        _observableVideoMeta.value = videoMetaInfo.copy(videoPlayer = newVidPlayer)
    }

    private fun handleOnboarding(videoMetaInfo: PlayVideoMetaInfoUiModel) {
        val userId = userSession.userId
        if (!playPreference.isOnboardingShown(userId) && !videoMetaInfo.videoPlayer.isYouTube) {
            viewModelScope.launch(dispatchers.main) {
                delay(ONBOARDING_DELAY)
                _observableOnboarding.value = Event(Unit)
            }
        }
    }

    private fun handleShareInfo(shareInfo: PlayShareInfoUiModel) {
        _observableShareInfo.value = shareInfo
    }

    private fun handleTotalViewInfo(totalViewInfo: PlayTotalViewUiModel) {
        _observableTotalViews.value = totalViewInfo
    }

    private fun handleLikeInfo(likeInfo: PlayLikeInfoUiModel) {
        _observableLikeInfo.value = when (likeInfo) {
            is PlayLikeInfoUiModel.Incomplete -> likeInfo
            is PlayLikeInfoUiModel.Complete -> likeInfo.copy(
                    status = likeInfo.status.copy(
                            source = LikeSource.Storage
                    )
            )
        }
    }

    private fun handleCartInfo(cartInfo: PlayCartInfoUiModel) {
        _observableCartInfo.value = cartInfo
    }

    private fun handlePinnedInfo(pinnedInfo: PlayPinnedInfoUiModel) {
        _observablePinnedMessage.value = pinnedInfo.pinnedMessage
        _observablePinnedProduct.value = pinnedInfo.pinnedProduct

        if (pinnedInfo.pinnedProduct.shouldShow) {
            _observableProductSheetContent.value = when (pinnedInfo.pinnedProduct.productTags) {
                is PlayProductTagsUiModel.Incomplete -> PlayResult.Loading(showPlaceholder = true)
                is PlayProductTagsUiModel.Complete -> PlayResult.Success(pinnedInfo.pinnedProduct.productTags)
            }
        }
    }

    private fun handleQuickReplyInfo(quickReplyInfo: PlayQuickReplyInfoUiModel) {
        _observableQuickReply.value = quickReplyInfo
    }

    /**
     * Update channel data
     */
    private fun updatePartnerInfo(partnerBasicInfo: PlayPartnerBasicInfoUiModel) {
        if (partnerBasicInfo.type == PartnerType.Shop) {
            viewModelScope.launchCatchError(block = {
                val followInfo = partnerRepo.getPartnerFollowInfo(partnerId = partnerBasicInfo.id)
                val status = if (followInfo.isFollowable) PlayFollowStatusUiState.Followable(followInfo.isFollowed) else PlayFollowStatusUiState.NotFollowable
                setUiState { copy(followStatus = status) }
            }, onError = {

            })
        }
    }

    private fun updateVideoMetaInfo(videoMetaInfo: PlayVideoMetaInfoUiModel) {
        if (videoMetaInfo.videoPlayer is PlayVideoPlayerUiModel.General) playGeneralVideo(videoMetaInfo.videoPlayer)
        else playVideoPlayer.release()
    }

    private fun updateLikeAndTotalViewInfo(likeParamInfo: PlayLikeParamInfoUiModel, channelId: String) {
        viewModelScope.launchCatchError(block = {
            supervisorScope {
                val deferredReportSummaries = async { getReportSummaries(channelId) }
                val deferredIsLiked = async { getIsLiked(likeParamInfo) }

                val (totalView, totalLike, totalLikeFormatted) = try {
                    val report = deferredReportSummaries.await().data.first().channel.metrics
                    Triple(report.totalViewFmt, report.totalLike.toLongOrZero(), report.totalLikeFmt)
                } catch (e: Throwable) {
                    Triple("0", 0 , "0")
                }

                val isLiked = try { deferredIsLiked.await() } catch (e: Throwable) { false }

                val newLikeStatus = PlayLikeStatusInfoUiModel(
                        totalLike = totalLike.toLong(),
                        totalLikeFormatted = totalLikeFormatted,
                        isLiked = isLiked,
                        source = LikeSource.Network
                )
                _observableLikeInfo.value = likeParamInfo + newLikeStatus

                _observableTotalViews.value = PlayTotalViewUiModel.Complete(totalView)
            }
        }, onError = {
            _observableLikeInfo.value = likeParamInfo + PlayLikeStatusInfoUiModel(
                    totalLike = 0,
                    totalLikeFormatted = "0",
                    isLiked = false,
                    source = LikeSource.Network
            )
        })
    }

    private fun updateCartInfo(cartInfo: PlayCartInfoUiModel) {
        if (cartInfo.shouldShow) {
            viewModelScope.launchCatchError(block = {
                val cartCount = getCartCount()
                _observableCartInfo.value = PlayCartInfoUiModel.Complete(
                        shouldShow = cartInfo.shouldShow,
                        count = cartCount
                )
            }, onError = {

            })
        }
    }

    private fun updateProductTagsInfo(productTags: PlayProductTagsUiModel, pinnedInfo: PlayPinnedInfoUiModel, channelId: String) {
        if (pinnedInfo.pinnedProduct.shouldShow) {
            viewModelScope.launchCatchError(block = {
                getProductTagItems(productTags.basicInfo, channelId)
            }, onError = {
                _observableProductSheetContent.value = PlayResult.Failure(it) {
                    updateProductTagsInfo(productTags, pinnedInfo, channelId)
                }
            })
        }
    }

    private fun updateStatusInfo(channelId: String) {
        if (statusType.isFreeze) return

        viewModelScope.launchCatchError(block = {
            val channelStatus = getChannelStatus(channelId)
            _observableStatusInfo.value = _observableStatusInfo.value?.copy(
                    statusType = playUiModelMapper.mapStatus(channelStatus),
                    shouldAutoSwipeOnFreeze = false
            )
        }, onError = {

        })
    }

    /**
     * Private Method
     */
    private fun setNewChat(chat: PlayChatUiModel) {
        val currentChatList = _observableChatList.value ?: mutableListOf()
        currentChatList.add(chat)
        _observableChatList.value = currentChatList
    }

    private suspend fun getReportSummaries(channelId: String): ReportSummaries = withContext(dispatchers.io) {
        getReportSummariesUseCase.params = GetReportSummariesUseCase.createParam(channelId)
        getReportSummariesUseCase.executeOnBackground()
    }

    private suspend fun getIsLiked(likeParamInfo: PlayLikeParamInfoUiModel) = withContext(dispatchers.io) {
        getIsLikeUseCase.params = GetIsLikeUseCase.createParam(
                contentId = likeParamInfo.contentId.toIntOrZero(),
                contentType = likeParamInfo.contentType
        )
        getIsLikeUseCase.executeOnBackground()
    }

    private suspend fun getCartCount(): Int = withContext(dispatchers.io) {
        try {
            getCartCountUseCase.executeOnBackground()
        } catch (e: Exception) {
            0
        }
    }

    private suspend fun getProductTagItems(productTagsBasicInfo: PlayProductTagsBasicInfoUiModel, channelId: String) {
        if (!isProductSheetInitialized) _observableProductSheetContent.value = PlayResult.Loading(
                showPlaceholder = true
        )

        val productTagsResponse = withContext(dispatchers.io) {
            getProductTagItemsUseCase.setRequestParams(GetProductTagItemsUseCase.createParam(channelId))
            getProductTagItemsUseCase.executeOnBackground()
        }
        val productTags = playUiModelMapper.mapProductTags(productTagsResponse.playGetTagsItem.listOfProducts)
        val merchantVouchers = playUiModelMapper.mapMerchantVouchers(productTagsResponse.playGetTagsItem.listOfVouchers)

        val newProductSheet = PlayProductTagsUiModel.Complete(
                basicInfo = productTagsBasicInfo.copy(
                        maxFeaturedProducts = productTagsResponse.playGetTagsItem.config.peekProductCount
                ),
                productList = productTags,
                voucherList = merchantVouchers
        )
        _observableProductSheetContent.value = PlayResult.Success(newProductSheet)

        trackProductTag(
                channelId = channelId,
                productList = productTags
        )
    }

    private fun trackProductTag(channelId: String, productList: List<PlayProductUiModel>) {
        viewModelScope.launchCatchError(block = {
            withContext(dispatchers.io) {
                val productIds = productList.mapNotNull { product -> if (product is PlayProductUiModel.Product) product.id else null }
                trackProductTagBroadcasterUseCase.params = TrackProductTagBroadcasterUseCase.createParams(channelId, productIds)
                trackProductTagBroadcasterUseCase.executeOnBackground()
            }
        }) {
        }
    }

    private fun trackVisitChannel(channelId: String) {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            trackVisitChannelBroadcasterUseCase.setRequestParams(TrackVisitChannelBroadcasterUseCase.createParams(channelId))
            trackVisitChannelBroadcasterUseCase.executeOnBackground()
        }) {
        }
    }

    private fun checkInteractive(channelId: String) {
        if (!channelType.isLive) return
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val interactive = interactiveRepo.getCurrentInteractive(channelId)
            handleInteractiveFromNetwork(interactive)
        }) {
            setUiState {
                copy(interactive = PlayInteractiveUiState.NoInteractive)
            }
        }
    }

    private fun mapInteractiveToState(interactive: PlayCurrentInteractiveModel): PlayInteractiveUiState {
        return when (val status = interactive.timeStatus) {
            is PlayInteractiveTimeStatus.Scheduled -> PlayInteractiveUiState.PreStart(status.timeToStartInMs, interactive.title)
            is PlayInteractiveTimeStatus.Live -> PlayInteractiveUiState.Ongoing(status.remainingTimeInMs, interactive.title)
            else -> PlayInteractiveUiState.NoInteractive
        }
    }

    private suspend fun handleInteractiveFromNetwork(interactive: PlayCurrentInteractiveModel) {
        val interactiveUiState = mapInteractiveToState(interactive)
        interactiveRepo.setDetail(interactive.id.toString(), interactive)
        if (interactive.timeStatus is PlayInteractiveTimeStatus.Scheduled || interactive.timeStatus is PlayInteractiveTimeStatus.Live) {
            interactiveRepo.setActive(interactive.id.toString())
        } else {
            interactiveRepo.setFinished(interactive.id.toString())
        }

        if (interactiveRepo.getActiveInteractiveId() != null) {
            setUiState {
                copy(interactive = interactiveUiState)
            }
        }

        if (interactive.timeStatus is PlayInteractiveTimeStatus.Finished) {
            val channelId = mChannelData?.id ?: return

            try {
                val interactiveLeaderboard = interactiveRepo.getInteractiveLeaderboard(channelId)
                setUiState {
                    copy(
                            showWinningBadge = true,
                            interactive = PlayInteractiveUiState.NoInteractive,
                            winnerLeaderboard = interactiveLeaderboard.leaderboardWinner
                    )
                }
            } catch (e: Throwable) {}
        }
    }

    private suspend fun getChannelStatus(channelId: String) = withContext(dispatchers.io) {
        getChannelStatusUseCase.apply {
            setRequestParams(GetChannelStatusUseCase.createParams(arrayOf(channelId)))
        }.executeOnBackground()
    }

    private fun doOnForbidden() {
        stopWebSocket()
        stopPlayer()
        onKeyboardHidden()
    }

    private suspend fun setUiState(fn: PlayViewerNewUiState.() -> PlayViewerNewUiState) = mutex.withLock {
        withContext(dispatchers.immediate) {
            val state = _uiState.value ?: error("State cannot be null")
            _uiState.value = state.fn()
        }
    }
    //endregion

    private suspend fun handleWebSocketResponse(response: WebSocketAction, channelId: String, socketCredential: SocketCredential) {
        when (response) {
            is WebSocketAction.NewMessage -> handleWebSocketMessage(response.message, channelId)
            is WebSocketAction.Closed -> if (response.reason == WebSocketClosedReason.Error) connectWebSocket(channelId, socketCredential)
        }
    }

    private suspend fun handleWebSocketMessage(message: WebSocketResponse, channelId: String) = withContext(dispatchers.main) {
        val result = withContext(dispatchers.computation) {
            val socketMapper = PlaySocketMapper(message)
            socketMapper.mapping()
        }
        when (result) {
            is TotalLike -> {
                val currentLikeInfo = _observableLikeInfo.value ?: return@withContext
                val mappedResult = playSocketToModelMapper.mapTotalLike(result)

                _observableLikeInfo.value = if (currentLikeInfo is PlayLikeInfoUiModel.Complete) currentLikeInfo.copy(
                        status = currentLikeInfo.status.copy(
                                totalLike = mappedResult.totalLike,
                                totalLikeFormatted = mappedResult.totalLikeFormatted,
                                source = mappedResult.source
                        )
                ) else currentLikeInfo.param + mappedResult
            }
            is TotalView -> {
                _observableTotalViews.value = PlayTotalViewUiModel.Complete(playSocketToModelMapper.mapTotalView(result))
            }
            is PlayChat -> {
                setNewChat(playUiModelMapper.mapChat(result))
            }
            is PinnedMessage -> {
                val currentPinnedMessage = _observablePinnedMessage.value ?: return@withContext
                val mappedResult = playSocketToModelMapper.mapPinnedMessage(result)
                _observablePinnedMessage.value = currentPinnedMessage.copy(
                        id = mappedResult.id,
                        applink = mappedResult.applink,
                        title = mappedResult.title,
                )
            }
            is QuickReply -> {
                _observableQuickReply.value = playSocketToModelMapper.mapQuickReplies(result)
            }
            is BannedFreeze -> {
                if (result.channelId.isNotEmpty() && result.channelId.equals(channelId, true) && !statusType.isFreeze) {
                    _observableStatusInfo.value = _observableStatusInfo.value?.copy(
                            shouldAutoSwipeOnFreeze = true,
                            statusType = playSocketToModelMapper.mapStatus(
                                    isBanned = result.isBanned && result.userId.isNotEmpty()
                                            && result.userId.equals(userSession.userId, true)))

                    channelStateProcessor.setIsFreeze(result.isFreeze)
                }
            }
            is ProductTag -> {
                val currentPinnedProduct = _observablePinnedProduct.value ?: return@withContext
                val (mappedProductTags, shouldShow) = playSocketToModelMapper.mapProductTag(result)
                _observablePinnedProduct.value = if (currentPinnedProduct.productTags is PlayProductTagsUiModel.Complete) {
                    currentPinnedProduct.copy(
                            shouldShow = shouldShow,
                            productTags = currentPinnedProduct.productTags.copy(
                                    productList = mappedProductTags
                            )
                    )
                } else {
                    currentPinnedProduct.copy(
                            shouldShow = shouldShow,
                            productTags = PlayProductTagsUiModel.Complete(
                                    currentPinnedProduct.productTags.basicInfo,
                                    mappedProductTags,
                                    emptyList()
                            )
                    )
                }
                trackProductTag(
                        channelId = channelId,
                        productList = mappedProductTags
                )
            }
            is MerchantVoucher -> {
                val currentPinnedProduct = _observablePinnedProduct.value ?: return@withContext
                val mappedVouchers = playSocketToModelMapper.mapMerchantVoucher(result)
                _observablePinnedProduct.value = if (currentPinnedProduct.productTags is PlayProductTagsUiModel.Complete) {
                    currentPinnedProduct.copy(
                            productTags = currentPinnedProduct.productTags.copy(
                                    voucherList = mappedVouchers
                            )
                    )
                } else {
                    currentPinnedProduct.copy(
                            productTags = PlayProductTagsUiModel.Complete(
                                    currentPinnedProduct.productTags.basicInfo,
                                    emptyList(),
                                    mappedVouchers
                            )
                    )
                }
            }
            is ChannelInteractiveStatus -> {
                if (result.isExist) checkInteractive(channelId)
            }
            is ChannelInteractive -> {
                val interactive = playSocketToModelMapper.mapInteractive(result)
                handleInteractiveFromNetwork(interactive)
            }
        }
    }

    /**
     * Called when user tap
     */
    private suspend fun onReceivedInteractiveAction(action: Unit) = withContext(dispatchers.io) {
        try {
            val activeInteractiveId = interactiveRepo.getActiveInteractiveId() ?: return@withContext
            if (interactiveRepo.hasJoined(activeInteractiveId)) return@withContext

            val channelId = mChannelData?.id ?: return@withContext
            val isSuccess = interactiveRepo.postInteractiveTap(channelId, activeInteractiveId)
            if (isSuccess) interactiveRepo.setJoined(activeInteractiveId)
        } catch (ignored: MessageErrorException) {}
    }

    /**
     * Handle UI Action
     */

    /**
     * When pre-start finished, interactive should be played (e.g. TapTap)
     */
    private fun handleInteractivePreStartFinished() {
        viewModelScope.launch {
            val activeInteractiveId = interactiveRepo.getActiveInteractiveId() ?: return@launch
            val interactiveDetail = interactiveRepo.getDetail(activeInteractiveId) ?: return@launch
            if (!interactiveDetail.timeStatus.isScheduled()) return@launch

            setUiState {
                copy(interactive = PlayInteractiveUiState.Ongoing(
                        timeRemainingInMs = interactiveDetail.timeStatus.interactiveDurationInMs,
                        title = "Tap terus!",
                ))
            }
        }
    }

    private fun handleInteractiveOngoingFinished() {
        viewModelScope.launchCatchError(block = {
            val channelId = mChannelData?.id ?: return@launchCatchError

            val activeInteractiveId = interactiveRepo.getActiveInteractiveId() ?: return@launchCatchError
            interactiveRepo.setFinished(activeInteractiveId)

            setUiState {
                copy(interactive = PlayInteractiveUiState.Finished(
                        info = R.string.play_interactive_finish_initial_text,
                ))
            }

            delay(INTERACTIVE_FINISH_MESSAGE_DELAY)

            setUiState {
                copy(interactive = PlayInteractiveUiState.Finished(
                        info = R.string.play_interactive_finish_loading_winner_text,
                ))
            }

            val interactiveLeaderboard = interactiveRepo.getInteractiveLeaderboard(channelId)
            val currentLeaderboard = interactiveLeaderboard.leaderboardWinner.first()
            val userInLeaderboard = currentLeaderboard.winners.find { it.id == userSession.userId }

            if (userInLeaderboard != null) {
                _uiEvent.emit(
                        ShowWinningDialogEvent(
                                userInLeaderboard.imageUrl,
                                interactiveLeaderboard.config.winnerMessage,
                                interactiveLeaderboard.config.winnerDetail
                        )
                )
            }

            setUiState {
                copy(
                        showWinningBadge = true,
                        interactive = PlayInteractiveUiState.NoInteractive,
                        winnerLeaderboard = interactiveLeaderboard.leaderboardWinner
                )
            }

            _uiEvent.emit(
                    ShowCoachMarkWinnerEvent(
                            interactiveLeaderboard.config.loserMessage,
                            interactiveLeaderboard.config.loserDetail
                    )
            )
        }) {}
    }

    private fun handleWinnerBadgeClicked(height: Int) {
        showLeaderboardSheet(height)
    }

    private fun handleTapTapAction() {
        viewModelScope.launch {
            interactiveFlow.emit(Unit)
        }
    }

    private fun handleCloseLeaderboardSheet() {
        hideLeaderboardSheet()
    }

    private fun handleClickFollow() = needLogin(REQUEST_CODE_LOGIN_FOLLOW) {
        viewModelScope.launchCatchError(block = {
            val followStatus = state.followStatus as? PlayFollowStatusUiState.Followable ?: return@launchCatchError
            val shouldFollow = !followStatus.isFollowing
            setUiState {
                copy(followStatus = PlayFollowStatusUiState.Followable(shouldFollow))
            }

            val shopId = mChannelData?.partnerInfo?.id ?: return@launchCatchError
            partnerRepo.postFollowStatus(
                    shopId = shopId.toString(),
                    followAction = if (shouldFollow) PartnerFollowAction.Follow else PartnerFollowAction.UnFollow
            )
        }) {}
    }

    private fun handleClickPartnerName() {
        viewModelScope.launch {
            val partnerInfo = mChannelData?.partnerInfo ?: return@launch
            when (partnerInfo.type) {
                PartnerType.Shop -> _uiEvent.emit(OpenPageEvent(ApplinkConst.SHOP, listOf(partnerInfo.id.toString())))
                PartnerType.Buyer -> _uiEvent.emit(OpenPageEvent(ApplinkConst.PROFILE, listOf(partnerInfo.id.toString())))
                else -> {}
            }
        }
    }

    private fun handleOpenPageResult(isSuccess: Boolean, requestCode: Int) {
        if (!isSuccess) return
        when (requestCode) {
            REQUEST_CODE_LOGIN_FOLLOW -> handleClickFollow()
            else -> {}
        }
    }

    /**
     * Utility Function
     */
    private fun needLogin(requestCode: Int? = null, fn: () -> Unit) {
        if (userSession.isLoggedIn) fn()
        else {
            viewModelScope.launch {
                _uiEvent.emit(
                        OpenPageEvent(
                                applink = ApplinkConst.LOGIN,
                                requestCode = requestCode
                        )
                )
            }
        }
    }

    companion object {
        private const val FIREBASE_REMOTE_CONFIG_KEY_PIP = "android_mainapp_enable_pip"
        private const val ONBOARDING_DELAY = 5000L
        private const val INTERACTIVE_FINISH_MESSAGE_DELAY = 1000L

        /**
         * Request Code When need login
         */
        private const val REQUEST_CODE_LOGIN_FOLLOW = 571
    }
}