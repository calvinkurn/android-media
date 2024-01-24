package com.tokopedia.gamification.pdp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponData
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponSlugData
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.data.model.KetupatReferralEventTimeStamp
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponSlugUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatLandingUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatReferralEventTimeStampUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatReferralUserRegistrationUseCase
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import kotlinx.coroutines.async
import org.json.JSONObject
import javax.inject.Inject

class KetupatLandingViewModel @Inject constructor(
    private val ketupatLandingUseCase: KetupatLandingUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val ketupatBenefitCouponUseCase: KetupatBenefitCouponUseCase,
    private val ketupatBenefitCouponSlugUseCase: KetupatBenefitCouponSlugUseCase,
    private val ketupatReferralUserRegistrationUseCase: KetupatReferralUserRegistrationUseCase,
    private val ketupatReferralEventTimeStampUseCase: KetupatReferralEventTimeStampUseCase
) : BaseViewModel() {

    private val errorMessage = MutableLiveData<Throwable>()
    private val landingPageData = MutableLiveData<KetupatLandingPageData>()
    private val benefitCouponData = MutableLiveData<KetupatBenefitCouponData>()
    private val benefitCouponSlugData = MutableLiveData<KetupatBenefitCouponSlugData>()
    private val ketaupatLandingDataList =
        MutableLiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>>()
    private val referralTimeData = MutableLiveData<KetupatReferralEventTimeStamp>()
    var data: KetupatLandingPageData? = null
    var eventSlug = ""
    fun getGamificationLandingPageData(slug: String = "") {
        launchCatchError(
            block = {
                val landingPageMainData =
                    async { ketupatLandingUseCase.getScratchCardLandingPage(slug) }
                val benefitCouponDataAPI =
                    async { ketupatBenefitCouponUseCase.getTokopointsCouponList() }
                val benefitCouponSlugDataAPI =
                    async { ketupatBenefitCouponSlugUseCase.getTokopointsCouponListStack() }

                landingPageMainData.await().apply {
                    landingPageData.value = this
                    eventSlug = this.gamiGetScratchCardLandingPage.sections.find { it?.type == "referral" }?.jsonParameter?.let {
                        JSONObject(
                            it
                        ).getString("eventSlug")
                    }.toString()
                }
                benefitCouponDataAPI.await().apply { benefitCouponData.value = this }
                benefitCouponSlugDataAPI.await().apply {
                    benefitCouponSlugData.value = this
                }

                ketupatReferralUserRegistrationUseCase.getKetupatReferralUserRegistrationData(eventSlug)

                val timeData =  async { ketupatReferralEventTimeStampUseCase.getKetupatReferralTimeStampData(eventSlug) }
                timeData.await().apply {
                    referralTimeData.value = this
                }
                landingPageData.value?.gamiGetScratchCardLandingPage?.let {
                    convertDataToVisitable(it).let { visitable ->
                        ketaupatLandingDataList.value = visitable
                    }
                }
            },
            onError = {
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    private fun convertDataToVisitable(data: KetupatLandingPageData.GamiGetScratchCardLandingPage): ArrayList<Visitable<KetupatLandingTypeFactory>>? {
        val tempList: ArrayList<Visitable<KetupatLandingTypeFactory>> = ArrayList()
        var header: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var crack: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var referral: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var benefitCoupon: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var benefitCouponSlug: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? =
            null
        var banner: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        data.sections?.forEach {
            when (it?.type) {
                "header" -> {
                    header = it
                }

                "crack" -> {
                    crack = it
                }

                "referral" -> {
                    referral = it
                }

                "benefit-coupon" -> {
                    benefitCoupon = it
                }

                "benefit-coupon-slug" -> {
                    benefitCouponSlug = it
                }

                "banner" -> {
                    banner = it
                }
            }
        }

        if (header != null) {
            data.scratchCard?.let { KetupatTopBannerVHModel(header!!, it) }
                ?.let { tempList.add(it) }
        }
        if (crack != null) {
            data.scratchCard?.let { KetupatCrackBannerVHModel(crack!!, it) }
                ?.let { tempList.add(it) }
        }
        if (referral != null) {
            referralTimeData.value?.let {
                data.scratchCard?.let { scratchCard -> KetupatReferralBannerVHModel(referral!!, it, scratchCard) }
            }?.let { tempList.add(it) }
        }
        if (benefitCoupon != null && benefitCouponData.value?.tokopointsCouponList?.tokopointsCouponData?.isEmpty() == false) {
            data.scratchCard?.let {
                KetupatBenefitCouponVHModel(benefitCoupon!!,
                    it, benefitCouponData.value)
            }?.let { tempList.add(it) }
        }
        if (benefitCouponSlug != null && benefitCouponSlugData.value?.tokopointsCouponListStack?.tokopointsCouponDataStack?.isEmpty() == false) {
            data.scratchCard?.let {
                KetupatBenefitCouponSlugVHModel(
                    benefitCouponSlug!!,
                    benefitCouponSlugData.value,
                    it
                )
            }?.let {
                tempList.add(
                    it
                )
            }
        }
        if (banner != null) {
            data.scratchCard?.let { KetupatRedirectionBannerVHModel(banner!!, it) }
                ?.let { tempList.add(it) }
        }
        return tempList
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getLandingPageData(): LiveData<KetupatLandingPageData> = landingPageData

    fun getReferralTimeData() : LiveData<KetupatReferralEventTimeStamp> = referralTimeData

    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>> =
        ketaupatLandingDataList
}
