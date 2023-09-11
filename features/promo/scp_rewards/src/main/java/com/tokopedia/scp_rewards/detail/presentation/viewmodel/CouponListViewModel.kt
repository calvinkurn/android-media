package com.tokopedia.scp_rewards.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.scp_rewards.common.constants.SUCCESS_CODE
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.GetMedalBenefitUseCase
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards.detail.mappers.MedalBenefitMapper
import com.tokopedia.scp_rewards_widgets.constants.CouponStatus
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import javax.inject.Inject

class CouponListViewModel @Inject constructor(
        private val getMedalBenefitUseCase: GetMedalBenefitUseCase,
        private val couponAutoApplyUseCase: CouponAutoApplyUseCase,
) : ViewModel() {

    var couponPageStatus: String? = ""

    private val _couponListLiveData: MutableLiveData<CouponState> = MutableLiveData(CouponState.Loading)
    val couponListLiveData: LiveData<CouponState> = _couponListLiveData

    private val _autoApplyCoupon: MutableLiveData<AutoApplyState> = MutableLiveData()
    val autoApplyCoupon: LiveData<AutoApplyState> = _autoApplyCoupon

    fun getCouponList(medaliSlug: String = "", sourceName: String, pageName: String = "") {
        viewModelScope.launchCatchError(
                block = {
                    val response = getMedalBenefitUseCase.getMedalBenefits(
                            medaliSlug = medaliSlug,
                            sourceName = sourceName,
                            pageName = pageName,
                            type = couponPageStatus.orEmpty()
                    )

                    when (val responseCode = response.scpRewardsMedaliBenefitList?.resultStatus?.code) {
                        SUCCESS_CODE -> {
                            val list =
                                MedalBenefitMapper.mapBenefitApiResponseToBenefitModelList(response.scpRewardsMedaliBenefitList.medaliBenefitList?.benefitList)
                            when (list?.firstOrNull()?.status) {
                                CouponStatus.EMPTY -> {
                                    _couponListLiveData.postValue(CouponState.HistoryTabEmpty)
                                }
                                CouponStatus.ERROR -> {
                                    _couponListLiveData.postValue(CouponState.Error(Throwable()))
                                }
                                else -> {
                                    _couponListLiveData.postValue(CouponState.Success(list))
                                }
                            }
                        }

                        else -> {
                            _couponListLiveData.postValue(CouponState.Error(Throwable(), responseCode.orEmpty()))
                        }
                    }
                },
                onError = {
                    _couponListLiveData.postValue(CouponState.Error(it))
                }
        )
    }

    fun applyCoupon(benefitModel: MedalBenefitModel, shopId: Int? = null, couponCode: String, position: Int) {
        viewModelScope.launchCatchError(
                block = {
                    _autoApplyCoupon.postValue(AutoApplyState.Loading(benefitModel, position))
                    val response = couponAutoApplyUseCase.applyCoupon(shopId, couponCode)
                    if (response.data != null && response.data.resultStatus.code == SUCCESS_CODE) {
                        if (response.data.couponAutoApply?.isSuccess == true) {
                            _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponApplied(response.data, benefitModel, position))
                        } else {
                            _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponFailed(response.data, benefitModel, position))
                        }
                    } else {
                        throw Throwable()
                    }
                },
                onError = {
                    _autoApplyCoupon.postValue(AutoApplyState.Error(it, benefitModel, position))
                }
        )
    }

    fun setCouponsList(list: List<MedalBenefitModel>?) {
        when (couponPageStatus) {
            CouponStatus.EMPTY -> _couponListLiveData.postValue(CouponState.ActiveTabEmpty)
            else -> {
                val isLocked = couponPageStatus == CouponStatus.INACTIVE
                _couponListLiveData.postValue(CouponState.Success(list, isLocked))
            }
        }
    }


    sealed class CouponState {
        class Success(val list: List<MedalBenefitModel>?, val isLocked: Boolean = false) : CouponState()
        class Error(val error: Throwable, val errorCode: String = "") : CouponState()
        object ActiveTabEmpty : CouponState()
        object HistoryTabEmpty : CouponState()
        object Loading : CouponState()
    }

    sealed class AutoApplyState {
        data class Loading(val benefit: MedalBenefitModel, val position: Int) : AutoApplyState()
        data class SuccessCouponApplied(val data: ScpRewardsCouponAutoApply?, val benefit: MedalBenefitModel, val position: Int) : AutoApplyState()
        data class SuccessCouponFailed(val data: ScpRewardsCouponAutoApply?, val benefit: MedalBenefitModel, val position: Int) : AutoApplyState()
        data class Error(val throwable: Throwable, val benefit: MedalBenefitModel, val position: Int) : AutoApplyState()
    }
}
