package com.tokopedia.play.view.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.data.websocket.revamp.WebSocketAction
import com.tokopedia.play.data.websocket.revamp.WebSocketClosedReason
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.domain.repository.PlayViewerLikeRepository
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateListener
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.setValue
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
import com.tokopedia.play.view.uimodel.state.PlayInteractiveUiState
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.uimodel.state.ViewVisibility
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.dto.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.dto.isScheduled
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean
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
        private val likeRepo: PlayViewerLikeRepository,
        private val playAnalytic: PlayNewAnalytic,
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

    private val _uiEvent = MutableSharedFlow<PlayViewerNewUiEvent>(extraBufferCapacity = 5)

    private val _partnerInfo = MutableStateFlow(PlayPartnerInfo())
    private val _bottomInsets = MutableStateFlow(emptyMap<BottomInsetsType, BottomInsetsState>())
    private val _status = MutableStateFlow(PlayStatusType.Active)
    private val _interactive = MutableStateFlow<PlayInteractiveUiState>(PlayInteractiveUiState.NoInteractive)
    private val _leaderboardInfo = MutableStateFlow<PlayLeaderboardInfoUiModel>(PlayLeaderboardInfoUiModel())

    /**
     * Until repeatOnLifecycle is available (by updating library version),
     * this can be used as an alternative to "complete" un-completable flow when page is not focused
     */
    private val isActive: AtomicBoolean = AtomicBoolean(false)

    val uiState: Flow<PlayViewerNewUiState> = combine(
            _partnerInfo,
            _bottomInsets,
            _interactive,
            _leaderboardInfo,
            _status
    ) { partnerInfo, bottomInsets, interactive, leaderboardInfo, status ->
        PlayViewerNewUiState(
                partnerName = partnerInfo.name,
                followStatus = partnerInfo.status,
                bottomInsets = bottomInsets,
                interactive = interactive,
                showInteractive = when {
                    /**
                     * Invisible because when unify timer is set during gone, it's not gonna get rounded when it's shown :x
                     */
                    bottomInsets.isAnyShown -> ViewVisibility.Invisible
                    status.isFreeze || status.isBanned -> ViewVisibility.Gone
                    interactive is PlayInteractiveUiState.NoInteractive -> ViewVisibility.Gone
                    else -> ViewVisibility.Visible
                },
                leaderboards = leaderboardInfo.leaderboardWinners,
                showWinnerBadge = !bottomInsets.isAnyShown && status.isActive && leaderboardInfo.leaderboardWinners.isNotEmpty() && channelType.isLive,
                status = status
        )
    }
    val uiEvent: Flow<PlayViewerNewUiEvent>
        get() = _uiEvent.takeWhile { isActive.get() }

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
                    leaderboardInfo = _leaderboardInfo.value
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
            _status.value = it.statusType
        }
        addSource(_observableBottomInsetsState) { insets ->
            _bottomInsets.value = insets

            viewModelScope.launch {
                if (insets.isAnyShown) _uiEvent.emit(HideCoachMarkWinnerEvent)
            }
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
            ClickFollowAction -> handleClickFollow(isFromLogin = false)
            ClickFollowInteractiveAction -> handleClickFollowInteractive()
            ClickPartnerNameAction -> handleClickPartnerName()
            ClickRetryInteractiveAction -> handleClickRetryInteractive()
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
        mChannelData = channelData
        handleStatusInfo(channelData.statusInfo)
        handleChannelInfo(channelData.channelInfo)
        handleOnboarding(channelData.videoMetaInfo)
        handleVideoMetaInfo(channelData.videoMetaInfo)
        handlePartnerInfo(channelData.partnerInfo)
        handleShareInfo(channelData.shareInfo)
        handleTotalViewInfo(channelData.totalViewInfo)
        handleLikeInfo(channelData.likeInfo)
        handleCartInfo(channelData.cartInfo)
        handlePinnedInfo(channelData.pinnedInfo)
        handleQuickReplyInfo(channelData.quickReplyInfo)
        handleLeaderboardInfo(channelData.leaderboardInfo)
    }

    fun focusPage(channelData: PlayChannelData) {
        isActive.compareAndSet(false, true)

        focusVideoPlayer(channelData)
        updateChannelInfo(channelData)
        startWebSocket(channelData.id)
        trackVisitChannel(channelData.id)

        checkLeaderboard(channelData.id)
        //TODO("This is mock")
//        checkInteractive(channelData.id)
    }

    fun defocusPage(shouldPauseVideo: Boolean) {
        isActive.compareAndSet(true, false)

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
        _interactive.value = PlayInteractiveUiState.NoInteractive
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

    private fun handlePartnerInfo(partnerInfo: PlayPartnerInfo) {
        this._partnerInfo.value = partnerInfo
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

    private fun handleLeaderboardInfo(leaderboardInfo: PlayLeaderboardInfoUiModel) {
        _leaderboardInfo.value = leaderboardInfo
    }

    /**
     * Update channel data
     */

    /**
     * Follow status should only be retrieved if and only if the partner is a [PartnerType.Shop]
     * and if it is not the viewer's own shop id
     */
    private fun updatePartnerInfo(partnerInfo: PlayPartnerInfo) {
        if (partnerInfo.type == PartnerType.Shop && partnerInfo.id.toString() != userSession.shopId) {
            viewModelScope.launchCatchError(block = {
                val isFollowing = partnerRepo.getIsFollowingPartner(partnerId = partnerInfo.id)
                _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(isFollowing)) }
            }, onError = {

            })
        } else {
            _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.NotFollowable) }
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
                val deferredIsLiked = async {
                    likeRepo.getIsLiked(contentId = likeParamInfo.contentId.toLong(), contentType = likeParamInfo.contentType)
                }

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

    private fun checkLeaderboard(channelId: String) {
        if (!channelType.isLive) return
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val interactiveLeaderboard = interactiveRepo.getInteractiveLeaderboard(channelId)
            _leaderboardInfo.value = interactiveLeaderboard
        }) {}
    }

    private fun checkInteractive(channelId: String) {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            if (!channelType.isLive) error("Interactive is not available on non-Live channel")

            _interactive.value = PlayInteractiveUiState.Loading

            val interactive = interactiveRepo.getCurrentInteractive(channelId)
            handleInteractiveFromNetwork(interactive)
        }) {
            _interactive.value = PlayInteractiveUiState.Error
        }
    }

    private fun mapInteractiveToState(interactive: PlayCurrentInteractiveModel): PlayInteractiveUiState {
        return when (val status = interactive.timeStatus) {
            is PlayInteractiveTimeStatus.Scheduled -> PlayInteractiveUiState.PreStart(status.timeToStartInMs, interactive.title)
            is PlayInteractiveTimeStatus.Live -> PlayInteractiveUiState.Ongoing(status.remainingTimeInMs)
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
            _interactive.value = interactiveUiState
        }

        if (interactive.timeStatus is PlayInteractiveTimeStatus.Finished) {
            val channelId = mChannelData?.id ?: return

            try {
                val interactiveLeaderboard = interactiveRepo.getInteractiveLeaderboard(channelId)
                _leaderboardInfo.value = interactiveLeaderboard
                _interactive.value = PlayInteractiveUiState.NoInteractive
            } catch (e: Throwable) {}
        }
    }

    private suspend fun getChannelStatus(channelId: String) = withContext(dispatchers.io) {
        getChannelStatusUseCase.apply {
            setRequestParams(GetChannelStatusUseCase.createParams(arrayOf(channelId)))
        }.executeOnBackground()
    }

    private fun doOnForbidden() {
        isActive.set(false)

        stopInteractive()
        stopWebSocket()
        stopPlayer()
        onKeyboardHidden()
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

    private fun doFollowUnfollow(shouldForceFollow: Boolean): PartnerFollowAction? {
        val channelData = mChannelData ?: return null
        val shopId = channelData.partnerInfo.id

        val followStatus = _partnerInfo.value.status as? PlayPartnerFollowStatus.Followable ?: return null
        val shouldFollow = if (shouldForceFollow) true else !followStatus.isFollowing
        val followAction = if (shouldFollow) PartnerFollowAction.Follow else PartnerFollowAction.UnFollow

        _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(shouldFollow)) }

        viewModelScope.launchCatchError(block = {
            partnerRepo.postFollowStatus(
                    shopId = shopId.toString(),
                    followAction = followAction,
            )
        }) {}

        return followAction
    }

    /**
     * When pre-start finished, interactive should be played (e.g. TapTap)
     */
    private fun handleInteractivePreStartFinished() {
        viewModelScope.launch {
            val activeInteractiveId = interactiveRepo.getActiveInteractiveId() ?: return@launch
            val interactiveDetail = interactiveRepo.getDetail(activeInteractiveId) ?: return@launch
            if (!interactiveDetail.timeStatus.isScheduled()) return@launch

            _interactive.value = PlayInteractiveUiState.Ongoing(
                    timeRemainingInMs = (interactiveDetail.timeStatus as PlayInteractiveTimeStatus.Scheduled).interactiveDurationInMs
            )
        }
    }

    private fun handleInteractiveOngoingFinished() {
        fun setInteractiveToFinished(interactiveId: String) {
            interactiveRepo.setFinished(interactiveId)

            _interactive.value = PlayInteractiveUiState.Finished(
                    info = R.string.play_interactive_finish_initial_text,
            )
        }

        suspend fun showCoachMark(leaderboard: PlayLeaderboardInfoUiModel) {
            _uiEvent.emit(
                    ShowCoachMarkWinnerEvent(
                            leaderboard.config.loserMessage,
                            leaderboard.config.loserDetail
                    )
            )
        }

        suspend fun fetchLeaderboard(channelId: String) = coroutineScope {
            _interactive.value = PlayInteractiveUiState.Finished(
                    info = R.string.play_interactive_finish_loading_winner_text,
            )

            val deferredDelay = async { delay(INTERACTIVE_FINISH_MESSAGE_DELAY) }
            val deferredInteractiveLeaderboard = async { interactiveRepo.getInteractiveLeaderboard(channelId) }

            deferredDelay.await()
            val interactiveLeaderboard = deferredInteractiveLeaderboard.await()

            val currentLeaderboard = interactiveLeaderboard.leaderboardWinners.first()
            val userInLeaderboard = currentLeaderboard.winners.firstOrNull()

            _interactive.value = PlayInteractiveUiState.NoInteractive
            _leaderboardInfo.value = interactiveLeaderboard

            if (userInLeaderboard != null) {
                showCoachMark(interactiveLeaderboard)

                if (userInLeaderboard.id == userId) {
                    _uiEvent.emit(
                            ShowWinningDialogEvent(
                                    userInLeaderboard.imageUrl,
                                    interactiveLeaderboard.config.winnerMessage,
                                    interactiveLeaderboard.config.winnerDetail
                            )
                    )
                }
            }
        }

        viewModelScope.launchCatchError(block = {
            val channelId = mChannelData?.id ?: return@launchCatchError
            val activeInteractiveId = interactiveRepo.getActiveInteractiveId() ?: return@launchCatchError

            setInteractiveToFinished(activeInteractiveId)
            delay(INTERACTIVE_FINISH_MESSAGE_DELAY)

            try {
                fetchLeaderboard(channelId)
            } catch (e: Throwable) {
                _interactive.value = PlayInteractiveUiState.NoInteractive
            }
        }) {}
    }

    private fun handleWinnerBadgeClicked(height: Int) {
        showLeaderboardSheet(height)

        val channelData = mChannelData ?: return
        playAnalytic.clickWinnerBadge(channelId = channelData.id, channelType = channelData.channelInfo.channelType)
    }

    private fun handleTapTapAction() {
        viewModelScope.launch {
            interactiveFlow.emit(Unit)
        }

        val channelData = mChannelData ?: return
        playAnalytic.clickTapTap(channelId = channelData.id, channelType = channelData.channelInfo.channelType)
    }

    private fun handleCloseLeaderboardSheet() {
        hideLeaderboardSheet()
    }

    /**
     * @param isFromLogin If true, it means follow action will always be follow,
     * if false, it depends on the current state
     */
    private fun handleClickFollow(isFromLogin: Boolean) = needLogin(REQUEST_CODE_LOGIN_FOLLOW) {
        val action = doFollowUnfollow(shouldForceFollow = isFromLogin) ?: return@needLogin
        val channelData = mChannelData ?: return@needLogin
        val shopId = channelData.partnerInfo.id
        playAnalytic.clickFollowShop(channelData.id, channelData.channelInfo.channelType, shopId.toString(), action.value)
    }

    /**
     * [PartnerFollowAction] from interactive will definitely [PartnerFollowAction.Follow]
     */
    private fun handleClickFollowInteractive() = needLogin(REQUEST_CODE_LOGIN_FOLLOW) {
        doFollowUnfollow(shouldForceFollow = true) ?: return@needLogin

        viewModelScope.launch {
            _uiEvent.emit(
                    ShowToasterEvent.Info(message = UiString.Resource(R.string.play_interactive_follow_success))
            )
        }

        val channelData = mChannelData ?: return@needLogin
        playAnalytic.clickFollowShopInteractive(channelData.id, channelData.channelInfo.channelType)
    }

    private fun handleClickPartnerName() {
        viewModelScope.launch {
            val channelData = mChannelData ?: return@launch
            val partnerInfo = channelData.partnerInfo

            when (partnerInfo.type) {
                PartnerType.Shop -> {
                    playAnalytic.clickShop(channelData.id, channelData.channelInfo.channelType, channelData.partnerInfo.id.toString())
                    _uiEvent.emit(OpenPageEvent(ApplinkConst.SHOP, listOf(partnerInfo.id.toString()), pipMode = true))
                }
                PartnerType.Buyer -> _uiEvent.emit(OpenPageEvent(ApplinkConst.PROFILE, listOf(partnerInfo.id.toString()), pipMode = true))
                else -> {}
            }
        }
    }

    private fun handleClickRetryInteractive() {
        val channelId = mChannelData?.id ?: return
        checkInteractive(channelId = channelId)
    }

    private fun handleOpenPageResult(isSuccess: Boolean, requestCode: Int) {
        if (!isSuccess) return
        when (requestCode) {
            REQUEST_CODE_LOGIN_FOLLOW -> handleClickFollow(isFromLogin = true)
            REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE -> handleClickFollowInteractive()
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
        private const val INTERACTIVE_FINISH_MESSAGE_DELAY = 2000L

        /**
         * Request Code When need login
         */
        private const val REQUEST_CODE_LOGIN_FOLLOW = 571
        private const val REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE = 572
    }
}