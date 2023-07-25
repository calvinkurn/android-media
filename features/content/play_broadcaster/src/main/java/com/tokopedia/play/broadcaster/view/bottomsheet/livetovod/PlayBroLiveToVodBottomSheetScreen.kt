package com.tokopedia.play.broadcaster.view.bottomsheet.livetovod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.livetovod.SpanAnnotationTextModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetUiModel

@Composable
fun PlayBroadcasterLiveToVodBottomSheetScreen(
    data: TickerBottomSheetUiModel,
    onButtonClick: () -> Unit,
    onActionTextPressed: (appLink: String) -> Unit,
) {
    NestTheme {
        LiveToVodBottomSheetContent(
            data = data,
            onButtonClick = onButtonClick,
            onActionTextPressed = onActionTextPressed,
        )
    }
}

@Composable
private fun LiveToVodBottomSheetContent(
    data: TickerBottomSheetUiModel,
    onButtonClick: () -> Unit,
    onActionTextPressed: (appLink: String) -> Unit,
) = with(data) {
    @Composable
    fun List<TickerBottomSheetUiModel.MainText>.MainText(
        onActionTextPressed: (appLink: String) -> Unit
    ) {
        @Composable
        fun TickerBottomSheetUiModel.MainText.TitleText() {
            NestTypography(
                text = title,
                textStyle = NestTheme.typography.heading2
                    .copy(
                        textAlign = TextAlign.Center,
                        color = NestTheme.colors.NN._1000,
                    ),
            )
        }

        @Composable
        fun TickerBottomSheetUiModel.MainText.DescriptionText(
            onActionTextPressed: (appLink: String) -> Unit
        ) {
            val descriptionText = generateSpanText(fullText = description, action = action)

            NestTypography(
                text = descriptionText,
                modifier = Modifier.padding(top = 8.dp),
                textStyle = NestTheme.typography.body2
                    .copy(
                        textAlign = TextAlign.Center,
                        color = NestTheme.colors.NN._600,
                    ),
                onClickText = { offset ->
                    descriptionText.getStringAnnotations(offset, offset)
                        .firstOrNull()?.let { span ->
                            action.map { current ->
                                if (span.item != current.item) return@map
                                onActionTextPressed.invoke(current.link)
                            }
                        }
                }
            )
        }

        with(first()) {
            TitleText()
            DescriptionText(onActionTextPressed)
        }
    }

    @Composable
    fun TickerBottomSheetUiModel.BottomText.BottomText(
        onActionTextPressed: (appLink: String) -> Unit
    ) {
        val bottomText = generateSpanText(fullText = description, action = action)
        NestTypography(
            text = bottomText,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            textStyle = NestTheme.typography.body3
                .copy(
                    textAlign = TextAlign.Center,
                    color = NestTheme.colors.NN._600,
                ),
            onClickText = { offset ->
                bottomText.getStringAnnotations(offset, offset)
                    .firstOrNull()?.let { span ->
                        action.map { current ->
                            if (span.item != current.item) return@map
                            onActionTextPressed.invoke(current.link)
                        }
                    }
            },
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        NestImage(
            source = ImageSource.Remote(source = data.imageURL),
            modifier = Modifier
                .aspectRatio(16f / 10f)
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        )
        mainText.MainText(onActionTextPressed)
        NestButton(
            text = stringResource(id = R.string.play_ok),
            onClick = { onButtonClick.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )
        bottomText.BottomText(onActionTextPressed)
    }
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
                    color = NestTheme.colors.GN._500,
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
internal fun Preview() {
    PlayBroadcasterLiveToVodBottomSheetScreen(
        data = TickerBottomSheetUiModel.Dummy,
        onButtonClick = {},
        onActionTextPressed = {},
    )
}
