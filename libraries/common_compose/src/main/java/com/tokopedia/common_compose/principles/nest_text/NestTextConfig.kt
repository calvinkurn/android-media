package com.tokopedia.common_compose.principles.nest_text

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by yovi.putra on 07/10/22"
 * Project name: android-tokopedia-core
 **/

internal fun getFontSize(
    style: TextStyle,
    isFontTypeOpenSauceOne: Boolean,
    type: NestTextType
): TextUnit = if (style.fontSize == TextUnit.Unspecified) {
    if (isFontTypeOpenSauceOne) {
        type.fontSize
    } else {
        type.openSourceSize
    }
} else {
    style.fontSize
}

internal fun getLetterSpacing(
    isFontTypeOpenSauceOne: Boolean,
    type: NestTextType,
    default: Float
): TextUnit = if (isFontTypeOpenSauceOne) {
    when (type) {
        NestTextType.Heading1 -> (-0.0083f).sp
        NestTextType.Heading2 -> (-0.005f).sp
        NestTextType.Heading3 -> (-0.0055f).sp
        NestTextType.Display1 -> (0.00625f).sp
        NestTextType.Display2 -> (0.01428f).sp
        NestTextType.Display3 -> (0.00833f).sp
        NestTextType.Display3Uppercase -> (0.025f).sp
        NestTextType.Paragraph1 -> (0.00625f).sp
        NestTextType.Paragraph2 -> (0.01428f).sp
        NestTextType.Paragraph3 -> (0.00833f).sp
        NestTextType.Small -> (0.01f).sp
        else -> if (default.isNaN()) 0.sp else default.sp
    }
} else {
    when (type) {
        NestTextType.Heading1 -> (-0.013f).sp
        NestTextType.Heading2, NestTextType.Heading3 -> (-0.01f).sp
        else -> 0.sp
    }
}

internal fun getFontFamily(
    type: NestTextType,
    weight: NestTextWeight,
    isFontTypeOpenSauceOne: Boolean
): FontFamily = if (isFontTypeOpenSauceOne) {
    val boldType = listOf(
        NestTextType.Heading1,
        NestTextType.Heading2,
        NestTextType.Heading3,
        NestTextType.Heading4,
        NestTextType.Heading5,
        NestTextType.Heading6,
        NestTextType.Display3
    )
    val isBold = boldType.contains(type) || weight == NestTextWeight.Bold

    if (isBold) {
        fontOpenSourceOneExtraBold
    } else {
        fontOpenSourceOneRegular
    }
} else {
    when (type) {
        NestTextType.Body1,
        NestTextType.Body2,
        NestTextType.Body3,
        NestTextType.Display1,
        NestTextType.Display2,
        NestTextType.Display3,
        NestTextType.Paragraph1,
        NestTextType.Paragraph2,
        NestTextType.Paragraph3,
        NestTextType.Small ->
            if (weight == NestTextWeight.Regular) {
                fontRobotoRegular
            } else {
                fontRobotoBold
            }
        else -> fontNunitoSansExtraBold
    }
}

internal fun Modifier.setPaddingOpenSauceOne(type: NestTextType): Modifier {
    return when (type) {
        NestTextType.Paragraph1 -> padding(top = 3.dp, bottom = 3.dp)
        NestTextType.Paragraph2 -> padding(top = 2.dp, bottom = 2.dp)
        NestTextType.Paragraph3 -> padding(top = 1.5f.dp, bottom = 1.5f.dp)
        else -> this
    }
}
