package com.tokopedia.play.broadcaster.view.bottomsheet.livetovod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R

@Composable
fun PlayBroadcasterLiveToVodBottomSheetScreen(
    onBackPressed: () -> Unit,
    onLearnMorePressed: () -> Unit,
) {
    NestTheme {
        LiveToVodBottomSheetContent(
            onButtonClick = { onBackPressed.invoke() },
            onLearnMorePressed = { onLearnMorePressed.invoke() },
        )
    }
}

@Composable
private fun LiveToVodBottomSheetContent(
    onButtonClick: () -> Unit,
    onLearnMorePressed: () -> Unit,
) {
    val textLearnMore = buildAnnotatedString {
        append(stringResource(id = R.string.play_prepare_broadcaster_disable_live_to_vod_learn_more_text_bottom_sheet))
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
    ) {
        NestImage(
            imageUrl = stringResource(id = R.string.play_shorts_affiliate_success),
            modifier = Modifier
                .width(350.dp)
                .height(280.dp),
        )
        NestTypography(
            text = stringResource(id = R.string.play_prepare_broadcaster_disable_live_to_vod_title_bottom_sheet),
            modifier = Modifier.padding(top = 16.dp),
            textStyle = NestTheme.typography.heading2
                .copy(
                    textAlign = TextAlign.Center,
                    color = NestTheme.colors.NN._0,
                ),
        )
        NestTypography(
            text = stringResource(id = R.string.play_prepare_broadcaster_disable_live_to_vod_description_bottom_sheet),
            modifier = Modifier.padding(top = 8.dp),
            textStyle = NestTheme.typography.body1
                .copy(
                    textAlign = TextAlign.Center,
                    color = NestTheme.colors.NN._1000,
                ),
        )
        NestButton(
            text = stringResource(id = R.string.play_ok),
            onClick = { onButtonClick.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )
        NestTypography(
            text = textLearnMore,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            textStyle = NestTheme.typography.body2
                .copy(
                    textAlign = TextAlign.Center,
                    color = NestTheme.colors.NN._1000,
                ),
            onClickText = { offset ->
                textLearnMore.getStringAnnotations(offset, offset)
                    .firstOrNull()?.let { span ->
                        if (span.item == LEARN_MORE_TEXT_TAG) onLearnMorePressed.invoke()
                    }
            },
        )
    }
}

@Composable
@Preview(showBackground = true)
internal fun Preview() {
    PlayBroadcasterLiveToVodBottomSheetScreen(
        onBackPressed = {},
        onLearnMorePressed = {},
    )
}

private const val LEARN_MORE_TEXT_TAG = "learn_more_text_tag"
