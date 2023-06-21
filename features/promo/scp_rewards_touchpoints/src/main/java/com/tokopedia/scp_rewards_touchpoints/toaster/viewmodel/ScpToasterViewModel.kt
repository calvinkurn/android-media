package com.tokopedia.scp_rewards_touchpoints.toaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.ScpResult
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.toaster.domain.ScpToasterUseCase
import javax.inject.Inject

class ScpToasterViewModel @Inject constructor(
    private val scpToasterUseCase: ScpToasterUseCase
) : ViewModel() {

    private val _toasterLiveData: MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val toasterLiveData: LiveData<ScpResult> = _toasterLiveData

    fun getToaster(orderID:Int, pageName:String, sourceName:String) {
        viewModelScope.launchCatchError(
            block = {
                val response = scpToasterUseCase.getToaster(
                    orderID, pageName, sourceName
                )
                if (response?.scpRewardsToasterTouchpointOrder?.resultStatus?.code == "200") {
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
