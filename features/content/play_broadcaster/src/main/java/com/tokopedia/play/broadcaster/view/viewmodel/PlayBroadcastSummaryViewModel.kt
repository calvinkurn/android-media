package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
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
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase,
) : ViewModel() {

    private val channelId: String
        get() = channelConfigStore.getChannelId()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val observableLiveSummary: LiveData<NetworkResult<List<TrafficMetricUiModel>>>
        get() = _observableLiveSummary
    private val _observableLiveSummary = MutableLiveData<NetworkResult<List<TrafficMetricUiModel>>>()

    val observableSaveVideo: LiveData<NetworkResult<Boolean>>
        get() = _observableSaveVideo
    private val _observableSaveVideo = MutableLiveData<NetworkResult<Boolean>>()

    val observableDeleteVideo: LiveData<NetworkResult<Boolean>>
        get() = _observableDeleteVideo
    private val _observableDeleteVideo = MutableLiveData<NetworkResult<Boolean>>()

    val observableReportDuration: LiveData<LiveDurationUiModel>
        get() = _observableReportDuration
    private val _observableReportDuration = MutableLiveData<LiveDurationUiModel>()

    val addedTags: Set<String>
        get() = _observableAddedTags.value.orEmpty()

    val observableRecommendedTagsModel: LiveData<List<PlayTagUiModel>>
        get() = _observableRecommendedTagsModel

    private val _observableAddedTags = MutableLiveData(setupDataStore.getTags())
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
        getTags()
    }

    fun fetchLiveTraffic() {
        _observableLiveSummary.value = NetworkResult.Loading
        scope.launchCatchError(block = {
            val reportChannelSummary = withContext(dispatcher.io) {
                delay(LIVE_STATISTICS_DELAY)
                /** TODO("remove hardcoded id") */
                getLiveStatisticsUseCase.params = GetLiveStatisticsUseCase.createParams("328076")
                return@withContext getLiveStatisticsUseCase.executeOnBackground()
            }
            _observableReportDuration.value = playBroadcastMapper.mapLiveDuration(reportChannelSummary.duration)
            _observableLiveSummary.value = NetworkResult.Success(playBroadcastMapper.mapToLiveTrafficUiMetrics(reportChannelSummary.channel.metrics))
        }) {
            _observableLiveSummary.value = NetworkResult.Fail(it) { fetchLiveTraffic() }
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
            /** TODO("remove hardcoded channelID") */
            setChannelId("328076")
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
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                /** TODO("remove delay") */
                delay(2000)

                saveTag()

                /** TODO("uncomment usecase") */
                updateChannelStatus()
            }
            _observableSaveVideo.value = NetworkResult.Success(true)
        }) {
            _observableSaveVideo.value = NetworkResult.Fail(it) { saveVideo() }
        }
    }

    private suspend fun saveTag() {
        setupDataStore.setTags(addedTags)
        uploadTags()
    }

    private suspend fun uploadTags() {
        val isSuccess = setupDataStore.uploadTags(channelConfigStore.getChannelId())
        if (!isSuccess) throw DefaultErrorThrowable("${DefaultErrorThrowable.DEFAULT_MESSAGE}: Error Tag")
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

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    companion object {
        private const val LIVE_STATISTICS_DELAY = 300L
    }
}