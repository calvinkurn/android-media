package com.tokopedia.scp_rewards.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.scp_rewards.cabinet.domain.GetUserMedaliUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.common.constants.SUCCESS_CODE
import com.tokopedia.scp_rewards.common.utils.PAGESIZE_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_NAME_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_PARAM
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.GetMedalBenefitUseCase
import com.tokopedia.scp_rewards.detail.domain.MedalDetailUseCase
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefitList
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards_common.parseJsonKey
import com.tokopedia.scp_rewards_widgets.common.model.CtaButton
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.async
import javax.inject.Inject

const val MDP_SECTION_TYPE_BENEFIT = "benefit"
const val MDP_SECTION_TYPE_BRAND_RECOMMENDATIONS = "brand-related"
private const val PAGE_SIZE_JSON_KEY = "page_size"
private const val PAGE_NAME_JSON_KEY = "page_name"
private const val MEDALI_SLUG_JSON_KEY = "medaliSlug"
private const val TYPE_JSON_KEY = "type"

class MedalDetailViewModel @Inject constructor(
    private val medalDetailUseCase: MedalDetailUseCase,
    private val getMedalBenefitUseCase: GetMedalBenefitUseCase,
    private val couponAutoApplyUseCase: CouponAutoApplyUseCase,
    private val userMedaliUseCase: GetUserMedaliUseCase
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

                val benefitResponse =
                    async { getMedalBenefits(mdpResponse, medaliSlug, sourceName, pageName) }

                val recommendationsResponse =
                    async { getMedalRecommendations(mdpResponse) }

                when (val responseCode = mdpResponse.detail?.resultStatus?.code) {
                    SUCCESS_CODE -> {
                        _badgeLiveData.postValue(
                            MdpState.Success(
                                mdpResponse,
                                benefitResponse.await(),
                                recommendationsResponse.await()
                            )
                        )
                        setupAnalyticsData(mdpResponse)
                    }

                    else -> {
                        _badgeLiveData.postValue(
                            MdpState.Error(
                                Throwable(),
                                responseCode.orEmpty()
                            )
                        )
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

    private suspend fun getMedalRecommendations(
        mdpResponse: MedalDetailResponseModel
    ): List<ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType.Medal>? {
        val section =
            mdpResponse.detail?.medaliDetailPage?.section?.find { it.type == MDP_SECTION_TYPE_BRAND_RECOMMENDATIONS }
        return if (section != null) {
            try {
                val response =
                    userMedaliUseCase.getUserMedalis(getRecommendationParams(section.jsonParameter))
                response?.scpRewardsGetUserMedalisByType?.medaliList
            } catch (exp: Exception) {
                FirebaseCrashlytics.getInstance().recordException(exp)
                emptyList()
            }
        } else {
            null
        }
    }

    private fun getRecommendationParams(json: String?): RequestParams {
        val pageSize = json?.parseJsonKey<Int>(PAGE_SIZE_JSON_KEY) ?: 3
        val pageName = json?.parseJsonKey<String>(PAGE_NAME_JSON_KEY).orEmpty()
        val medaliSlug = json?.parseJsonKey<String>(MEDALI_SLUG_JSON_KEY).orEmpty()
        val type = json?.parseJsonKey<String>(TYPE_JSON_KEY).orEmpty()

        return RequestParams().apply {
            putObject(MEDALI_SLUG_JSON_KEY, arrayOf(medaliSlug))
            putString(PAGE_NAME_PARAM, pageName)
            putInt(PAGE_PARAM, 1)
            putString(TYPE_JSON_KEY, type)
            putInt(PAGESIZE_PARAM, pageSize)
        }
    }

    fun applyCoupon(ctaButton: CtaButton?, shopId: Int? = null, couponCode: String) {
        viewModelScope.launchCatchError(
            block = {
                _autoApplyCoupon.postValue(AutoApplyState.Loading)
                val response = couponAutoApplyUseCase.applyCoupon(shopId, couponCode)
                if (response.data != null && response.data.resultStatus.code == SUCCESS_CODE) {
                    if (response.data.couponAutoApply?.isSuccess == true) {
                        _autoApplyCoupon.postValue(
                            AutoApplyState.SuccessCouponApplied(
                                ctaButton,
                                response.data
                            )
                        )
                    } else {
                        _autoApplyCoupon.postValue(
                            AutoApplyState.SuccessCouponFailed(
                                ctaButton,
                                response.data
                            )
                        )
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
            val benefitData: MedaliBenefitList?,
            val medalRecommendations: List<ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType.Medal>?
        ) : MdpState()

        class Error(val error: Throwable, val errorCode: String = "") : MdpState()
        object Loading : MdpState()
    }

    sealed class AutoApplyState {
        object Loading : AutoApplyState()
        data class SuccessCouponApplied(
            val ctaButton: CtaButton?,
            val data: ScpRewardsCouponAutoApply?
        ) : AutoApplyState()

        data class SuccessCouponFailed(
            val ctaButton: CtaButton?,
            val data: ScpRewardsCouponAutoApply?
        ) : AutoApplyState()

        data class Error(val ctaButton: CtaButton?, val throwable: Throwable) : AutoApplyState()
    }
}
