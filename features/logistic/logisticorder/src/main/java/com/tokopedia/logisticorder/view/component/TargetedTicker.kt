package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.components.ticker.NestTickerData
import com.tokopedia.nest.components.ticker.TickerType
import com.tokopedia.nest.components.ticker.TickerVariant
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.targetedticker.domain.TargetedTickerHelper.toTickerData
import com.tokopedia.targetedticker.domain.TickerModel
import com.tokopedia.unifycomponents.HtmlLinkHelper

@Composable
fun TargetedTicker(tickerData: TickerModel?, openWebview: (url: String) -> Unit) {
    if (tickerData?.item?.isNotEmpty() == true) {
        val model = tickerData.toTickerData().map {
            NestTickerData(
                tickerTitle = it.title.orEmpty(),
                tickerDescription = HtmlLinkHelper(
                    LocalContext.current,
                    it.description
                ).spannedString?.toAnnotatedString() ?: "",
                tickerVariant = TickerVariant.LOOSE,
                tickerType = it.type.toTickerType
            )
        }
        NestTicker(
            modifier = Modifier.padding(horizontal = 20.dp),
            ticker = model
        ) { spannedRange ->
            if (spannedRange.tag == URL_TAG) openWebview(spannedRange.item)
        }
    }
}

private val Int.toTickerType: TickerType
    get() {
        return when (this) {
            1 -> TickerType.ERROR
            3 -> TickerType.WARNING
            else -> TickerType.ANNOUNCEMENT
        }
    }

private val URL_TAG = "url"

@Preview
@Composable
fun TargetedTickerPreview() {
    val tickerData = listOf(
        TickerModel.TickerItem(
            type = 3,
            title = "Pengiriman mengalami kendala",
            content = "Pengiriman terlambat karena cuaca buruk",
            linkUrl = "https://tokopedia/help"
        ),
        TickerModel.TickerItem(
            type = 2,
            title = "Pengiriman menggunakan Kurir Rekomendasi",
            content = "Informasi mengenai kurir rekomendasi",
            linkUrl = "https://tokopedia/help"
        )
    )
    val data = TickerModel(
        item = tickerData
    )
    NestTheme {
        TargetedTicker(tickerData = data, openWebview = {})
    }
}
