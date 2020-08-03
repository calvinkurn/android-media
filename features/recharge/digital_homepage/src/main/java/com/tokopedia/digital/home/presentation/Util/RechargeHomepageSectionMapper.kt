package com.tokopedia.digital.home.presentation.Util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

object RechargeHomepageSectionMapper {
    // TODO: Finish section mapper
    fun mapHomepageSections(sections: List<RechargeHomepageSections.Section>): List<Visitable<*>?> {
        return sections.mapNotNull {
            // TODO: Remove temporary data
            with(DigitalHomePageViewModel.Companion) {
                when (it.template) {
                    SECTION_TOP_BANNER -> RechargeHomepageBannerModel(it)
                    SECTION_TOP_BANNER_EMPTY -> RechargeHomepageBannerEmptyModel(it)
                    SECTION_TOP_ICONS -> RechargeHomepageFavoriteModel(it)
                    SECTION_URGENCY_WIDGET -> getReminderWidgetModel(it)
                    SECTION_VIDEO_HIGHLIGHT -> RechargeHomepageVideoHighlightModel(it)
                    SECTION_DYNAMIC_ICONS -> RechargeHomepageCategoryModel(it)
                    SECTION_DUAL_ICONS -> RechargeHomepageTrustMarkModel(it)
                    SECTION_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it)
                    SECTION_COUNTDOWN_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it)
                    SECTION_DUAL_BANNERS -> RechargeHomepageDualBannersModel(it)
                    SECTION_LEGO_BANNERS -> getDynamicLegoBannerModel(it)
                    SECTION_PRODUCT_CARD_ROW -> RechargeHomepageProductCardsModel(it)
                    SECTION_COUNTDOWN_PRODUCT_BANNER -> RechargeHomepageProductBannerModel(it)
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
                    title = section.title,
                    mainText = title,
                    subText = content,
                    buttonText = textlink,
                    buttonType = when (buttonType) {
                        "primary" -> UnifyButton.Type.MAIN
                        "transaction" -> UnifyButton.Type.TRANSACTION
                        else -> UnifyButton.Type.MAIN
                    },
                    state = when (template) {
                        "INFO" -> ReminderState.NEUTRAL
                        "DANGER" -> ReminderState.ATTENTION
                        else -> ReminderState.NEUTRAL
                    }
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
            val serverDateMillisecond = getServerTime(serverDate).time

            return ChannelModel(sectionId, sectionId,
                    channelHeader = ChannelHeader(sectionId, section.title, section.subtitle, dueDate),
                    channelConfig = ChannelConfig(serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffset(serverDateMillisecond))
            )
        }
        return null
    }

    private fun getServerTime(serverTimeString: String): Date {
        return DateHelper.getExpiredTime(serverTimeString)
    }
}