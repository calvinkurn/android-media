package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.event.UiString
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.ChannelSummaryUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.state.ChannelSummaryUiState
import com.tokopedia.play.broadcaster.ui.state.LiveReportUiState
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastSummaryUiState
import com.tokopedia.play.broadcaster.ui.state.TagUiState
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.play_common.util.extension.setValue
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * @author by jessica on 27/05/20
 */

class PlayBroadcastSummaryViewModel @Inject constructor(
        private val channelConfigStore: ChannelConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val getLiveStatisticsUseCase: GetLiveStatisticsUseCase,
        private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
        private val userSession: UserSessionInterface,
        private val playBroadcastMapper: PlayBroadcastMapper,
        private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase,
        private val setChannelTagsUseCase: SetChannelTagsUseCase,
        private val getChannelUseCase: GetChannelUseCase,
) : ViewModel() {

    private val channelId: String
        get() = channelConfigStore.getChannelId()

    private val _channelSummary = MutableStateFlow(ChannelSummaryUiModel.empty())
    private val _trafficMetric = MutableStateFlow<NetworkResult<List<TrafficMetricUiModel>>>(NetworkResult.Loading)
    private val _tags = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())

    private val _channelSummaryUiState = _channelSummary.map {
        ChannelSummaryUiState(
            title = it.title,
            date = it.date,
            duration = it.duration,
            isEligiblePostVideo = it.isEligiblePostVideo,
        )
    }
    private val _liveReportUiState = _trafficMetric.map {
        LiveReportUiState(it)
    }

    private val _tagUiState = combine(
        _tags, _selectedTags,
    ) { tags, selectedTags ->
        TagUiState(
            tags = tags.map {
                PlayTagUiModel(
                    tag = it,
                    isChosen = selectedTags.contains(it)
                )
            },
        )
    }

    val uiState: Flow<PlayBroadcastSummaryUiState> = combine(
        _channelSummaryUiState,
        _liveReportUiState.distinctUntilChanged(),
        _tagUiState.distinctUntilChanged(),
    ) { channelSummaryUiState, liveReportUiState, tagUiState ->
        PlayBroadcastSummaryUiState(
            channelSummary = channelSummaryUiState,
            liveReport = liveReportUiState,
            tag = tagUiState,
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
        when(action) {
            PlayBroadcastSummaryAction.ClickCloseReportPage -> handleClickCloseReportPage()
            PlayBroadcastSummaryAction.ClickViewLeaderboard -> handleClickViewLeaderboard()
            PlayBroadcastSummaryAction.ClickPostVideo -> handleClickPostVideo()

            PlayBroadcastSummaryAction.ClickBackToReportPage -> handleClickBackToReportPage()
            PlayBroadcastSummaryAction.ClickEditCover -> handleClickEditCover()
            is PlayBroadcastSummaryAction.ToggleTag -> handleToggleTag(action.tagUiModel)
            PlayBroadcastSummaryAction.ClickPostVideoNow -> handleClickPostVideoNow()
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

    private fun handleToggleTag(tagUiModel: PlayTagUiModel) {
        viewModelScope.launchCatchError(context = dispatcher.main, block = {
            val newSelectedTag = _selectedTags.value.toMutableSet().apply {
                with(tagUiModel) {
                    if(_selectedTags.value.contains(tag)) remove(tag)
                    else add(tag)
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
            _uiEvent.emit(PlayBroadcastSummaryEvent.PostVideo(NetworkResult.Fail(it) {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }))
        }
    }

    /** Fetch Area */
    private fun fetchLiveTraffic() {
        viewModelScope.launchCatchError(context = dispatcher.main, block = {
            _trafficMetric.emit(NetworkResult.Loading)

            val channel = getChannelUseCase.apply {
                params = GetChannelUseCase.createParams("337761")
            }.executeOnBackground()

            val reportChannelSummary = withContext(dispatcher.io) {
                delay(LIVE_STATISTICS_DELAY)

                var fetchTryCount = 0
                lateinit var response : GetLiveStatisticsResponse.ReportChannelSummary

                getLiveStatisticsUseCase.params = GetLiveStatisticsUseCase.createParams("337761")
                do {
                    response = getLiveStatisticsUseCase.executeOnBackground()
                    fetchTryCount++
                }
                while(response.duration.isEmpty() && fetchTryCount < FETCH_REPORT_MAX_RETRY)

                response
            }

            _channelSummary.value = playBroadcastMapper.mapChannelSummary(
                                        channel.basic.title,
                                        convertDate(channel.basic.timestamp.publishedAt),
                                        reportChannelSummary.duration,
                                        isEligiblePostVideo(reportChannelSummary.duration),
                                    )

            _trafficMetric.value = NetworkResult.Success(playBroadcastMapper.mapToLiveTrafficUiMetrics(reportChannelSummary.channel.metrics))

            if(!isEligiblePostVideo(reportChannelSummary.duration))
                _uiEvent.emit(PlayBroadcastSummaryEvent.ShowInfo(UiString.Resource(R.string.play_bro_cant_post_video_message)))
        }) {
            _channelSummary.value = ChannelSummaryUiModel.empty()
            _trafficMetric.value = NetworkResult.Fail(it) { fetchLiveTraffic() }

            _uiEvent.emit(PlayBroadcastSummaryEvent.ShowInfo(UiString.Resource(R.string.play_bro_cant_post_video_message)))
        }
    }

    private fun getTags() {
        viewModelScope.launchCatchError(context = dispatcher.main, block = {
            val response = getRecommendedChannelTagsUseCase.apply {
                setChannelId(channelId)
            }.executeOnBackground()

            _tags.value = response.recommendedTags.tags.toSet()
        }) {}
    }

    private suspend fun saveTag() {
        val isSuccess = setChannelTagsUseCase.apply {
            setParams(channelId, _selectedTags.value)
        }.executeOnBackground().recommendedTags.success

        if(!isSuccess) throw DefaultErrorThrowable("${DefaultErrorThrowable.DEFAULT_MESSAGE}: Error Tag")
    }

    private suspend fun updateChannelStatus() {
        updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusRequest(
                    channelId = channelId,
                    authorId = userSession.shopId,
                    status = PlayChannelStatusType.Transcoding
                )
            )
        }.executeOnBackground()
    }

    /** Helper */
    private fun convertDate(raw: String): String =
        PlayDateTimeFormatter.formatDate(raw, outputPattern = PlayDateTimeFormatter.dMMMMyyyy)

    private fun isEligiblePostVideo(duration: String): Boolean {
        return try {
            val split = duration.split(":")
            (split.size == 3 && split[1].toInt() > 0) || (split.size == 2 && split[0].toInt() > 0)
        }
        catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val LIVE_STATISTICS_DELAY = 300L
        private const val FETCH_REPORT_MAX_RETRY = 3
    }
}