package com.tokopedia.gamification.pdp.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponData
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponSlugData
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.data.model.KetupatReferralEventTimeStamp
import com.tokopedia.gamification.pdp.data.model.request.BenefitCouponRequest
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponSlugUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatLandingUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatReferralEventTimeStampUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatReferralUserRegistrationUseCase
import com.tokopedia.gamification.pdp.presentation.LandingPageRefreshCallback
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifications.common.launchCatchError
import kotlinx.coroutines.async
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class KetupatLandingViewModel @Inject constructor(
    private val ketupatLandingUseCase: KetupatLandingUseCase,
    private val ketupatBenefitCouponUseCase: KetupatBenefitCouponUseCase,
    private val ketupatBenefitCouponSlugUseCase: KetupatBenefitCouponSlugUseCase,
    private val ketupatReferralUserRegistrationUseCase: KetupatReferralUserRegistrationUseCase,
    private val ketupatReferralEventTimeStampUseCase: KetupatReferralEventTimeStampUseCase
) : BaseViewModel() {

    private val errorMessage = MutableLiveData<Throwable>()
    private val isRecommVisible = MutableLiveData<Boolean>()
    private val landingPageData = MutableLiveData<KetupatLandingPageData>()
    private val benefitCouponData = MutableLiveData<KetupatBenefitCouponData>()
    private val benefitCouponSlugData = MutableLiveData<KetupatBenefitCouponSlugData>()
    private val ketaupatLandingDataList =
        MutableLiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>>()
    private val referralTimeData = MutableLiveData<KetupatReferralEventTimeStamp>()
    private var eventSlug = ""
    private var benefitCouponRequest: BenefitCouponRequest? = null
    private var catalogSlugJSON: JSONArray? = null
    private val slugList: MutableList<String> = arrayListOf()
    private var landingPageRefreshCallback: LandingPageRefreshCallback? = null

    fun getGamificationLandingPageData(
        context: Context?,
        slug: String = "",
        landingPageRefreshCallback: LandingPageRefreshCallback? = null
    ) {
        launchCatchError(
            block = {

                this.landingPageRefreshCallback = landingPageRefreshCallback
                val landingPageMainData =
                    async { ketupatLandingUseCase.getScratchCardLandingPage(slug) }


                landingPageMainData.await().apply {
                    val code = this.gamiGetScratchCardLandingPage.resultStatus?.code
                    if (code != "200") {
                        val error = ErrorHandler.getErrorMessage(
                            context,
                            MessageErrorException(this.gamiGetScratchCardLandingPage.resultStatus?.message.toString())
                        )
                        throw Throwable(error)
                    }
                    landingPageData.value = this
                    eventSlug =
                        this.gamiGetScratchCardLandingPage.sections.find { it?.type == "referral" }?.jsonParameter?.let {
                            JSONObject(
                                it
                            ).getString("eventSlug")
                        }.toString()
                    this.gamiGetScratchCardLandingPage.sections.find { it?.type == "benefit-coupon" }?.jsonParameter.apply {
                        benefitCouponRequest = BenefitCouponRequest(
                            categoryIDCoupon = JSONObject(this.toString()).getLong("categoryIDCoupon"),
                            categoryID = JSONObject(this.toString()).getLong("categoryID"),
                            limit = JSONObject(this.toString()).getLong("limit"),
                            page = JSONObject(this.toString()).getLong("page"),
                            serviceID = "marketplace"
                        )
                    }
                    this.gamiGetScratchCardLandingPage.sections.find { it?.type == "benefit-coupon-slug" }?.jsonParameter.apply {
                        catalogSlugJSON =
                            JSONObject(this.toString()).get("catalogSlugs") as JSONArray
                        for (i in 0 until catalogSlugJSON?.length().orZero()) {
                            slugList.add(catalogSlugJSON?.get(i).toString())
                        }
                    }

                }

                val benefitCouponDataAPI =
                    async {
                        benefitCouponRequest?.let {
                            ketupatBenefitCouponUseCase.getTokopointsCouponList(it)
                        }
                    }

                val benefitCouponSlugDataAPI =
                    async { ketupatBenefitCouponSlugUseCase.getTokopointsCouponListStack(slugList) }

                benefitCouponDataAPI.await().apply { benefitCouponData.value = this }
                benefitCouponSlugDataAPI.await().apply { benefitCouponSlugData.value = this }

                ketupatReferralUserRegistrationUseCase.getKetupatReferralUserRegistrationData(
                    eventSlug
                )

                val timeData = async {
                    ketupatReferralEventTimeStampUseCase.getKetupatReferralTimeStampData(eventSlug)
                }

                timeData.await().apply { referralTimeData.value = this }

                landingPageData.value?.gamiGetScratchCardLandingPage?.let {
                    convertDataToVisitable(it).let { visitable ->
                        ketaupatLandingDataList.value = visitable
                    }
                }
            },
            onError =
            {
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    private fun convertDataToVisitable(data: KetupatLandingPageData.GamiGetScratchCardLandingPage): ArrayList<Visitable<KetupatLandingTypeFactory>>? {
        val tempList: ArrayList<Visitable<KetupatLandingTypeFactory>> = ArrayList()

        data.sections[0]?.let { KetupatTopBannerVHModel(it, data.scratchCard) }
            ?.let { tempList.add(it) }

        data.sections[1]?.let { KetupatCrackBannerVHModel(it, data.scratchCard) }
            ?.let { tempList.add(it) }

        if (referralTimeData.value?.gameReferralEventContent?.eventContent?.remainingTime.orZero() > 0) {
            data.sections[2]?.let {
                referralTimeData.value?.let { referralTimeData ->
                    KetupatReferralBannerVHModel(
                        it,
                        referralTimeData, data.scratchCard
                    )
                }
            }?.let { tempList.add(it) }
        }

        if (benefitCouponData.value?.tokopointsCouponList?.tokopointsCouponData?.isEmpty() == false) {
            data.sections[3]?.let {
                KetupatBenefitCouponVHModel(
                    it,
                    data.scratchCard, benefitCouponData.value
                )
            }?.let { tempList.add(it) }
        }

        if (benefitCouponSlugData.value?.tokopointsCouponListStack?.tokopointsCouponDataStack?.isEmpty() == false) {
            data.sections[4]?.let {
                KetupatBenefitCouponSlugVHModel(
                    it,
                    benefitCouponSlugData.value,
                    data.scratchCard
                )
            }?.let { tempList.add(it) }
        }

        data.sections[5]?.let { KetupatRedirectionBannerVHModel(it, data.scratchCard) }
            ?.let { tempList.add(it) }

        isRecommVisible.value = data.sections.find { it?.type == "product-recommendation" } != null

        return tempList
    }

    fun getRecommVisibility(): LiveData<Boolean> = isRecommVisible
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getLandingPageData(): LiveData<KetupatLandingPageData> = landingPageData

    fun getReferralTimeData(): LiveData<KetupatReferralEventTimeStamp> = referralTimeData

    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>> =
        ketaupatLandingDataList

}
