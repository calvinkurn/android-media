package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.LiveDurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
        private val playBroadcastMapper: PlayBroadcastMapper
) : ViewModel() {

    private val channelId: String
        get() = channelConfigStore.getChannelId()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val observableTrafficMetrics: LiveData<NetworkResult<List<TrafficMetricUiModel>>>
        get() = _observableTrafficMetrics
    private val _observableTrafficMetrics = MutableLiveData<NetworkResult<List<TrafficMetricUiModel>>>()

    val observableSaveVideo: LiveData<NetworkResult<Boolean>>
        get() = _observableSaveVideo
    private val _observableSaveVideo = MutableLiveData<NetworkResult<Boolean>>()

    val observableDeleteVideo: LiveData<NetworkResult<Boolean>>
        get() = _observableDeleteVideo
    private val _observableDeleteVideo = MutableLiveData<NetworkResult<Boolean>>()

    val observableReportDuration: LiveData<LiveDurationUiModel>
        get() = _observableReportDuration
    private val _observableReportDuration = MutableLiveData<LiveDurationUiModel>()

    fun fetchLiveTraffic() {
        _observableTrafficMetrics.value = NetworkResult.Loading
        scope.launchCatchError(block = {
            val reportChannelSummary = withContext(dispatcher.io) {
                getLiveStatisticsUseCase.params = GetLiveStatisticsUseCase.createParams(channelId)
                return@withContext getLiveStatisticsUseCase.executeOnBackground()
            }
            _observableReportDuration.value = playBroadcastMapper.mapLiveDuration(reportChannelSummary.duration)
            _observableTrafficMetrics.value = NetworkResult.Success(playBroadcastMapper.mapToLiveTrafficUiMetrics(reportChannelSummary.channel.metrics))
        }) {
            _observableTrafficMetrics.value = NetworkResult.Fail(it) { fetchLiveTraffic() }
        }

    }

    fun saveVideo() {
        _observableSaveVideo.value = NetworkResult.Loading
        scope.launchCatchError(block = {
            updateChannelUseCase.apply {
                setQueryParams(
                        UpdateChannelUseCase.createUpdateStatusRequest(
                                channelId = channelId,
                                authorId = userSession.shopId,
                                status = PlayChannelStatusType.Transcoding
                        )
                )
            }.executeOnBackground()
            _observableSaveVideo.value = NetworkResult.Success(true)
        }) {
            _observableSaveVideo.value = NetworkResult.Fail(it) { saveVideo() }
        }
    }

    fun deleteVideo() {
        _observableDeleteVideo.value = NetworkResult.Loading
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                updateChannelUseCase.apply {
                    setQueryParams(
                            UpdateChannelUseCase.createUpdateStatusRequest(
                                    channelId = channelId,
                                    authorId = userSession.shopId,
                                    status = PlayChannelStatusType.Deleted
                            )
                    )
                }.executeOnBackground()
            }
            _observableDeleteVideo.value = NetworkResult.Success(true)
        }) {
            _observableDeleteVideo.value = NetworkResult.Fail(it) { deleteVideo() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}