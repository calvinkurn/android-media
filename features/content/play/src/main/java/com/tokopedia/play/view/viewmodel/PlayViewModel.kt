package com.tokopedia.play.view.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.gms.cast.framework.CastStateListener
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.multiplelikes.UpdateMultipleLikeConfig
import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.play_common.sse.PlayChannelSSEPageSource
import com.tokopedia.play.data.ssemapper.PlaySSEMapper
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.data.UpcomingChannelUpdateActive
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.*
import com.tokopedia.play.extensions.combine
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CastPlayerHelper
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateListener
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.setValue
import com.tokopedia.play.util.timer.TimerFactory
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.*
import com.tokopedia.play.view.monitoring.PlayVideoLatencyPerformanceMonitoring
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play.view.uimodel.state.*
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.dto.interactive.isScheduled
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.sse.*
import com.tokopedia.play_common.sse.model.SSEAction
import com.tokopedia.play_common.sse.model.SSECloseReason
import com.tokopedia.play_common.sse.model.SSEResponse
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketClosedReason
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.flow.collect
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
        private val getProductTagItemsUseCase: GetProductTagItemsUseCase,
        private val trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase,
        private val trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
        private val playChannelReminderUseCase: PlayChannelReminderUseCase,
        private val playSocketToModelMapper: PlaySocketToModelMapper,
        private val playUiModelMapper: PlayUiModelMapper,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers,
        private val remoteConfig: RemoteConfig,
        private val playPreference: PlayPreference,
        private val videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring,
        private val playChannelWebSocket: PlayChannelWebSocket,
        private val playChannelSSE: PlayChannelSSE,
        private val repo: PlayViewerRepository,
        private val playAnalytic: PlayNewAnalytic,
        private val timerFactory: TimerFactory,
        private val castPlayerHelper: CastPlayerHelper
) : ViewModel() {

    val observableChannelInfo: LiveData<PlayChannelInfoUiModel> /**Added**/
        get() = _observableChannelInfo
    val observableVideoMeta: LiveData<PlayVideoMetaInfoUiModel> /**Changed**/
        get() = _observableVideoMeta
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
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
    val observableEventPiPState: LiveData<Event<PiPState>>
        get() = _observableEventPiPState
    val observableOnboarding: LiveData<Event<Unit>>
        get() = _observableOnboarding
    val observableUpcomingInfo: LiveData<PlayUpcomingUiModel>
        get() = _observableUpcomingInfo
    val observableCastState: LiveData<PlayCastUiModel>
        get() = _observableCastState

    /**
     * Interactive Remote Config defaults to true, because it should be enabled by default,
     * and will be disabled only if something goes wrong
     */
    private val isInteractiveRemoteConfigEnabled: Boolean
        get() = remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_INTERACTIVE, true)

    private val isInteractiveAllowed: Boolean
        get() = channelType.isLive && videoOrientation.isVertical && videoPlayer.isGeneral() && isInteractiveRemoteConfigEnabled

    private val _uiEvent = MutableSharedFlow<PlayViewerNewUiEvent>(extraBufferCapacity = 50)

    private val _channelDetail = MutableStateFlow(PlayChannelDetailUiModel())
    private val _partnerInfo = MutableStateFlow(PlayPartnerInfo())
    private val _bottomInsets = MutableStateFlow(emptyMap<BottomInsetsType, BottomInsetsState>())
    private val _status = MutableStateFlow(PlayStatusType.Active)
    private val _interactive = MutableStateFlow<PlayInteractiveUiState>(PlayInteractiveUiState.NoInteractive)
    private val _leaderboardInfo = MutableStateFlow<PlayLeaderboardWrapperUiModel>(PlayLeaderboardWrapperUiModel.Unknown)
    private val _leaderboardUserBadgeState = MutableStateFlow(PlayLeaderboardBadgeUiState())
    private val _likeInfo = MutableStateFlow(PlayLikeInfoUiModel())
    private val _channelReport = MutableStateFlow(PlayChannelReportUiModel())
    private val _upcomingInfo = MutableStateFlow<PlayUpcomingUiModel?>(null)

    private val _interactiveUiState = combine(
        _interactive, _bottomInsets, _status
    ) { interactive, bottomInsets, status ->
        PlayInteractiveViewUiState(
            interactive = interactive,
            visibility = when {
                /**
                 * Invisible because when unify timer is set during gone, it's not gonna get rounded when it's shown :x
                 */
                bottomInsets.isAnyShown -> ViewVisibility.Invisible
                status.isFreeze || status.isBanned -> ViewVisibility.Gone
                interactive is PlayInteractiveUiState.NoInteractive -> ViewVisibility.Gone
                else -> ViewVisibility.Visible
            },
        )
    }.flowOn(dispatchers.computation)

    private val _partnerUiState = _partnerInfo.map {
        PlayPartnerUiState(it.name, it.status)
    }.flowOn(dispatchers.computation)

    private val _winnerBadgeUiState = combine(
        _leaderboardInfo, _bottomInsets, _status, _channelDetail, _leaderboardUserBadgeState
    ) { leaderboardInfo, bottomInsets, status, channelDetail, leaderboardUserBadgeState ->
        PlayWinnerBadgeUiState(
            leaderboards = leaderboardInfo,
            shouldShow = !bottomInsets.isAnyShown &&
                    status.isActive &&
                    leaderboardUserBadgeState.showLeaderboard &&
                    channelDetail.channelInfo.channelType.isLive,
        )
    }.flowOn(dispatchers.computation)

    private val _likeUiState = combine(
        _likeInfo, _channelDetail, _bottomInsets, _status, _channelReport
    ) { likeInfo, channelDetail, bottomInsets, status, channelReport ->
        PlayLikeUiState(
            shouldShow = !bottomInsets.isAnyShown && status.isActive,
            canLike = likeInfo.status != PlayLikeStatus.Unknown,
            totalLike = channelReport.totalLikeFmt,
            likeMode = if (channelDetail.channelInfo.channelType.isLive) PlayLikeMode.Multiple
            else PlayLikeMode.Single,
            isLiked = likeInfo.status == PlayLikeStatus.Liked,
            canShowBubble = !bottomInsets.isAnyShown &&
                    channelDetail.channelInfo.channelType.isLive &&
                    status.isActive,
        )
    }.flowOn(dispatchers.computation)

    private val _totalViewUiState = _channelReport.map {
        PlayTotalViewUiState(it.totalViewFmt)
    }.flowOn(dispatchers.computation)

    private val _shareUiState = combine(
        _channelDetail, _bottomInsets, _status, _upcomingInfo
    ) { channelDetail, bottomInsets, status, upcomingInfo ->
        PlayShareUiState(shouldShow = channelDetail.shareInfo.shouldShow &&
                !bottomInsets.isAnyShown &&
                (status.isActive || upcomingInfo?.isUpcoming == true)
        )
    }.flowOn(dispatchers.computation)

    private val _rtnUiState = combine(
        _channelDetail, _bottomInsets, _status
    ) { channelDetail, bottomInsets, status ->
        PlayRtnUiState(
            shouldShow = channelType.isLive &&
                    !bottomInsets.isAnyShown &&
                    status.isActive &&
                    !channelDetail.videoInfo.orientation.isHorizontal &&
                    !videoPlayer.isYouTube,
            lifespanInMs = channelDetail.rtnConfigInfo.lifespan,
        )
    }.flowOn(dispatchers.computation)

    private val _titleUiState = combine(
        _channelDetail, _bottomInsets
    ) { channelDetail, _ ->
        PlayTitleUiState(
            title = channelDetail.channelInfo.title
        )
    }.flowOn(dispatchers.computation)

    private val _viewAllProductUiState = combine(
        _channelDetail, _bottomInsets
    ) { _, bottomInsets ->
        PlayViewAllProductUiState(
            shouldShow = !bottomInsets.isAnyShown
        )
    }.flowOn(dispatchers.computation)

    /**
     * Until repeatOnLifecycle is available (by updating library version),
     * this can be used as an alternative to "complete" un-completable flow when page is not focused
     */
    private val isActive: AtomicBoolean = AtomicBoolean(false)

    val uiState: Flow<PlayViewerNewUiState> = combine(
        _interactiveUiState.distinctUntilChanged(),
        _partnerUiState.distinctUntilChanged(),
        _winnerBadgeUiState.distinctUntilChanged(),
        _bottomInsets,
        _likeUiState.distinctUntilChanged(),
        _totalViewUiState.distinctUntilChanged(),
        _shareUiState.distinctUntilChanged(),
        _rtnUiState.distinctUntilChanged(),
        _titleUiState.distinctUntilChanged(),
        _viewAllProductUiState.distinctUntilChanged()
    ) { interactive, partner, winnerBadge, bottomInsets, like, totalView, share, rtn, title, viewAllProduct ->
        PlayViewerNewUiState(
            interactiveView = interactive,
            partner = partner,
            winnerBadge = winnerBadge,
            bottomInsets = bottomInsets,
            like = like,
            totalView = totalView,
            share = share,
            rtn = rtn,
            title = title,
            viewAllProduct = viewAllProduct
        )
    }.flowOn(dispatchers.computation)

    val uiEvent: Flow<PlayViewerNewUiEvent>
        get() = _uiEvent.filter {
            isActive.get() || it is AllowedWhenInactiveEvent ||
                    (upcomingInfo != null && upcomingInfo?.isUpcoming == true)
        }.map { if (it is AllowedWhenInactiveEvent) it.event else it }
            .flowOn(dispatchers.computation)

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
        get() = _channelDetail.value.channelInfo.channelType
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

    val totalView: String
        get() = _channelReport.value.totalViewFmt

    val videoLatency: Long
        get() = videoLatencyPerformanceMonitoring.totalDuration

    val upcomingInfo: PlayUpcomingUiModel?
        get() = _observableUpcomingInfo.value

    private var mChannelData: PlayChannelData? = null

    private val channelId: String
        get() = _channelDetail.value.channelInfo.id

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
                    partnerInfo = channelData.partnerInfo,
                    likeInfo = _likeInfo.value,
                    channelReportInfo = _channelReport.value,
                    pinnedInfo = PlayPinnedInfoUiModel(
                            pinnedMessage = pinnedMessage,
                            pinnedProduct = pinnedProduct,
                    ),
                    quickReplyInfo = _observableQuickReply.value ?: channelData.quickReplyInfo,
                    videoMetaInfo = newVideoMeta,
                    statusInfo = _observableStatusInfo.value ?: channelData.statusInfo,
                    leaderboardInfo = _leaderboardInfo.value,
                    upcomingInfo = _observableUpcomingInfo.value ?: channelData.upcomingInfo
            )
        }

    val pipState: PiPState
        get() = _observableEventPiPState.value?.peekContent() ?: PiPState.Stop

    val isPiPAllowed: Boolean
        get() {
            return remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_PIP, true)
                    && !videoPlayer.isYouTube && !videoPlayer.isCasting()
        }

    val userId: String
        get() = userSession.userId

    val isCastAllowed: Boolean
        get() {
            val castState = observableCastState.value ?: return false
            return (castState.currentState != PlayCastState.NO_DEVICE_AVAILABLE && !videoPlayer.isYouTube
                    && remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_CAST, true)) || castState.currentState == PlayCastState.CONNECTED
        }

    private val isProductSheetInitialized: Boolean
        get() = _observableProductSheetContent.value != null

    private var socketJob: Job? = null

    private var sseJob: Job? = null

    private val _observableChannelInfo = MutableLiveData<PlayChannelInfoUiModel>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableQuickReply = MutableLiveData<PlayQuickReplyInfoUiModel>() /**Changed**/
    private val _observableStatusInfo = MutableLiveData<PlayStatusInfoUiModel>() /**Changed**/
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observablePinnedProduct = MutableLiveData<PinnedProductUiModel>() /**Changed**/
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val _observableVideoMeta = MutableLiveData<PlayVideoMetaInfoUiModel>() /**Changed**/
    private val _observableProductSheetContent = MutableLiveData<PlayResult<PlayProductTagsUiModel.Complete>>() /**Changed**/
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableEventPiPState = MutableLiveData<Event<PiPState>>()
    private val _observableOnboarding = MutableLiveData<Event<Unit>>() /**Added**/
    private val _observableUpcomingInfo = MutableLiveData<PlayUpcomingUiModel>()
    private val _observableCastState = MutableLiveData<PlayCastUiModel>()
    private val _observableUserWinnerStatus = MutableLiveData<PlayUserWinnerStatusUiModel>()
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
        addSource(_observableUpcomingInfo) {
            _upcomingInfo.value = it
        }
    }

    //region helper
    private val hasWordsOrDotsRegex = Regex("(\\.+|[a-z]+)")
    private val amountStringStepArray = arrayOf("k", "m")
    private fun String.trimMultipleNewlines() = trim().replace(Regex("(\\n+)"), "\n")
    //endregion

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

    private var likeReminderTimer: Job? = null

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
        stopSSE()
        if (!pipState.isInPiP) stopPlayer()
        playVideoPlayer.removeListener(videoManagerListener)
        videoStateProcessor.removeStateListener(videoStateListener)
        videoStateProcessor.removeStateListener(videoPerformanceListener)
        channelStateProcessor.removeStateListener(channelStateListener)
        removeCastSessionListener()
        removeCastStateListener()
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
            SetChannelActiveAction -> handleSetChannelActive()
            InteractivePreStartFinishedAction -> handleInteractivePreStartFinished()
            InteractiveOngoingFinishedAction -> handleInteractiveOngoingFinished()
            is InteractiveWinnerBadgeClickedAction -> handleWinnerBadgeClicked(action.height)
            InteractiveTapTapAction -> handleTapTapAction()
            ClickCloseLeaderboardSheetAction -> handleCloseLeaderboardSheet()
            ClickFollowAction -> handleClickFollow(isFromLogin = false)
            ClickFollowInteractiveAction -> handleClickFollowInteractive()
            ClickPartnerNameAction -> handleClickPartnerName()
            ClickRetryInteractiveAction -> handleClickRetryInteractive()
            ImpressUpcomingChannel -> handleImpressUpcomingChannel()
            ClickRemindMeUpcomingChannel -> handleRemindMeUpcomingChannel(userClick = true)
            ClickWatchNowUpcomingChannel -> handleWatchNowUpcomingChannel()
            is OpenPageResultAction -> handleOpenPageResult(action.isSuccess, action.requestCode)
            ClickLikeAction -> handleClickLike(isFromLogin = false)
            ClickShareAction -> handleClickShare()
            RefreshLeaderboard -> handleRefreshLeaderboard()
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
        handleChannelDetail(channelData.channelDetail)
        handleStatusInfo(channelData.statusInfo)
        handleChannelInfo(channelData.channelDetail.channelInfo)
        handleOnboarding(channelData.videoMetaInfo)
        handleVideoMetaInfo(channelData.videoMetaInfo)
        handlePartnerInfo(channelData.partnerInfo)
        handleChannelReportInfo(channelData.channelReportInfo)
        handleLikeInfo(channelData.likeInfo)
        handlePinnedInfo(channelData.pinnedInfo)
        handleQuickReplyInfo(channelData.quickReplyInfo)
        handleLeaderboardInfo(channelData.leaderboardInfo)
    }

    fun focusPage(channelData: PlayChannelData) {
        isActive.compareAndSet(false, true)

        _observableUpcomingInfo.value = channelData.upcomingInfo

        if(channelData.upcomingInfo.isUpcoming) {
            startSSE(channelData.id)
        }
        else {
            focusVideoPlayer(channelData)
            startWebSocket(channelData.id)
            checkLeaderboard(channelData.id)

            prepareSelfLikeBubbleIcon()
            checkLikeReminderTimer()

            addCastStateListener()

            setCastState(castPlayerHelper.mapCastState(castPlayerHelper.castContext.castState))
        }

        updateChannelInfo(channelData)
        trackVisitChannel(channelData.id)
    }

    fun defocusPage(shouldPauseVideo: Boolean) {
        isActive.compareAndSet(true, false)

        defocusVideoPlayer(shouldPauseVideo)
        returnToNonCastPlayer()
        stopWebSocket()
        cancelReminderLikeTimer()

        stopInteractive()
        stopSSE()

        removeCastSessionListener()
        removeCastStateListener()
    }

    private fun focusVideoPlayer(channelData: PlayChannelData) {
        fun loadCast() {
            if (channelData.statusInfo.statusType.isFreeze) return

            playVideoPlayer.stop(resetState = false)
            playVideoPlayer.removeListener(videoManagerListener)

            val videoStream = channelData.videoMetaInfo.videoStream
            if(mChannelData?.id.toString() != castPlayerHelper.getCurrentMediaChannelId()) {
                castPlayerHelper.castPlay(
                    channelId = channelData.id,
                    title = videoStream.title,
                    partnerName = channelData.partnerInfo.name,
                    coverUrl = channelData.channelDetail.channelInfo.coverUrl,
                    videoUrl = if(channelData.videoMetaInfo.videoPlayer.isGeneral())
                                    channelData.videoMetaInfo.videoPlayer.params.videoUrl
                                else "",
                    currentPosition = playVideoPlayer.getCurrentPosition()
                )
            }

            if(channelData.videoMetaInfo.videoPlayer.isGeneral())
                _observableVideoMeta.value = channelData.videoMetaInfo.copy(
                    videoPlayer = channelData.videoMetaInfo.videoPlayer.setPlayer(castPlayerHelper.player, channelData.channelDetail.channelInfo.coverUrl)
                )
        }

        fun loadPlayer() {
            if (channelData.statusInfo.statusType.isFreeze) return
            if (!channelData.videoMetaInfo.videoPlayer.isGeneral()) return

            playVideoPlayer.addListener(videoManagerListener)
            playVideoPlayer.resume()
            updateVideoMetaInfo(channelData.videoMetaInfo)

            _observableVideoMeta.value = channelData.videoMetaInfo.copy(
                videoPlayer = channelData.videoMetaInfo.videoPlayer.setPlayer(playVideoPlayer.videoPlayer)
            )
        }

        castPlayerHelper.setSessionAvailabilityListener(object : SessionAvailabilityListener {
            override fun onCastSessionAvailable() {
                loadCast()
            }

            override fun onCastSessionUnavailable() {
                loadPlayer()
            }
        })

        if (castPlayerHelper.hasAvailableSession) loadCast()
        else loadPlayer()
    }

    private fun defocusVideoPlayer(shouldPauseVideo: Boolean) {
        if (shouldPauseVideo) {
            if (playVideoPlayer.isVideoLive() || viewerVideoState.hasNoData) playVideoPlayer.stop()
            else playVideoPlayer.pause(preventLoadingBuffer = true)
        }
        playVideoPlayer.removeListener(videoManagerListener)
    }

    fun removeCastSessionListener() {
        castPlayerHelper.setSessionAvailabilityListener(null)
    }

    private fun returnToNonCastPlayer() {
        val vidPlayer = videoPlayer
        if (vidPlayer.isGeneral()) {
            _observableVideoMeta.value = _observableVideoMeta.value?.copy(
                    videoPlayer = vidPlayer.setPlayer(playVideoPlayer.videoPlayer)
            )
        }
    }

    private fun updateChannelInfo(channelData: PlayChannelData) {
        updateStatusInfo(channelData.id)
        updatePartnerInfo(channelData.partnerInfo)
        if (!channelData.statusInfo.statusType.isFreeze && upcomingInfo != null && upcomingInfo?.isUpcoming == false) {
            updateLikeAndTotalViewInfo(channelData.likeInfo, channelData.id)
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

    /**
     * @return true means that back has been consumed/handled
     * false means that back is allowed
     */
    fun goBack(): Boolean {
        val shownBottomSheets = observableBottomInsetsState.value
                ?.filter { it.value.isShown }
                ?.mapValues { it.value as BottomInsetsState.Shown }
                .orEmpty()
        val entry = shownBottomSheets.minByOrNull { it.value.deepLevel }
        when (entry?.key) {
            BottomInsetsType.Keyboard -> onKeyboardHidden()
            BottomInsetsType.ProductSheet -> onHideProductSheet()
            BottomInsetsType.VariantSheet -> onHideVariantSheet()
            BottomInsetsType.LeaderboardSheet -> hideLeaderboardSheet()
        }
        return shownBottomSheets.isNotEmpty()
    }

    fun getVideoPlayer() = playVideoPlayer

    private fun startWebSocket(channelId: String) {
        socketJob?.cancel()
        socketJob = viewModelScope.launch {
            val socketCredential = getSocketCredential()

            if (!isActive) return@launch
            connectWebSocket(
                    channelId = channelId,
                    socketCredential = socketCredential
            )

            playChannelWebSocket.listenAsFlow()
                    .collect {
                        handleWebSocketResponse(it, channelId, socketCredential)
                    }
        }
    }

    private fun connectWebSocket(channelId: String, socketCredential: SocketCredential) {
        playChannelWebSocket.connectSocket(channelId, socketCredential.gcToken)
    }

    private fun stopWebSocket() {
        socketJob?.cancel()
        playChannelWebSocket.close()
    }

    private fun stopInteractive() {
        _interactive.value = PlayInteractiveUiState.NoInteractive
    }

    private fun startSSE(channelId: String) {
        sseJob?.cancel()
        sseJob = viewModelScope.launch {
            val socketCredential = getSocketCredential()
            connectSSE(channelId, PlayChannelSSEPageSource.PlayUpcomingChannel.source, socketCredential.gcToken)
            playChannelSSE.listen().collect {
                when (it) {
                    is SSEAction.Message -> handleSSEMessage(it.message, channelId)
                    is SSEAction.Close -> {
                        if (it.reason == SSECloseReason.ERROR)
                            connectSSE(channelId, PlayChannelSSEPageSource.PlayUpcomingChannel.source, socketCredential.gcToken)
                    }
                }
            }
        }
    }

    private fun connectSSE(channelId: String, pageSource: String, gcToken: String) {
        playChannelSSE.connect(channelId, pageSource, gcToken)
    }

    private fun stopSSE() {
        sseJob?.cancel()
        playChannelSSE.close()
    }

    private suspend fun getSocketCredential(): SocketCredential = try {
        withContext(dispatchers.io) {
            return@withContext getSocketCredentialUseCase.executeOnBackground()
        }
    } catch (e: Throwable) {
        SocketCredential()
    }

    /**
     * Handle existing channel data
     */
    private fun handleChannelDetail(channelDetail: PlayChannelDetailUiModel) {
        _channelDetail.value = channelDetail
    }

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

    private fun handleChannelReportInfo(channelReport: PlayChannelReportUiModel) {
        _channelReport.value = channelReport
    }

    private fun handleLikeInfo(likeInfo: PlayLikeInfoUiModel) {
        _likeInfo.value = likeInfo
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

    private fun handleLeaderboardInfo(leaderboardInfo: PlayLeaderboardWrapperUiModel) {
        _leaderboardInfo.value = leaderboardInfo
        if(leaderboardInfo is PlayLeaderboardWrapperUiModel.Success)
            setLeaderboardBadgeState(leaderboardInfo.data)
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
                val isFollowing = repo.getIsFollowingPartner(partnerId = partnerInfo.id)
                _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(isFollowing)) }
            }, onError = {

            })
        } else {
            _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.NotFollowable) }
        }
    }

    private fun updateVideoMetaInfo(videoMetaInfo: PlayVideoMetaInfoUiModel) {
        when (videoMetaInfo.videoPlayer) {
            is PlayVideoPlayerUiModel.General.Incomplete -> {
                if (!castPlayerHelper.hasAvailableSession) playGeneralVideo(videoMetaInfo.videoPlayer)
                else playVideoPlayer.release()
            }
            is PlayVideoPlayerUiModel.General.Complete -> {
                if (videoMetaInfo.videoPlayer.playerType == PlayerType.Client) playGeneralVideo(videoMetaInfo.videoPlayer)
                else playVideoPlayer.release()
            }
            else -> playVideoPlayer.release()
        }
    }

    private fun updateLikeAndTotalViewInfo(likeInfo: PlayLikeInfoUiModel, channelId: String) {
        viewModelScope.launchCatchError(block = {
            supervisorScope {
                val deferredReportSummaries = async { getReportSummaries(channelId) }
                val deferredIsLiked = async {
                    repo.getIsLiked(contentId = likeInfo.contentId.toLong(), contentType = likeInfo.contentType)
                }

                try {
                    val report = deferredReportSummaries.await().data.first().channel.metrics
                    _channelReport.value = PlayChannelReportUiModel(report.totalViewFmt, report.totalLike.toLongOrZero(), report.totalLikeFmt)
                } catch (e: Throwable) {

                }

                val isLiked = try { deferredIsLiked.await() } catch (e: Throwable) { false }

                _likeInfo.setValue {
                    copy(status = if (isLiked) PlayLikeStatus.Liked else PlayLikeStatus.NotLiked, source = LikeSource.Network)
                }
            }
        }, onError = {
            _likeInfo.setValue {
                copy(status = PlayLikeStatus.NotLiked, source = LikeSource.Network)
            }
        })
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
        if (!isInteractiveAllowed) return
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val interactiveLeaderboard = repo.getInteractiveLeaderboard(channelId)
            _leaderboardInfo.value = PlayLeaderboardWrapperUiModel.Success(interactiveLeaderboard)

            setLeaderboardBadgeState(interactiveLeaderboard)
        }) {
            _leaderboardInfo.value = PlayLeaderboardWrapperUiModel.Error
        }
    }

    private fun setLeaderboardBadgeState(leaderboardInfo: PlayLeaderboardInfoUiModel) {
        if(leaderboardInfo.leaderboardWinners.isNotEmpty()) {
            _leaderboardUserBadgeState.setValue { copy(showLeaderboard = true) }
        }
    }

    private fun checkInteractive(channelId: String) {
        if (!isInteractiveAllowed) return
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _interactive.value = PlayInteractiveUiState.Loading

            val interactive = repo.getCurrentInteractive(channelId)
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
        if (!isInteractiveAllowed) return
        val interactiveUiState = mapInteractiveToState(interactive)
        repo.setDetail(interactive.id.toString(), interactive)
        if (interactive.timeStatus is PlayInteractiveTimeStatus.Scheduled || interactive.timeStatus is PlayInteractiveTimeStatus.Live) {
            repo.setActive(interactive.id.toString())
        } else {
            repo.setFinished(interactive.id.toString())
        }

        _interactive.value = if (repo.getActiveInteractiveId() != null) interactiveUiState else PlayInteractiveUiState.NoInteractive

        if (interactive.timeStatus is PlayInteractiveTimeStatus.Finished) {
            val channelId = mChannelData?.id ?: return

            try {
                val interactiveLeaderboard = repo.getInteractiveLeaderboard(channelId)
                _leaderboardInfo.value = PlayLeaderboardWrapperUiModel.Success(interactiveLeaderboard)
                setLeaderboardBadgeState(interactiveLeaderboard)

                _interactive.value = PlayInteractiveUiState.NoInteractive
            } catch (e: Throwable) {}
        }
    }

    private fun prepareSelfLikeBubbleIcon() {
        viewModelScope.launch {
            _uiEvent.emit(
                PreloadLikeBubbleIconEvent(
                    _likeInfo.value.likeBubbleConfig.bubbleMap.keys
                )
            )
        }
    }

    private fun checkLikeReminderTimer() {
        fun shouldRemindLike() = _likeInfo.value.status != PlayLikeStatus.Liked && channelType.isLive
        suspend fun sendLikeReminder() = _uiEvent.emit(RemindToLikeEvent)

        if (!shouldRemindLike()) return
        cancelReminderLikeTimer()
        likeReminderTimer = viewModelScope.launch(dispatchers.computation) {
            delay(TimeUnit.MINUTES.toMillis(1))
            timerFactory.createLoopingAlarm(
                millisInFuture = TimeUnit.MINUTES.toMillis(5),
                stopCondition = { !shouldRemindLike() },
                onStart = { sendLikeReminder() }
            )
        }
    }

    private fun cancelReminderLikeTimer() = likeReminderTimer?.cancel()

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
            is WebSocketAction.Closed -> {
                val reason = response.reason
                if (reason is WebSocketClosedReason.Error) {
                    playAnalytic.socketError(channelId, channelType, reason.error.localizedMessage.orEmpty())

                    connectWebSocket(channelId, socketCredential)
                }
            }
        }
    }

    private suspend fun handleWebSocketMessage(message: WebSocketResponse, channelId: String) = withContext(dispatchers.main) {
        val result = withContext(dispatchers.computation) {
            val socketMapper = PlaySocketMapper(message)
            socketMapper.mapping()
        }

        when (result) {
            is TotalLike -> withContext(dispatchers.computation) {
                val (totalLike, totalLikeFmt) = playSocketToModelMapper.mapTotalLike(result)
                val prevLike = _channelReport.value.totalLike

                if (channelType.isLive && totalLike < prevLike) return@withContext

                _channelReport.setValue {
                    copy(totalLike = totalLike, totalLikeFmt = totalLikeFmt)
                }

                if (!channelType.isLive) return@withContext

                val diffLike = totalLike - prevLike

                if (diffLike >= LIKE_BURST_THRESHOLD) {
                    _uiEvent.emit(
                        ShowLikeBubbleEvent.Burst(
                            LIKE_BURST_THRESHOLD,
                            reduceOpacity = true,
                            config = _likeInfo.value.likeBubbleConfig,
                        )
                    )
                } else if (diffLike > 0) {
                    _uiEvent.emit(
                        ShowLikeBubbleEvent.Single(
                            diffLike.toInt(),
                            reduceOpacity = true,
                            config = _likeInfo.value.likeBubbleConfig,
                        )
                    )
                }
            }
            is TotalView -> withContext(dispatchers.computation) {
                _channelReport.setValue {
                    copy(totalViewFmt = playSocketToModelMapper.mapTotalView(result))
                }
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
            is RealTimeNotification -> withContext(dispatchers.computation) {
                val notif = playSocketToModelMapper.mapRealTimeNotification(result)
                _uiEvent.emit(ShowRealTimeNotificationEvent(notif))
            }
            is UpdateMultipleLikeConfig -> {
                if (result.channelId.toString() != channelId) return@withContext
                val config = playSocketToModelMapper.mapMultipleLikeConfig(
                    result.configuration
                )
                _likeInfo.setValue {
                    copy(likeBubbleConfig = config)
                }
                viewModelScope.launch {
                    _uiEvent.emit(PreloadLikeBubbleIconEvent(config.bubbleMap.keys))
                }
            }
            is UserWinnerStatus -> {
                val interactiveState = _interactiveUiState.firstOrNull() ?: return@withContext

                val winnerStatus = playSocketToModelMapper.mapUserWinnerStatus(result)
                _observableUserWinnerStatus.value = winnerStatus

                if(interactiveState.interactive is PlayInteractiveUiState.Finished) {
                    handleUserWinnerStatus(winnerStatus)
                }
            }
        }
    }

    private suspend fun handleSSEMessage(message: SSEResponse, channelId: String) {
        val result = withContext(dispatchers.computation) {
            val sseMapper = PlaySSEMapper(message)
            sseMapper.mapping()
        }

        when(result) {
            is UpcomingChannelUpdateLive -> handleUpdateChannelStatus(result.channelId, channelId)
            is UpcomingChannelUpdateActive -> handleUpdateChannelStatus(result.channelId, channelId)
        }
    }

    private fun handleUpdateChannelStatus(changedChannelId: String, currentChannelId: String) {
        if(changedChannelId == currentChannelId) {
            _observableUpcomingInfo.value = _observableUpcomingInfo.value?.copy(isAlreadyLive = true)
            stopSSE()
        }
    }

    /**
     * Called when user tap
     */
    private suspend fun onReceivedInteractiveAction(action: Unit) = withContext(dispatchers.io) {
        try {
            val activeInteractiveId = repo.getActiveInteractiveId() ?: return@withContext
            if (repo.hasJoined(activeInteractiveId)) return@withContext

            val channelId = mChannelData?.id ?: return@withContext
            val isSuccess = repo.postInteractiveTap(channelId, activeInteractiveId)
            if (isSuccess) repo.setJoined(activeInteractiveId)
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
            repo.postFollowStatus(
                    shopId = shopId.toString(),
                    followAction = followAction,
            )
        }) {}

        return followAction
    }

    private fun handleSetChannelActive() {
        if (!userSession.isLoggedIn) return
        viewModelScope.launch {
            val welcomeFormat = _channelDetail.value.rtnConfigInfo.welcomeNotification
            _uiEvent.emit(ShowRealTimeNotificationEvent(welcomeFormat))
        }
    }

    /**
     * When pre-start finished, interactive should be played (e.g. TapTap)
     */
    private fun handleInteractivePreStartFinished() {
        viewModelScope.launch {
            val activeInteractiveId = repo.getActiveInteractiveId() ?: return@launch
            val interactiveDetail = repo.getDetail(activeInteractiveId) ?: return@launch
            if (!interactiveDetail.timeStatus.isScheduled()) return@launch

            _interactive.value = PlayInteractiveUiState.Ongoing(
                    timeRemainingInMs = (interactiveDetail.timeStatus as PlayInteractiveTimeStatus.Scheduled).interactiveDurationInMs
            )
        }
    }

    private fun handleInteractiveOngoingFinished() {
        suspend fun waitingForSocketOrDuration(activeInteractive: PlayCurrentInteractiveModel, activeInteractiveId: String) {
            delay(activeInteractive.endGameDelayInMs)
            repo.setFinished(activeInteractiveId)
            handleUserWinnerStatus(null)
        }

        viewModelScope.launchCatchError(block = {
            val activeInteractiveId = repo.getActiveInteractiveId() ?: return@launchCatchError
            val activeInteractive = repo.getDetail(activeInteractiveId) ?: return@launchCatchError

            _interactive.value = PlayInteractiveUiState.Finished(
                info = R.string.play_interactive_finish_initial_text,
            )
            delay(INTERACTIVE_FINISH_MESSAGE_DELAY)

            _interactive.value = PlayInteractiveUiState.Finished(
                info = R.string.play_interactive_finish_loading_winner_text,
            )
            delay(INTERACTIVE_FINISH_MESSAGE_DELAY)

            val winnerStatus = _observableUserWinnerStatus.value
            if(winnerStatus != null && winnerStatus.interactiveId.toString() == activeInteractiveId){
                handleUserWinnerStatus(winnerStatus)
            }
            else {
                waitingForSocketOrDuration(activeInteractive, activeInteractiveId)
            }
        }) {}
    }

    private suspend fun handleUserWinnerStatus(winnerStatus: PlayUserWinnerStatusUiModel?) {
        fun setNoInteractive() {
            if(_interactive.value is PlayInteractiveUiState.Finished)
                _interactive.value = PlayInteractiveUiState.NoInteractive
        }

        _leaderboardUserBadgeState.setValue {
            copy(showLeaderboard = true, shouldRefreshData = true)
        }

        winnerStatus?.let {
            val activeInteractiveId = repo.getActiveInteractiveId() ?: return
            val isUserJoined = repo.hasJoined(activeInteractiveId)

            if(winnerStatus.interactiveId.toString() == activeInteractiveId && isUserJoined) {
                setNoInteractive()
                repo.setFinished(activeInteractiveId)

                _uiEvent.emit(
                    if(winnerStatus.userId.toString() == userId){
                        ShowWinningDialogEvent(winnerStatus.imageUrl, winnerStatus.winnerTitle, winnerStatus.winnerText)
                    }
                    else {
                        ShowCoachMarkWinnerEvent(winnerStatus.loserTitle, winnerStatus.loserText)
                    }
                )
            }
            else {
                setNoInteractive()
            }
        } ?: run {
            setNoInteractive()
        }
    }

    private fun handleWinnerBadgeClicked(height: Int) {
        showLeaderboardSheet(height)

        playAnalytic.clickWinnerBadge(
                channelId = channelId,
                channelType = channelType,
        )
    }

    private fun handleTapTapAction() {
        viewModelScope.launch {
            interactiveFlow.emit(Unit)
        }

        val interactiveId = repo.getActiveInteractiveId() ?: return
        playAnalytic.clickTapTap(
                channelId = channelId,
                channelType = channelType,
                interactiveId = interactiveId,
        )
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
        val shopId = _partnerInfo.value.id
        playAnalytic.clickFollowShop(channelId, channelType, shopId.toString(), action.value)
    }

    /**
     * [PartnerFollowAction] from interactive will definitely [PartnerFollowAction.Follow]
     */
    private fun handleClickFollowInteractive() = needLogin(REQUEST_CODE_LOGIN_FOLLOW) {
        doFollowUnfollow(shouldForceFollow = true) ?: return@needLogin

        viewModelScope.launch {
            _uiEvent.emit(
                ShowInfoEvent(message = UiString.Resource(R.string.play_interactive_follow_success))
            )
        }

        val interactiveId = repo.getActiveInteractiveId() ?: return@needLogin
        playAnalytic.clickFollowShopInteractive(
                channelId,
                channelType,
                interactiveId,
        )
    }

    private fun handleClickPartnerName() {
        viewModelScope.launch {
            val partnerInfo = _partnerInfo.value

            when (partnerInfo.type) {
                PartnerType.Shop -> {
                    playAnalytic.clickShop(channelId, channelType, partnerInfo.id.toString())
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

    private fun handleImpressUpcomingChannel() {
        playAnalytic.impressUpcomingPage(channelId)
    }

    private fun handleRemindMeUpcomingChannel(userClick: Boolean)  {
        if(userClick) playAnalytic.clickRemindMe(channelId)

        needLogin(REQUEST_CODE_LOGIN_REMIND_ME) {
            viewModelScope.launchCatchError(block = {

                mChannelData?.let {
                    val status: Boolean

                    withContext(dispatchers.io) {
                        playChannelReminderUseCase.setRequestParams(PlayChannelReminderUseCase.createParams(it.id, true))
                        val response = playChannelReminderUseCase.executeOnBackground()
                        status = PlayChannelReminderUseCase.checkRequestSuccess(response)
                    }

                    _observableUpcomingInfo.value = _observableUpcomingInfo.value?.copy(isReminderSet = status)

                    _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_remind_me_success), isSuccess = status))

                } ?: _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
            }) {
                _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
            }
        }
    }

    private fun handleWatchNowUpcomingChannel() {
        playAnalytic.clickWatchNow(channelId)
        stopSSE()
    }

    private fun handleOpenPageResult(isSuccess: Boolean, requestCode: Int) {
        if (!isSuccess) return
        when (requestCode) {
            REQUEST_CODE_LOGIN_FOLLOW -> handleClickFollow(isFromLogin = true)
            REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE -> handleClickFollowInteractive()
            REQUEST_CODE_LOGIN_REMIND_ME -> handleRemindMeUpcomingChannel(userClick = false)
            REQUEST_CODE_LOGIN_LIKE -> handleClickLike(isFromLogin = true)
            else -> {}
        }
    }

    private fun handleClickLike(isFromLogin: Boolean) = needLogin(REQUEST_CODE_LOGIN_LIKE) {

        fun getNewTotalLikes(status: PlayLikeStatus): Pair<Long, String> {
            val currentTotalLike = _channelReport.value.totalLike
            val currentTotalLikeFmt = _channelReport.value.totalLikeFmt
            return if (!hasWordsOrDotsRegex.containsMatchIn(currentTotalLikeFmt)) {
                val totalLike =
                    (_channelReport.value.totalLike + (if (status == PlayLikeStatus.Liked) 1 else -1))
                        .coerceAtLeast(0)
                val fmt = totalLike.toAmountString(amountStringStepArray, separator = ".")
                totalLike to fmt
            } else {
                currentTotalLike to currentTotalLikeFmt
            }
        }

        /**
         * Like for Live Channel
         * - New status will always be LIKE
         * - Send Data to BE via Socket
         */
        fun handleClickLikeLive(onNewStatusFn: (status: PlayLikeStatus) -> Unit) {
            val newStatus = PlayLikeStatus.Liked
            _likeInfo.setValue {
                copy(status = newStatus, source = LikeSource.UserAction)
            }

            viewModelScope.launch {
                _uiEvent.emit(AnimateLikeEvent(fromIsLiked = true))
                _uiEvent.emit(ShowLikeBubbleEvent.Single(
                    count = 1,
                    reduceOpacity = false,
                    config = _likeInfo.value.likeBubbleConfig,
                ))
            }

            val (newTotalLike, newTotalLikeFmt) = getNewTotalLikes(newStatus)
            _channelReport.setValue {
                copy(totalLike = newTotalLike, totalLikeFmt = newTotalLikeFmt)
            }

            playChannelWebSocket.send(
                playSocketToModelMapper.mapSendLike(channelId)
            )

            onNewStatusFn(newStatus)
        }

        /**
         * Like for Non-Live Channel
         * - New status can be UNLIKE or LIKE
         * - Send Data to BE via GQL
         */
        fun handleClickLikeNonLive(isFromLogin: Boolean, onNewStatusFn: (status: PlayLikeStatus) -> Unit) {

            val likeInfo = _likeInfo.value
            if (likeInfo.status == PlayLikeStatus.Unknown) return

            val newStatus = when {
                isFromLogin -> PlayLikeStatus.Liked
                likeInfo.status == PlayLikeStatus.Liked -> PlayLikeStatus.NotLiked
                else -> PlayLikeStatus.Liked
            }
            _likeInfo.setValue { copy(status = newStatus, source = LikeSource.UserAction) }

            if (newStatus == PlayLikeStatus.Liked) {
                viewModelScope.launch {
                    _uiEvent.emit(
                        AnimateLikeEvent(
                            fromIsLiked = likeInfo.status == PlayLikeStatus.Liked
                        )
                    )
                }
            }

            val (newTotalLike, newTotalLikeFmt) = getNewTotalLikes(newStatus)
            _channelReport.setValue {
                copy(totalLike = newTotalLike, totalLikeFmt = newTotalLikeFmt)
            }

            viewModelScope.launch {
                repo.postLike(
                    contentId = likeInfo.contentId.toLongOrZero(),
                    contentType = likeInfo.contentType,
                    likeType = likeInfo.likeType,
                    shouldLike = newStatus == PlayLikeStatus.Liked
                )
            }

            onNewStatusFn(newStatus)
        }

        /**
         * Main Like Condition Logic
         */
        val newStatusHandler: (PlayLikeStatus) -> Unit = { status ->
            if (status == PlayLikeStatus.Liked) cancelReminderLikeTimer()

            playAnalytic.clickLike(
                channelId = channelId,
                channelType = channelType,
                channelName = _channelDetail.value.channelInfo.title,
                likeStatus = status,
            )
        }

        if (channelType.isLive) handleClickLikeLive(newStatusHandler)
        else handleClickLikeNonLive(isFromLogin, newStatusHandler)
    }

    private fun handleClickShare() {
        val shareInfo = _channelDetail.value.shareInfo

        viewModelScope.launch {
            _uiEvent.emit(
                CopyToClipboardEvent(shareInfo.content)
            )

            _uiEvent.emit(
                ShowInfoEvent(
                    UiString.Resource(R.string.play_link_copied)
                )
            )
        }
    }

    private fun handleRefreshLeaderboard() {
        if(_leaderboardUserBadgeState.value.shouldRefreshData) {
            _leaderboardInfo.value = PlayLeaderboardWrapperUiModel.Loading
            _leaderboardUserBadgeState.setValue { copy(shouldRefreshData = false) }
        }

        checkLeaderboard(channelId)
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

    private val castStateListener = CastStateListener {
        setCastState(castPlayerHelper.mapCastState(it))
    }

    private fun addCastStateListener() {
        castPlayerHelper.addCastStateListener(castStateListener)
    }

    private fun removeCastStateListener() {
        castPlayerHelper.removeCastStateListener(castStateListener)
    }

    private fun setCastState(castState: PlayCastState) {
        var model = _observableCastState.value
        if(model == null) {
            model = PlayCastUiModel(currentState = castState)
        }
        else {
            model.previousState = model.currentState
            model.currentState = castState
        }
        _observableCastState.value = model
    }

    companion object {
        private const val FIREBASE_REMOTE_CONFIG_KEY_PIP = "android_mainapp_enable_pip"
        private const val FIREBASE_REMOTE_CONFIG_KEY_INTERACTIVE = "android_main_app_enable_play_interactive"
        private const val FIREBASE_REMOTE_CONFIG_KEY_CAST = "android_main_app_enable_play_cast"
        private const val ONBOARDING_DELAY = 5000L
        private const val INTERACTIVE_FINISH_MESSAGE_DELAY = 2000L

        private const val LIKE_BURST_THRESHOLD = 30

        /**
         * Request Code When need login
         */
        private const val REQUEST_CODE_LOGIN_FOLLOW = 571
        private const val REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE = 572
        private const val REQUEST_CODE_LOGIN_LIKE = 573
        private const val REQUEST_CODE_LOGIN_REMIND_ME = 574
    }
}