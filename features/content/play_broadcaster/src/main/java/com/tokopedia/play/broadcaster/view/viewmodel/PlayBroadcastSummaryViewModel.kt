package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.event.UiString
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.state.LiveReportUiState
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastSummaryUiState
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
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
) : ViewModel() {

    private val channelId: String
        get() = channelConfigStore.getChannelId()

    private val _trafficMetric = MutableStateFlow<NetworkResult<List<TrafficMetricUiModel>>>(NetworkResult.Loading)
    private val _liveDuration = MutableStateFlow(LiveDurationUiModel.empty())

    private val _liveReport = combine(
        _trafficMetric, _liveDuration,
    ) { trafficMetric, liveDuration ->
        LiveReportUiState(
            trafficMetric,
            liveDuration,
        )
    }

    val uiState: Flow<PlayBroadcastSummaryUiState> = combine(
        _liveReport.distinctUntilChanged(),
        _trafficMetric, /** TODO("will be removed later") */
    ) { liveReport, _ ->
        PlayBroadcastSummaryUiState(
            liveReport = liveReport,
        )
    }.flowOn(dispatcher.computation)


    private val _uiEvent = MutableSharedFlow<PlayBroadcastSummaryEvent>()
    val uiEvent: Flow<PlayBroadcastSummaryEvent>
        get() = _uiEvent

    val observableSaveVideo: LiveData<NetworkResult<Boolean>>
        get() = _observableSaveVideo
    private val _observableSaveVideo = MutableLiveData<NetworkResult<Boolean>>()

    private val addedTags: Set<String>
        get() = _observableAddedTags.value.orEmpty()
    private val _observableAddedTags = MutableLiveData<Set<String>>(mutableSetOf())

    val observableRecommendedTagsModel: LiveData<List<PlayTagUiModel>>
        get() = _observableRecommendedTagsModel

    private val _observableRecommendedTags = MutableLiveData<Set<String>>()
    private val _observableRecommendedTagsModel = MediatorLiveData<List<PlayTagUiModel>>().apply {
        addSource(_observableAddedTags) { addedTags ->
            value = _observableRecommendedTags.value.orEmpty().map { tag ->
                PlayTagUiModel(
                    tag = tag,
                    isChosen = addedTags.contains(tag)
                )
            }
        }
        addSource(_observableRecommendedTags) { recommendedTags ->
            value = recommendedTags.map { tag ->
                PlayTagUiModel(
                    tag = tag,
                    isChosen = addedTags.contains(tag)
                )
            }
        }
    }

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
            PlayBroadcastSummaryAction.ClickPostVideoNow -> handleClickPostVideoNow()
        }
    }

    private fun handleClickCloseReportPage() {
        viewModelScope.launch {
            _uiEvent.emit(PlayBroadcastSummaryEvent.CloseReportPage)
        }
    }

    private fun handleClickViewLeaderboard() {
        viewModelScope.launch {
            _uiEvent.emit(PlayBroadcastSummaryEvent.OpenLeaderboardBottomSheet)
        }
    }

    private fun handleClickPostVideo() {
        viewModelScope.launch {
            _uiEvent.emit(PlayBroadcastSummaryEvent.OpenPostVideoPage)
        }
    }

    private fun handleClickPostVideoNow() {

    }

    fun fetchLiveTraffic() {
        viewModelScope.launchCatchError(block = {
            _trafficMetric.emit(NetworkResult.Loading)

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

            _liveDuration.value = playBroadcastMapper.mapLiveDuration(
                                        getCurrentDate(),
                                        reportChannelSummary.duration,
                                        isEligiblePostVideo(reportChannelSummary.duration),
                                    )
            _trafficMetric.value = NetworkResult.Success(playBroadcastMapper.mapToLiveTrafficUiMetrics(reportChannelSummary.channel.metrics))

            if(!isEligiblePostVideo(reportChannelSummary.duration))
                _uiEvent.emit(PlayBroadcastSummaryEvent.ShowInfo(UiString.Resource(R.string.play_bro_cant_post_video_message)))
        }) {
            _liveDuration.value = LiveDurationUiModel.empty()
            _trafficMetric.value = NetworkResult.Fail(it) { fetchLiveTraffic() }

            _uiEvent.emit(PlayBroadcastSummaryEvent.ShowInfo(UiString.Resource(R.string.play_bro_cant_post_video_message)))
        }
    }

    private fun getCurrentDate() = PlayDateTimeFormatter.getTodayDateTime(PlayDateTimeFormatter.dMMMMyyyy)

    private fun isEligiblePostVideo(duration: String): Boolean {
        return try {
            val split = duration.split(":")
            (split.size == 3 && split[1].toInt() > 0) || (split.size == 2 && split[0].toInt() > 0)
        }
        catch (e: Exception) {
            false
        }
    }

    private fun getTags() {
        viewModelScope.launchCatchError(block = {
            val recommendedTags = getRecommendedTags().toSet()
            val addedNotRecommendedTags = withContext(dispatcher.computation) { addedTags - recommendedTags }
            _observableRecommendedTags.value = addedNotRecommendedTags + recommendedTags
        }) {}
    }

    private suspend fun getRecommendedTags(): List<String> = withContext(dispatcher.io) {
        val recommendedTags = getRecommendedChannelTagsUseCase.apply {
            setChannelId(channelId)
        }.executeOnBackground()

        return@withContext recommendedTags.recommendedTags.tags
    }

    fun toggleTag(tag: String) {
        val oldAddedTags = addedTags
        val newAddedTags = if (!oldAddedTags.contains(tag)) oldAddedTags + tag
                            else oldAddedTags - tag

        _observableAddedTags.value = newAddedTags
    }

    fun saveVideo() {
        _observableSaveVideo.value = NetworkResult.Loading
        viewModelScope.launchCatchError(block = {
            withContext(dispatcher.io) {
                saveTag()
                updateChannelStatus()
            }
            _observableSaveVideo.value = NetworkResult.Success(true)
        }) {
            _observableSaveVideo.value = NetworkResult.Fail(it) { saveVideo() }
        }
    }

    private suspend fun saveTag() {
        val isSuccess = setChannelTagsUseCase.apply {
            setParams(channelId, addedTags.toSet())
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

    companion object {
        private const val LIVE_STATISTICS_DELAY = 300L
        private const val FETCH_REPORT_MAX_RETRY = 3
    }
}