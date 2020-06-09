package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlaySummaryUiMapper
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by jessica on 27/05/20
 */

class PlayBroadcastSummaryViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) private val dispatcher: CoroutineDispatcher,
        private val getLiveStatisticsUseCase: GetLiveStatisticsUseCase
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    private val _observableTrafficMetrics = MutableLiveData<List<TrafficMetricUiModel>>()
    val observableTrafficMetricsUiModel: LiveData<List<TrafficMetricUiModel>>
        get() = _observableTrafficMetrics

    init {
        _observableTrafficMetrics.value = listOf()
    }

    fun getLiveTrafficMetrics(channelId: String) {
        scope.launch {
            try {
                val response = getLiveStatisticsUseCase.apply {
                    params = GetLiveStatisticsUseCase.createParams(channelId)
                }.executeOnBackground()
                _observableTrafficMetrics.postValue(PlaySummaryUiMapper.mapToLiveTrafficUiMetrics(response.response.channel.metrics))
            } catch (t: Throwable) { }
        }
    }
}