package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Temp, will remove once nest version is up
 */

@Composable
fun NestRibbon(
    modifier: Modifier = Modifier,
    text: String,
    top: Dp,
    color: NestRibbonColor = NestRibbonColor.RED,
    position: NestRibbonPosition = NestRibbonPosition.LEFT,
    additionalOffsetX: Dp = 0.dp
) {
    @Composable
    fun getRibbonColor(nestRibbonColor: NestRibbonColor): Color {
        return when (nestRibbonColor) {
            NestRibbonColor.RED -> NestTheme.colors.RN._500
            NestRibbonColor.BLUE -> NestTheme.colors.BN._500
            NestRibbonColor.YELLOW -> NestTheme.colors.YN._400
            NestRibbonColor.GREEN -> NestTheme.colors.GN._500
            NestRibbonColor.PURPLE -> NestTheme.colors.PN._500
            NestRibbonColor.TEAL -> NestTheme.colors.TN._500
        }
    }

    @Composable
    fun getRibbonFoldColor(nestRibbonColor: NestRibbonColor): Color {
        return when (nestRibbonColor) {
            NestRibbonColor.RED -> NestTheme.colors.RN._700
            NestRibbonColor.BLUE -> NestTheme.colors.BN._700
            NestRibbonColor.YELLOW -> NestTheme.colors.YN._700
            NestRibbonColor.GREEN -> NestTheme.colors.GN._700
            NestRibbonColor.PURPLE -> NestTheme.colors.PN._700
            NestRibbonColor.TEAL -> NestTheme.colors.TN._700
        }
    }

    @Composable
    fun getRibbonRoundedCornerShape(nestRibbonPosition: NestRibbonPosition): RoundedCornerShape {
        return if (nestRibbonPosition == NestRibbonPosition.LEFT) {
            RoundedCornerShape(8.dp, 12.dp, 12.dp, 0.dp)
        } else {
            RoundedCornerShape(12.dp, 8.dp, 0.dp, 12.dp)
        }
    }

    @Composable
    fun getRibbonFoldAlignment(nestRibbonPosition: NestRibbonPosition): Alignment.Horizontal {
        return if (nestRibbonPosition == NestRibbonPosition.LEFT) {
            Alignment.Start
        } else Alignment.End
    }

    @Composable
    fun getRibbonFoldClipShape(nestRibbonPosition: NestRibbonPosition): RoundedCornerShape {
        return if (nestRibbonPosition == NestRibbonPosition.LEFT) {
            RoundedCornerShape(0.dp, 0.dp, 0.dp, 4.dp)
        } else {
            RoundedCornerShape(0.dp, 0.dp, 4.dp, 0.dp)
        }
    }

    fun getRibbonOffsetX(nestRibbonPosition: NestRibbonPosition): Dp {
        return if ((nestRibbonPosition == NestRibbonPosition.LEFT)) ((-4).dp + additionalOffsetX) else (4.dp + additionalOffsetX)
    }

    Column(
        modifier = modifier
            .offset {
                IntOffset(
                    getRibbonOffsetX(position)
                        .toPx()
                        .toInt(),
                    top
                        .toPx()
                        .toInt()
                )
            },
        horizontalAlignment = getRibbonFoldAlignment(position)
    ) {
        NestTypography(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = getRibbonColor(color),
                    shape = getRibbonRoundedCornerShape(position)
                )
                .padding(8.dp, 4.dp),
            text = text,
            textStyle = NestTheme.typography.small.copy(
                color = NestTheme.colors.NN._0,
                fontWeight = FontWeight.Bold
            )
        )
        Box(
            modifier = Modifier.size(4.dp)
                .clip(shape = getRibbonFoldClipShape(position))
                .background(getRibbonFoldColor(color))
        )
    }
}

enum class NestRibbonPosition {
    LEFT,
    RIGHT
}

enum class NestRibbonColor {
    RED,
    BLUE,
    YELLOW,
    GREEN,
    PURPLE,
    TEAL
}
