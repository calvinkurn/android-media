package com.tokopedia.gamification.pdp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponData
import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponSlugData
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponSlugUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponUseCase
import com.tokopedia.gamification.pdp.domain.usecase.KetupatLandingUseCase
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponSlugVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatProductRecommItemVHmodel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatRedirectionBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatTopBannerVHModel
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import javax.inject.Inject

class KetupatLandingViewModel @Inject constructor(
    private val ketupatLandingUseCase: KetupatLandingUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val ketupatBenefitCouponUseCase: KetupatBenefitCouponUseCase,
    private val ketupatBenefitCouponSlugUseCase: KetupatBenefitCouponSlugUseCase
) : BaseViewModel() {

    private val errorMessage = MutableLiveData<Throwable>()
    private val landingPageData = MutableLiveData<KetupatLandingPageData>()
    private val benefitCouponData = MutableLiveData<KetupatBenefitCouponData>()
    private val benefitCouponSlugData = MutableLiveData<KetupatBenefitCouponSlugData>()
    private val ketaupatLandingDataList =
        MutableLiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>>()
    var data: KetupatLandingPageData? = null

    fun getScratchCardsLandingInfo(slug: String = "") {
        launchCatchError(
            block = {
                landingPageData.value =
                    ketupatLandingUseCase.getScratchCardLandingPage(slug).apply {
                        convertDataToVisitable(this.gamiGetScratchCardLandingPage)?.let { visitable ->
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

    fun getTokopointsCouponList() {
        launchCatchError(
            block = {
                benefitCouponData.value  = ketupatBenefitCouponUseCase.getTokopointsCouponList()
            },
            onError = {
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getTokopointsCouponListStack() {
        launchCatchError(
            block = {
                val v  = ketupatBenefitCouponSlugUseCase.getTokopointsCouponListStack()
                benefitCouponSlugData.value = v
            },
            onError = {
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getProductRecommendation() {
        launchCatchError(
            block = {
                val requestParam = GetRecommendationRequestParam()
                getRecommendationUseCase.getData(requestParam)
            },
            onError = {
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    private fun convertDataToVisitable(data: KetupatLandingPageData.GamiGetScratchCardLandingPage?): ArrayList<Visitable<KetupatLandingTypeFactory>>? {
        val tempList: ArrayList<Visitable<KetupatLandingTypeFactory>> = ArrayList()
        var header: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var crack: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var referral: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var benefitCoupon: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var benefitCouponSlug: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? =
            null
        var banner: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? = null
        var ProductRecommendation: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem? =
            null
        data?.sections?.forEach {
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

                "product-recommendation" -> {
                    ProductRecommendation = it
                }
            }
        }

        if (header != null) {
            data?.scratchCard?.let { KetupatTopBannerVHModel(header!!, it) }
                ?.let { tempList.add(it) }
        }
        if (crack != null) {
            tempList.add(KetupatCrackBannerVHModel(crack!!))
        }
        if (referral != null) {
            tempList.add(KetupatReferralBannerVHModel(referral!!))
        }
        if (benefitCoupon != null) {
            tempList.add(KetupatBenefitCouponVHModel(benefitCoupon!!, benefitCouponData.value))
        }
        if (benefitCouponSlug != null) {
            tempList.add(KetupatBenefitCouponSlugVHModel(benefitCouponSlug!!, benefitCouponSlugData.value))
        }
        if (banner != null) {
            tempList.add(KetupatRedirectionBannerVHModel(banner!!))
        }
        tempList.add(
            KetupatProductRecommItemVHmodel(
                RecommendationWidgetModel(
                    metadata = RecommendationWidgetMetadata(
                        pageName = "inbox_post-purchase"
                    ),
                    trackingModel = RecommendationWidgetTrackingModel()
                )
            )
        )
        return tempList
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getLandingPageData(): LiveData<KetupatLandingPageData> = landingPageData

    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>> =
        ketaupatLandingDataList
}
