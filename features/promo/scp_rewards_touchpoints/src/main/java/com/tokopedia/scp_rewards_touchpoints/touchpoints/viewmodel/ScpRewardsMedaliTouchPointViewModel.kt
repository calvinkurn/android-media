package com.tokopedia.scp_rewards_touchpoints.touchpoints.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.ScpResult
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.touchpoints.domain.ScpRewardsMedaliTouchPointUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class ScpRewardsMedaliTouchPointViewModel @Inject constructor(
    private val touchPointUseCase: ScpRewardsMedaliTouchPointUseCase
) : ViewModel() {

    companion object{
        private const val DEFAULT_DELAY = 1000L
    }

    private val _toasterLiveData: MutableLiveData<ScpResult> = MutableLiveData()
    val touchPointLiveData: LiveData<ScpResult> = _toasterLiveData

    fun getTouchPoint(orderID: Long, pageName: String = "", sourceName: String,delay:Boolean = true) {
        viewModelScope.launchCatchError(
            block = {
                if(delay){
                    delay(DEFAULT_DELAY)
                }
                _toasterLiveData.postValue(Loading)
                val response = touchPointUseCase.getTouchPoint(
                    orderID,
                    pageName,
                    sourceName
                )
                if (response.scpRewardsMedaliTouchpointOrder?.resultStatus?.code == "200") {
                    _toasterLiveData.postValue(Success(response))
                } else {
                    throw Throwable()
                }
            },
            onError = {
                _toasterLiveData.postValue(Error(it))
            }
        )
    }
}
