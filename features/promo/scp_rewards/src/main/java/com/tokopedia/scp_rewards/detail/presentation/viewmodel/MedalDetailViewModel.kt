package com.tokopedia.scp_rewards.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.scp_rewards.common.constants.SUCCESS_CODE
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.MedalDetailUseCase
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards_widgets.medal_footer.FooterData
import javax.inject.Inject

class MedalDetailViewModel @Inject constructor(
    private val medalDetailUseCase: MedalDetailUseCase,
    private val couponAutoApplyUseCase: CouponAutoApplyUseCase
) : ViewModel() {

    var couponCode: String = ""
        private set
    var couponStatus: String = ""
        private set
    var couponNotes: String = ""
        private set

    private val _badgeLiveData: MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val badgeLiveData: LiveData<ScpResult> = _badgeLiveData

    private val _autoApplyCoupon: MutableLiveData<AutoApplyState> = MutableLiveData()
    val autoApplyCoupon: LiveData<AutoApplyState> = _autoApplyCoupon

    fun getMedalDetail(medaliSlug: String = "", sourceName: String, pageName: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val response = medalDetailUseCase.getMedalDetail(
                    medaliSlug = medaliSlug,
                    sourceName = sourceName,
                    pageName = pageName
                )
                when (val responseCode = response.detail?.resultStatus?.code) {
                    SUCCESS_CODE -> {
                        _badgeLiveData.postValue(Success(response))
                        setupAnalyticsData(response)
                    }

                    else -> {
                        _badgeLiveData.postValue(Error(Throwable(), responseCode.orEmpty()))
                    }
                }
            },
            onError = {
                _badgeLiveData.postValue(Error(it))
            }
        )
    }

    fun applyCoupon(footerData: FooterData, shopId: Int? = null, couponCode: String) {
        viewModelScope.launchCatchError(
            block = {
                _autoApplyCoupon.postValue(AutoApplyState.Loading(footerData))
                val response = couponAutoApplyUseCase.applyCoupon(shopId, couponCode)
                if (response.data != null && response.data.resultStatus.code == SUCCESS_CODE) {
                    if (response.data.couponAutoApply?.isSuccess == true) {
                        _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponApplied(footerData, response.data))
                    } else {
                        _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponFailed(footerData, response.data))
                    }
                } else {
                    throw Throwable()
                }
            },
            onError = {
                _autoApplyCoupon.postValue(AutoApplyState.Error(footerData, it))
            }
        )
    }

    private fun setupAnalyticsData(response: MedalDetailResponseModel) {
        couponCode = response.detail?.medaliDetailPage?.benefitButtons
            ?.firstOrNull { it.couponCode.isNullOrEmpty().not() }?.couponCode.orEmpty()
        couponStatus =
            response.detail?.medaliDetailPage?.benefits?.first()?.status.orEmpty()
        couponNotes =
            response.detail?.medaliDetailPage?.benefits?.first()?.statusDescription.orEmpty()
    }

    sealed class AutoApplyState {
        data class Loading(val footerData: FooterData) : AutoApplyState()
        data class SuccessCouponApplied(val footerData: FooterData, val data: ScpRewardsCouponAutoApply?) : AutoApplyState()
        data class SuccessCouponFailed(val footerData: FooterData, val data: ScpRewardsCouponAutoApply?) : AutoApplyState()
        data class Error(val footerData: FooterData, val throwable: Throwable) : AutoApplyState()
    }
}
