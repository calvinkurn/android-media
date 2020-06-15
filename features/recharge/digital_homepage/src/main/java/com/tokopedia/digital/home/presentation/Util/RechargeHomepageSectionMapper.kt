package com.tokopedia.digital.home.presentation.Util

import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel

object RechargeHomepageSectionMapper {
    // TODO: Finish section mapper
    fun mapHomepageSections(sections: List<RechargeHomepageSections.Section>): List<RechargeHomepageAbstractSectionModel?> {
        return sections.map {
            with(DigitalHomePageViewModel.Companion) {
                when (it.template) {
                    SECTION_TOP_BANNER -> RechargeHomepageBannerModel(it)
//                    SECTION_TOP_BANNER_EMPTY -> RechargeHomepageBannerEmptyModel(it)
                    SECTION_TOP_ICONS -> RechargeHomepageFavoriteModel(it)
                    SECTION_DYNAMIC_ICONS -> RechargeHomepageCategoryModel(it)
                    SECTION_DUAL_ICONS -> RechargeHomepageTrustMarkModel(it)
                    else -> null
                }
            }
        }
    }
}