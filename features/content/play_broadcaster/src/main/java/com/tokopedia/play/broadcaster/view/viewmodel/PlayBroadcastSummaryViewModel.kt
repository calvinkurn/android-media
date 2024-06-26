package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveSummaryLivestreamUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetSellerLeaderboardUseCase
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.state.ChannelSummaryUiState
import com.tokopedia.play.broadcaster.ui.state.LiveReportUiState
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastSummaryUiState
import com.tokopedia.play.broadcaster.ui.state.TagUiState
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.play_common.util.error.DefaultErrorThrowable
import com.tokopedia.play_common.util.extension.setValue
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author by jessica on 27/05/20
 */
@Suppress("LateinitUsage")
class PlayBroadcastSummaryViewModel @AssistedInject constructor(
    @Assisted("account") val account: ContentAccountUiModel,
    @Assisted("channelId") val channelId: String,
    @Assisted("channelTitle") val channelTitle: String,
    private val dispatcher: CoroutineDispatchers,
    private val getLiveStatisticsUseCase: GetLiveStatisticsUseCase,
    private val getInteractiveSummaryLivestreamUseCase: GetInteractiveSummaryLivestreamUseCase,
    private val getSellerLeaderboardUseCase: GetSellerLeaderboardUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val userSession: UserSessionInterface,
    private val playBroadcastMapper: PlayBroadcastMapper,
    private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase,
    private val setChannelTagsUseCase: SetChannelTagsUseCase,
    private val getChannelUseCase: GetChannelUseCase,
    private val hydraConfigStore: HydraConfigStore,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("account") account: ContentAccountUiModel,
            @Assisted("channelId") channelId: String,
            @Assisted("channelTitle") channelTitle: String,
        ): PlayBroadcastSummaryViewModel
    }

    val shopName: String
        get() = userSession.shopName

    private val _channelSummary = MutableStateFlow(ChannelSummaryUiModel.empty())
    private val _trafficMetric = MutableStateFlow<NetworkResult<List<TrafficMetricUiModel>>>(NetworkResult.Loading)
    private val _trafficMetricHighlight = MutableStateFlow<NetworkResult<List<LiveStatsUiModel>>>(NetworkResult.Loading)
    private val _tags = MutableStateFlow<NetworkResult<Set<String>>>(NetworkResult.Loading)
    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())

    private val _channelSummaryUiState = _channelSummary.map {
        ChannelSummaryUiState(
            title = it.title,
            coverUrl = it.coverUrl,
            date = it.date,
            duration = it.duration,
            author = it.author,
        )
    }

    private val _liveReportUiState = combine(
        _trafficMetric,
        _trafficMetricHighlight
    ) { trafficMetric, trafficMetricHighlight ->
        LiveReportUiState(trafficMetric, trafficMetricHighlight)
    }

    private val _tagUiState = combine(
        _tags,
        _selectedTags
    ) { tags, selectedTags ->
        when (tags) {
            is NetworkResult.Unknown -> NetworkResult.Unknown
            is NetworkResult.Loading -> NetworkResult.Loading
            is NetworkResult.Fail -> NetworkResult.Fail(tags.error)
            is NetworkResult.Success -> {
                NetworkResult.Success(
                    TagUiState(
                        tags = tags.data.map {
                            PlayTagItem(
                                tag = it,
                                isChosen = selectedTags.contains(it),
                                isActive = true,
                            )
                        }
                    )
                )
            }
        }
    }

    val uiState: Flow<PlayBroadcastSummaryUiState> = combine(
        _channelSummaryUiState,
        _liveReportUiState.distinctUntilChanged(),
        _tagUiState.distinctUntilChanged()
    ) { channelSummaryUiState, liveReportUiState, tagUiState ->
        PlayBroadcastSummaryUiState(
            channelSummary = channelSummaryUiState,
            liveReport = liveReportUiState,
            tag = tagUiState
        )
    }.flowOn(dispatcher.computation)

    private val _uiEvent = MutableSharedFlow<PlayBroadcastSummaryEvent>()
    val uiEvent: Flow<PlayBroadcastSummaryEvent>
        get() = _uiEvent

    init {
        fetchLiveTraffic()
        getTags()
    }

    /** Action Area */
    fun submitAction(action: PlayBroadcastSummaryAction) {
        when (action) {
            PlayBroadcastSummaryAction.ClickCloseReportPage -> handleClickCloseReportPage()
            PlayBroadcastSummaryAction.ClickViewLeaderboard -> handleClickViewLeaderboard()
            PlayBroadcastSummaryAction.ClickPostVideo -> handleClickPostVideo()

            PlayBroadcastSummaryAction.ClickBackToReportPage -> handleClickBackToReportPage()
            PlayBroadcastSummaryAction.ClickEditCover -> handleClickEditCover()
            is PlayBroadcastSummaryAction.SetCover -> handleSetCover(action.cover)
            is PlayBroadcastSummaryAction.ToggleTag -> handleToggleTag(action.tagUiModel)
            PlayBroadcastSummaryAction.ClickPostVideoNow -> handleClickPostVideoNow()
            PlayBroadcastSummaryAction.RefreshLoadTag -> handleRefreshLoadTag()
        }
    }

    private fun handleClickCloseReportPage() {
        viewModelScope.launch(context = dispatcher.main) {
            _uiEvent.emit(PlayBroadcastSummaryEvent.CloseReportPage)
        }
    }

    private fun handleClickViewLeaderboard() {
        viewModelScope.launch(context = dispatcher.main) {
            _uiEvent.emit(PlayBroadcastSummaryEvent.OpenLeaderboardBottomSheet)
        }
    }

    private fun handleClickPostVideo() {
        viewModelScope.launch(context = dispatcher.main) {
            _uiEvent.emit(PlayBroadcastSummaryEvent.OpenPostVideoPage)
        }
    }

    private fun handleClickBackToReportPage() {
        viewModelScope.launch(context = dispatcher.main) {
            _uiEvent.emit(PlayBroadcastSummaryEvent.BackToReportPage)
        }
    }

    private fun handleClickEditCover() {
        viewModelScope.launch(context = dispatcher.main) {
            _uiEvent.emit(PlayBroadcastSummaryEvent.OpenSelectCoverBottomSheet)
        }
    }

    private fun handleSetCover(cover: PlayCoverUiModel) {
        viewModelScope.launch(context = dispatcher.main) {
            when (val croppedCover = cover.croppedCover) {
                is CoverSetupState.Cropped.Uploaded -> {
                    val imageUri = if (croppedCover.coverImage.toString().isNotEmpty() &&
                        croppedCover.coverImage.toString().contains("http")
                    ) {
                        croppedCover.coverImage.toString()
                    } else if (!croppedCover.localImage?.toString().isNullOrEmpty()) {
                        croppedCover.localImage.toString()
                    } else {
                        ""
                    }

                    if (imageUri.isNotEmpty()) {
                        _channelSummary.setValue {
                            copy(coverUrl = imageUri)
                        }
                    }
                }
                is CoverSetupState.GeneratedCover -> {
                    if (croppedCover.coverImage.isEmpty()) return@launch
                    _channelSummary.setValue {
                        copy(coverUrl = croppedCover.coverImage)
                    }
                }
                else -> {
                    //no-op
                }
            }
        }
    }

    private fun handleToggleTag(tagUiModel: PlayTagItem) {
        viewModelScope.launchCatchError(context = dispatcher.main, block = {
            val newSelectedTag = _selectedTags.value.toMutableSet().apply {
                with(tagUiModel) {
                    if (_selectedTags.value.contains(tag)) {
                        remove(tag)
                    } else {
                        add(tag)
                    }
                }
            }

            _selectedTags.value = newSelectedTag
        }) { }
    }

    private fun handleClickPostVideoNow() {
        viewModelScope.launchCatchError(context = dispatcher.main, block = {
            _uiEvent.emit(PlayBroadcastSummaryEvent.PostVideo(NetworkResult.Loading))
            withContext(dispatcher.io) {
                saveTag()
                updateChannelStatus()
            }
            _uiEvent.emit(PlayBroadcastSummaryEvent.PostVideo(NetworkResult.Success(true)))
        }) {
            _uiEvent.emit(
                PlayBroadcastSummaryEvent.PostVideo(
                    NetworkResult.Fail(it) {
                        submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
                    }
                )
            )
        }
    }

    private fun handleRefreshLoadTag() {
        getTags()
    }

    /** Fetch Area */
    private fun fetchLiveTraffic() {
        viewModelScope.launchCatchError(context = dispatcher.io, block = {
            _trafficMetric.emit(NetworkResult.Loading)

            val channel = getChannelUseCase.apply {
                params = GetChannelUseCase.createParams(channelId)
            }.executeOnBackground()

            delay(LIVE_STATISTICS_DELAY)

            var fetchTryCount = 0
            lateinit var reportChannelSummary: GetLiveStatisticsResponse.ReportChannelSummary

            getLiveStatisticsUseCase.params = GetLiveStatisticsUseCase.createParams(channelId)
            do {
                reportChannelSummary = getLiveStatisticsUseCase.executeOnBackground()
                fetchTryCount++
            }
            while (reportChannelSummary.duration.isEmpty() && fetchTryCount < FETCH_REPORT_MAX_RETRY)

            getInteractiveSummaryLivestreamUseCase.setRequestParams(GetInteractiveSummaryLivestreamUseCase.createParams(channelId))
            val participantResponse = getInteractiveSummaryLivestreamUseCase.executeOnBackground()

            _channelSummary.value = playBroadcastMapper.mapChannelSummary(
                                        channel.basic.title,
                                        channel.basic.coverUrl,
                                        convertDate(channel.basic.timestamp.publishedAt),
                                        reportChannelSummary.duration,
                                        hydraConfigStore.getAuthor(),
                                    )
            getSellerLeaderboardUseCase.setRequestParams(GetSellerLeaderboardUseCase.createParams(channelId))

            val leaderboard = getSellerLeaderboardUseCase.executeOnBackground()
            val metrics = playBroadcastMapper.mapToLiveTrafficUiMetrics(
                authorType = hydraConfigStore.getAuthorType(),
                metrics = reportChannelSummary
            )

            val metricHighlight = mutableListOf<LiveStatsUiModel>().apply {
                if (account.isShop) {
                    add(
                        LiveStatsUiModel.EstimatedIncome(reportChannelSummary.channel.metrics.estimatedIncome)
                    )
                }

                if (leaderboard.data.slots.isNotEmpty()) {
                    add(
                        LiveStatsUiModel.GameParticipant(participantResponse.playInteractiveGetSummaryLivestream.participantCount.toString())
                    )
                }
            }

            _trafficMetric.value = NetworkResult.Success(metrics)
            _trafficMetricHighlight.update { NetworkResult.Success(metricHighlight) }
        }) { throwable ->
            _channelSummary.value = ChannelSummaryUiModel.empty()
            _trafficMetric.value = NetworkResult.Fail(throwable) { fetchLiveTraffic() }
            _trafficMetricHighlight.update { NetworkResult.Fail(throwable) { fetchLiveTraffic() } }
        }
    }

    private fun getTags() {
        viewModelScope.launchCatchError(context = dispatcher.main, block = {
            _tags.value = NetworkResult.Loading

            val response = getRecommendedChannelTagsUseCase.apply {
                setChannelId(channelId)
            }.executeOnBackground()

            _tags.value = NetworkResult.Success(response.recommendedTags.tags.toSet())
        }) {
            _tags.value = NetworkResult.Fail(it)
        }
    }

    private suspend fun saveTag() {
        val isSuccess = setChannelTagsUseCase.apply {
            setParams(channelId, _selectedTags.value)
        }.executeOnBackground().recommendedTags.success

        if (!isSuccess) throw DefaultErrorThrowable("${DefaultErrorThrowable.DEFAULT_MESSAGE}: Error Tag")
    }

    private suspend fun updateChannelStatus() {
        updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusRequest(
                    channelId = channelId,
                    authorId = account.id,
                    status = PlayChannelStatusType.Transcoding
                )
            )
        }.executeOnBackground()
    }

    /** Helper */
    private fun convertDate(raw: String): String =
        PlayDateTimeFormatter.formatDate(raw, outputPattern = PlayDateTimeFormatter.dMMMMyyyy)

    companion object {
        private const val LIVE_STATISTICS_DELAY = 300L
        private const val FETCH_REPORT_MAX_RETRY = 3
    }
}
