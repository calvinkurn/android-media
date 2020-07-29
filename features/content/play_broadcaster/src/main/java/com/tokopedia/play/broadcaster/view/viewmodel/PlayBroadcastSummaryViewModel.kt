package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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

    private val _observableTrafficMetrics = MutableLiveData<List<TrafficMetricUiModel>>()
    val observableTrafficMetrics: LiveData<List<TrafficMetricUiModel>>
        get() = _observableTrafficMetrics

    init {
        _observableTrafficMetrics.value = PlayBroadcastMocker.getMetricSummary()
    }

    // TODO("handle initial, loading & error state")
    fun getLiveTrafficMetrics(channelId: String) {
        scope.launch {
            try {
                val response = getLiveStatisticsUseCase.apply {
                    params = GetLiveStatisticsUseCase.createParams(channelId)
                }.executeOnBackground()
                _observableTrafficMetrics.postValue(PlayBroadcastUiMapper.mapToLiveTrafficUiMetrics(response.response.channel.metrics))
            } catch (t: Throwable) { }
        }
    }
}