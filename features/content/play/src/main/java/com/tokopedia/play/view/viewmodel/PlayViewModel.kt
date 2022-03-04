package com.tokopedia.play.view.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.gms.cast.framework.CastStateListener
import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.multiplelikes.UpdateMultipleLikeConfig
import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.*
import com.tokopedia.play.extensions.combine
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CastPlayerHelper
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateListener
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.setValue
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.util.share.PlayShareExperienceData
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
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play.view.uimodel.state.*
import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.dto.interactive.isScheduled
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.sse.*
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketClosedReason
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.flow.collect
import kotlin.math.max

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @AssistedInject constructor(
    @Assisted val channelId: String,
    playVideoBuilder: PlayVideoWrapper.Builder,
    videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory,
    channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory,
    videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory,
    private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
    private val getReportSummariesUseCase: GetReportSummariesUseCase,
    private val trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
    private val playSocketToModelMapper: PlaySocketToModelMapper,
    private val playUiModelMapper: PlayUiModelMapper,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val remoteConfig: RemoteConfig,
    private val playPreference: PlayPreference,
    private val videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring,
    private val playChannelWebSocket: PlayWebSocket,
    private val repo: PlayViewerRepository,
    private val playAnalytic: PlayNewAnalytic,
    private val timerFactory: TimerFactory,
    private val castPlayerHelper: CastPlayerHelper,
    private val playShareExperience: PlayShareExperience,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(channelId: String): PlayViewModel
    }

    val observableChannelInfo: LiveData<PlayChannelInfoUiModel> /**Added**/
        get() = _observableChannelInfo
    val observableVideoMeta: LiveData<PlayVideoMetaInfoUiModel> /**Changed**/
        get() = _observableVideoMeta
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableBottomInsetsState: LiveData<Map<BottomInsetsType, BottomInsetsState>>
        get() = _observableBottomInsetsState
    val observablePinnedMessage: LiveData<PinnedMessageUiModel>
        get() = _observablePinnedMessage
    val observableVideoProperty: LiveData<VideoPropertyUiModel>
        get() = _observableVideoProperty
    val observableEventPiPState: LiveData<Event<PiPState>>
        get() = _observableEventPiPState
    val observableOnboarding: LiveData<Event<Unit>>
        get() = _observableOnboarding
    val observableCastState: LiveData<PlayCastUiModel>
        get() = _observableCastState

    /**
     * Remote Config for bubble like
     */
    private val isLikeBubbleEnabled: Boolean
        get() = remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_LIKE_BUBBLE, true)

    /**
     * Interactive Remote Config defaults to true, because it should be enabled by default,
     * and will be disabled only if something goes wrong
     */
    private val isInteractiveRemoteConfigEnabled: Boolean
        get() = remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_INTERACTIVE, true)

    private val isInteractiveAllowed: Boolean
        get() = channelType.isLive && videoOrientation.isVertical && videoPlayer.isGeneral() && isInteractiveRemoteConfigEnabled

    private val _uiEvent = MutableSharedFlow<PlayViewerNewUiEvent>(extraBufferCapacity = 50)

    /**
     * Data State
     */
    private val _channelDetail = MutableStateFlow(PlayChannelDetailUiModel())
    private val _partnerInfo = MutableStateFlow(PlayPartnerInfo())
    private val _bottomInsets = MutableStateFlow(emptyMap<BottomInsetsType, BottomInsetsState>())
    private val _status = MutableStateFlow(PlayStatusUiModel.Empty)
    private val _interactive = MutableStateFlow<PlayInteractiveUiState>(PlayInteractiveUiState.NoInteractive)
    private val _leaderboardInfo = MutableStateFlow<PlayLeaderboardWrapperUiModel>(PlayLeaderboardWrapperUiModel.Unknown)
    private val _leaderboardUserBadgeState = MutableStateFlow(PlayLeaderboardBadgeUiState())
    private val _likeInfo = MutableStateFlow(PlayLikeInfoUiModel())
    private val _channelReport = MutableStateFlow(PlayChannelReportUiModel())
    private val _tagItems = MutableStateFlow(TagItemUiModel.Empty)
    private val _quickReply = MutableStateFlow(PlayQuickReplyInfoUiModel.Empty)

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
                status.channelStatus.statusType.isFreeze || status.channelStatus.statusType.isFreeze -> ViewVisibility.Gone
                interactive is PlayInteractiveUiState.NoInteractive -> ViewVisibility.Gone
                else -> ViewVisibility.Visible
            },
        )
    }.flowOn(dispatchers.computation)

    private val _winnerBadgeUiState = combine(
        _leaderboardInfo, _bottomInsets, _status, _channelDetail, _leaderboardUserBadgeState
    ) { leaderboardInfo, bottomInsets, status, channelDetail, leaderboardUserBadgeState ->
        PlayWinnerBadgeUiState(
            leaderboards = leaderboardInfo,
            shouldShow = !bottomInsets.isAnyShown &&
                    status.channelStatus.statusType.isActive &&
                    leaderboardUserBadgeState.showLeaderboard &&
                    channelDetail.channelInfo.channelType.isLive,
        )
    }.flowOn(dispatchers.computation)

    private val _likeUiState = combine(
        _likeInfo, _channelDetail, _bottomInsets, _status, _channelReport
    ) { likeInfo, channelDetail, bottomInsets, status, channelReport ->
        PlayLikeUiState(
            shouldShow = !bottomInsets.isAnyShown && status.channelStatus.statusType.isActive,
            canLike = likeInfo.status != PlayLikeStatus.Unknown,
            totalLike = channelReport.totalLikeFmt,
            likeMode = if (channelDetail.channelInfo.channelType.isLive) PlayLikeMode.Multiple
            else PlayLikeMode.Single,
            isLiked = likeInfo.status == PlayLikeStatus.Liked,
            canShowBubble = !bottomInsets.isAnyShown &&
                    channelDetail.channelInfo.channelType.isLive &&
                    status.channelStatus.statusType.isActive,
        )
    }.flowOn(dispatchers.computation)

    private val _totalViewUiState = _channelReport.map {
        PlayTotalViewUiState(it.totalViewFmt)
    }.flowOn(dispatchers.computation)

    private val _rtnUiState = combine(
        _channelDetail, _bottomInsets, _status
    ) { channelDetail, bottomInsets, status ->
        PlayRtnUiState(
            shouldShow = channelType.isLive &&
                    !bottomInsets.isAnyShown &&
                    status.channelStatus.statusType.isActive &&
                    !channelDetail.videoInfo.orientation.isHorizontal &&
                    !videoPlayer.isYouTube,
            lifespanInMs = channelDetail.rtnConfigInfo.lifespan,
        )
    }.flowOn(dispatchers.computation)

    private val _titleUiState = _channelDetail.map {
        PlayTitleUiState(
            title = it.channelInfo.title
        )
    }.flowOn(dispatchers.computation)

    private val _kebabMenuUiState = _bottomInsets.map {
        PlayKebabMenuUiState(
            shouldShow = !isFreezeOrBanned && !it.isKeyboardShown
        )
    }.flowOn(dispatchers.computation)


    /**
     * Until repeatOnLifecycle is available (by updating library version),
     * this can be used as an alternative to "complete" un-completable flow when page is not focused
     */
    private val isActive: AtomicBoolean = AtomicBoolean(false)

    val uiState: Flow<PlayViewerNewUiState> = combine(
        _channelDetail,
        _interactiveUiState.distinctUntilChanged(),
        _partnerInfo,
        _winnerBadgeUiState.distinctUntilChanged(),
        _bottomInsets,
        _likeUiState.distinctUntilChanged(),
        _totalViewUiState.distinctUntilChanged(),
        _rtnUiState.distinctUntilChanged(),
        _titleUiState.distinctUntilChanged(),
        _tagItems,
        _status,
        _quickReply,
        _kebabMenuUiState.distinctUntilChanged(),
    ) { channelDetail, interactive, partner, winnerBadge, bottomInsets,
        like, totalView, rtn, title, tagItems,
        status, quickReply, kebabMenu ->
        PlayViewerNewUiState(
            channel = channelDetail,
            interactiveView = interactive,
            partner = partner,
            winnerBadge = winnerBadge,
            bottomInsets = bottomInsets,
            like = like,
            totalView = totalView,
            rtn = rtn,
            title = title,
            tagItems = tagItems,
            status = status,
            quickReply = quickReply,
            kebabMenu = kebabMenu,
        )
    }.flowOn(dispatchers.computation)

    val uiEvent: Flow<PlayViewerNewUiEvent>
        get() = _uiEvent.filter {
            isActive.get() || it is AllowedWhenInactiveEvent
        }.map { if (it is AllowedWhenInactiveEvent) it.event else it }
            .flowOn(dispatchers.computation)

    val videoOrientation: VideoOrientation
        get() {
            val videoStream = _observableVideoMeta.value?.videoStream
            return videoStream?.orientation ?: VideoOrientation.Unknown
        }
    val statusType: PlayStatusType
        get() {
            return _status.value.channelStatus.statusType
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
            val statusType = _status.value.channelStatus.statusType
            return statusType.isFreeze || statusType.isBanned
        }

    /**
     * Temporary
     */
    val quickReply: PlayQuickReplyInfoUiModel
        get() = _quickReply.value

    val partnerId: Long?
        get() = mChannelData?.partnerInfo?.id

    val totalView: String
        get() = _channelReport.value.totalViewFmt

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

            return channelData.copy(
                partnerInfo = channelData.partnerInfo,
                likeInfo = _likeInfo.value,
                channelReportInfo = _channelReport.value,
                pinnedInfo = PlayPinnedInfoUiModel(
                    pinnedMessage = pinnedMessage,
                ),
                quickReplyInfo = _quickReply.value,
                videoMetaInfo = newVideoMeta,
                status = _status.value,
                leaderboardInfo = _leaderboardInfo.value,
                tagItems = _tagItems.value,
                upcomingInfo = channelData.upcomingInfo
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

    private var socketJob: Job? = null

    private val _observableChannelInfo = MutableLiveData<PlayChannelInfoUiModel>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val _observableVideoMeta = MutableLiveData<PlayVideoMetaInfoUiModel>() /**Changed**/
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableEventPiPState = MutableLiveData<Event<PiPState>>()
    private val _observableOnboarding = MutableLiveData<Event<Unit>>() /**Added**/
    private val _observableCastState = MutableLiveData<PlayCastUiModel>()
    private val _observableUserWinnerStatus = MutableLiveData<PlayUserWinnerStatusUiModel>()
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
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
                val statusType = _status.value.channelStatus.statusType
                if (!statusType.isFreeze && shouldFreeze) {
                    _status.update {
                        it.copy(
                            channelStatus = it.channelStatus.copy(
                                statusType = PlayStatusType.Freeze,
                            )
                        )
                    }
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

    private val gson by lazy { Gson() }

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

        viewModelScope.launch {
            _status.collectLatest {
                if (it.channelStatus.statusType.isFreeze || it.channelStatus.statusType.isBanned) {
                    doOnForbidden()
                }
            }
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

    fun onShowKebabMenuSheet(estimatedSheetHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.KebabMenuSheet] =
            BottomInsetsState.Shown(
                estimatedInsetsHeight = estimatedSheetHeight,
                isPreviousStateSame = insetsMap[BottomInsetsType.KebabMenuSheet]?.isShown == true
            )

        _observableBottomInsetsState.value = insetsMap
    }

    fun hideKebabMenuSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.KebabMenuSheet] =
            BottomInsetsState.Hidden(
                isPreviousStateSame = insetsMap[BottomInsetsType.KebabMenuSheet]?.isHidden == true
            )

        _observableBottomInsetsState.value = insetsMap
    }

    fun showCouponSheet(estimatedHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.CouponSheet] =
            BottomInsetsState.Shown(
                estimatedInsetsHeight = estimatedHeight,
                isPreviousStateSame = insetsMap[BottomInsetsType.CouponSheet]?.isShown == true
            )

        _observableBottomInsetsState.value = insetsMap
    }

    fun hideCouponSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.CouponSheet] =
            BottomInsetsState.Hidden(
                isPreviousStateSame = insetsMap[BottomInsetsType.CouponSheet]?.isHidden == true
            )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowUserReportSheet(estimatedSheetHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.UserReportSheet] =
            BottomInsetsState.Shown(
                estimatedInsetsHeight = estimatedSheetHeight,
                isPreviousStateSame = insetsMap[BottomInsetsType.UserReportSheet]?.isShown == true
            )

        _observableBottomInsetsState.value = insetsMap
    }

    fun hideUserReportSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.UserReportSheet] =
            BottomInsetsState.Hidden(
                isPreviousStateSame = insetsMap[BottomInsetsType.UserReportSheet]?.isHidden == true
            )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowUserReportSubmissionSheet(estimatedSheetHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.UserReportSubmissionSheet] =
            BottomInsetsState.Shown(
                estimatedInsetsHeight = estimatedSheetHeight,
                isPreviousStateSame = insetsMap[BottomInsetsType.UserReportSubmissionSheet]?.isShown == true
            )

        _observableBottomInsetsState.value = insetsMap
    }

    fun hideUserReportSubmissionSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.UserReportSubmissionSheet] =
            BottomInsetsState.Hidden(
                isPreviousStateSame = insetsMap[BottomInsetsType.UserReportSubmissionSheet]?.isHidden == true
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
        val defaultCouponSheetState = currentBottomInsetsMap?.get(BottomInsetsType.CouponSheet)?.isHidden ?: true
        val defaultKebabMenuSheet = currentBottomInsetsMap?.get(BottomInsetsType.KebabMenuSheet)?.isHidden ?: true
        val defautUserReportListSheet = currentBottomInsetsMap?.get(BottomInsetsType.UserReportSheet)?.isHidden ?: true
        val defaultUserReportSubmissionSHeet = currentBottomInsetsMap?.get(BottomInsetsType.UserReportSubmissionSheet)?.isHidden ?: true
        return mapOf(
                BottomInsetsType.Keyboard to BottomInsetsState.Hidden(defaultKeyboardState),
                BottomInsetsType.ProductSheet to BottomInsetsState.Hidden(defaultProductSheetState),
                BottomInsetsType.VariantSheet to BottomInsetsState.Hidden(defaultVariantSheetState),
                BottomInsetsType.LeaderboardSheet to BottomInsetsState.Hidden(defaultLeaderboardSheetState),
                BottomInsetsType.CouponSheet to BottomInsetsState.Hidden(defaultCouponSheetState),
                BottomInsetsType.KebabMenuSheet to BottomInsetsState.Hidden(defaultKebabMenuSheet),
                BottomInsetsType.UserReportSheet to BottomInsetsState.Hidden(defautUserReportListSheet),
                BottomInsetsType.UserReportSubmissionSheet to BottomInsetsState.Hidden(defaultUserReportSubmissionSHeet)
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

    fun getVideoTimestamp(): Long = playVideoPlayer.getCurrentPosition()

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
            is OpenPageResultAction -> handleOpenPageResult(action.isSuccess, action.requestCode)
            ClickLikeAction -> handleClickLike(isFromLogin = false)
            RefreshLeaderboard -> handleRefreshLeaderboard()
            RetryGetTagItemsAction -> handleRetryGetTagItems()
            CopyLinkAction -> handleCopyLink()
            ClickShareAction -> handleClickShareIcon()
            ShowShareExperienceAction -> handleOpenSharingOption(false)
            ScreenshotTakenAction -> handleOpenSharingOption(true)
            CloseSharingOptionAction -> handleCloseSharingOption()
            is ClickSharingOptionAction -> handleSharingOption(action.shareModel)
            is SharePermissionAction -> handleSharePermission(action.label)
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
        handleChannelInfo(channelData.channelDetail.channelInfo)
        handleOnboarding(channelData.videoMetaInfo)
        handleVideoMetaInfo(channelData.videoMetaInfo)
        handlePartnerInfo(channelData.partnerInfo)
        handleChannelReportInfo(channelData.channelReportInfo)
        handleLikeInfo(channelData.likeInfo)
        handlePinnedInfo(channelData.pinnedInfo)
        handleLeaderboardInfo(channelData.leaderboardInfo)

        _partnerInfo.value = channelData.partnerInfo
        _status.value = channelData.status
        _tagItems.value = channelData.tagItems
        _quickReply.value = channelData.quickReplyInfo
    }

    fun focusPage(channelData: PlayChannelData) {
        isActive.compareAndSet(false, true)

        focusVideoPlayer(channelData)
        startWebSocket(channelData.id)
        checkLeaderboard(channelData.id)

        prepareSelfLikeBubbleIcon()
        checkLikeReminderTimer()

        addCastStateListener()

        setCastState(castPlayerHelper.mapCastState(castPlayerHelper.castContext?.castState))

        trackVisitChannel(channelData.id, channelData.channelReportInfo.shouldTrack, channelData.channelReportInfo.sourceType)

        updateTagItems()
        updateChannelStatus()

        updateChannelInfo(channelData)
    }

    fun defocusPage(shouldPauseVideo: Boolean) {
        isActive.compareAndSet(true, false)

        defocusVideoPlayer(shouldPauseVideo)
        returnToNonCastPlayer()
        stopWebSocket()
        cancelReminderLikeTimer()

        stopInteractive()

        removeCastSessionListener()
        removeCastStateListener()
    }

    private fun focusVideoPlayer(channelData: PlayChannelData) {
        fun loadCast() {
            if (channelData.status.channelStatus.statusType.isFreeze) return

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
                    currentPosition = max(playVideoPlayer.getCurrentPosition(), 0)
                )
            }

            if(channelData.videoMetaInfo.videoPlayer.isGeneral())
                castPlayerHelper.player?.let {
                    _observableVideoMeta.value = channelData.videoMetaInfo.copy(
                        videoPlayer = channelData.videoMetaInfo.videoPlayer.setPlayer(it, channelData.channelDetail.channelInfo.coverUrl)
                    )
                }
        }

        fun loadPlayer() {
            if (channelData.status.channelStatus.statusType.isFreeze) return
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
        updatePartnerInfo(channelData.partnerInfo)
        if (!channelData.status.channelStatus.statusType.isFreeze) {
            updateLikeAndTotalViewInfo(channelData.likeInfo, channelData.id)
        }
    }

    /**
     * Updating the tag items (Product, Voucher) associated with this channel,
     *
     * If the config defined that pinned product should not be shown,
     * then we don't need to retrieve the product.
     */
    private fun updateTagItems() {
        if (!_tagItems.value.product.canShow) {
            _tagItems.update { it.copy(resultState = ResultState.Success) }
            return
        }

        _tagItems.update { it.copy(resultState = ResultState.Loading) }
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val tagItem = repo.getTagItem(channelId)
            _tagItems.value = tagItem

            sendProductTrackerToBro(
                productList = tagItem.product.productList
            )
        }) { err ->
            _tagItems.update { it.copy(resultState = ResultState.Fail(err)) }
        }
    }

    /**
     * Handy feature to send tracker to bro
     * by getting the product ids of the given products
     * @param productList the product list which tracker will be sent to bro
     */
    private fun sendProductTrackerToBro(productList: List<PlayProductUiModel.Product>) {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val productIds = productList.map(PlayProductUiModel.Product::id)
            repo.trackProducts(channelId, productIds)
        }) {}
    }

    /**
     * Updating channel status
     */
    private fun updateChannelStatus() {
        val currentStatus = _status.value.channelStatus.statusType
        if (currentStatus.isFreeze || currentStatus.isBanned) return

        viewModelScope.launchCatchError(dispatchers.io, block = {
            val channelStatus = repo.getChannelStatus(channelId)
            _status.update {
                it.copy(channelStatus = channelStatus)
            }
        }) {}
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
            BottomInsetsType.KebabMenuSheet -> hideKebabMenuSheet()
            BottomInsetsType.UserReportSheet -> hideUserReportSheet()
            BottomInsetsType.UserReportSubmissionSheet -> hideUserReportSubmissionSheet()
            BottomInsetsType.CouponSheet -> hideCouponSheet()
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
        playChannelWebSocket.connect(channelId, socketCredential.gcToken, WEB_SOCKET_SOURCE_PLAY_VIEWER)
    }

    private fun stopWebSocket() {
        socketJob?.cancel()
        playChannelWebSocket.close()
    }

    private fun stopInteractive() {
        _interactive.value = PlayInteractiveUiState.NoInteractive
    }

    private suspend fun getSocketCredential(): SocketCredential = try {
        withContext(dispatchers.io) {
            require(userSession.isLoggedIn)
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
                val isFollowing = if (userSession.isLoggedIn) {
                    repo.getIsFollowingPartner(partnerId = partnerInfo.id)
                } else false
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
                    if (userSession.isLoggedIn) {
                        repo.getIsLiked(contentId = likeInfo.contentId.toLong(), contentType = likeInfo.contentType)
                    } else false
                }

                try {
                    val report = deferredReportSummaries.await().data.first().channel.metrics
                    _channelReport.setValue {
                        copy(
                            totalViewFmt = report.totalViewFmt,
                            totalLike = report.totalLike.toLongOrZero(),
                            totalLikeFmt = report.totalLikeFmt
                        )
                    }
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

    private fun trackVisitChannel(channelId: String, shouldTrack: Boolean, sourceType: String) {
        if(shouldTrack) {
            viewModelScope.launchCatchError(dispatchers.io, block = {
                trackVisitChannelBroadcasterUseCase.setRequestParams(TrackVisitChannelBroadcasterUseCase.createParams(channelId, sourceType))
                trackVisitChannelBroadcasterUseCase.executeOnBackground()
            }) { }
        }
        _channelReport.setValue { copy(shouldTrack = true) }
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

    private suspend fun handleWebSocketMessage(
        message: WebSocketResponse,
        channelId: String
    ) = withContext(dispatchers.main) {
        val result = withContext(dispatchers.computation) {
            val socketMapper = PlaySocketMapper(message, gson)
            socketMapper.mapping()
        }

        when (result) {
            is TotalLike -> withContext(dispatchers.computation) likeContext@ {
                val (totalLike, totalLikeFmt) = playSocketToModelMapper.mapTotalLike(result)
                val prevLike = _channelReport.value.totalLike

                if (channelType.isLive && totalLike < prevLike) return@likeContext

                _channelReport.setValue {
                    copy(totalLike = totalLike, totalLikeFmt = totalLikeFmt)
                }

                if (!channelType.isLive || !isLikeBubbleEnabled) return@likeContext

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
                        appLink = mappedResult.appLink,
                        title = mappedResult.title,
                )
            }
            is QuickReply -> {
                _quickReply.value = playSocketToModelMapper.mapQuickReplies(result)
            }
            is BannedFreeze -> {
                if (result.channelId.isNotEmpty() && result.channelId.equals(channelId, true) && !statusType.isFreeze) {
                    _status.update {
                        it.copy(
                            channelStatus = it.channelStatus.copy(
                                statusType = playSocketToModelMapper.mapStatus(
                                    isBanned = result.isBanned && result.userId.isNotEmpty()
                                            && result.userId.equals(userSession.userId, true)),
                                statusSource = PlayStatusSource.Socket,
                            )
                        )
                    }

                    channelStateProcessor.setIsFreeze(result.isFreeze)
                }
            }
            is ProductTag -> {
                val (mappedProductTags, shouldShow) = playSocketToModelMapper.mapProductTag(result)
                _tagItems.update {
                    it.copy(
                        product = it.product.copy(
                            productList = mappedProductTags,
                            canShow = shouldShow
                        ),
                    )
                }

                sendProductTrackerToBro(
                    productList = mappedProductTags
                )
            }
            is MerchantVoucher -> {
                val mappedVouchers = playSocketToModelMapper.mapMerchantVoucher(result)

                _tagItems.update {
                    it.copy(
                        voucher = it.voucher.copy(
                            voucherList = mappedVouchers,
                        ),
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

        _partnerInfo.setValue { (copy(isLoadingFollow = true)) }

        viewModelScope.launchCatchError(block = {
            val isFollowing = repo.postFollowStatus(
                    shopId = shopId.toString(),
                    followAction = followAction,
            )
            _partnerInfo.setValue {
                copy(isLoadingFollow = false, status = PlayPartnerFollowStatus.Followable(isFollowing))
            }
        }) {
            _partnerInfo.setValue { (copy(isLoadingFollow = false)) }
            _uiEvent.emit(ShowErrorEvent(it))
        }

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

    private fun handleOpenPageResult(isSuccess: Boolean, requestCode: Int) {
        if (!isSuccess) return
        when (requestCode) {
            REQUEST_CODE_LOGIN_FOLLOW -> handleClickFollow(isFromLogin = true)
            REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE -> handleClickFollowInteractive()
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
                if (isLikeBubbleEnabled) {
                    _uiEvent.emit(ShowLikeBubbleEvent.Single(
                        count = 1,
                        reduceOpacity = false,
                        config = _likeInfo.value.likeBubbleConfig,
                    ))
                }
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

            viewModelScope.launchCatchError(block = {
                repo.postLike(
                    contentId = likeInfo.contentId.toLongOrZero(),
                    contentType = likeInfo.contentType,
                    likeType = likeInfo.likeType,
                    shouldLike = newStatus == PlayLikeStatus.Liked
                )
            }) { }

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

    private suspend fun copyLink() {
        val shareInfo = _channelDetail.value.shareInfo

        _uiEvent.emit(
            CopyToClipboardEvent(shareInfo.content)
        )

        _uiEvent.emit(
            ShowInfoEvent(
                UiString.Resource(R.string.play_link_copied)
            )
        )
    }

    private fun handleRefreshLeaderboard() {
        if(_leaderboardUserBadgeState.value.shouldRefreshData) {
            _leaderboardInfo.value = PlayLeaderboardWrapperUiModel.Loading
            _leaderboardUserBadgeState.setValue { copy(shouldRefreshData = false) }
        }

        checkLeaderboard(channelId)
    }

    private fun handleRetryGetTagItems() {
        updateTagItems()
    }

    private fun handleCopyLink() {
        viewModelScope.launch { copyLink() }
    }

    private fun handleClickShareIcon() {
        viewModelScope.launch {
            playAnalytic.clickShareButton(channelId, partnerId, channelType.value)

            _uiEvent.emit(
                SaveTemporarySharingImage(imageUrl = _channelDetail.value.channelInfo.coverUrl)
            )
        }
    }

    private fun handleOpenSharingOption(isScreenshot: Boolean) {
        viewModelScope.launch {
            if(isScreenshot)
                playAnalytic.takeScreenshotForSharing(channelId, partnerId, channelType.value)

            if(playShareExperience.isCustomSharingAllow()) {
                playAnalytic.impressShareBottomSheet(channelId, partnerId, channelType.value)

                _uiEvent.emit(OpenSharingOptionEvent(
                    title = _channelDetail.value.channelInfo.title,
                    coverUrl = _channelDetail.value.channelInfo.coverUrl,
                    userId = userId,
                    channelId = channelId
                ))
            }
            else if(!isScreenshot) {
                copyLink()
            }
        }
    }

    private fun handleCloseSharingOption() {
        playAnalytic.closeShareBottomSheet(channelId, partnerId, channelType.value, playShareExperience.isScreenshotBottomSheet())
    }

    private fun handleSharingOption(shareModel: ShareModel) {
        viewModelScope.launch {
            playAnalytic.clickSharingOption(channelId, partnerId, channelType.value, shareModel.channel, playShareExperience.isScreenshotBottomSheet())

            val playShareExperienceData = getPlayShareExperienceData()

            playShareExperience
                .setShareModel(shareModel)
                .setData(playShareExperienceData)
                .createUrl(object: PlayShareExperience.Listener {
                    override fun onUrlCreated(
                        linkerShareData: LinkerShareResult?,
                        shareModel: ShareModel,
                        shareString: String
                    ) {
                        viewModelScope.launch {
                            _uiEvent.emit(CloseShareExperienceBottomSheet)
                            _uiEvent.emit(
                                OpenSelectedSharingOptionEvent(
                                    linkerShareData,
                                    shareModel,
                                    shareString
                                )
                            )
                        }
                    }

                    override fun onError(e: Exception) {
                        viewModelScope.launch {
                            _uiEvent.emit(CloseShareExperienceBottomSheet)
                            _uiEvent.emit(ErrorGenerateShareLink)
                        }
                    }
                }
            )
        }
    }

    private fun handleSharePermission(label: String) {
        playAnalytic.clickSharePermission(channelId, partnerId, channelType.value, label)
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

    private fun getPlayShareExperienceData(): PlayShareExperienceData {
        val (channelInfo, shareInfo) = _channelDetail.let {
            return@let Pair(it.value.channelInfo, it.value.shareInfo)
        }

        return PlayShareExperienceData(
            id = channelId,
            title = channelInfo.title,
            partnerName = _partnerInfo.value.name,
            coverUrl = channelInfo.coverUrl,
            redirectUrl = shareInfo.redirectUrl,
            textDescription = shareInfo.textDescription,
            metaTitle = shareInfo.metaTitle,
            metaDescription = shareInfo.metaDescription,
        )
    }

    companion object {
        private const val FIREBASE_REMOTE_CONFIG_KEY_PIP = "android_mainapp_enable_pip"
        private const val FIREBASE_REMOTE_CONFIG_KEY_INTERACTIVE = "android_main_app_enable_play_interactive"
        private const val FIREBASE_REMOTE_CONFIG_KEY_CAST = "android_main_app_enable_play_cast"
        private const val FIREBASE_REMOTE_CONFIG_KEY_LIKE_BUBBLE = "android_main_app_enable_play_bubbles"
        private const val ONBOARDING_DELAY = 5000L
        private const val INTERACTIVE_FINISH_MESSAGE_DELAY = 2000L

        private const val LIKE_BURST_THRESHOLD = 30

        /**
         * Request Code When need login
         */
        private const val REQUEST_CODE_LOGIN_FOLLOW = 571
        private const val REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE = 572
        private const val REQUEST_CODE_LOGIN_LIKE = 573

        private const val WEB_SOCKET_SOURCE_PLAY_VIEWER = "Viewer"
    }
}