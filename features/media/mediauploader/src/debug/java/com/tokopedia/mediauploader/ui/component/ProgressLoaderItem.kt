package com.tokopedia.mediauploader.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun ProgressLoaderItem(type: String, value: Int, modifier: Modifier = Modifier) {
    NestTypography(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
                    append("$type: ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = NestTheme.colors.GN._500
                    )
                ) {
                    append("$value%")
                }
            }
        },
        modifier
    )
}

@Preview
@Composable
fun ProgressLoaderItemPreview() {
    ProgressLoaderItem(
        type = "Upload",
        value = 0
    )
}
