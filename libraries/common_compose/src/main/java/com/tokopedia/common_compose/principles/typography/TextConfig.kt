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

object TextConfig {

    var isFontTypeOpenSauceOne: Boolean = true
}

internal fun getFontSize(
    style: TextStyle,
    isFontTypeOpenSauceOne: Boolean,
    type: TextUnifyType
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
        else -> if (default.isNaN()) 0.sp else default.sp
    }
} else {
    when (type) {
        TextUnifyType.Heading1 -> (-0.013f).sp
        TextUnifyType.Heading2, TextUnifyType.Heading3 -> (-0.01f).sp
        else -> 0.sp
    }
}

internal fun getFontFamily(
    type: TextUnifyType,
    weight: TextUnifyWeight,
    isFontTypeOpenSauceOne: Boolean
): FontFamily = if (isFontTypeOpenSauceOne) {
    val boldType = listOf(
        TextUnifyType.Heading1,
        TextUnifyType.Heading2,
        TextUnifyType.Heading3,
        TextUnifyType.Heading4,
        TextUnifyType.Heading5,
        TextUnifyType.Heading6,
        TextUnifyType.Display3
    )
    val isBold = boldType.contains(type) || weight == TextUnifyWeight.Bold

    if (isBold) {
        fontOpenSourceOneExtraBold
    } else {
        fontOpenSourceOneRegular
    }
} else {
    when (type) {
        TextUnifyType.Body1,
        TextUnifyType.Body2,
        TextUnifyType.Body3,
        TextUnifyType.Display1,
        TextUnifyType.Display2,
        TextUnifyType.Display3,
        TextUnifyType.Paragraph1,
        TextUnifyType.Paragraph2,
        TextUnifyType.Paragraph3,
        TextUnifyType.Small ->
            if (weight == TextUnifyWeight.Regular) {
                fontRobotoRegular
            } else {
                fontRobotoBold
            }
        else -> fontNunitoSansExtraBold
    }
}

internal fun Modifier.setPaddingOpenSauceOne(type: TextUnifyType): Modifier {
    return when (type) {
        TextUnifyType.Paragraph1 -> padding(top = 3.dp, bottom = 3.dp)
        TextUnifyType.Paragraph2 -> padding(top = 2.dp, bottom = 2.dp)
        TextUnifyType.Paragraph3 -> padding(top = 1.5f.dp, bottom = 1.5f.dp)
        else -> this
    }
}
