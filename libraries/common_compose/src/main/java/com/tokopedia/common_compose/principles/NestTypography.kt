package com.tokopedia.common_compose.principles

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
    textStyle: TextStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN600),
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
    textStyle: TextStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN600),
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
    NestTypography(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN600)) {
                    append("Hello ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = NestTheme.colors.GN500
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