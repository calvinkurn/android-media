package com.tokopedia.targetedticker.domain

import com.tokopedia.unifycomponents.ticker.Ticker

object TargetedTickerMapper {

    const val TICKER_INFO_TYPE = "info"
    const val TICKER_WARNING_TYPE = "warning"
    const val TICKER_ERROR_TYPE = "danger"

    private const val HTML_HYPERLINK_FORMAT = "<a href=\"%s\">%s</a>"

    fun convertTargetedTickerToUiModel(
        targetedTickerData: GetTargetedTickerResponse.GetTargetedTickerData? = null
    ): TickerModel {
        val tickerItems = arrayListOf<TickerModel.TickerItem>()

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
            TICKER_INFO_TYPE -> Ticker.TYPE_ANNOUNCEMENT
            TICKER_WARNING_TYPE -> Ticker.TYPE_WARNING
            TICKER_ERROR_TYPE -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    private fun GetTargetedTickerResponse.GetTargetedTickerData.ListItem.Action.getUrl(): String {
        return this.appURL.ifEmpty { this.webURL }
    }

    private fun GetTargetedTickerResponse.GetTargetedTickerData.ListItem.generateContent(): String {
        return StringBuilder().apply {
            append(content)
            action.takeIf { it.label.isNotEmpty() }?.let { action ->
                appendEmptySpace()
                appendHyperlinkText(label = action.label, url = action.getUrl())
            }
        }.toString()
    }

    private fun StringBuilder.appendHyperlinkText(label: String, url: String) {
        if (label.isNotBlank() && url.isNotBlank()) {
            append(String.format(HTML_HYPERLINK_FORMAT, url, label))
        }
    }

    private fun StringBuilder.appendEmptySpace() {
        append(" ")
    }
}
