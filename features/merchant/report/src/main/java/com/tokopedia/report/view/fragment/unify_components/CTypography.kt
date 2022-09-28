package com.tokopedia.report.view.fragment.unify_components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by yovi.putra on 28/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun CTypography(
    modifier: Modifier = Modifier,
    text: String,
    type: TextUnifyType,
    weight: TextUnifyWeight = TextUnifyWeight.Regular,
    fontStyle: FontStyle? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    isFontTypeOpenSauceOne: Boolean = false,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {

    val fontSize = getFontSize(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type
    )
    val letterSpacing = getLetterSpacing(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type,
        default = textStyle.letterSpacing.value
    )

    Text(
        modifier = modifier.setPaddingOpenSauceOne(type = type),
        text = text,
        fontStyle = fontStyle,
        style = textStyle.copy(
            fontSize = fontSize,
            letterSpacing = letterSpacing
        ),
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = onTextLayout
    )
}

private fun getFontSize(
    isFontTypeOpenSauceOne: Boolean,
    type: TextUnifyType
): TextUnit = if (isFontTypeOpenSauceOne) {
    type.fontSize
} else {
    type.openSourceSize
}

private fun getLetterSpacing(
    isFontTypeOpenSauceOne: Boolean,
    type: TextUnifyType,
    default: Float
): TextUnit = if (isFontTypeOpenSauceOne) {
    when (type) {
        TextUnifyType.Heading1 -> (-0.0083f).sp
        TextUnifyType.Heading2 -> (-0.005f).sp
        TextUnifyType.Heading3 -> (-0.0055f).sp
        TextUnifyType.Display1 -> (0.00625f).sp
        TextUnifyType.Display2 -> (0.01428f).sp
        TextUnifyType.Display3 -> (0.00833f).sp
        TextUnifyType.Display3Uppercase -> (0.025f).sp
        TextUnifyType.Paragraph1 -> (0.00625f).sp
        TextUnifyType.Paragraph2 -> (0.01428f).sp
        TextUnifyType.Paragraph3 -> (0.00833f).sp
        TextUnifyType.Small -> (0.01f).sp
        else -> default.sp
    }
} else {
    when (type) {
        TextUnifyType.Heading1 -> (-0.013f).sp
        TextUnifyType.Heading2, TextUnifyType.Heading3 -> (-0.01f).sp
        else -> 0.sp
    }
}

private fun getFontWeight(
    type: TextUnifyType,
    weight: TextUnifyWeight,
    isFontTypeOpenSauceOne: Boolean
) {
    if (isFontTypeOpenSauceOne) {
        val boldType = listOf(
            TextUnifyType.Heading1,
            TextUnifyType.Heading2,
            TextUnifyType.Heading3,
            TextUnifyType.Heading4,
            TextUnifyType.Heading5,
            TextUnifyType.Heading6,
            TextUnifyType.Display3
        )
        val isBold = boldType.contains(type) || weight is TextUnifyWeight.Bold

        if (isBold) {

        }
    }
}

private fun Modifier.setPaddingOpenSauceOne(type: TextUnifyType): Modifier {
    return when (type) {
        TextUnifyType.Paragraph1 -> padding(top = 3.dp, bottom = 3.dp)
        TextUnifyType.Paragraph2 -> padding(top = 2.dp, bottom = 2.dp)
        TextUnifyType.Paragraph3 -> padding(top = 1.5f.dp, bottom = 1.5f.dp)
        else -> this
    }
}

@Composable
fun CTypography(
    modifier: Modifier = Modifier,
    text: AnnotatedString
) {

    Text(
        modifier = modifier,
        text = text
    )
}

@Preview
@Composable
fun CTypographyPreview() {
}