package com.tokopedia.digital.home.presentation.Util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import java.util.*

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
                    SECTION_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it)
                    SECTION_COUNTDOWN_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it)
                    SECTION_DUAL_BANNERS -> RechargeHomepageDualBannersModel(it)
                    SECTION_LEGO_BANNERS -> getDynamicLegoBannerModel(it)
//                    SECTION_COUNTDOWN_PRODUCT_BANNER -> RechargeHomepageProductBannerModel(it)
                    // TODO: Remove temporary data
                    SECTION_COUNTDOWN_PRODUCT_BANNER -> RechargeHomepageProductBannerModel(RechargeHomepageSections.Section(
                            id = 1,
                            title = "Test",
                            items = listOf(RechargeHomepageSections.Item(
                                    title = "Test Product",
                                    subtitle = "This product is cool",
                                    label1 = "Rp 10.000",
                                    label2 = "Rp 15.000",
                                    label3 = "30%"
                            ))
                    ))
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
                channelHeader = ChannelHeader(name = section.title, subtitle = section.subtitle),
                channelGrids = section.items.map { item ->
                    ChannelGrid(item.id.toString(), imageUrl = item.mediaUrl, applink = item.applink)
                }))
    }

    fun setDynamicHeaderViewChannel(headerView: DynamicChannelHeaderView, section: RechargeHomepageSections.Section, listener: HeaderListener? = null) {
        val headerListener = listener ?: object : HeaderListener {
            override fun onSeeAllClick(link: String) { /* do nothing */ }

            override fun onChannelExpired(channelModel: ChannelModel) { /* do nothing */ }
        }

        mapSectionToChannel(section)?.let { channel ->
            headerView.setChannel(channel, headerListener)
        }
    }

    private fun mapSectionToChannel(section: RechargeHomepageSections.Section): ChannelModel? {
        with (section) {
            return if (section.items.isNotEmpty()) {
                val sectionId = id.toString()
                val item = items[0]
                val serverTimeOffset = DateHelper.getExpiredTime(item.serverDate).time - Date().time

                ChannelModel(sectionId, sectionId,
                        channelHeader = ChannelHeader(sectionId, title, subtitle, item.dueDate),
                        channelConfig = ChannelConfig(serverTimeOffset = serverTimeOffset)
                )
            } else null
        }
    }
}