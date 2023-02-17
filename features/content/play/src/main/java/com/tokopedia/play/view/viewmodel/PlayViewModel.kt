package com.tokopedia.play.view.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.gms.cast.framework.CastStateListener
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CastPlayerHelper
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateListener
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.chat.ChatManager
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play.util.logger.PlayLog
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
import com.tokopedia.play.view.uimodel.recom.interactive.InteractiveStateUiModel
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VariantUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play.view.uimodel.state.*
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.sse.*
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketClosedReason
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
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
    private val getReportSummariesUseCase: GetReportSummariesUseCase,
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
    private val playLog: PlayLog,
    chatManagerFactory: ChatManager.Factory,
    chatStreamsFactory: ChatStreams.Factory,
    private val liveRoomMetricsCommon: PlayLiveRoomMetricsCommon
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(channelId: String): PlayViewModel
    }

    private val jobMap = mutableMapOf<String, Job>()

    val observableChannelInfo: LiveData<PlayChannelInfoUiModel> /**Added**/
    get() = _observableChannelInfo
    val observableVideoMeta: LiveData<PlayVideoMetaInfoUiModel> /**Changed**/
    get() = _observableVideoMeta
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
    val observableKebabMenuSheet: LiveData<Map<KebabMenuType, BottomInsetsState>>
        get() = _observableKebabSheets

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

    private var _winnerStatus: MutableStateFlow<PlayUserWinnerStatusUiModel?> = MutableStateFlow(null)

    /**
     * Remote Config for Explore Widget
     */
    private val isExploreWidget: Boolean
        get() = remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_EXPLORE_WIDGET, true)

    /***
     * User Report
     */
    private val _userReportItems = MutableStateFlow(PlayUserReportUiModel.Empty)
    val userReportItems: StateFlow<PlayUserReportUiModel.Loaded> = _userReportItems

    private val _userReportSubmission = MutableStateFlow<ResultState>(ResultState.Loading)
    val userReportSubmission: StateFlow<ResultState> = _userReportSubmission

    /**
     * Data State
     */
    private val _channelDetail = MutableStateFlow(PlayChannelDetailUiModel())
    private val _partnerInfo = MutableStateFlow(PlayPartnerInfo())
    private val _bottomInsets = MutableStateFlow(emptyMap<BottomInsetsType, BottomInsetsState>())
    private val _status = MutableStateFlow(PlayStatusUiModel.Empty)
    private val _interactive = MutableStateFlow(InteractiveStateUiModel.Empty)
    private val _leaderboard = MutableStateFlow(LeaderboardUiModel.Empty)
    private val _leaderboardUserBadgeState = MutableStateFlow(PlayLeaderboardBadgeUiState())
    private val _likeInfo = MutableStateFlow(PlayLikeInfoUiModel())
    private val _channelReport = MutableStateFlow(PlayChannelReportUiModel())
    private val _tagItems = MutableStateFlow(TagItemUiModel.Empty)
    private val _quickReply = MutableStateFlow(PlayQuickReplyInfoUiModel.Empty)
    private val _selectedVariant = MutableStateFlow<NetworkResult<VariantUiModel>>(
        NetworkResult.Loading
    )
    private val _loadingBuy = MutableStateFlow(false)
    private val _autoOpenInteractive = MutableStateFlow(false)
    private val _warehouseInfo = MutableStateFlow(WarehouseInfoUiModel.Empty)

    /** Needed to decide whether we need to call setResult() or no when leaving play room */
    private val _isChannelReportLoaded = MutableStateFlow(false)
    private val _exploreWidget = MutableStateFlow(ExploreWidgetUiModel.Empty)

    private val _isFollowPopUpShown = MutableStateFlow(FollowPopUpUiState.Empty)

    private val _videoProperty = MutableStateFlow(VideoPropertyUiModel.Empty)

    private val _isBottomSheetsShown = MutableStateFlow(false)

    private val _followPopUpUiState = combine(_bottomInsets, _isFollowPopUpShown, _partnerInfo, _interactive, _videoProperty, _isBottomSheetsShown) {
            bottomInsets, popUp, partner, interactive, videoState, bottomSheets ->
        !bottomInsets.isAnyShown && popUp.shouldShow && partner.needFollow && partner.id == popUp.partnerId && !interactive.isPlaying && (!videoState.state.hasNoData || videoPlayer.isYouTube) && !bottomSheets
    }.flowOn(dispatchers.computation)

    private val _winnerBadgeUiState = combine(
        _leaderboard,
        _bottomInsets,
        _status,
        _channelDetail,
        _leaderboardUserBadgeState
    ) { leaderboard, bottomInsets, status, channelDetail, leaderboardUserBadgeState ->
        PlayWinnerBadgeUiState(
            leaderboards = leaderboard,
            shouldShow = !bottomInsets.isAnyShown &&
                status.channelStatus.statusType.isActive &&
                leaderboardUserBadgeState.showLeaderboard &&
                channelDetail.channelInfo.channelType.isLive
        )
    }.flowOn(dispatchers.computation)

    private val _likeUiState = combine(
        _likeInfo,
        _channelDetail,
        _bottomInsets,
        _status,
        _channelReport
    ) { likeInfo, channelDetail, bottomInsets, status, channelReport ->
        PlayLikeUiState(
            shouldShow = !bottomInsets.isAnyShown && status.channelStatus.statusType.isActive,
            canLike = likeInfo.status != PlayLikeStatus.Unknown,
            totalLike = channelReport.totalLikeFmt,
            likeMode = if (channelDetail.channelInfo.channelType.isLive) {
                PlayLikeMode.Multiple
            } else {
                PlayLikeMode.Single
            },
            isLiked = likeInfo.status == PlayLikeStatus.Liked,
            canShowBubble = !bottomInsets.isAnyShown &&
                channelDetail.channelInfo.channelType.isLive &&
                status.channelStatus.statusType.isActive
        )
    }.flowOn(dispatchers.computation)

    private val _totalViewUiState = _channelReport.map {
        PlayTotalViewUiState(it.totalViewFmt)
    }.flowOn(dispatchers.computation)

    private val _rtnUiState = combine(
        _channelDetail,
        _bottomInsets,
        _status
    ) { channelDetail, bottomInsets, status ->
        PlayRtnUiState(
            shouldShow = channelType.isLive &&
                !bottomInsets.isAnyShown &&
                status.channelStatus.statusType.isActive &&
                !channelDetail.videoInfo.orientation.isHorizontal &&
                !videoPlayer.isYouTube,
            lifespanInMs = channelDetail.rtnConfigInfo.lifespan
        )
    }.flowOn(dispatchers.computation)

    private val _titleUiState = _channelDetail.map {
        PlayTitleUiState(
            title = it.channelInfo.title
        )
    }.flowOn(dispatchers.computation)

    private val _addressUiState = combine(_partnerInfo, _warehouseInfo, _bottomInsets) { partnerInfo, warehouseInfo, bottomInset ->
        AddressWidgetUiState(
            warehouseInfo = warehouseInfo,
            shouldShow = partnerInfo.type == PartnerType.TokoNow && warehouseInfo.isOOC && (channelType.isLive || channelType.isVod) && !isFreezeOrBanned && !bottomInset.isAnyShown
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val _engagementUiState = combine(_tagItems, _interactive, _bottomInsets, _status) {
            voucher, game, bottomInsets, status ->
        EngagementUiState(
            shouldShow = (voucher.voucher.voucherList.isNotEmpty() || game.game !is GameUiModel.Unknown) && !game.isPlaying && !bottomInsets.isAnyShown && videoOrientation.isVertical && videoPlayer.isGeneral() && status.channelStatus.statusType.isActive,
            data = buildList {
                val vouchers = voucher.voucher.voucherList.filterIsInstance<PlayVoucherUiModel.Merchant>()
                if (game.game !is GameUiModel.Unknown && isInteractiveAllowed) {
                    add(EngagementUiModel.Game(game = game.game))
                }
                if (vouchers.isNotEmpty() && vouchers.firstOrNull { it.highlighted } != null) {
                    add(EngagementUiModel.Promo(info = vouchers.first { it.highlighted }, size = vouchers.size - 1))
                }
            }
        )
    }.flowOn(dispatchers.computation)

    private val _featuredProducts = _tagItems.map { tagItems ->
        /**
         * Get first found Pinned Product if any
         */
        var pinnedProduct: PlayProductUiModel.Product? = null
        run {
            tagItems.product.productSectionList.forEach { section ->
                if (section is ProductSectionUiModel.Section) {
                    pinnedProduct = section.productList.firstOrNull { it.isPinned }
                    if (pinnedProduct != null) return@run
                }
            }
        }

        /**
         * Get original featured products including the pinned one
         */
        val products = mutableListOf<PlayProductUiModel.Product>()
        tagItems.product.productSectionList.forEach { section ->
            if (section is ProductSectionUiModel.Section) {
                products.addAll(
                    section.productList.take(
                        (tagItems.maxFeatured - products.size).coerceAtLeast(0)
                    )
                )
            }
            if (products.size >= tagItems.maxFeatured) return@forEach
        }

        /**
         * Move pinned product to the first index if any
         */
        pinnedProduct?.let { pinned ->
            listOf(pinned) + products.filterNot { it.isPinned }
        } ?: products
    }.flowOn(dispatchers.computation)

    private val _explore = combine(_status, _bottomInsets, _exploreWidget) {
            status, bottomInsets, widgets ->
        ExploreWidgetUiState(
            shouldShow = !bottomInsets.isAnyShown &&
                status.channelStatus.statusType.isActive &&
                !videoPlayer.isYouTube && isExploreWidget,
            data = widgets
        )
    }.flowOn(dispatchers.computation)

    /**
     * Until repeatOnLifecycle is available (by updating library version),
     * this can be used as an alternative to "complete" un-completable flow when page is not focused
     */
    private val isActive: AtomicBoolean = AtomicBoolean(false)

    val uiState: StateFlow<PlayViewerNewUiState> = combine(
        _channelDetail,
        _interactive,
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
        _selectedVariant,
        _loadingBuy,
        _addressUiState,
        _featuredProducts.distinctUntilChanged(),
        _engagementUiState,
        _followPopUpUiState,
        _explore.distinctUntilChanged()
    ) { channelDetail, interactive, partner, winnerBadge, bottomInsets,
        like, totalView, rtn, title, tagItems,
        status, quickReply, selectedVariant, isLoadingBuy, address,
        featuredProducts, engagement, followPopUp, explore ->
        PlayViewerNewUiState(
            channel = channelDetail,
            interactive = interactive,
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
            selectedVariant = selectedVariant,
            isLoadingBuy = isLoadingBuy,
            address = address,
            featuredProducts = featuredProducts,
            engagement = engagement,
            followPopUp = followPopUp,
            exploreWidget = explore
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(SUBSCRIBE_AWAY_THRESHOLD),
        PlayViewerNewUiState.Empty
    )

    val uiEvent: Flow<PlayViewerNewUiEvent>
        get() = _uiEvent.filter {
            isActive.get() || it is AllowedWhenInactiveEvent
        }.map { if (it is AllowedWhenInactiveEvent) it.event else it }
            .flowOn(dispatchers.computation)

    private val chatManager = chatManagerFactory.create(
        chatStreamsFactory.create(viewModelScope)
    )

    val chats: StateFlow<List<PlayChatUiModel>>
        get() = chatManager.chats

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

    val hasNoMedia: Boolean
        get() = _videoProperty.value.state.hasNoData

    val isAnyBottomSheetsShown: Boolean
        get() = bottomInsets.isAnyShown || _isBottomSheetsShown.value || _isFollowPopUpShown.value.shouldShow

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

    val performanceSummaryPageLink: String
        get() = _channelReport.value.performanceSummaryPageLink

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
                    pinnedMessage = pinnedMessage
                ),
                quickReplyInfo = _quickReply.value,
                videoMetaInfo = newVideoMeta,
                status = _status.value,
                leaderboard = _leaderboard.value,
                tagItems = _tagItems.value,
                upcomingInfo = channelData.upcomingInfo
            )
        }

    val pipState: PiPState
        get() = _observableEventPiPState.value?.peekContent() ?: PiPState.Stop

    val isPiPAllowed: Boolean
        get() {
            return remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_PIP, true) &&
                !videoPlayer.isYouTube && !videoPlayer.isCasting()
        }

    val userId: String
        get() = userSession.userId

    val isCastAllowed: Boolean
        get() {
            val castState = observableCastState.value ?: return false
            return (
                castState.currentState != PlayCastState.NO_DEVICE_AVAILABLE && !videoPlayer.isYouTube &&
                    remoteConfig.getBoolean(FIREBASE_REMOTE_CONFIG_KEY_CAST, true)
                ) || castState.currentState == PlayCastState.CONNECTED
        }

    val gameData: GameUiModel
        get() = _interactive.value.game

    val isTotalViewLoaded: Boolean
        get() = _isChannelReportLoaded.value

    val partnerType: String
        get() = mChannelData?.partnerInfo?.type?.value ?: ""

    private var socketJob: Job? = null

    // Explore Widget
    val selectedChips: String
        get() = _exploreWidget.value.chips.items.find { it.isSelected }?.text ?: ""

    val exploreWidgetConfig: PlayWidgetConfigUiModel
        get() = _exploreWidget.value.widgets.firstOrNull()?.item?.config ?: PlayWidgetConfigUiModel.Empty

    private val _observableChannelInfo = MutableLiveData<PlayChannelInfoUiModel>()
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()

    /**Added StateFlow*/
    private val _observableVideoMeta = MutableLiveData<PlayVideoMetaInfoUiModel>()

    /**Changed**/
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observableKebabSheets = MutableLiveData<Map<KebabMenuType, BottomInsetsState>>()
    private val _observableEventPiPState = MutableLiveData<Event<PiPState>>()
    private val _observableOnboarding = MutableLiveData<Event<Unit>>()

    /**Added**/
    private val _observableCastState = MutableLiveData<PlayCastUiModel>()
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(_observableBottomInsetsState) { insets ->
            _bottomInsets.value = insets

            viewModelScope.launch {
                if (insets.isAnyShown) _uiEvent.emit(HideCoachMarkWinnerEvent)
            }
        }
    }

    private val _observableKolId = MutableLiveData<String>()

    //region helper
    private val hasWordsOrDotsRegex = Regex("(\\.+|[a-z]+)")
    private val amountStringStepArray = arrayOf("k", "m")
    private fun String.trimMultipleNewlines() = trim().replace(Regex("(\\n+)"), "\n")
    //endregion

    private val videoStateListener = object : PlayViewerVideoStateListener {
        override fun onStateChanged(state: PlayViewerVideoState) {
            viewModelScope.launch(dispatchers.immediate) {
                val newState = VideoPropertyUiModel(state)
                _observableVideoProperty.value = newState
                _videoProperty.update { newState }
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
                                statusType = PlayStatusType.Freeze
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
            if (videoLatencyPerformanceMonitoring.hasStarted) {
                videoLatencyPerformanceMonitoring.stop()
                val durationInSecond = videoLatencyPerformanceMonitoring.totalDuration / DURATION_DIVIDER
                playLog.logTimeToFirstByte(durationInSecond.toLong())
            }
        }

        override fun onError() {
            videoLatencyPerformanceMonitoring.reset()
        }
    }

    private val playVideoPlayer = playVideoBuilder.build()

    /**
     * Interactive
     */
    private val tapGiveawayFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 5)

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

    private var delayTapJob: Job? = null

    init {
        videoStateProcessor.addStateListener(videoStateListener)
        videoStateProcessor.addStateListener(videoPerformanceListener)
        channelStateProcessor.addStateListener(channelStateListener)
        videoBufferGovernor.startBufferGovernance()

        stateHandler.observeForever(stateHandlerObserver)

        viewModelScope.launch {
            tapGiveawayFlow.collect(::onReceivedTapGiveawayAction)
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
            if (isLive) {
                BottomInsetsState.Shown(
                    estimatedInsetsHeight = estimatedKeyboardHeight,
                    isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isShown == true
                )
            } else {
                BottomInsetsState.Hidden(
                    isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isHidden == true
                )
            }

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

    fun onShowVariantSheet(estimatedProductSheetHeight: Int) {
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

    /***
     * Estimated sheet is always 0 bcz height is set based on wrap_content
     */
    fun onShowKebabMenuSheet(estimatedSheetHeight: Int = 0) {
        val insetsMap = getLatestKebabBottomInset().toMutableMap()

        insetsMap[KebabMenuType.ThreeDots] =
            BottomInsetsState.Shown(
                estimatedInsetsHeight = estimatedSheetHeight,
                isPreviousStateSame = insetsMap[KebabMenuType.ThreeDots]?.isShown == true
            )

        _observableKebabSheets.value = insetsMap
    }

    fun hideKebabMenuSheet() {
        val insetsMap = getLatestKebabBottomInset().toMutableMap()

        insetsMap[KebabMenuType.ThreeDots] =
            BottomInsetsState.Hidden(
                isPreviousStateSame = insetsMap[KebabMenuType.ThreeDots]?.isHidden == true
            )

        _observableKebabSheets.value = insetsMap
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

    fun onShowUserReportSheet(estimatedSheetHeight: Int = 0) {
        val insetsMap = getLatestKebabBottomInset().toMutableMap()

        insetsMap[KebabMenuType.UserReportList] =
            BottomInsetsState.Shown(
                estimatedInsetsHeight = estimatedSheetHeight,
                isPreviousStateSame = insetsMap[KebabMenuType.UserReportList]?.isShown == true
            )

        _observableKebabSheets.value = insetsMap
    }

    fun hideUserReportSheet() {
        val insetsMap = getLatestKebabBottomInset().toMutableMap()

        insetsMap[KebabMenuType.UserReportList] =
            BottomInsetsState.Hidden(
                isPreviousStateSame = insetsMap[KebabMenuType.UserReportList]?.isHidden == true
            )

        _observableKebabSheets.value = insetsMap
    }

    fun onShowUserReportSubmissionSheet(estimatedSheetHeight: Int = 0) {
        val insetsMap = getLatestKebabBottomInset().toMutableMap()

        insetsMap[KebabMenuType.UserReportSubmission] =
            BottomInsetsState.Shown(
                estimatedInsetsHeight = estimatedSheetHeight,
                isPreviousStateSame = insetsMap[KebabMenuType.UserReportSubmission]?.isShown == true
            )

        _observableKebabSheets.value = insetsMap
    }

    fun hideUserReportSubmissionSheet() {
        val insetsMap = getLatestKebabBottomInset().toMutableMap()

        insetsMap[KebabMenuType.UserReportSubmission] =
            BottomInsetsState.Hidden(
                isPreviousStateSame = insetsMap[KebabMenuType.UserReportSubmission]?.isHidden == true
            )

        _observableKebabSheets.value = insetsMap
    }

    fun hideInsets(isKeyboardHandled: Boolean) {
        val defaultBottomInsets = getDefaultBottomInsetsMapState()
        _observableBottomInsetsState.value = if (isKeyboardHandled) {
            defaultBottomInsets.toMutableMap().apply {
                this[BottomInsetsType.Keyboard] = BottomInsetsState.Hidden(true)
            }
        } else {
            defaultBottomInsets
        }
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

    private fun getLatestKebabBottomInset(): Map<KebabMenuType, BottomInsetsState> {
        val currentValue = _observableKebabSheets.value ?: return getDefaultKebabInsets()
        currentValue.values.forEach {
            it.isPreviousStateSame = true
            if (it is BottomInsetsState.Shown) it.deepLevel += 1
        }
        return currentValue
    }

    fun hideThreeDotsSheet() {
        _observableKebabSheets.value = getDefaultKebabInsets()
        _isBottomSheetsShown.update { false }
    }

    private fun getDefaultKebabInsets(): Map<KebabMenuType, BottomInsetsState> {
        val currentValue = _observableKebabSheets.value
        val defaultUserReportListState = currentValue?.get(KebabMenuType.UserReportList)?.isHidden ?: true
        val defaultUserReportSubmissionState = currentValue?.get(KebabMenuType.UserReportSubmission)?.isHidden ?: true

        /**Make sure the default state for kebab menu is threedots must need to be shown. If you have any feedback please comment
         * */
        return mapOf(
            KebabMenuType.ThreeDots to BottomInsetsState.Shown(100, false),
            KebabMenuType.UserReportList to BottomInsetsState.Hidden(defaultUserReportListState),
            KebabMenuType.UserReportSubmission to BottomInsetsState.Hidden(defaultUserReportSubmissionState)
        )
    }

    private fun getDefaultBottomInsetsMapState(): Map<BottomInsetsType, BottomInsetsState> {
        val currentBottomInsetsMap = _observableBottomInsetsState.value
        val defaultKeyboardState = currentBottomInsetsMap?.get(BottomInsetsType.Keyboard)?.isHidden ?: true
        val defaultProductSheetState = currentBottomInsetsMap?.get(BottomInsetsType.ProductSheet)?.isHidden ?: true
        val defaultVariantSheetState = currentBottomInsetsMap?.get(BottomInsetsType.VariantSheet)?.isHidden ?: true
        val defaultLeaderboardSheetState = currentBottomInsetsMap?.get(BottomInsetsType.LeaderboardSheet)?.isHidden ?: true
        val defaultCouponSheetState = currentBottomInsetsMap?.get(BottomInsetsType.CouponSheet)?.isHidden ?: true
        return mapOf(
            BottomInsetsType.Keyboard to BottomInsetsState.Hidden(defaultKeyboardState),
            BottomInsetsType.ProductSheet to BottomInsetsState.Hidden(defaultProductSheetState),
            BottomInsetsType.VariantSheet to BottomInsetsState.Hidden(defaultVariantSheetState),
            BottomInsetsType.LeaderboardSheet to BottomInsetsState.Hidden(defaultLeaderboardSheetState),
            BottomInsetsType.CouponSheet to BottomInsetsState.Hidden(defaultCouponSheetState)
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

            PlayViewerNewAction.GiveawayUpcomingEnded -> handleGiveawayUpcomingEnded()
            PlayViewerNewAction.GiveawayOngoingEnded -> handleGiveawayOngoingEnded()
            PlayViewerNewAction.TapGiveaway -> handleTapGiveaway()
            PlayViewerNewAction.QuizEnded -> handleQuizEnded()
            PlayViewerNewAction.StartPlayingInteractive -> handlePlayingInteractive(shouldPlay = true)
            PlayViewerNewAction.StopPlayingInteractive -> handlePlayingInteractive(shouldPlay = false)
            is PlayViewerNewAction.ClickQuizOptionAction -> handleClickQuizOption(action.item)
            is PlayViewerNewAction.BuyProduct -> handleBuyProduct(
                product = action.product,
                action = ProductAction.Buy,
                isProductFeatured = action.isProductFeatured
            )
            is PlayViewerNewAction.AtcProduct -> handleBuyProduct(
                product = action.product,
                action = ProductAction.AddToCart,
                isProductFeatured = action.isProductFeatured
            )
            is PlayViewerNewAction.OCCProduct -> handleBuyProduct(
                product = action.product,
                action = ProductAction.OCC,
                isProductFeatured = action.isProductFeatured
            )

            is InteractiveWinnerBadgeClickedAction -> handleWinnerBadgeClicked(action.height)
            is InteractiveGameResultBadgeClickedAction -> showLeaderboardSheet(action.height)
            ClickCloseLeaderboardSheetAction -> handleCloseLeaderboardSheet()
            PlayViewerNewAction.Follow -> handleClickFollow(isFromLogin = false)
            PlayViewerNewAction.FollowInteractive -> handleClickFollowInteractive()
            is ClickPartnerNameAction -> handleClickPartnerName(action.appLink)
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
            OpenKebabAction -> handleThreeDotsMenuClick()
            is OpenFooterUserReport -> handleFooterClick(action.appLink)
            OpenUserReport -> handleUserReport()
            is SendUpcomingReminder -> handleSendReminder(action.section)

            is BuyProductAction -> handleBuyProduct(
                action.sectionInfo,
                action.product,
                ProductAction.Buy,
                isProductFeatured = false
            )
            is BuyProductVariantAction -> handleBuyProductVariant(action.id, ProductAction.Buy)
            is AtcProductAction -> handleBuyProduct(
                action.sectionInfo,
                action.product,
                ProductAction.AddToCart,
                isProductFeatured = false
            )
            is OCCProductAction -> handleBuyProduct(
                action.sectionInfo,
                action.product,
                ProductAction.OCC,
                isProductFeatured = false
            )
            is AtcProductVariantAction -> handleBuyProductVariant(action.id, ProductAction.AddToCart)
            is OCCProductVariantAction -> handleBuyProductVariant(action.id, ProductAction.OCC)
            is SelectVariantOptionAction -> handleSelectVariantOption(action.option)
            PlayViewerNewAction.AutoOpenInteractive -> handleAutoOpen()
            is SendWarehouseId -> handleWarehouse(action.id, action.isOOC)
            OpenCart -> openWithLogin(ApplinkConstInternalMarketplace.CART, REQUEST_CODE_LOGIN_CART)
            DismissFollowPopUp -> _isFollowPopUpShown.update { it.copy(shouldShow = false) }
            FetchWidgets -> {
                _isBottomSheetsShown.update { true }
                fetchWidgets()
            }
            is ClickChipWidget -> handleClickChip(action.item)
            NextPageWidgets -> onActionWidget(isNextPage = true)
            RefreshWidget -> onActionWidget(isNextPage = false)
            is UpdateReminder -> updateReminderWidget(action.channelId, action.reminderType)
            DismissExploreWidget -> {
                // Resetting
                _exploreWidget.update {
                    it.copy(widgets = emptyList(), chips = TabMenuUiModel.Empty)
                }
                _channelDetail.value.exploreWidgetConfig.let {
                    updateWidgetParam(group = it.group, sourceId = it.sourceId, sourceType = it.sourceType)
                }
                _isBottomSheetsShown.update { false }
            }
            EmptyPageWidget -> handleEmptyExplore()
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
        if (playVideoPlayer.isVideoLive() || channelType.isLive || isFreezeOrBanned) {
            playVideoPlayer.release()
        } else {
            playVideoPlayer.stop()
        }
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
        handleLeaderboardInfo(channelData.leaderboard)

        _partnerInfo.value = channelData.partnerInfo
        _status.value = channelData.status
        _tagItems.value = channelData.tagItems
        _quickReply.value = channelData.quickReplyInfo

        with(channelData.channelDetail.exploreWidgetConfig) { updateWidgetParam(group, sourceType, sourceId) }
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
        updateLiveChannelChatHistory(channelData)
        updateChannelInfo(channelData)
        sendInitialLog()
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

        resetChannelReportLoadedStatus()
        cancelJob(FOLLOW_POP_UP_ID)
        cancelJob(ONBOARDING_COACHMARK_ID)
    }

    private fun focusVideoPlayer(channelData: PlayChannelData) {
        fun loadCast() {
            if (channelData.status.channelStatus.statusType.isFreeze) return

            playVideoPlayer.stop(resetState = false)
            playVideoPlayer.removeListener(videoManagerListener)

            val videoStream = channelData.videoMetaInfo.videoStream
            if (mChannelData?.id.toString() != castPlayerHelper.getCurrentMediaChannelId()) {
                castPlayerHelper.castPlay(
                    channelId = channelData.id,
                    title = videoStream.title,
                    partnerName = channelData.partnerInfo.name,
                    coverUrl = channelData.channelDetail.channelInfo.coverUrl,
                    videoUrl = if (channelData.videoMetaInfo.videoPlayer.isGeneral()) {
                        channelData.videoMetaInfo.videoPlayer.params.videoUrl
                    } else {
                        ""
                    },
                    currentPosition = max(playVideoPlayer.getCurrentPosition(), 0)
                )
            }

            if (channelData.videoMetaInfo.videoPlayer.isGeneral()) {
                castPlayerHelper.player?.let {
                    _observableVideoMeta.value = channelData.videoMetaInfo.copy(
                        videoPlayer = channelData.videoMetaInfo.videoPlayer.setPlayer(it, channelData.channelDetail.channelInfo.coverUrl)
                    )
                }
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

        if (castPlayerHelper.hasAvailableSession) {
            loadCast()
        } else {
            loadPlayer()
        }
    }

    private fun defocusVideoPlayer(shouldPauseVideo: Boolean) {
        if (shouldPauseVideo) {
            if (playVideoPlayer.isVideoLive() || viewerVideoState.hasNoData) {
                playVideoPlayer.stop()
            } else {
                playVideoPlayer.pause(preventLoadingBuffer = true)
            }
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
        updatePartnerInfo(channelData)
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
            val warehouseId = _warehouseInfo.value.warehouseId
            val tagItem = repo.getTagItem(channelId, warehouseId, _partnerInfo.value.name)

            _tagItems.update {
                tagItem
            }

            sendProductTrackerToBro(
                productList = tagItem.product.productSectionList
                    .filterIsInstance<ProductSectionUiModel.Section>()
                    .flatMap { it.productList }
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

    /**
     * Updating chat history for live channel only
     */
    private fun updateLiveChannelChatHistory(channelData: PlayChannelData) {
        viewModelScope.launchCatchError(block = {
            if (channelData.channelDetail.channelInfo.channelType.isLive) {
                chatManager.setWaitingForHistory()
                val response = repo.getChatHistory(channelId)
                chatManager.addHistoryChat(response.chatList.reversed())
            }
        }) { }
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
                        image = userSession.profilePicture
                    )
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
                socketCredential = socketCredential,
                warehouseId = _warehouseInfo.value.warehouseId
            )

            playChannelWebSocket.listenAsFlow()
                .collect {
                    handleWebSocketResponse(it, channelId, socketCredential)
                }
        }
    }

    private fun connectWebSocket(channelId: String, warehouseId: String, socketCredential: SocketCredential) {
        playChannelWebSocket.connect(channelId, warehouseId, socketCredential.gcToken, WEB_SOCKET_SOURCE_PLAY_VIEWER)
    }

    private fun stopWebSocket() {
        socketJob?.cancel()
        playChannelWebSocket.close()
    }

    private fun stopInteractive() {
        _interactive.value = InteractiveStateUiModel.Empty
    }

    private suspend fun getSocketCredential(): SocketCredential = try {
        withContext(dispatchers.io) {
            require(userSession.isLoggedIn)
            return@withContext repo.getSocketCredential()
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
        if (videoMetaInfo.videoPlayer.isYouTube) return
        cancelJob(ONBOARDING_COACHMARK_ID)

        jobMap[ONBOARDING_COACHMARK_ID] = viewModelScope.launch(dispatchers.computation) {
            delay(ONBOARDING_DELAY)

            withContext(dispatchers.main) {
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

    private fun handleLeaderboardInfo(leaderboard: LeaderboardUiModel) {
        _leaderboard.value = leaderboard
        _leaderboard.value = LeaderboardUiModel(
            data = leaderboard.data,
            state = ResultState.Success
        )
        setLeaderboardBadgeState(leaderboard.data)
    }

    /**
     * Update channel data
     */

    private fun updatePartnerInfo(channelData: PlayChannelData) {
        val partnerInfo = channelData.partnerInfo
        val isNeedToBeShown = if (userSession.isLoggedIn) partnerInfo.id.toString() != userSession.shopId && partnerInfo.id.toString() != userSession.userId else true
        if (partnerInfo.status !is PlayPartnerFollowStatus.NotFollowable && isNeedToBeShown) {
            viewModelScope.launchCatchError(block = {
                val isFollowing = getFollowingStatus(partnerInfo)

                val result = if (isFollowing) PartnerFollowableStatus.Followed else PartnerFollowableStatus.NotFollowed
                _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(result)) }
            }, onError = {}).invokeOnCompletion {
                handleFollowPopUp(channelData)
            }
        } else {
            _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.NotFollowable) }
        }
    }

    private suspend fun getFollowingStatus(partnerInfo: PlayPartnerInfo): Boolean {
        return if (userSession.isLoggedIn) {
            when (partnerInfo.type) {
                PartnerType.Shop -> repo.getIsFollowingPartner(partnerId = partnerInfo.id)
                PartnerType.Buyer -> {
                    val data = repo.getFollowingKOL(partnerInfo.id.toString())
                    _observableKolId.value = data.second
                    data.first
                }
                else -> false
            }
        } else {
            false
        }
    }

    private fun updateVideoMetaInfo(videoMetaInfo: PlayVideoMetaInfoUiModel) {
        when (videoMetaInfo.videoPlayer) {
            is PlayVideoPlayerUiModel.General.Incomplete -> {
                if (!castPlayerHelper.hasAvailableSession) {
                    playGeneralVideo(videoMetaInfo.videoPlayer)
                } else {
                    playVideoPlayer.release()
                }
            }
            is PlayVideoPlayerUiModel.General.Complete -> {
                if (videoMetaInfo.videoPlayer.playerType == PlayerType.Client) {
                    playGeneralVideo(videoMetaInfo.videoPlayer)
                } else {
                    playVideoPlayer.release()
                }
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
                    } else {
                        false
                    }
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
                    _isChannelReportLoaded.setValue { true }
                } catch (e: Throwable) { }

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

    private fun resetChannelReportLoadedStatus() {
        _isChannelReportLoaded.setValue { false }
    }

    /**
     * Private Method
     */
    private fun setNewChat(chat: PlayChatUiModel) {
        chatManager.addChat(chat)
    }

    private suspend fun getReportSummaries(channelId: String): ReportSummaries = withContext(dispatchers.io) {
        getReportSummariesUseCase.params = GetReportSummariesUseCase.createParam(channelId)
        getReportSummariesUseCase.executeOnBackground()
    }

    private fun trackVisitChannel(channelId: String, shouldTrack: Boolean, sourceType: String) {
        if (shouldTrack) {
            viewModelScope.launchCatchError(dispatchers.io, block = {
                repo.trackVisitChannel(channelId, sourceType)
            }) { }
        }
        _channelReport.setValue { copy(shouldTrack = true) }
    }

    private fun checkLeaderboard(channelId: String) {
        if (!isInteractiveAllowed) return
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val interactiveLeaderboard = repo.getInteractiveLeaderboard(channelId)

            _leaderboard.update {
                it.copy(
                    data = interactiveLeaderboard,
                    state = ResultState.Success
                )
            }

            setLeaderboardBadgeState(interactiveLeaderboard)
        }) { err ->
            _leaderboard.update {
                it.copy(state = ResultState.Fail(err))
            }
        }
    }

    private fun setLeaderboardBadgeState(leaderboardInfo: List<LeaderboardGameUiModel>) {
        if (leaderboardInfo.isNotEmpty()) _leaderboardUserBadgeState.setValue { copy(showLeaderboard = true) }
    }

    private fun checkInteractive(channelId: String) {
        if (!isInteractiveAllowed) return
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val interactive = repo.getCurrentInteractive(channelId)
            setupInteractive(interactive)
        }) {
            _interactive.value = InteractiveStateUiModel.Empty
        }
    }

    private suspend fun setupInteractive(game: GameUiModel) {
        if (!isInteractiveAllowed) return
        /**
         * cancel task delay waiting duration in winner socket if game is coming in flash
         */
        cancelAllDelayFromSocketWinner()
        repo.save(game)
        repo.setActive(game.id)

        // new game set as first game
        _autoOpenInteractive.setValue { true }

        when (game) {
            is GameUiModel.Giveaway -> setupGiveaway(game)
            is GameUiModel.Quiz -> handleQuizFromNetwork(game)
            else -> {}
        }
    }

    private fun handleAutoOpen() {
        if (_autoOpenInteractive.value && !repo.hasJoined(_interactive.value.game.id) && !_interactive.value.isPlaying && !isAnyBottomSheetsShown) {
            _autoOpenInteractive.setValue { false }
            handlePlayingInteractive(shouldPlay = true)
        }
    }

    private suspend fun setupGiveaway(giveaway: GameUiModel.Giveaway) {
        if (giveaway.status == GameUiModel.Giveaway.Status.Finished || giveaway.status == GameUiModel.Giveaway.Status.Unknown) {
            _interactive.update {
                it.copy(game = GameUiModel.Unknown, isPlaying = false)
            }
            checkLeaderboard(channelId)
        } else {
            _interactive.update {
                it.copy(game = giveaway)
            }
        }

        if (giveaway.status is GameUiModel.Giveaway.Status.Ongoing) handleAutoOpen()
    }

    private suspend fun handleQuizFromNetwork(quiz: GameUiModel.Quiz) {
        if (quiz.status == GameUiModel.Quiz.Status.Finished || quiz.status == GameUiModel.Quiz.Status.Unknown) {
            _interactive.update {
                it.copy(game = GameUiModel.Unknown, isPlaying = false)
            }
            checkLeaderboard(channelId)
        } else {
            _interactive.update {
                it.copy(game = quiz)
            }
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
                millisInFuture = TimeUnit.MINUTES.toMillis(INTERVAL_LIKE_REMINDER_IN_MIN),
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

                    connectWebSocket(channelId, warehouseId = _warehouseInfo.value.warehouseId, socketCredential)
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
            is TotalLike -> withContext(dispatchers.computation) likeContext@{
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
                            config = _likeInfo.value.likeBubbleConfig
                        )
                    )
                } else if (diffLike > 0) {
                    _uiEvent.emit(
                        ShowLikeBubbleEvent.Single(
                            diffLike,
                            reduceOpacity = true,
                            config = _likeInfo.value.likeBubbleConfig
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
                    title = mappedResult.title
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
                                    isBanned = result.isBanned && result.userId.isNotEmpty() &&
                                        result.userId.equals(userSession.userId, true)
                                ),
                                statusSource = PlayStatusSource.Socket
                            )
                        )
                    }

                    channelStateProcessor.setIsFreeze(result.isFreeze)
                }
            }
            is ProductSection -> {
                val mappedData = playSocketToModelMapper.mapProductSection(result)
                val updatedSections = repo.updateCampaignReminderStatus(
                    mappedData.product.productSectionList.filterIsInstance<ProductSectionUiModel.Section>()
                )
                val newProduct = mappedData.product.copy(
                    productSectionList = updatedSections
                )

                _tagItems.update {
                    it.copy(
                        product = newProduct,
                        bottomSheetTitle = mappedData.bottomSheetTitle,
                        maxFeatured = mappedData.maxFeatured,
                        resultState = mappedData.resultState
                    )
                }
            }
            is MerchantVoucher -> {
                val mappedVoucher = playSocketToModelMapper.mapMerchantVoucher(result, _partnerInfo.value.name)
                _tagItems.update {
                    it.copy(
                        voucher = mappedVoucher
                    )
                }
            }
            is ChannelInteractiveStatus -> {
                if (result.isExist) checkInteractive(channelId)
            }
            is GiveawayResponse -> {
                val interactive = playSocketToModelMapper.mapInteractive(result)
                setupInteractive(interactive)
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
                val interactive = _interactive.value.game
                val isFinished = when (interactive) {
                    is GameUiModel.Giveaway -> {
                        interactive.status == GameUiModel.Giveaway.Status.Finished
                    }
                    is GameUiModel.Quiz -> {
                        interactive.status == GameUiModel.Quiz.Status.Finished
                    }
                    GameUiModel.Unknown -> false
                }

                val winnerStatus = playSocketToModelMapper.mapUserWinnerStatus(result)
                _winnerStatus.value = winnerStatus

                cancelAllDelayFromSocketWinner()
                if (isFinished) processWinnerStatus(winnerStatus, interactive)
            }
            is QuizResponse -> {
                val interactive = playSocketToModelMapper.mapQuizFromSocket(result)
                setupInteractive(interactive)
            }
        }
    }

    /**
     * Called when user tap
     */
    private suspend fun onReceivedTapGiveawayAction(action: Unit) = withContext(dispatchers.io) {
        try {
            val giveaway = _interactive.value.game as? GameUiModel.Giveaway
                ?: return@withContext
            if (repo.hasJoined(giveaway.id)) return@withContext

            val channelId = mChannelData?.id ?: return@withContext
            val isSuccess = repo.postGiveawayTap(channelId, giveaway.id)
            if (isSuccess) repo.setJoined(giveaway.id)
        } catch (ignored: MessageErrorException) {}
    }

    private fun doFollowUnfollow(shouldForceFollow: Boolean): PartnerFollowAction? {
        val channelData = mChannelData ?: return null
        val shopId = channelData.partnerInfo.id

        val followStatus = _partnerInfo.value.status as? PlayPartnerFollowStatus.Followable ?: return null
        val shouldFollow = if (shouldForceFollow) true else followStatus.followStatus == PartnerFollowableStatus.NotFollowed
        val followAction = if (shouldFollow) PartnerFollowAction.Follow else PartnerFollowAction.UnFollow

        _partnerInfo.setValue { (copy(isLoadingFollow = true)) }

        viewModelScope.launchCatchError(block = {
            val isFollowing = if (channelData.partnerInfo.type == PartnerType.Shop) {
                repo.postFollowStatus(
                    shopId = shopId.toString(),
                    followAction = followAction
                )
            } else {
                val data = repo.postFollowKol(followedKol = _observableKolId.value.toString(), followAction = followAction)
                if (data) followAction == PartnerFollowAction.Follow else false
            }
            _partnerInfo.setValue {
                val result = if (isFollowing) PartnerFollowableStatus.Followed else PartnerFollowableStatus.NotFollowed
                copy(isLoadingFollow = false, status = PlayPartnerFollowStatus.Followable(result))
            }
            _uiEvent.emit(ShowInfoEvent(message = UiString.Resource(R.string.play_interactive_follow_success)))
        }) {
            _partnerInfo.setValue { (copy(isLoadingFollow = false)) }
            _uiEvent.emit(FailedFollow)
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
     * When pre-start finished, game should be played (e.g. TapTap)
     */
    private fun handleGiveawayUpcomingEnded() {
        viewModelScope.launchCatchError(dispatchers.computation, block = {
            _interactive.update {
                val currentGiveaway = it.game as? GameUiModel.Giveaway ?: error("Interactive is not giveaway")
                val upcomingStatus = currentGiveaway.status as? GameUiModel.Giveaway.Status.Upcoming
                    ?: error("Giveaway status is not upcoming")

                val interactive = it.game.copy(
                    status = GameUiModel.Giveaway.Status.Ongoing(
                        endTime = upcomingStatus.endTime
                    )
                )
                it.copy(game = interactive)
            }

            handleAutoOpen()
        }) {
            _interactive.value = InteractiveStateUiModel.Empty
        }
    }

    private fun handleGiveawayOngoingEnded() {
        viewModelScope.launchCatchError(dispatchers.computation, block = {
            val interactive = _interactive.getAndUpdate {
                if (it.game !is GameUiModel.Giveaway) error("Interactive is not giveaway")
                val newInteractive = it.game.copy(
                    status = GameUiModel.Giveaway.Status.Finished
                )
                it.copy(
                    game = newInteractive,
                    isPlaying = false
                )
            }

            delay(INTERACTIVE_FINISH_MESSAGE_DELAY)

            val winnerStatus = _winnerStatus.value
            val interactiveType = _interactive.value.game

            suspend fun checkWinnerStatus(): Boolean {
                return if (winnerStatus != null) {
                    processWinnerStatus(winnerStatus, interactive.game)
                    true
                } else {
                    false
                }
            }

            if (!checkWinnerStatus() && interactive.game is GameUiModel.Giveaway) {
                delayTapJob?.cancel()
                delayTapJob = viewModelScope.launch(dispatchers.computation) {
                    delay(interactive.game.waitingDuration)
                    if (!checkWinnerStatus()) showLeaderBoard(interactiveId = interactiveType.id)
                }
            }

            _interactive.value = InteractiveStateUiModel.Empty
        }) {
            _interactive.value = InteractiveStateUiModel.Empty
        }
    }

    private fun handleTapGiveaway() {
        viewModelScope.launch {
            tapGiveawayFlow.emit(Unit)
        }
    }

    private fun handleQuizEnded() {
        viewModelScope.launchCatchError(dispatchers.computation, block = {
            _interactive.update {
                if (it.game !is GameUiModel.Quiz) error("Error")
                val newInteractive = it.game.copy(
                    status = GameUiModel.Quiz.Status.Finished
                )
                it.copy(
                    game = newInteractive,
                    isPlaying = false
                )
            }
            showLeaderBoard(_interactive.value.game.id)
        }) {
            _interactive.value = InteractiveStateUiModel.Empty
        }
    }

    private suspend fun showLeaderBoard(interactiveId: String) {
        if (repo.hasProcessedWinner(interactiveId)) return

        _leaderboardUserBadgeState.setValue {
            copy(showLeaderboard = true, shouldRefreshData = true)
        }

        _uiEvent.emit(
            ShowCoachMarkWinnerEvent("", UiString.Resource(R.string.play_quiz_finished))
        )

        repo.setHasProcessedWinner(interactiveId)

        _interactive.value = InteractiveStateUiModel.Empty
    }

    private suspend fun processWinnerStatus(
        status: PlayUserWinnerStatusUiModel,
        game: GameUiModel
    ) {
        if (repo.hasProcessedWinner(game.id)) return

        val interactiveType = _interactive.value.game
        _leaderboardUserBadgeState.setValue {
            copy(showLeaderboard = true, shouldRefreshData = true)
        }

        _interactive.value = InteractiveStateUiModel.Empty

        if (status.interactiveId == game.id && repo.hasJoined(game.id)) {
            _uiEvent.emit(
                if (status.userId.toString() == userId) {
                    ShowWinningDialogEvent(status.imageUrl, status.winnerTitle, status.winnerText, interactiveType)
                } else {
                    ShowCoachMarkWinnerEvent(status.loserTitle, UiString.Text(status.loserText))
                }
            )
            repo.setHasProcessedWinner(game.id)
        }
    }

    private fun handlePlayingInteractive(shouldPlay: Boolean) {
        fun updateIsPlaying() {
            _interactive.update {
                val isOngoing = when (it.game) {
                    is GameUiModel.Giveaway -> {
                        it.game.status is GameUiModel.Giveaway.Status.Ongoing
                    }
                    is GameUiModel.Quiz -> {
                        it.game.status is GameUiModel.Quiz.Status.Ongoing
                    }
                    else -> false
                }
                it.copy(isPlaying = if (shouldPlay) isOngoing else shouldPlay)
            }
        }
        if (_partnerInfo.value.status !is PlayPartnerFollowStatus.Followable) {
            needLogin(REQUEST_CODE_LOGIN_PLAY_INTERACTIVE) { updateIsPlaying() }
        } else {
            updateIsPlaying()
        }
    }

    private fun handleClickQuizOption(option: QuizChoicesUiModel) {
        val interactiveId = _interactive.value.game.id

        viewModelScope.launchCatchError(block = {
            val activeInteractiveId = repo.getActiveInteractiveId() ?: return@launchCatchError
            if (repo.hasJoined(activeInteractiveId)) return@launchCatchError
            setUpQuizOptionLoader(selectedId = option.id, isLoading = true)

            val response = repo.answerQuiz(interactiveId = interactiveId, choiceId = option.id)
            repo.setJoined(interactiveId)

            updateQuizOptionUi(selectedId = option.id, correctId = response)
            handleEventQuizAnswered(option.id == response)
        }) {
            handleEventQuizAnswered(false)
            setUpQuizOptionLoader(selectedId = option.id, isLoading = false)
            _uiEvent.emit(
                ShowErrorEvent(it)
            )
        }
    }

    private fun handleEventQuizAnswered(isCorrect: Boolean) {
        viewModelScope.launchCatchError(dispatchers.computation, block = {
            _uiEvent.emit(QuizAnsweredEvent(isCorrect))
        }) {}
    }

    private fun updateQuizOptionUi(selectedId: String, correctId: String) {
        _interactive.update {
            val quiz = it.game as GameUiModel.Quiz
            val new = quiz.copy(
                listOfChoices = quiz.listOfChoices.map { choice ->
                    when (choice.id) {
                        selectedId -> choice.copy(isLoading = false, type = PlayQuizOptionState.Answered(isCorrect = correctId == selectedId))
                        else -> choice.copy(isLoading = false, type = PlayQuizOptionState.Other(correctId == choice.id))
                    }
                }
            )
            it.copy(game = new)
        }
    }

    private fun setUpQuizOptionLoader(selectedId: String, isLoading: Boolean) {
        _interactive.update {
            val quiz = it.game as GameUiModel.Quiz
            val new = quiz.copy(
                listOfChoices = quiz.listOfChoices.map { choice ->
                    if (choice.id == selectedId) {
                        choice.copy(isLoading = isLoading)
                    } else {
                        choice
                    }
                }
            )
            it.copy(game = new)
        }
    }

    private fun handleWinnerBadgeClicked(height: Int) {
        showLeaderboardSheet(height)
    }

    private fun handleCloseLeaderboardSheet() {
        hideLeaderboardSheet()
    }

    /**
     * @param isFromLogin If true, it means follow action will always be follow,
     * if false, it depends on the current state
     */
    private fun handleClickFollow(isFromLogin: Boolean) = needLogin(REQUEST_CODE_LOGIN_FOLLOW) {
        if (_partnerInfo.value.status !is PlayPartnerFollowStatus.NotFollowable) {
            val action = doFollowUnfollow(shouldForceFollow = isFromLogin) ?: return@needLogin
            val shopId = _partnerInfo.value.id

            if (_partnerInfo.value.type == PartnerType.Shop) playAnalytic.clickFollowShop(channelId, channelType, shopId.toString(), action.value)
        }
    }

    /**
     * [PartnerFollowAction] from game will definitely [PartnerFollowAction.Follow]
     */
    private fun handleClickFollowInteractive() = needLogin(REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE) {
        if (_partnerInfo.value.status !is PlayPartnerFollowStatus.NotFollowable) {
            doFollowUnfollow(shouldForceFollow = true) ?: return@needLogin
        }
    }

    fun openPage(appLink: String) {
        viewModelScope.launch {
            _uiEvent.emit(OpenPageEvent(applink = appLink, pipMode = true))
        }
    }

    private fun openWithLogin(appLink: String, requestCode: Int? = null) {
        needLogin(requestCode) {
            openPage(appLink)
        }
    }

    private fun handleClickPartnerName(applink: String) {
        val partnerInfo = _partnerInfo.value
        if (partnerInfo.type == PartnerType.Shop) playAnalytic.clickShop(channelId, channelType, partnerInfo.id.toString())

        if (partnerInfo.type == PartnerType.TokoNow) {
            needLogin {
                openPage(applink)
            }
        } else {
            openPage(applink)
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
            REQUEST_CODE_LOGIN_PLAY_INTERACTIVE -> handlePlayingInteractive(shouldPlay = true)
            REQUEST_CODE_USER_REPORT -> handleUserReport()
            REQUEST_CODE_LOGIN_PLAY_TOKONOW -> updateTagItems()
            REQUEST_CODE_LOGIN_CART -> openPage(ApplinkConstInternalMarketplace.CART)
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
                    _uiEvent.emit(
                        ShowLikeBubbleEvent.Single(
                            count = 1,
                            reduceOpacity = false,
                            config = _likeInfo.value.likeBubbleConfig
                        )
                    )
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
                likeStatus = status
            )
        }

        if (channelType.isLive) {
            handleClickLikeLive(newStatusHandler)
        } else {
            handleClickLikeNonLive(isFromLogin, newStatusHandler)
        }
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
        if (_leaderboardUserBadgeState.value.shouldRefreshData) {
            _leaderboard.update {
                it.copy(state = ResultState.Loading)
            }
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
            if (playShareExperience.isCustomSharingAllow()) {
                if (isScreenshot) {
                    playAnalytic.takeScreenshotForSharing(channelId, partnerId, channelType.value)
                } else {
                    playAnalytic.impressShareBottomSheet(channelId, partnerId, channelType.value)
                }

                _uiEvent.emit(
                    OpenSharingOptionEvent(
                        title = _channelDetail.value.channelInfo.title,
                        coverUrl = _channelDetail.value.channelInfo.coverUrl,
                        userId = userId,
                        channelId = channelId
                    )
                )
            } else if (!isScreenshot) {
                copyLink()
            }
        }

        _isBottomSheetsShown.update { true }
    }

    private fun handleCloseSharingOption() {
        playAnalytic.closeShareBottomSheet(channelId, partnerId, channelType.value, playShareExperience.isScreenshotBottomSheet())
        _isBottomSheetsShown.update { false }
    }

    private fun handleSharingOption(shareModel: ShareModel) {
        viewModelScope.launch {
            playAnalytic.clickSharingOption(channelId, partnerId, channelType.value, shareModel.channel, playShareExperience.isScreenshotBottomSheet())

            val playShareExperienceData = getPlayShareExperienceData()

            playShareExperience
                .setShareModel(shareModel)
                .setData(playShareExperienceData)
                .createUrl(
                    object : PlayShareExperience.Listener {
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

    private fun handleThreeDotsMenuClick() {
        viewModelScope.launch {
            _uiEvent.emit(OpenKebabEvent)
        }

        _isBottomSheetsShown.update { true }
    }

    private fun handleFooterClick(appLink: String) {
        viewModelScope.launch {
            _uiEvent.emit(OpenPageEvent(applink = appLink))
        }
    }

    private fun handleUserReport() {
        needLogin(REQUEST_CODE_USER_REPORT) {
            viewModelScope.launch {
                _uiEvent.emit(OpenUserReportEvent)
            }
        }
    }

    /**
     * Handle buying product
     * @param productId the id of the product
     */
    private fun handleBuyProduct(
        sectionInfo: ProductSectionUiModel.Section = ProductSectionUiModel.Section.Empty,
        product: PlayProductUiModel.Product,
        action: ProductAction,
        isProductFeatured: Boolean
    ) {
        if (product.isVariantAvailable) {
            openVariantDetail(product, sectionInfo, isProductFeatured)
        } else {
            needLogin {
                addProductToCart(product, action) { cartId ->
                    _uiEvent.emit(
                        when (action) {
                            ProductAction.Buy -> BuySuccessEvent(product, false, cartId, sectionInfo, isProductFeatured)
                            ProductAction.OCC -> OCCSuccessEvent(product, false, cartId, sectionInfo, isProductFeatured)
                            else -> AtcSuccessEvent(product, false, cartId, sectionInfo, isProductFeatured)
                        }
                    )
                }
            }
        }
    }

    /**
     * Handle buying product variant
     * @param productId the id of the product
     */
    private fun handleBuyProductVariant(productId: String, action: ProductAction) = needLogin {
        val selectedVariant = _selectedVariant.value
        if (selectedVariant !is NetworkResult.Success ||
            selectedVariant.data.variantDetail.id != productId
        ) {
            return@needLogin
        }

        addProductToCart(selectedVariant.data.variantDetail, action) { cartId ->
            _uiEvent.emit(
                when (action) {
                    ProductAction.Buy -> BuySuccessEvent(
                        selectedVariant.data.variantDetail,
                        true,
                        cartId,
                        selectedVariant.data.sectionInfo,
                        selectedVariant.data.isFeatured
                    )
                    ProductAction.OCC -> OCCSuccessEvent(
                        selectedVariant.data.variantDetail,
                        true,
                        cartId,
                        selectedVariant.data.sectionInfo,
                        selectedVariant.data.isFeatured
                    )
                    else ->
                        AtcSuccessEvent(
                            selectedVariant.data.variantDetail,
                            true,
                            cartId,
                            selectedVariant.data.sectionInfo,
                            selectedVariant.data.isFeatured
                        )
                }
            )
        }
    }

    /**
     * Handle selecting variant option from available variant
     */
    private fun handleSelectVariantOption(option: VariantOptionWithAttribute) {
        val selectedVariant = _selectedVariant.value
        if (selectedVariant !is NetworkResult.Success) return

        viewModelScope.launchCatchError(dispatchers.io, block = {
            _selectedVariant.value = NetworkResult.Success(
                repo.selectVariantOption(
                    variant = selectedVariant.data,
                    selectedOption = option
                )
            )
        }) {
            // Ignore for now since there shouldn't be any error (no network call)
            // and since there was never error handling for this
        }
    }

    /**
     * Adding product to cart
     * @param product product to be added
     */
    private fun addProductToCart(
        product: PlayProductUiModel.Product,
        action: ProductAction,
        onSuccess: suspend (String) -> Unit
    ) {
        _loadingBuy.value = true
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val cartId = if (action == ProductAction.OCC) {
                repo.addProductToCartOcc(
                    id = product.id,
                    name = product.title,
                    shopId = product.shopId,
                    minQty = product.minQty,
                    price = when (product.price) {
                        is OriginalPrice -> product.price.priceNumber
                        is DiscountedPrice -> product.price.discountedPriceNumber
                    }
                )
            } else {
                repo.addProductToCart(
                    id = product.id,
                    name = product.title,
                    shopId = product.shopId,
                    minQty = product.minQty,
                    price = when (product.price) {
                        is OriginalPrice -> product.price.priceNumber
                        is DiscountedPrice -> product.price.discountedPriceNumber
                    }
                )
            }
            _loadingBuy.value = false
            onSuccess(cartId)
        }) {
            _uiEvent.emit(ShowErrorEvent(it))
            _loadingBuy.value = false
        }
    }

    /**
     * Utility Function
     */
    private fun needLogin(requestCode: Int? = null, fn: () -> Unit) {
        if (userSession.isLoggedIn) {
            fn()
        } else {
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

    private fun authenticated(fn: (wasAuthenticated: Boolean) -> Unit) {
        if (userSession.isLoggedIn) {
            fn(true)
        } else {
            viewModelScope.launch {
                _uiEvent.emit(
                    LoginEvent { fn(false) }
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
        if (model == null) {
            model = PlayCastUiModel(currentState = castState)
        } else {
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
            metaDescription = shareInfo.metaDescription
        )
    }

    fun getUserReportList() {
        _userReportItems.update { it.copy(resultState = ResultState.Loading) }

        viewModelScope.launchCatchError(block = {
            val userReportUiModel = repo.getReasoningList()

            _userReportItems.update { it.copy(resultState = ResultState.Success, reasoningList = userReportUiModel) }
        }) {
                error ->
            _userReportItems.update {
                it.copy(resultState = ResultState.Fail(error = error))
            }
        }
    }

    fun submitUserReport(
        mediaUrl: String,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String
    ) {
        viewModelScope.launchCatchError(block = {
            _userReportSubmission.value = ResultState.Loading
            val isSuccess = repo.submitReport(
                channelId = channelId.toLongOrZero(),
                partnerId = partnerId.orZero(),
                partnerType = PartnerType.getTypeByValue(partnerType),
                reasonId = reasonId,
                timestamp = timestamp,
                reportDesc = reportDesc,
                mediaUrl = mediaUrl
            )
            if (isSuccess) {
                _userReportSubmission.value = ResultState.Success
            } else {
                throw Exception()
            }
        }) { err ->
            _userReportSubmission.value = ResultState.Fail(err)
        }
    }

    private fun handleSendReminder(section: ProductSectionUiModel.Section) = authenticated { wasAuthenticated ->
        viewModelScope.launchOverride("$REMINDER_JOB_ID-${section.id}", block = {
            val (isReminded, message) = repo.subscribeUpcomingCampaign(
                section.id,
                shouldRemind = if (!wasAuthenticated) {
                    true
                } else {
                    section.config.reminder == PlayUpcomingBellStatus.Off
                }
            )

            updateReminderUi(
                reminderType = if (isReminded) PlayUpcomingBellStatus.On else PlayUpcomingBellStatus.Off,
                campaignId = section.id
            )
            _uiEvent.emit(
                ChangeCampaignReminderSuccess(
                    isReminded = isReminded,
                    message = message
                )
            )
        }) {
            _uiEvent.emit(ShowErrorEvent(it))
        }
    }

    private fun updateReminderUi(reminderType: PlayUpcomingBellStatus, campaignId: String) {
        _tagItems.update { tagItemUiModel ->
            val sectionList = tagItemUiModel.product.productSectionList
            val newSections = sectionList.map { section ->
                if (section is ProductSectionUiModel.Section && section.id == campaignId) {
                    section.copy(config = section.config.copy(reminder = reminderType))
                } else {
                    section
                }
            }
            tagItemUiModel.copy(
                product = tagItemUiModel.product.copy(
                    productSectionList = newSections
                )
            )
        }
    }

    fun sendUpcomingReminderImpression(sectionUiModel: ProductSectionUiModel.Section) {
        playAnalytic.impressUpcomingReminder(sectionUiModel, channelId, channelType)
    }

    /**
     * Variant Util
     */
    private fun openVariantDetail(
        product: PlayProductUiModel.Product,
        sectionUiModel: ProductSectionUiModel.Section,
        isProductFeatured: Boolean
    ) {
        _selectedVariant.value = NetworkResult.Loading
        viewModelScope.launchCatchError(block = {
            _selectedVariant.value = NetworkResult.Success(repo.getVariant(product, isProductFeatured))
            _selectedVariant.update {
                if (it is NetworkResult.Success) {
                    it.copy(data = it.data.copy(sectionInfo = sectionUiModel))
                } else {
                    it
                }
            }
        }) {
            _selectedVariant.value = NetworkResult.Fail(it)
        }
    }

    private fun handleWarehouse(id: String, isOOC: Boolean) {
        viewModelScope.launchCatchError(dispatchers.io, block = {
            _warehouseInfo.value = WarehouseInfoUiModel(id, isOOC)
        }) {}
    }

    private fun cancelAllDelayFromSocketWinner() {
        if (delayTapJob?.isActive == true) delayTapJob?.cancel()
    }

    private fun sendInitialLog() {
        playLog.logDownloadSpeed(liveRoomMetricsCommon.getInetSpeed())
        playLog.sendAll(channelId, videoPlayer)
    }

    private fun fetchWidgets() {
        viewModelScope.launchCatchError(block = {
            _uiEvent.emit(ExploreWidgetInitialState)
            _exploreWidget.update { it.copy(state = ExploreWidgetState.Loading, chips = it.chips.copy(state = ResultState.Loading)) }
            val data = getWidgets()
            val chips = data.getChips

            _exploreWidget.update {
                it.copy(chips = chips)
            }

            if (!data.isSubSlotAvailable && chips.items.isEmpty()) return@launchCatchError
            updateWidgetParam(group = chips.items.first().group, sourceType = chips.items.first().sourceType, sourceId = chips.items.first().sourceId)
            val widgets = getWidgets()
            _exploreWidget.update {
                val newList = it.widgets + widgets
                it.copy(
                    param = it.param.copy(cursor = widgets.getConfig.cursor),
                    widgets = newList.getChannelBlocks,
                    state = if (newList.isEmpty()) ExploreWidgetState.Empty else ExploreWidgetState.Success
                )
            }
        }) { exception ->
            _exploreWidget.update { it.copy(state = ExploreWidgetState.Fail(exception)) }
        }
    }

    /**
     * Next Page or Chips Clicked
     */
    private fun onActionWidget(isNextPage: Boolean = false) {
        if (!_exploreWidget.value.param.hasNextPage && isNextPage) return
        viewModelScope.launchCatchError(block = {
            if (!isNextPage) _uiEvent.emit(ExploreWidgetInitialState)
            _exploreWidget.update { it.copy(state = if (isNextPage) it.state else ExploreWidgetState.Loading, param = it.param.copy(cursor = if (isNextPage) it.param.cursor else "")) }

            val widgets = getWidgets()

            _exploreWidget.update {
                val newList = if (isNextPage) it.widgets + widgets else widgets

                it.copy(
                    widgets = newList.getChannelBlocks,
                    param = it.param.copy(cursor = widgets.getConfig.cursor),
                    state = if (newList.isEmpty()) ExploreWidgetState.Empty else ExploreWidgetState.Success
                )
            }
        }) { exception -> _exploreWidget.update { it.copy(state = ExploreWidgetState.Fail(exception)) } }
    }

    private fun handleClickChip(item: ChipWidgetUiModel) {
        updateWidgetParam(item.group, item.sourceType, item.sourceId)
        onActionWidget()
    }

    private fun updateWidgetParam(group: String, sourceType: String, sourceId: String, cursor: String = "") {
        _exploreWidget.update {
            it.copy(
                param = it.param.copy(
                    group = group,
                    sourceType = sourceType,
                    sourceId = sourceId,
                    cursor = cursor
                ),
                chips = it.chips.copy(
                    items = it.chips.items.map { chip ->
                        if (group == chip.group) {
                            chip.copy(isSelected = true)
                        } else {
                            chip.copy(isSelected = false)
                        }
                    }
                )
            )
        }
    }

    private suspend fun getWidgets(): List<WidgetUiModel> {
        val config = _exploreWidget.value.param
        return repo.getWidgets(
            group = config.group,
            sourceType = config.sourceType,
            sourceId = config.sourceId,
            cursor = config.cursor
        )
    }

    private fun updateReminderWidget(channelId: String, reminderType: PlayWidgetReminderType) =
        authenticated {
            viewModelScope.launchCatchError(block = {
                val result = repo.updateReminder(channelId, reminderType)
                if (result) {
                    _exploreWidget.update {
                        it.copy(
                            widgets = it.widgets.map {
                                it.copy(
                                    item = it.item.copy(
                                        items = it.item.items.map { widget ->
                                            if (widget is PlayWidgetChannelUiModel && widget.channelId == channelId) {
                                                widget.copy(reminderType = reminderType)
                                            } else {
                                                widget
                                            }
                                        }
                                    )
                                )
                            }
                        )
                    }
                } else {
                    throw MessageErrorException()
                }
            }) {
                _uiEvent.emit(ShowErrorEvent(it))
            }
        }

    private fun handleFollowPopUp(channelData: PlayChannelData) {
        val streamerId = channelData.partnerInfo.id.toString()
        val config = channelData.channelDetail.popupConfig
        val cache = playPreference.isFollowPopup(streamerId)

        viewModelScope.launch {
            cancelJob(FOLLOW_POP_UP_ID)
            jobMap[FOLLOW_POP_UP_ID] = launch(dispatchers.computation) {
                delay(config.duration)
                val needToBeShown = !isFreezeOrBanned && cache && config.isEnabled
                if (!needToBeShown) return@launch
                _isFollowPopUpShown.update { it.copy(shouldShow = true, partnerId = streamerId.toLong()) }
                playPreference.setFollowPopUp(streamerId)
            }
        }
    }

    private fun handleEmptyExplore() {
        val position = _exploreWidget.value.chips.items.indexOfFirst { it.isSelected }
        val finalPosition = if (position >= _exploreWidget.value.chips.items.size) 0 else position.plus(1)
        handleClickChip(_exploreWidget.value.chips.items[finalPosition])
    }

    private fun cancelJob(identifier: String) {
        jobMap[identifier]?.cancel()
    }

    private fun CoroutineScope.launch(
        jobId: String = "",
        context: CoroutineContext = coroutineContext,
        block: suspend CoroutineScope.() -> Unit,
        onError: suspend (Throwable) -> Unit
    ) {
        val job = launchCatchError(context, block, onError)
        jobMap[jobId] = job
    }

    private fun CoroutineScope.launchOverride(
        jobId: String = "",
        context: CoroutineContext = coroutineContext,
        block: suspend CoroutineScope.() -> Unit,
        onError: suspend (Throwable) -> Unit
    ) {
        jobMap[jobId]?.cancel()
        launch(jobId, context, block, onError)
    }

    companion object {
        private const val FIREBASE_REMOTE_CONFIG_KEY_PIP = "android_mainapp_enable_pip"
        private const val FIREBASE_REMOTE_CONFIG_KEY_INTERACTIVE = "android_main_app_enable_play_interactive"
        private const val FIREBASE_REMOTE_CONFIG_KEY_CAST = "android_main_app_enable_play_cast"
        private const val FIREBASE_REMOTE_CONFIG_KEY_LIKE_BUBBLE = "android_main_app_enable_play_bubbles"
        private const val FIREBASE_REMOTE_CONFIG_KEY_EXPLORE_WIDGET = "android_main_app_enable_play_explore_widget"
        private const val ONBOARDING_DELAY = 5000L
        private const val INTERACTIVE_FINISH_MESSAGE_DELAY = 2000L

        private const val LIKE_BURST_THRESHOLD = 30L

        /**
         * Request Code When need login
         */
        private const val REQUEST_CODE_LOGIN_FOLLOW = 571
        private const val REQUEST_CODE_LOGIN_FOLLOW_INTERACTIVE = 572
        private const val REQUEST_CODE_LOGIN_LIKE = 573
        private const val REQUEST_CODE_USER_REPORT = 575
        private const val REQUEST_CODE_LOGIN_PLAY_INTERACTIVE = 576
        private const val REQUEST_CODE_LOGIN_PLAY_TOKONOW = 577
        private const val REQUEST_CODE_LOGIN_CART = 578

        private const val WEB_SOCKET_SOURCE_PLAY_VIEWER = "Viewer"

        /**
         * Reminder
         */
        private const val INTERVAL_LIKE_REMINDER_IN_MIN = 5L
        private const val DURATION_DIVIDER = 1000
        private const val REMINDER_JOB_ID = "RJ"
        private const val SUBSCRIBE_AWAY_THRESHOLD = 5000L
        private val defaultSharingStarted = SharingStarted.WhileSubscribed(SUBSCRIBE_AWAY_THRESHOLD)

        private const val FOLLOW_POP_UP_ID = "FOLLOW_POP_UP"
        private const val ONBOARDING_COACHMARK_ID = "ONBOARDING_COACHMARK"
    }
}
