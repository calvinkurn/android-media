package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.UpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.PlayChannelStatus
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * @author by jessica on 27/05/20
 */

class PlayBroadcastSummaryViewModel @Inject constructor(
        private val channelConfigStore: ChannelConfigStore,
        private val dispatcher: CoroutineDispatcherProvider,
        private val getLiveStatisticsUseCase: GetLiveStatisticsUseCase,
        private val updateChannelUseCase: UpdateChannelUseCase,
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

    fun fetchLiveTraffic() {
        _observableTrafficMetrics.value = NetworkResult.Loading
        scope.launchCatchError(block = {
            val liveMetrics = withContext(dispatcher.io) {
                getLiveStatisticsUseCase.params = GetLiveStatisticsUseCase.createParams(channelId)
                return@withContext getLiveStatisticsUseCase.executeOnBackground()
            }
            _observableTrafficMetrics.value = NetworkResult.Success(playBroadcastMapper.mapToLiveTrafficUiMetrics(liveMetrics))
        }) {
            _observableTrafficMetrics.value = NetworkResult.Fail(it) { fetchLiveTraffic() }
        }

    }

    // TODO: ask PO last status this use case
    fun saveVideo() {
        _observableSaveVideo.value = NetworkResult.Loading
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                delay(2000)
            }
            _observableSaveVideo.value = NetworkResult.Success(true)
        }) {
            _observableSaveVideo.value = NetworkResult.Fail(it) { saveVideo() }
        }
    }

    fun deleteVideo() {
        _observableDeleteVideo.value = NetworkResult.Loading
        scope.launchCatchError(block = {
            // TODO: testing only
            withContext(dispatcher.io) {
                delay(2000)
            }
//            withContext(dispatcher.io) {
//                updateChannelUseCase.apply {
//                    setQueryParams(
//                            UpdateChannelUseCase.createUpdateStatusRequest(
//                                    channelId = channelId,
//                                    authorId = userSession.shopId,
//                                    status = PlayChannelStatus.Deleted
//                            )
//                    )
//                }.executeOnBackground()
//            }
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