package com.tokopedia.digital.home.presentation.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageViewModel
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.DateHelper.isExpired
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerType
import java.util.*

object RechargeHomepageSectionMapper {

    fun updateSectionsData(
            oldData: List<RechargeHomepageSections.Section>,
            newData: RechargeHomepageSections): List<RechargeHomepageSections.Section> {
        // Remove empty sections
        var sections = oldData.toMutableList()
        val updatedSections = newData.sections.filter { it.items.isNotEmpty() }
        val requestIDs = newData.requestIDs.map { it.toString() }
        when (updatedSections.size) {
            0 -> {
                // Remove sections
                sections = sections.filter { it.id !in requestIDs }.toMutableList()
            }
            1 -> {
                // One on one mapping; remove other IDs except the first one
                if (requestIDs.size > 1) {
                    val indexes = requestIDs.subList(1, requestIDs.size)
                    sections = sections.filter { it.id !in indexes }.toMutableList()
                }
                val index = sections.indexOfFirst { it.id == requestIDs.first() }
                sections[index] = updatedSections.first()
            }
            else -> {
                /*
                    Special case; remove other IDs except the first one,
                    then insert all sections to the appropriate index
                 */
                if (requestIDs.size > 1) {
                    val indexes = requestIDs.subList(1, requestIDs.size)
                    sections = sections.filter { it.id !in indexes }.toMutableList()
                }
                val index = sections.indexOfFirst { it.id == requestIDs.first() }
                sections.removeAt(index)
                sections.addAll(index, updatedSections)
            }
        }
        return sections.toList()
    }

    fun mapInitialHomepageSections(sections: List<RechargeHomepageSectionSkeleton.Item>): List<RechargeHomepageSections.Section> {
        val sectionsList = mutableListOf<RechargeHomepageSections.Section>()
        for (section in sections){
            sectionsList.add(RechargeHomepageSections.Section(section.id, template = section.template))
            if(section.template.equals(RechargeHomepageViewModel.SECTION_TOP_BANNER) ||
                    section.template.equals(RechargeHomepageViewModel.SECTION_TOP_BANNER_EMPTY)){
                sectionsList.add(RechargeHomepageSections.Section(RechargeHomepageViewModel.ID_TICKER, template = RechargeHomepageViewModel.SECTION_TICKER))
            }
        }
        return sectionsList
    }

    fun mapHomepageSections(sections: List<RechargeHomepageSections.Section>, tickerList: RechargeTickerHomepageModel): List<Visitable<*>> {
        return sections.mapNotNull {
            val id = it.id
            with(RechargeHomepageViewModel.Companion) {
                when (it.template) {
                    SECTION_TOP_BANNER -> RechargeHomepageBannerModel(it)
                    SECTION_TOP_BANNER_EMPTY -> RechargeHomepageBannerEmptyModel(it)
                    SECTION_TOP_ICONS -> RechargeHomepageFavoriteModel(it)
                    SECTION_URGENCY_WIDGET -> {
                        // Check if it is initial sections or not
                        if (it.title.isEmpty() && it.items.isEmpty()) {
                            ReminderWidgetModel(ReminderWidget(id), ReminderEnum.RECHARGE)
                        } else {
                            getReminderWidgetModel(it)
                        }
                    }
                    SECTION_VIDEO_HIGHLIGHT -> RechargeHomepageVideoHighlightModel(it)
                    SECTION_DYNAMIC_ICONS -> RechargeHomepageCategoryModel(it)
                    SECTION_DUAL_ICONS -> RechargeHomepageTrustMarkModel(it)
                    SECTION_SINGLE_BANNER -> RechargeHomepageSingleBannerModel(it, mapSectionToChannel(it))
                    SECTION_COUNTDOWN_SINGLE_BANNER -> {
                        /**
                         * Count down widget is always from cloud because
                         * its countdown time is based on server time
                         */
                        if (!isExpired(it)) {
                            RechargeHomepageSingleBannerModel(it, mapSectionToChannel(it), true)
                        } else null
                    }
                    SECTION_DUAL_BANNERS -> RechargeHomepageDualBannersModel(it)
                    SECTION_LEGO_BANNERS -> {
                        // Check if it is initial sections or not
                        if (it.title.isEmpty() && it.items.isEmpty()) {
                            DynamicLegoBannerDataModel(ChannelModel(id, id))
                        } else {
                            getDynamicLegoBannerModel(it)
                        }
                    }
                    SECTION_PRODUCT_CARD_ROW -> RechargeHomepageProductCardsModel(it)
                    SECTION_COUNTDOWN_PRODUCT_BANNER -> {
                        /**
                         * Count down widget is always from cloud because
                         * its countdown time is based on server time
                         */
                        if (!isExpired(it)) {
                            RechargeHomepageProductBannerModel(it, mapSectionToChannel(it), true)
                        } else null
                    }
                    SECTION_PRODUCT_CARD_CUSTOM_BANNER -> RechargeProductCardCustomBannerModel(it)
                    SECTION_MINI_CAROUSELL -> RechargeHomepageCarousellModel(it)
                    SECTION_TICKER -> {
                        if(!tickerList.rechargeTickers.isEmpty()){
                            tickerList
                        } else null
                    }
                    else -> null
                }
            }
        }
    }

    private fun getReminderWidgetModel(section: RechargeHomepageSections.Section): ReminderWidgetModel? {
        section.items.firstOrNull()?.run {
            return ReminderWidgetModel(ReminderWidget(section.id.toString(),
                    listOf(ReminderData(
                            applink,
                            id = section.id.toString(),
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
                    ))
            ), ReminderEnum.RECHARGE)
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

    fun setDynamicHeaderViewChannel(headerView: DynamicChannelHeaderView, channelModel: ChannelModel?,
                                    listener: HeaderListener? = null) {
        val headerListener = listener ?: object : HeaderListener {
            override fun onSeeAllClick(link: String) { /* do nothing */
            }

            override fun onChannelExpired(channelModel: ChannelModel) { /* do nothing */
            }
        }

        channelModel?.let {
            if (hasExpired(it)) headerListener.onChannelExpired(it)
            else headerView.setChannel(it, headerListener)
        }
    }

    private fun hasExpired(channel: ChannelModel): Boolean {
        val serverTime = Date(System.currentTimeMillis())
        val expiredTime = DateHelper.getExpiredTime(channel.channelHeader.expiredTime)
        serverTime.time = serverTime.time + channel.channelConfig.serverTimeOffset
        return serverTime.after(expiredTime)
    }

    private fun mapSectionToChannel(section: RechargeHomepageSections.Section): ChannelModel? {
        section.items.firstOrNull()?.run {
            val sectionId = section.id.toString()
            val serverDateMillisecond = getServerTime(serverDate).time

            return ChannelModel(sectionId, sectionId,
                    channelHeader = ChannelHeader(sectionId, section.title, section.subtitle, dueDate),
                    channelConfig = ChannelConfig(serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffset(serverDateMillisecond),
                            enableTimeDiffMoreThan24h = true)
            )
        }
        return null
    }

    fun mapItemsToSearchCategoryModels(sections: RechargeHomepageSections): List<DigitalHomePageSearchCategoryModel> {
        val searchCategoryModels = mutableListOf<DigitalHomePageSearchCategoryModel>()
        sections.sections.forEach {
            searchCategoryModels.addAll(it.items.map{ item ->
                DigitalHomePageSearchCategoryModel(
                        item.id.toString(),
                        item.title,
                        item.title,
                        item.applink,
                        item.mediaUrl
                )
            })
        }
        return searchCategoryModels
    }

    fun mapRechargeTickertoTickerData(list: List<RechargeTicker>): List<TickerData>{
        return list.map {
            TickerData(
                    it.name,
                    it.content,
                    when(it.type){
                        TickerRechargeEnum.INFO.type -> Ticker.TYPE_INFORMATION
                        TickerRechargeEnum.SUCCESS.type -> Ticker.TYPE_ANNOUNCEMENT
                        TickerRechargeEnum.DANGER.type -> Ticker.TYPE_ERROR
                        TickerRechargeEnum.WARNING.type -> Ticker.TYPE_WARNING
                        else -> Ticker.TYPE_INFORMATION
                    },
                    true
            )
        }
    }

    private fun isExpired(section: RechargeHomepageSections.Section): Boolean {
        section.items.firstOrNull()?.run {
            if (dueDate.isNotEmpty()) {
                val serverDateMillisecond = getServerTime(serverDate).time
                val expiredTime = DateHelper.getExpiredTime(dueDate)
                val serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffset(serverDateMillisecond)
                return isExpired(serverTimeOffset, expiredTime)
            }
        }
        return false
    }

    private fun getServerTime(serverTimeString: String): Date {
        return DateHelper.getExpiredTime(serverTimeString)
    }
}