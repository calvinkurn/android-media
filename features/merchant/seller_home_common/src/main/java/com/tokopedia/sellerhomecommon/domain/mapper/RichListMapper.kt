package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetRichListDataResponse
import com.tokopedia.sellerhomecommon.domain.model.RichListSectionItemsModel
import com.tokopedia.sellerhomecommon.domain.model.RichListSectionModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.model.RichListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 12/04/23.
 */

class RichListMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface, lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetRichListDataResponse, List<RichListDataUiModel>> {

    companion object {
        private const val DOWN_TREND = "downtrend"
        private const val UP_TREND = "uptrend"
        private const val DISABLED_TREND = "disabled"
        private const val SECTION_TYPE_RANK = "simpleRank"
        private const val SECTION_TYPE_CAPTION = "tileBox"
        private const val SECTION_TYPE_TICKER = "ticker"
        private const val LAST_UPDATED_FORMAT = "Update terakhir %s WIB"
        private const val START_DELIMITER_URL = "='"
        private const val END_DELIMITER_URL = "'>"
        private const val START_DELIMITER_CTA_TEXT = "'>"
        private const val END_DELIMITER_CTA_TEXT = "</"
    }

    override fun mapRemoteDataToUiData(
        response: GetRichListDataResponse, isFromCache: Boolean
    ): List<RichListDataUiModel> {
        return response.data.widgetData.map { data ->
            RichListDataUiModel(
                dataKey = data.dataKey,
                error = data.errorMsg,
                showWidget = data.shouldShowWidget,
                isFromCache = isFromCache,
                lastUpdated = getLastUpdated(data.updateInfoUnix),
                richListData = getRichListData(data.sections)
            )
        }
    }

    private fun getLastUpdated(updateInfoUnix: Long): String {
        val sdf = SimpleDateFormat(DateTimeUtil.FORMAT_DD_MMMM_YYYY, DateTimeUtil.localeID)
        sdf.timeZone = TimeZone.getTimeZone(DateTimeUtil.TIME_ZONE)
        val oneSecInMillis = TimeUnit.SECONDS.toMillis(Int.ONE.toLong())
        val timeInMillis = updateInfoUnix.times(oneSecInMillis)
        return String.format(LAST_UPDATED_FORMAT, sdf.format(Date(timeInMillis)))
    }

    private fun getRichListData(sections: List<RichListSectionModel>): List<BaseRichListItem> {
        return sections.mapNotNull { item ->
            when {
                item.type.equals(SECTION_TYPE_RANK, true) -> getRankItems(item.items)
                item.type.equals(SECTION_TYPE_CAPTION, true) -> getCaptionItems(item)
                item.type.equals(SECTION_TYPE_TICKER, true) -> getTickerItems(item.items)
                else -> null
            }
        }.flatten()
    }

    private fun getCaptionItems(item: RichListSectionModel): List<BaseRichListItem> {
        return listOf(
            BaseRichListItem.CaptionItemUiModel(
                caption = item.caption,
                ctaText = getCtaText(item.caption),
                url = getURL(item.caption)
            )
        )
    }

    private fun getURL(caption: String): String {
        return caption.substringAfter(START_DELIMITER_URL)
            .substringBefore(END_DELIMITER_URL)
    }

    private fun getCtaText(caption: String): String {
        return caption.substringAfter(START_DELIMITER_CTA_TEXT)
            .substringBefore(END_DELIMITER_CTA_TEXT)
    }

    private fun getTickerItems(items: List<RichListSectionItemsModel>): List<BaseRichListItem> {
        return items.map {
            BaseRichListItem.TickerItemUiModel(tickerDescription = it.subtitle)
        }
    }

    private fun getRankItems(items: List<RichListSectionItemsModel>): List<BaseRichListItem> {
        return items.map {
            BaseRichListItem.RankItemUiModel(
                title = it.title,
                subTitle = it.stateText,
                rankTrend = getTrend(it.rankTrend),
                rankValue = it.rankValue,
                rankNote = it.rankNote,
                tooltip = if (it.stateTooltip.shouldShow) {
                    TooltipUiModel(
                        title = it.stateTooltip.title,
                        content = it.stateTooltip.description,
                        shouldShow = true,
                        list = emptyList()
                    )
                } else {
                    null
                }
            )
        }
    }

    private fun getTrend(trend: String): BaseRichListItem.RankItemUiModel.RankTrend {
        return when {
            trend.equals(DISABLED_TREND, true) -> {
                BaseRichListItem.RankItemUiModel.RankTrend.DISABLED
            }
            trend.equals(DOWN_TREND, true) -> BaseRichListItem.RankItemUiModel.RankTrend.DOWN
            trend.equals(UP_TREND, true) -> BaseRichListItem.RankItemUiModel.RankTrend.UP
            else -> BaseRichListItem.RankItemUiModel.RankTrend.NONE
        }
    }
}