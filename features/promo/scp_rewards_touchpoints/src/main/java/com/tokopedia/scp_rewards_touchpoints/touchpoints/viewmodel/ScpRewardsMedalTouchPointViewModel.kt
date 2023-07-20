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
import com.tokopedia.scp_rewards_touchpoints.touchpoints.domain.ScpRewardsMedalTouchPointUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class ScpRewardsMedalTouchPointViewModel @Inject constructor(
    private val medalTouchPointUseCase: ScpRewardsMedalTouchPointUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object{
        private const val DEFAULT_DELAY = 1000L
    }

    private val _medalTouchPointData: MutableLiveData<ScpResult> = MutableLiveData()
    val medalTouchPointData: LiveData<ScpResult> = _medalTouchPointData

    fun getMedalTouchPoint(
        orderId: Long,
        pageName: String = String.EMPTY,
        sourceName: String,
        delayDuration: Long = DEFAULT_DELAY
    ) {
        launchCatchError(
            block = {
                _medalTouchPointData.postValue(Loading)
                delay(
                    timeMillis = delayDuration
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
                        delayDuration = response.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.retryChecking.durationToRetry
                    )
                } else {
                    _medalTouchPointData.postValue(Success(response))
                }
            },
            onError = {
                _medalTouchPointData.postValue(Error(it))
            }
        )
    }
}
