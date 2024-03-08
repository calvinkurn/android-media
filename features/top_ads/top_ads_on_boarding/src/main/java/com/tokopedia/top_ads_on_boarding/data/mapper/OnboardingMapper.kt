package com.tokopedia.top_ads_on_boarding.data.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant
import com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel

class OnboardingMapper {

    private val faqList = mutableListOf<OnboardingFaqItemUiModel>()

    fun getFaqList(): List<OnboardingFaqItemUiModel> {
        if(faqList.isEmpty()) {
            val titleList = getFaqTitleList()
            val descList = getFaqDescriptionList()
            titleList.onEach { entry ->
                faqList.add(
                    OnboardingFaqItemUiModel(
                        id = entry.key,
                        title = entry.value,
                        desc = descList.get(entry.key) ?: String.EMPTY,
                        isExpanded = false
                    )
                )
            }
        }
        return faqList
    }

    private fun getFaqTitleList(): Map<Int, String> {
        return mapOf(
            1 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_TITLE_1,
            2 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_TITLE_2,
            3 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_TITLE_3,
            4 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_TITLE_4,
            5 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_TITLE_5,
            6 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_TITLE_6,
        )
    }

    private fun getFaqDescriptionList(): Map<Int, String> {
        return mapOf(
            1 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_DESC_1,
            2 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_DESC_2,
            3 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_DESC_3,
            4 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_DESC_4,
            5 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_DESC_5,
            6 to TopAdsOnBoardingConstant.ONBOARDING_FAQ_DESC_6,
        )
    }
}
