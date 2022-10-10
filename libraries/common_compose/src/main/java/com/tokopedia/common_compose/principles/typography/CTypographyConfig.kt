package com.tokopedia.common_compose.principles.typography

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
    type: CTypographyType
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
    type: CTypographyType,
    default: Float
): TextUnit = if (isFontTypeOpenSauceOne) {
    when (type) {
        CTypographyType.Heading1 -> (-0.0083f).sp
        CTypographyType.Heading2 -> (-0.005f).sp
        CTypographyType.Heading3 -> (-0.0055f).sp
        CTypographyType.Display1 -> (0.00625f).sp
        CTypographyType.Display2 -> (0.01428f).sp
        CTypographyType.Display3 -> (0.00833f).sp
        CTypographyType.Display3Uppercase -> (0.025f).sp
        CTypographyType.Paragraph1 -> (0.00625f).sp
        CTypographyType.Paragraph2 -> (0.01428f).sp
        CTypographyType.Paragraph3 -> (0.00833f).sp
        CTypographyType.Small -> (0.01f).sp
        else -> if (default.isNaN()) 0.sp else default.sp
    }
} else {
    when (type) {
        CTypographyType.Heading1 -> (-0.013f).sp
        CTypographyType.Heading2, CTypographyType.Heading3 -> (-0.01f).sp
        else -> 0.sp
    }
}

internal fun getFontFamily(
    type: CTypographyType,
    weight: CTypographyWeight,
    isFontTypeOpenSauceOne: Boolean
): FontFamily = if (isFontTypeOpenSauceOne) {
    val boldType = listOf(
        CTypographyType.Heading1,
        CTypographyType.Heading2,
        CTypographyType.Heading3,
        CTypographyType.Heading4,
        CTypographyType.Heading5,
        CTypographyType.Heading6,
        CTypographyType.Display3
    )
    val isBold = boldType.contains(type) || weight == CTypographyWeight.Bold

    if (isBold) {
        fontOpenSourceOneExtraBold
    } else {
        fontOpenSourceOneRegular
    }
} else {
    when (type) {
        CTypographyType.Body1,
        CTypographyType.Body2,
        CTypographyType.Body3,
        CTypographyType.Display1,
        CTypographyType.Display2,
        CTypographyType.Display3,
        CTypographyType.Paragraph1,
        CTypographyType.Paragraph2,
        CTypographyType.Paragraph3,
        CTypographyType.Small ->
            if (weight == CTypographyWeight.Regular) {
                fontRobotoRegular
            } else {
                fontRobotoBold
            }
        else -> fontNunitoSansExtraBold
    }
}

internal fun Modifier.setPaddingOpenSauceOne(type: CTypographyType): Modifier {
    return when (type) {
        CTypographyType.Paragraph1 -> padding(top = 3.dp, bottom = 3.dp)
        CTypographyType.Paragraph2 -> padding(top = 2.dp, bottom = 2.dp)
        CTypographyType.Paragraph3 -> padding(top = 1.5f.dp, bottom = 1.5f.dp)
        else -> this
    }
}
