package com.tokopedia.mediauploader.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun ProgressLoaderItem(type: String, value: Int, modifier: Modifier = Modifier) {
    val typeColor = NestTheme.colors.NN._600
    val valueColor = NestTheme.colors.GN._500

    Box(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        NestTypography(
            text = buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(style = SpanStyle(color = typeColor)) {
                        append("$type: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = valueColor
                        )
                    ) {
                        append("$value%")
                    }
                }
            },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview
@Composable
fun ProgressLoaderItemPreview() {
    ProgressLoaderItem(
        type = "Upload",
        value = 0
    )
}
