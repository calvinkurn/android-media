package com.tokopedia.common_compose.principles.nest_typography

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
    type: NestTypographyType
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
    type: NestTypographyType,
    default: Float
): TextUnit = if (isFontTypeOpenSauceOne) {
    when (type) {
        NestTypographyType.Heading1 -> (-0.0083f).sp
        NestTypographyType.Heading2 -> (-0.005f).sp
        NestTypographyType.Heading3 -> (-0.0055f).sp
        NestTypographyType.Display1 -> (0.00625f).sp
        NestTypographyType.Display2 -> (0.01428f).sp
        NestTypographyType.Display3 -> (0.00833f).sp
        NestTypographyType.Display3Uppercase -> (0.025f).sp
        NestTypographyType.Paragraph1 -> (0.00625f).sp
        NestTypographyType.Paragraph2 -> (0.01428f).sp
        NestTypographyType.Paragraph3 -> (0.00833f).sp
        NestTypographyType.Small -> (0.01f).sp
        else -> if (default.isNaN()) 0.sp else default.sp
    }
} else {
    when (type) {
        NestTypographyType.Heading1 -> (-0.013f).sp
        NestTypographyType.Heading2, NestTypographyType.Heading3 -> (-0.01f).sp
        else -> 0.sp
    }
}

internal fun getFontFamily(
    type: NestTypographyType,
    weight: NestTypographyWeight,
    isFontTypeOpenSauceOne: Boolean
): FontFamily = if (isFontTypeOpenSauceOne) {
    val boldType = listOf(
        NestTypographyType.Heading1,
        NestTypographyType.Heading2,
        NestTypographyType.Heading3,
        NestTypographyType.Heading4,
        NestTypographyType.Heading5,
        NestTypographyType.Heading6,
        NestTypographyType.Display3
    )
    val isBold = boldType.contains(type) || weight == NestTypographyWeight.Bold

    if (isBold) {
        fontOpenSourceOneExtraBold
    } else {
        fontOpenSourceOneRegular
    }
} else {
    when (type) {
        NestTypographyType.Body1,
        NestTypographyType.Body2,
        NestTypographyType.Body3,
        NestTypographyType.Display1,
        NestTypographyType.Display2,
        NestTypographyType.Display3,
        NestTypographyType.Paragraph1,
        NestTypographyType.Paragraph2,
        NestTypographyType.Paragraph3,
        NestTypographyType.Small ->
            if (weight == NestTypographyWeight.Regular) {
                fontRobotoRegular
            } else {
                fontRobotoBold
            }
        else -> fontNunitoSansExtraBold
    }
}

internal fun Modifier.setPaddingOpenSauceOne(type: NestTypographyType): Modifier {
    return when (type) {
        NestTypographyType.Paragraph1 -> padding(top = 3.dp, bottom = 3.dp)
        NestTypographyType.Paragraph2 -> padding(top = 2.dp, bottom = 2.dp)
        NestTypographyType.Paragraph3 -> padding(top = 1.5f.dp, bottom = 1.5f.dp)
        else -> this
    }
}
