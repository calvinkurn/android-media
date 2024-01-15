package com.tokopedia.gamification.pdp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.C1VHModel
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.domain.usecase.KetupatLandingUseCase
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingTypeFactory
import com.tokopedia.notifications.common.launchCatchError
import javax.inject.Inject

class KetupatLandingViewModel @Inject constructor(
    private val ketupatLandingUseCase: KetupatLandingUseCase
) : BaseViewModel() {

    private val errorMessage = MutableLiveData<Throwable>()
    private val landingPageData = MutableLiveData<KetupatLandingPageData>()
    private val ketaupatLandingDataList =
        MutableLiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>>()
    var data: KetupatLandingPageData? = null

    fun getScratchCardsLandingInfo(slug: String = "") {
        launchCatchError(
            block = {
                landingPageData.value =
                    ketupatLandingUseCase.getScratchCardLandingPage(slug).apply {
                        convertDataToVisitable(this.gamiGetScratchCardLandingPage.sections)?.let { visitable ->
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

    private fun convertDataToVisitable(data: List<KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem?>): ArrayList<Visitable<KetupatLandingTypeFactory>>? {
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
        data.forEach {
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

        if(header!=null) {
            tempList.add(C1VHModel(header!!))
        }
        return tempList
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getLandingPageData(): LiveData<KetupatLandingPageData> = landingPageData

    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<KetupatLandingTypeFactory>>> =
        ketaupatLandingDataList
}
