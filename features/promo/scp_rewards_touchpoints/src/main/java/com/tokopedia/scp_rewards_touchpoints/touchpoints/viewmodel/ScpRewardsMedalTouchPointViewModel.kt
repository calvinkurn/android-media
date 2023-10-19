package com.tokopedia.scp_rewards_touchpoints.touchpoints.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.ScpResult
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse
import com.tokopedia.scp_rewards_touchpoints.touchpoints.domain.ScpRewardsMedalTouchPointUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

class ScpRewardsMedalTouchPointViewModel @Inject constructor(
    private val medalTouchPointUseCase: ScpRewardsMedalTouchPointUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val INITIAL_DELAY = 0L
    }

    private val _medalTouchPointData: MutableLiveData<ScpTouchPointResult> = MutableLiveData()
    val medalTouchPointData: LiveData<ScpTouchPointResult> = _medalTouchPointData

    private var touchPointJob: Job? = null

    fun getMedalTouchPoint(
        orderId: Long,
        pageName: String = String.EMPTY,
        sourceName: String,
        delayTime: Long = INITIAL_DELAY,
        initialLoad: Boolean = false
    ) {
        touchPointJob?.cancel()
        touchPointJob = launchCatchError(
            block = {
                if (delayTime != INITIAL_DELAY) {
                    delay(delayTime)
                }
                _medalTouchPointData.postValue(
                    ScpTouchPointResult(
                        initialLoad = initialLoad,
                        result = Loading
                    )
                )
                val response = medalTouchPointUseCase.getTouchPoint(
                    orderId = orderId,
                    pageName = pageName,
                    sourceName = sourceName
                )
                if (response.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.retryChecking.isRetryable) {
                    getMedalTouchPoint(
                        orderId = orderId,
                        pageName = pageName,
                        sourceName = sourceName,
                        delayTime = response.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.retryChecking.durationToRetry,
                        initialLoad = initialLoad
                    )
                } else {
                    _medalTouchPointData.postValue(
                        ScpTouchPointResult(
                            initialLoad = initialLoad,
                            result = Success(response)
                        )
                    )
                }
            },
            onError = {
                _medalTouchPointData.postValue(
                    ScpTouchPointResult(
                        initialLoad = initialLoad,
                        result = Error(it)
                    )
                )
            }
        )
    }

    data class ScpTouchPointResult(
        val initialLoad: Boolean,
        val result: ScpResult<ScpRewardsMedalTouchPointResponse>
    )
}
