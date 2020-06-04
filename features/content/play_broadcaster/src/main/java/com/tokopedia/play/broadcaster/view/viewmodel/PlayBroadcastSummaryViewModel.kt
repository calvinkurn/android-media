package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.broadcaster.data.model.BroadcasterReportLiveSummaries
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlaySummaryUiMapper
import com.tokopedia.play.broadcaster.ui.model.SummaryUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by jessica on 27/05/20
 */

class PlayBroadcastSummaryViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) private val dispatcher: CoroutineDispatcher,
        val graphqlRepository: GraphqlRepository
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    private val _observableSummary = MutableLiveData<SummaryUiModel>()
    val observableSummary: LiveData<SummaryUiModel>
        get() = _observableSummary

    private val _observableTrafficMetrics = MutableLiveData<List<TrafficMetricUiModel>>()
    val observableTrafficMetricsUiModel: LiveData<List<TrafficMetricUiModel>>
        get() = _observableTrafficMetrics

    init {
        _observableSummary.value = PlayBroadcastMocker.getSummary()
        _observableTrafficMetrics.value = listOf()
    }

    fun getSummaryLiveReport(rawQuery: String, channelId: String) {
        scope.launch {
            try {
                val params = mapOf(PARAM_CHANNEL_ID to channelId)
                val graphqlRequest = GraphqlRequest(rawQuery, BroadcasterReportLiveSummaries.Response::class.java, params)

                val data = withContext(Dispatchers.IO + job) {
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }.getSuccessData<BroadcasterReportLiveSummaries.Response>()
                _observableTrafficMetrics.postValue(PlaySummaryUiMapper.mapToLiveTrafficUiMetrics(data.response.channel.metrics))
            } catch (t: Throwable) {

            }
        }
    }

    companion object {
        const val PARAM_CHANNEL_ID = "channelID"
    }
}