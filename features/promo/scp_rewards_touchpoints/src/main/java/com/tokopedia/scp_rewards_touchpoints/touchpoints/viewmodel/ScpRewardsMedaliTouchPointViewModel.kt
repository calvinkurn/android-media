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
import com.tokopedia.scp_rewards_touchpoints.touchpoints.domain.ScpRewardsMedaliTouchPointUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class ScpRewardsMedaliTouchPointViewModel @Inject constructor(
    private val touchPointUseCase: ScpRewardsMedaliTouchPointUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object{
        private const val DEFAULT_DELAY = 1000L
    }

    private val _touchPointData: MutableLiveData<ScpResult> = MutableLiveData()
    val touchPointData: LiveData<ScpResult> = _touchPointData

    fun getTouchPoint(
        orderId: Long,
        pageName: String = String.EMPTY,
        sourceName: String,
        delayDuration: Long = DEFAULT_DELAY
    ) {
        launchCatchError(
            block = {
                _touchPointData.postValue(Loading)
                delay(
                    timeMillis = delayDuration
                )
                val response = touchPointUseCase.getTouchPoint(
                    orderId = orderId,
                    pageName = pageName,
                    sourceName = sourceName
                )
                if (response.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.retryChecking.isRetryable) {
                    getTouchPoint(
                        orderId = orderId,
                        pageName = pageName,
                        sourceName = sourceName,
                        delayDuration = response.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.retryChecking.durationToRetry
                    )
                } else {
                    _touchPointData.postValue(Success(response))
                }
            },
            onError = {
                _touchPointData.postValue(Error(it))
            }
        )
    }
}
