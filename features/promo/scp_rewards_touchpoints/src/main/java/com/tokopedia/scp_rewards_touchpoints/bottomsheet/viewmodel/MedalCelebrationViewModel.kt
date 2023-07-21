package com.tokopedia.scp_rewards_touchpoints.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.ScpResult
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.domain.RewardsGetMedaliCelebrationPageUseCase
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.CouponAutoApplyResponseModel
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsCelebrationModel

typealias BenefitData = ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.CelebrationPage.BenefitButton
class MedalCelebrationViewModel @Inject constructor(
    private val rewardsGetMedaliCelebrationPageUseCase: RewardsGetMedaliCelebrationPageUseCase,
    private val autoApplyUseCase: CouponAutoApplyUseCase
) : ViewModel() {

    private val _badgeLiveData:MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val badgeLiveData: LiveData<ScpResult> = _badgeLiveData

    private val _autoApplyLiveData:MutableLiveData<AutoApplyState> = MutableLiveData()
    val autoApplyLiveData: LiveData<AutoApplyState> = _autoApplyLiveData

    fun getRewards(medaliSlug:String = "",pageName:String = ""){
        viewModelScope.launchCatchError(
            block = {
                    val response = rewardsGetMedaliCelebrationPageUseCase.getRewards(
                        medaliSlug = medaliSlug,
                        pageName = pageName
                    )
                    if(response.scpRewardsCelebrationPage?.resultStatus?.code == "200"){
                        _badgeLiveData.postValue(Success(response))
                    }
                    else throw Throwable()
            },
            onError = {
                it.localizedMessage
                  _badgeLiveData.postValue(Error(it))
            }
        )
    }

    fun autoApplyCoupon(shopId: Int? = null, couponCode: String,benefitData: BenefitData?) {
       viewModelScope.launchCatchError(block = {
           _autoApplyLiveData.postValue(AutoApplyState.Loading)
           val response = autoApplyUseCase.applyCoupon(shopId,couponCode)
           if(response.data?.couponAutoApply != null){
             _autoApplyLiveData.postValue(
                 AutoApplyState.SuccessCoupon(
                     benefitData = benefitData,
                     autoApplyData = response
                 )
             )
           }
           else throw Throwable()
       }, onError = {
            _autoApplyLiveData.postValue(AutoApplyState.Error(
                benefitData = benefitData,
                throwable = it
            ))
       })
    }

    sealed class AutoApplyState {
        object Loading : AutoApplyState()
        data class SuccessCoupon(val benefitData: BenefitData?, val autoApplyData: CouponAutoApplyResponseModel) : AutoApplyState()
        data class Error(val benefitData: BenefitData?, val throwable: Throwable) : AutoApplyState()
    }

}
