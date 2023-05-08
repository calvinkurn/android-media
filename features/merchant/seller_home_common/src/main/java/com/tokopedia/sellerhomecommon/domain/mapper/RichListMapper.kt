package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetRichListDataResponse
import com.tokopedia.sellerhomecommon.domain.model.RichListSectionItemsModel
import com.tokopedia.sellerhomecommon.domain.model.RichListSectionModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RichListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipListItemUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 12/04/23.
 */

class RichListMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetRichListDataResponse, List<RichListDataUiModel>> {

    companion object {
        private const val DOWN_TREND = -1
        private const val UP_TREND = 1
        private const val SECTION_TYPE_RANK = 0
        private const val SECTION_TYPE_CAPTION = 1
        private const val SECTION_TYPE_TICKER = 2
    }

    override fun mapRemoteDataToUiData(
        response: GetRichListDataResponse,
        isFromCache: Boolean
    ): List<RichListDataUiModel> {
        return response.data.widgetData.map { data ->
            RichListDataUiModel(
                dataKey = data.dataKey,
                error = data.errorMsg,
                showWidget = data.shouldShowWidget,
                isFromCache = isFromCache,
                lastUpdated = getLastUpdatedMillis(data.dataKey, isFromCache),
                richListData = getRichListData(data.sections)
            )
        }
    }

    private fun getRichListData(sections: List<RichListSectionModel>): List<BaseRichListItemUiModel> {
        return sections.mapNotNull { item ->
            when (item.type) {
                SECTION_TYPE_RANK -> getRankItems(item.items)
                SECTION_TYPE_CAPTION -> getCaptionItems(item)
                SECTION_TYPE_TICKER -> getTickerItems(item.items)
                else -> null
            }
        }.flatten()
    }

    private fun getCaptionItems(item: RichListSectionModel): List<BaseRichListItemUiModel> {
        return listOf(
            BaseRichListItemUiModel.CaptionItemUiModel(
                caption = item.caption,
                ctaList = item.items.map {
                    BaseRichListItemUiModel.CaptionItemUiModel.CaptionCtaUiModel(
                        title = it.title,
                        subtitle = it.subtitle,
                        imageUrl = it.imageUrl
                    )
                }
            )
        )
    }

    private fun getTickerItems(items: List<RichListSectionItemsModel>): List<BaseRichListItemUiModel> {
        return items.map {
            BaseRichListItemUiModel.TickerItemUiModel(tickerDescription = it.subtitle)
        }
    }

    private fun getRankItems(items: List<RichListSectionItemsModel>): List<BaseRichListItemUiModel> {
        return items.map {
            BaseRichListItemUiModel.RankItemUiModel(
                title = it.title,
                subTitle = it.stateText,
                imageUrl = it.imageUrl,
                rankTrend = when (it.rankTrend) {
                    DOWN_TREND -> BaseRichListItemUiModel.RankItemUiModel.RankTrend.DOWN
                    UP_TREND -> BaseRichListItemUiModel.RankItemUiModel.RankTrend.UP
                    else -> BaseRichListItemUiModel.RankItemUiModel.RankTrend.NONE
                },
                rankValue = it.rankValue,
                rankNote = it.rankNote,
                tooltip = if (it.stateTooltip.shouldShow) {
                    TooltipListItemUiModel(
                        title = it.stateTooltip.title,
                        description = it.stateTooltip.description,
                    )
                } else {
                    null
                }
            )
        }
    }
}