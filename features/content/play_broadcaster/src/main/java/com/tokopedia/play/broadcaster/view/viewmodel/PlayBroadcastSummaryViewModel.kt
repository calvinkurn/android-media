package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * @author by jessica on 27/05/20
 */

class PlayBroadcastSummaryViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val getLiveStatisticsUseCase: GetLiveStatisticsUseCase
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    private val _observableTrafficMetrics = MutableLiveData<NetworkResult<List<TrafficMetricUiModel>>>()
    val observableTrafficMetrics: LiveData<NetworkResult<List<TrafficMetricUiModel>>>
        get() = _observableTrafficMetrics

    fun fetchLiveTraffic(channelId: String) {
        _observableTrafficMetrics.value = NetworkResult.Loading
        scope.launchCatchError(block = {
//            val liveMetrics = withContext(dispatcher.io) {
//                getLiveStatisticsUseCase.params = GetLiveStatisticsUseCase.createParams(channelId)
//                return@withContext getLiveStatisticsUseCase.executeOnBackground()
//            }
//            _observableTrafficMetrics.value = NetworkResult.Success(PlayBroadcastUiMapper.mapToLiveTrafficUiMetrics(liveMetrics))
            // TODO(remove mock)
            delay(2000)
            _observableTrafficMetrics.value = NetworkResult.Success(PlayBroadcastMocker.getMetricSummary())
        }) {
            _observableTrafficMetrics.value = NetworkResult.Fail(it) { fetchLiveTraffic(channelId) }
        }

    }
}