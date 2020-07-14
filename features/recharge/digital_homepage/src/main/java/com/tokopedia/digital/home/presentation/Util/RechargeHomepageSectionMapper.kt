package com.tokopedia.digital.home.presentation.Util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import java.util.*

object RechargeHomepageSectionMapper {
    // TODO: Finish section mapper
    fun mapHomepageSections(sections: List<RechargeHomepageSections.Section>): List<Visitable<*>?> {
        return sections.mapNotNull {
            // TODO: Remove temporary data
            with(DigitalHomePageViewModel.Companion) {
                when (it.template) {
                    SECTION_TOP_BANNER -> RechargeHomepageBannerModel(it)
//                    SECTION_TOP_BANNER -> RechargeHomepageBannerEmptyModel(it)
                    SECTION_TOP_BANNER_EMPTY -> RechargeHomepageBannerEmptyModel(it)
                    SECTION_TOP_ICONS -> RechargeHomepageFavoriteModel(it)
//                    SECTION_URGENCY_WIDGET -> getReminderWidgetModel(it)
                    // TODO: Replace background color with backend data
                    SECTION_URGENCY_WIDGET -> ReminderWidgetModel(ReminderWidget(listOf(ReminderData(
                            "test",
                            id = "99",
                            iconURL = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/10/22/21181130/21181130_31fffa3a-b61f-4b67-b183-785aef289a5b.png",
                            title = "Bayar!!!",
                            mainText = "Tagihan kamu sudah waktu tenggat",
                            subText = "Ayo bayar sekarang",
                            buttonText = "Bayar Sekarang",
                            backgroundColor = listOf("#0153B5", "#06DD69")
                    ))), ReminderEnum.RECHARGE)
                    SECTION_VIDEO_HIGHLIGHT -> RechargeHomepageVideoHighlightModel(it)
                    SECTION_DYNAMIC_ICONS -> RechargeHomepageCategoryModel(it)
                    SECTION_DUAL_ICONS -> RechargeHomepageTrustMarkModel(it)
                    SECTION_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it)
                    SECTION_COUNTDOWN_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it)
                    SECTION_DUAL_BANNERS -> RechargeHomepageDualBannersModel(it)
                    SECTION_LEGO_BANNERS -> getDynamicLegoBannerModel(it)
//                    SECTION_PRODUCT_CARD_ROW -> RechargeHomepageProductCardsModel(it)
                    SECTION_PRODUCT_CARD_ROW -> RechargeHomepageProductCardsModel(RechargeHomepageSections.Section(
                            id = 100,
                            title = "Product Card Row",
                            items = listOf(
                                    RechargeHomepageSections.Item(
                                            title = "Product 1",
                                            mediaUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/10/22/21181130/21181130_31fffa3a-b61f-4b67-b183-785aef289a5b.png",
                                            label1 = "Rp 10.000",
                                            label2 = "Mulai dari",
                                            label3 = "Streaming"),
                                    RechargeHomepageSections.Item(
                                            title = "Product 2",
                                            mediaUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/10/22/21181130/21181130_31fffa3a-b61f-4b67-b183-785aef289a5b.png",
                                            label1 = "Rp 20.000",
                                            label2 = "Mulai dari",
                                            label3 = "Voucher"
                                    )
                            )
                    ))
//                    SECTION_COUNTDOWN_PRODUCT_BANNER -> RechargeHomepageProductBannerModel(it)
                    SECTION_COUNTDOWN_PRODUCT_BANNER -> RechargeHomepageProductBannerModel(RechargeHomepageSections.Section(
                            id = 101,
                            title = "Product Banner",
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

    private fun getReminderWidgetModel(section: RechargeHomepageSections.Section): ReminderWidgetModel? {
        section.items.firstOrNull()?.run {
            return ReminderWidgetModel(ReminderWidget(listOf(ReminderData(
                    applink,
                    id = id.toString(),
                    iconURL = mediaUrl,
                    title = title,
                    mainText = label1,
                    subText = label2,
                    buttonText = label3
            ))), ReminderEnum.RECHARGE)
        }
        return null
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
        section.items.firstOrNull()?.run {
            val sectionId = section.id.toString()
            val serverTimeOffset = DateHelper.getExpiredTime(serverDate).time - Date().time

            return ChannelModel(sectionId, sectionId,
                    channelHeader = ChannelHeader(sectionId, section.title, section.subtitle, dueDate),
                    channelConfig = ChannelConfig(serverTimeOffset = serverTimeOffset)
            )
        }
        return null
    }

    fun getSection(sections: List<RechargeHomepageSections.Section>?, sectionType: String): RechargeHomepageSections.Section? {
        return sections?.firstOrNull { it.template == sectionType }
    }
}