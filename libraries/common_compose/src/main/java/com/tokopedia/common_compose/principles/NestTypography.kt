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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTypography(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = NestTheme.typography.display3,
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

@Preview(name = "Typography (Heading 1)")
@Composable
fun NestTypographyHeading1Preview() {
    NestTheme {
        NestTypography(
            text = "Jetpack Compose",
            modifier = Modifier,
            textStyle = NestTheme.typography.heading1
        )
    }

}

@Preview(name = "Typography (Heading 2)")
@Composable
fun NestTypographyHeading2Preview() {
    NestTheme {
        NestTypography(
            text = "Jetpack Compose",
            modifier = Modifier,
            textStyle = NestTheme.typography.heading2
        )
    }

}

@Preview(name = "Typography (Heading 3)")
@Composable
fun NestTypographyHeading3Preview() {
    NestTheme {
        NestTypography(
            text = "Jetpack Compose",
            modifier = Modifier,
            textStyle = NestTheme.typography.heading3
        )
    }

}

@Composable
fun NestTypography(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = NestTheme.typography.display3,
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

@Composable
fun NestTypography(
    text: CharSequence,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600),
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {
    if (text is AnnotatedString) {
        Text(
            text = text,
            modifier = modifier,
            style = textStyle,
            maxLines = maxLines,
            overflow = overflow,
            onTextLayout = onTextLayout
        )
    } else {
        Text(
            text = text.toString(),
            modifier = modifier,
            style = textStyle,
            maxLines = maxLines,
            overflow = overflow,
            onTextLayout = onTextLayout
        )
    }
}

@Preview(name = "Typography (Display 1)")
@Composable
fun NestTypographyDisplay1Preview() {
    NestTheme {
        NestTypography(
            text = "Jetpack Compose",
            modifier = Modifier,
            textStyle = NestTheme.typography.display1
        )
    }

}

@Preview(name = "Typography (Display 2)")
@Composable
fun NestTypographyDisplay2Preview() {
    NestTheme {
        NestTypography(
            text = "Jetpack Compose",
            modifier = Modifier,
            textStyle = NestTheme.typography.display2
        )
    }
}



@Preview(name = "Typography (Display 3)")
@Composable
fun NestTypographyDisplay3Preview() {
    NestTheme {
        NestTypography(
            text = "Jetpack Compose",
            modifier = Modifier,
            textStyle = NestTheme.typography.display3
        )
    }
}

@Preview(name = "Typography with Annotation String")
@Composable
fun NestTypographyAnnotationPreview() {
    val helloColor = NestTheme.colors.NN._600
    val worldColor = NestTheme.colors.GN._500

    NestTypography(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                withStyle(style = SpanStyle(color = helloColor)) {
                    append("Hello ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = worldColor
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
