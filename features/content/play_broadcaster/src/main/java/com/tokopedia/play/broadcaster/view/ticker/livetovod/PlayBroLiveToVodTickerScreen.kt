package com.tokopedia.play.broadcaster.view.ticker.livetovod

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.components.ticker.NestTickerData
import com.tokopedia.nest.components.ticker.TickerType
import com.tokopedia.nest.components.ticker.TickerVariant
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.ui.model.livetovod.SpanAnnotationTextModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetUiModel

@Composable
fun PlayBroLiveToVodTickerScreen(
    data: TickerBottomSheetUiModel,
    onDismissedPressed: () -> Unit,
    onActionTextPressed: (appLink: String) -> Unit,
) {
    NestTheme {
        LiveToVodTickerContent(
            data = data,
            onDismissedPressed = onDismissedPressed,
            onActionTextPressed = onActionTextPressed,
        )
    }
}

@Composable
private fun LiveToVodTickerContent(
    data: TickerBottomSheetUiModel,
    onDismissedPressed: () -> Unit,
    onActionTextPressed: (appLink: String) -> Unit,
) = with(data.mainText) {

    val nestTickerData = mutableListOf<NestTickerData>()
    forEach { tickerData ->
        val descriptionText = generateSpanText(
            fullText = tickerData.description,
            action = tickerData.action,
        )
        nestTickerData.add(
            NestTickerData(
                tickerTitle = tickerData.title,
                tickerDescription = descriptionText,
                tickerType = TickerType.ANNOUNCEMENT,
                tickerVariant = TickerVariant.FULL,
            )
        )
    }

    NestTicker(
        ticker = nestTickerData,
        onDismissed = { onDismissedPressed.invoke() },
        onClickText = { offset ->
//            TODO change implementation after updated version from unify compose merged
//            descriptionText.getStringAnnotations(offset, offset)
//                .firstOrNull()?.let { span ->
//                    action.map { current ->
//                        if (span.item != current.item) return@map
//                        onActionTextPressed.invoke(current.link)
//                    }
//                }
        },
    )
}

@Composable
private fun generateSpanText(
    fullText: String,
    action: List<TickerBottomSheetUiModel.Action>,
): AnnotatedString {
    var newFullText = fullText
    val mappedText = action.map { data ->
        if (!newFullText.contains(data.item)) return AnnotatedString(fullText)
        val index = newFullText.indexOf(data.item)
        newFullText = newFullText.replaceFirst(data.item, data.text)
        SpanAnnotationTextModel(
            item = data.item,
            start = index,
            end = index + data.text.length
        )
    }

    val spannableText = buildAnnotatedString {
        append(newFullText)
        mappedText.forEach { map ->
            addStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_GN500),
                ),
                start = map.start,
                end = map.end,
            )
            addStringAnnotation(
                tag = map.item,
                annotation = map.item,
                start = map.start,
                end = map.end,
            )
        }
    }

    return spannableText
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    PlayBroLiveToVodTickerScreen(
        data = TickerBottomSheetUiModel.Dummy,
        onDismissedPressed = {},
        onActionTextPressed = {},
    )
}
