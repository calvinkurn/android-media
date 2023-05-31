package com.tokopedia.scp_rewards.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.MedalDetailUseCase
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import javax.inject.Inject

class MedalDetailViewModel @Inject constructor(
    private val medalDetailUseCase: MedalDetailUseCase,
    private val couponAutoApplyUseCase: CouponAutoApplyUseCase
) : ViewModel() {

    private companion object {
        private const val SUCCESS_CODE = "200"
    }

    private val _badgeLiveData: MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val badgeLiveData: LiveData<ScpResult> = _badgeLiveData

    private val _autoApplyCoupon: MutableLiveData<AutoApplyState> = MutableLiveData()
    val autoApplyCoupon: LiveData<AutoApplyState> = _autoApplyCoupon

    fun getMedalDetail(medaliSlug: String = "", sourceName: String = "", pageName: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val response = medalDetailUseCase.getMedalDetail(
                    medaliSlug = "INJECT_BADGE_1",
                    sourceName = "celebration_page",
                    pageName = ""
                )
                if (response.detail?.resultStatus?.code == SUCCESS_CODE) {
                    _badgeLiveData.postValue(Success(response))
                } else {
                    throw Throwable()
                }
            },
            onError = {
                _badgeLiveData.postValue(Error(it))
            }
        )
    }

    fun applyCoupon(id: Int, shopId: Int? = null, couponCode: String) {
        viewModelScope.launchCatchError(
            block = {
                _autoApplyCoupon.postValue(AutoApplyState.Loading(id))
                val response = couponAutoApplyUseCase.applyCoupon(shopId, couponCode)
                if (response.data != null) {
                    if (response.data.couponAutoApply?.isSuccess == true) {
                        _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponApplied(id, response.data))
                    } else {
                        _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponFailed(id, response.data))
                    }
                } else {
                    throw Throwable()
                }
            },
            onError = {
                _autoApplyCoupon.postValue(AutoApplyState.Error(id, it))
            }
        )
    }

    sealed class AutoApplyState {
        data class Loading(val id: Int) : AutoApplyState()
        data class SuccessCouponApplied(val id: Int, val data: ScpRewardsCouponAutoApply?) : AutoApplyState()
        data class SuccessCouponFailed(val id: Int, val data: ScpRewardsCouponAutoApply?) : AutoApplyState()
        data class Error(val id: Int, val throwable: Throwable) : AutoApplyState()
    }
}
