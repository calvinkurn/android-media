package com.tokopedia.digital.home.presentation.Util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel

object RechargeHomepageSectionMapper {
    // TODO: Finish section mapper
    fun mapHomepageSections(sections: List<RechargeHomepageSections.Section>): List<Visitable<*>?> {
        return sections.map {
            with(DigitalHomePageViewModel.Companion) {
                when (it.template) {
                    SECTION_TOP_BANNER -> RechargeHomepageBannerModel(it)
//                    SECTION_TOP_BANNER_EMPTY -> RechargeHomepageBannerEmptyModel(it)
                    SECTION_TOP_ICONS -> RechargeHomepageFavoriteModel(it)
                    SECTION_VIDEO_HIGHLIGHT -> RechargeHomepageVideoHighlightModel(it)
                    SECTION_DYNAMIC_ICONS -> RechargeHomepageCategoryModel(it)
                    SECTION_DUAL_ICONS -> RechargeHomepageTrustMarkModel(it)
                    SECTION_LEGO_BANNERS -> getDynamicLegoBannerModel(it)
                    else -> null
                }
            }
        }
    }

    private fun getDynamicLegoBannerModel(section: RechargeHomepageSections.Section): DynamicLegoBannerDataModel {
        return DynamicLegoBannerDataModel(ChannelModel(
                section.id.toString(),
                section.id.toString(),
                channelConfig = ChannelConfig(DynamicChannelLayout.LAYOUT_6_IMAGE),
                channelHeader = ChannelHeader(name = section.title, subtitle = section.subTitle),
                channelGrids = section.items.map { item ->
                    ChannelGrid(item.id.toString(), imageUrl = item.mediaUrl, applink = item.applink)
                }))
    }
}