package com.tokopedia.logisticCommon.domain.mapper

import com.tokopedia.logisticCommon.domain.model.TickerModel
import com.tokopedia.logisticCommon.domain.response.GetTargetedTickerResponse
import com.tokopedia.logisticCommon.util.StringFormatterHelper.appendEmptySpace
import com.tokopedia.logisticCommon.util.StringFormatterHelper.appendHyperlinkText
import com.tokopedia.unifycomponents.ticker.Ticker

object TargetedTickerMapper {

    const val TICKER_INFO_TYPE = "info"
    const val TICKER_WARNING_TYPE = "warning"
    const val TICKER_ERROR_TYPE = "danger"

    fun convertTargetedTickerToUiModel(
        firstTickerContent: String? = null,
        targetedTickerData: GetTargetedTickerResponse.GetTargetedTickerData? = null
    ): TickerModel {
        val tickerItems = arrayListOf<TickerModel.TickerItem>()

        if (firstTickerContent?.isNotBlank() == true) {
            tickerItems.add(
                TickerModel.TickerItem(
                    type = Ticker.TYPE_ANNOUNCEMENT,
                    content = firstTickerContent
                )
            )
        }

        targetedTickerData?.list?.sortedBy { it.priority }?.apply {
            tickerItems.addAll(
                map {
                    TickerModel.TickerItem(
                        id = it.id,
                        type = it.toTickerType(),
                        title = it.title,
                        content = it.generateContent(),
                        linkUrl = it.action.getUrl(),
                        priority = it.priority
                    )
                }
            )
        }

        return TickerModel(
            item = tickerItems
        )
    }

    private fun GetTargetedTickerResponse.GetTargetedTickerData.ListItem.toTickerType(): Int {
        return when (this.type.lowercase()) {
            TICKER_INFO_TYPE -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
            TICKER_WARNING_TYPE -> {
                Ticker.TYPE_WARNING
            }
            TICKER_ERROR_TYPE -> {
                Ticker.TYPE_ERROR
            }
            else -> {
                Ticker.TYPE_INFORMATION
            }
        }
    }

    private fun GetTargetedTickerResponse.GetTargetedTickerData.ListItem.Action.getUrl(): String {
        return this.appURL.ifEmpty { this.webURL }
    }

    private fun GetTargetedTickerResponse.GetTargetedTickerData.ListItem.generateContent(): String {
        return java.lang.StringBuilder().apply {
            append(content)
            action.takeIf { it.label.isNotEmpty() }?.let { action ->
                appendEmptySpace()
                appendHyperlinkText(label = action.label, url = action.getUrl())
            }
        }.toString()
    }
}
