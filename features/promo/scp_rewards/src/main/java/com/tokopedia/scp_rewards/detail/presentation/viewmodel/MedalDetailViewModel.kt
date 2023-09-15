package com.tokopedia.scp_rewards.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.scp_rewards.common.constants.SUCCESS_CODE
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.GetMedalBenefitUseCase
import com.tokopedia.scp_rewards.detail.domain.MedalDetailUseCase
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefitList
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards_widgets.common.model.CtaButton
import javax.inject.Inject

const val MDP_SECTION_TYPE_BENEFIT = "benefit"

class MedalDetailViewModel @Inject constructor(
    private val medalDetailUseCase: MedalDetailUseCase,
    private val getMedalBenefitUseCase: GetMedalBenefitUseCase,
    private val couponAutoApplyUseCase: CouponAutoApplyUseCase
) : ViewModel() {

    var couponCode: String = ""
        private set
    var couponStatus: String = ""
        private set
    var couponNotes: String = ""
        private set

    private val _badgeLiveData: MutableLiveData<MdpState> = MutableLiveData(MdpState.Loading)
    val badgeLiveData: LiveData<MdpState> = _badgeLiveData

    private val _autoApplyCoupon: MutableLiveData<AutoApplyState> = MutableLiveData()
    val autoApplyCoupon: LiveData<AutoApplyState> = _autoApplyCoupon

    fun getMedalDetail(medaliSlug: String = "", sourceName: String, pageName: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val mdpResponse = medalDetailUseCase.getMedalDetail(
                    medaliSlug = medaliSlug,
                    sourceName = sourceName,
                    pageName = pageName
                )

                val benefitResponse = getMedalBenefits(mdpResponse, medaliSlug, sourceName, pageName)

                when (val responseCode = mdpResponse.detail?.resultStatus?.code) {
                    SUCCESS_CODE -> {
                        _badgeLiveData.postValue(MdpState.Success(mdpResponse, benefitResponse))
                        setupAnalyticsData(mdpResponse)
                    }

                    else -> {
                        _badgeLiveData.postValue(MdpState.Error(Throwable(), responseCode.orEmpty()))
                    }
                }
            },
            onError = {
                _badgeLiveData.postValue(MdpState.Error(it))
            }
        )
    }

    private suspend fun getMedalBenefits(
        mdpResponse: MedalDetailResponseModel,
        medaliSlug: String = "",
        sourceName: String,
        pageName: String = ""
    ): MedaliBenefitList? {
        return if (mdpResponse.detail?.medaliDetailPage?.section?.any { it.type == MDP_SECTION_TYPE_BENEFIT } == true) {
            try {
                val response = getMedalBenefitUseCase.getMedalBenefits(
                    medaliSlug = medaliSlug,
                    sourceName = sourceName,
                    pageName = pageName
                )

                response.scpRewardsMedaliBenefitList?.medaliBenefitList
            } catch (exp: Exception) {
                FirebaseCrashlytics.getInstance().recordException(exp)
                MedaliBenefitList(emptyList())
            }
        } else {
            null
        }
    }

    fun applyCoupon(ctaButton: CtaButton?, shopId: Int? = null, couponCode: String) {
        viewModelScope.launchCatchError(
            block = {
                _autoApplyCoupon.postValue(AutoApplyState.Loading)
                val response = couponAutoApplyUseCase.applyCoupon(shopId, couponCode)
                if (response.data != null && response.data.resultStatus.code == SUCCESS_CODE) {
                    if (response.data.couponAutoApply?.isSuccess == true) {
                        _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponApplied(ctaButton, response.data))
                    } else {
                        _autoApplyCoupon.postValue(AutoApplyState.SuccessCouponFailed(ctaButton, response.data))
                    }
                } else {
                    throw Throwable()
                }
            },
            onError = {
                _autoApplyCoupon.postValue(AutoApplyState.Error(ctaButton, it))
            }
        )
    }

    private fun setupAnalyticsData(response: MedalDetailResponseModel) {
        couponCode = response.detail?.medaliDetailPage?.benefitButtons
            ?.firstOrNull { it.couponCode.isNullOrEmpty().not() }?.couponCode.orEmpty()
        couponStatus =
            response.detail?.medaliDetailPage?.benefits?.firstOrNull()?.status.orEmpty()
        couponNotes =
            response.detail?.medaliDetailPage?.benefits?.firstOrNull()?.statusDescription.orEmpty()
    }

    sealed class MdpState {
        class Success(
            val data: MedalDetailResponseModel,
            val benefitData: MedaliBenefitList?
        ) : MdpState()

        class Error(val error: Throwable, val errorCode: String = "") : MdpState()
        object Loading : MdpState()
    }

    sealed class AutoApplyState {
        object Loading : AutoApplyState()
        data class SuccessCouponApplied(val ctaButton: CtaButton?, val data: ScpRewardsCouponAutoApply?) : AutoApplyState()
        data class SuccessCouponFailed(val ctaButton: CtaButton?, val data: ScpRewardsCouponAutoApply?) : AutoApplyState()
        data class Error(val ctaButton: CtaButton?, val throwable: Throwable) : AutoApplyState()
    }
}
