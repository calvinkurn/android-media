package com.tokopedia.common_compose.principles

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTypography(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600),
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = onTextLayout,
        textAlign = textAlign
    )
}

@Composable
fun NestTypography(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600),
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {
    Text(
        text = text,
        modifier = modifier,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = onTextLayout
    )
}

@Preview(name = "Typography")
@Composable
fun NestTypographyPreview() {
    NestTypography(
        text = "Flash Sale",
        Modifier
    )
}

@Preview(name = "Typography (Bold)")
@Composable
fun NestTypographyBoldPreview() {
    NestTypography(
        text = "Flash Sale",
        Modifier,
        textStyle = NestTheme.typography.display3.copy(fontWeight = FontWeight.Bold)
    )
}

@Preview(name = "Typography with Annotation String")
@Composable
fun NestTypographyAnnotationPreview() {
    NestTypography(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
                    append("Hello ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = NestTheme.colors.GN._500
                    )
                ) {
                    append("World ")
                }
                append("Compose")
            }
        },
        Modifier
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestLightPreview() {
    NestTheme {
        NestTypography(
            text = "Flash Sale",
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._0
            )
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun NestDarkPreview() {
    NestTheme {
        NestTypography(
            text = "Flash Sale",
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._0
            )
        )
    }
}
