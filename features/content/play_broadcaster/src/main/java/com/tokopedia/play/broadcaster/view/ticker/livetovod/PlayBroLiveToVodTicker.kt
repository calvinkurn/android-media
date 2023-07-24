package com.tokopedia.play.broadcaster.view.ticker.livetovod

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.components.ticker.NestTickerData
import com.tokopedia.nest.components.ticker.TickerType
import com.tokopedia.nest.components.ticker.TickerVariant
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R

@Composable
fun PlayBroLiveToVodTicker(
    onDismissedPressed: () -> Unit,
    onLearnMorePressed: () -> Unit,
) {
    NestTheme {
        LiveToVodTickerContent(
            onDismissedPressed = onDismissedPressed,
            onLearnMorePressed = onLearnMorePressed,
        )
    }
}

@Composable
private fun LiveToVodTickerContent(
    onDismissedPressed: () -> Unit,
    onLearnMorePressed: () -> Unit,
) {
    val textDescription = buildAnnotatedString {
        append(stringResource(id = R.string.play_prepare_broadcaster_disable_live_to_vod_description_ticker))
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.GN._500,
            )
        ) {
            pushStringAnnotation(tag = LEARN_MORE_TEXT_TAG, annotation = LEARN_MORE_TEXT_TAG)
            append(" ")
            append(stringResource(id = R.string.play_prepare_broadcaster_disable_live_to_vod_learn_more))
            append(" ")
        }
    }
    NestTicker(
        ticker = listOf(
            NestTickerData(
                tickerTitle = stringResource(id = R.string.play_prepare_broadcaster_disable_live_to_vod_title_ticker),
                tickerDescription = textDescription,
                tickerType = TickerType.ANNOUNCEMENT,
                tickerVariant = TickerVariant.FULL,
            )
        ),
        onDismissed = { onDismissedPressed.invoke() },
        onClickText = { offset ->
            textDescription.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { span ->
                    if (span.item == LEARN_MORE_TEXT_TAG) onLearnMorePressed.invoke()
                }
        },
    )
}

@Composable
@Preview(showBackground = true)
internal fun Preview() {
    PlayBroLiveToVodTicker(
        onDismissedPressed = {},
        onLearnMorePressed = {},
    )
}

private const val LEARN_MORE_TEXT_TAG = "learn_more_text_tag"
