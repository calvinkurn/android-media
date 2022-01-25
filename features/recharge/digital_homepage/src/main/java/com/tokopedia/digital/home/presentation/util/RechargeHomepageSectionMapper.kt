package com.tokopedia.digital.home.presentation.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.old.model.DigitalHomepageSearchEnumLayoutType
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
import java.util.*

object RechargeHomepageSectionMapper {

    private const val LEGO_BANNER_SIZE_6 = 6
    private const val LEGO_BANNER_SIZE_3 = 3

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
            if(section.template == RechargeHomepageViewModel.SECTION_TOP_BANNER ||
                section.template == RechargeHomepageViewModel.SECTION_TOP_BANNER_EMPTY
            ){
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
                            ReminderWidgetModel(id=id, data = ReminderWidget(id), source = ReminderEnum.RECHARGE)
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
                    SECTION_PRODUCT_CARD_ROW, SECTION_PRODUCT_CARD_ROW_1X1 -> {
                        RechargeHomepageProductCardsModel(it)
                    }
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
                    SECTION_SWIPE_BANNER -> RechargeHomepageSwipeBannerModel(it)
                    SECTION_PRODUCT_CARD_DGU -> RechargeProductCardUnifyModel(it)
                    SECTION_3_ICONS -> RechargeHomepageThreeIconsModel(it)
                    else -> null
                }
            }
        }
    }

    private fun getReminderWidgetModel(section: RechargeHomepageSections.Section): ReminderWidgetModel? {
        section.items.firstOrNull()?.run {
            return ReminderWidgetModel(id = section.id,
                    data = ReminderWidget(section.id,
                    listOf(ReminderData(
                            applink,
                            id = section.id,
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
            ), source = ReminderEnum.RECHARGE)
        }
        return null
    }

    private fun getDynamicLegoBannerModel(section: RechargeHomepageSections.Section): DynamicLegoBannerDataModel? {
        if (section.items.size < LEGO_BANNER_SIZE_3) return null

        val (imageCount, layoutConfig) = when {
            section.items.size >= LEGO_BANNER_SIZE_6 -> LEGO_BANNER_SIZE_6 to DynamicChannelLayout.LAYOUT_6_IMAGE
            else -> LEGO_BANNER_SIZE_3 to DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE
        }
        return DynamicLegoBannerDataModel(ChannelModel(
                section.id,
                section.id,
                channelConfig = ChannelConfig(layoutConfig),
                channelHeader = ChannelHeader(name = section.title, subtitle = section.subtitle),
                channelGrids = section.items.take(imageCount).map { item ->
                    ChannelGrid(item.id, imageUrl = item.mediaUrl, applink = item.applink)
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
                        item.id,
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

    fun mapSearchAutoCompletetoSearch(autoComplete: DigitalHomePageSearchAutoComplete, searchQuery:String): List<DigitalHomePageSearchCategoryModel> {
        val searchCategoryModels = mutableListOf<DigitalHomePageSearchCategoryModel>()
        autoComplete.digiPersoSearchSuggestion.data.items.map{ item ->
           searchCategoryModels.add(DigitalHomePageSearchCategoryModel(
                   id = "",
                   label = item.title,
                   subtitle = item.subtitle,
                   applink = item.applink,
                   icon =  item.imageUrl,
                   searchQuery = searchQuery,
                   typeLayout = getLayoutType(item.template),
                   trackerUser = autoComplete.digiPersoSearchSuggestion.data.tracking,
                   trackerItem = item.tracking
           ))
        }
        return searchCategoryModels
    }

    fun boldReverseSearchAutoComplete(label: String, searchQuery: String): SpannableStringBuilder {
        val splittedString = label.split(" ")
        val resultString = SpannableStringBuilder()
        if (splittedString.isNotEmpty()) {
            for (splitted in splittedString) {
                resultString.append(spannableBoldString(splitted, searchQuery))
            }
        } else resultString.append(spannableBoldString(label, searchQuery))

        return resultString
    }

    private fun spannableBoldString(label:String, searchQuery: String): SpannableStringBuilder {
        val spannableString = SpannableStringBuilder(label.plus(" "))
        val searchQueryIndexes = label.indexesOf(searchQuery, ignoreCase = true)
        for (searchQueryIndex in searchQueryIndexes){
            if (searchQueryIndex > -1) {
                spannableString.setSpan(StyleSpan(Typeface.BOLD), searchQueryIndex, searchQueryIndex + searchQuery.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return spannableString
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

    private fun getLayoutType(template:String): Int {
        return when(template){
            DigitalHomepageSearchEnumLayoutType.SINGLE_LINE.layoutTemplate -> DigitalHomepageSearchEnumLayoutType.SINGLE_LINE.layoutType
            DigitalHomepageSearchEnumLayoutType.DOUBLE_LINE.layoutTemplate -> DigitalHomepageSearchEnumLayoutType.DOUBLE_LINE.layoutType
            DigitalHomepageSearchEnumLayoutType.HEADER.layoutTemplate -> DigitalHomepageSearchEnumLayoutType.HEADER.layoutType
            DigitalHomepageSearchEnumLayoutType.DEFAULT.layoutTemplate -> DigitalHomepageSearchEnumLayoutType.DEFAULT.layoutType
            else -> DigitalHomepageSearchEnumLayoutType.DEFAULT.layoutType
        }
    }

    fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
        tailrec fun String.collectIndexesOf(offset: Int = 0, indexes: List<Int> = emptyList()): List<Int> =
                when (val index = indexOf(substr, offset, ignoreCase)) {
                    -1 -> indexes
                    else -> collectIndexesOf(index + substr.length, indexes + index)
                }

        return when (this) {
            null -> emptyList()
            else -> collectIndexesOf()
        }
    }
}