package com.tokopedia.common_compose.components.ticker
import com.tokopedia.common_compose.R

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestGN
import com.tokopedia.common_compose.ui.NestNN
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.IconUnify

enum class TickerType {
    ANNOUNCEMENT, WARNING, ERROR
}

enum class TickerVariant {
    LOOSE, FULL
}

@Composable
fun NestTicker(
    modifier: Modifier = Modifier,
    tickerTitle: CharSequence,
    tickerDescription: CharSequence,
    tickerVariant: TickerVariant = TickerVariant.LOOSE,
    tickerType: TickerType = TickerType.ANNOUNCEMENT,
    onDismissed: () -> Unit = {},
    closeButtonVisibility: Boolean = true
) {
    // Implementation are specifically to cater ANNOUNCEMENT ticker type
    val backgroundColor = getBackgroundColor(tickerType)
    val strokeColor = getStrokeColor(tickerType)
    val iconColor = getIconColor(LocalContext.current, tickerType)
    val surfaceShape = getSurfaceShape(tickerVariant)
    val tickerIcon = getIcon(tickerType)
    val closeIconColor = NestTheme.colors.NN._900

    Box {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = surfaceShape,
            color = backgroundColor.default,
            border = getTickerBorderShape(tickerVariant, strokeColor),
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    TickerimageIcon(Modifier.align(Alignment.CenterVertically), tickerIcon, iconColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    TickerContent(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f), tickerTitle, tickerDescription)
                    Spacer(modifier = Modifier.width(10.dp))
                    if (closeButtonVisibility) {
                        TickerCloseIcon(onDismissed, closeIconColor)
                    }
                }
            }
        }
        Row(
            Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TickerIndicatorDot()
            TickerIndicatorDot()
            TickerIndicatorDot()
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (tickerVariant == TickerVariant.FULL) {
            Divider(modifier.align(Alignment.BottomEnd), strokeColor.default)
        }
    }
}

@Composable
private fun TickerIndicatorDot(
    selectedColor: Color = NestGN.light._500,
    onClick: () -> Unit = {},
    shape: Shape = CircleShape,
    notSelectedColor: Color = NestNN.light._200,
    isSelected: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .size(6.dp)
            .background(
                if (isSelected) {
                    selectedColor
                } else {
                    notSelectedColor
                }
            )
            .then(
                Modifier
                    .clickable {
                        onClick.invoke()
                    }))
}

@Composable
private fun TickerDot() {
    Canvas(
        modifier = Modifier
            .wrapContentSize()
            .border(color = Color.Magenta, width = 2.dp)
    ) {
        drawCircle(
            color = Color.Cyan,
            radius = 6.dp.toPx()
        )
    }
}

@Composable
private fun TickerContent(
    modifier: Modifier,
    tickerTitle: CharSequence,
    tickerDescription: CharSequence
) {
    Column(modifier) {
        if (tickerTitle.isNotEmpty()) {
            NestTypography(
                text = tickerTitle,
                modifier = Modifier.fillMaxWidth(),
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.NN._950,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        if (tickerDescription.isNotEmpty()) {
            NestTypography(
                text = tickerDescription,
                modifier = Modifier.fillMaxWidth(),
                textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950)
            )
        }
    }
}

@Composable
private fun TickerCloseIcon(
    onDismissed: () -> Unit,
    closeIconColor: Color
) {
    Icon(
        imageVector = Icons.Outlined.Close,
        modifier = Modifier.clickable { onDismissed() },
        contentDescription = "Close Icon",
        tint = closeIconColor
    )
}

@Composable
private fun TickerimageIcon(
    modifier: Modifier,
    tickerIcon: Int,
    iconColor: ColorType<Int>
) {
    AndroidView(
        modifier = modifier.size(24.dp),
        factory = {
            val iconUnify = IconUnify(it)
            iconUnify.apply {
                this.setImage(
                    newIconId = tickerIcon,
                    newLightEnable = iconColor.light,
                    newDarkEnable = iconColor.dark,
                    newDarkDisable = iconColor.disabled,
                    newLightDisable = iconColor.disabled
                )
            }
        }
    )
}

@Composable
private fun getTickerBorderShape(
    tickerVariant: TickerVariant,
    strokeColor: ColorType<Color>
) = when (tickerVariant) {
    TickerVariant.FULL -> {
        null
    }
    TickerVariant.LOOSE -> {
        BorderStroke(1.dp, strokeColor.default)
    }
}

@Composable
private fun getIcon(tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            IconUnify.INFORMATION
        }
        TickerType.ERROR -> {
            IconUnify.ERROR
        }
        TickerType.WARNING -> {
            IconUnify.WARNING
        }
    }

@Composable
private fun getSurfaceShape(tickerVariant: TickerVariant) =
    when (tickerVariant) {
        TickerVariant.LOOSE -> {
            RoundedCornerShape(6.dp)
        }
        TickerVariant.FULL -> {
            RoundedCornerShape(0.dp)
        }
    }

@Composable
private fun getIconColor(context: Context, tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            ColorType(
                default = ContextCompat.getColor(context, R.color.Unify_BN400)
            )
        }
        TickerType.WARNING -> {
            ColorType(
                default = ContextCompat.getColor(context, R.color.Unify_YN300)
            )
        }
        TickerType.ERROR -> {
            ColorType(
                default = ContextCompat.getColor(context, R.color.Unify_RN500)
            )
        }
    }

@Composable
private fun getStrokeColor(tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            ColorType(default = NestTheme.colors.BN._200)
        }
        TickerType.WARNING -> {
            ColorType(default = NestTheme.colors.YN._200)
        }
        TickerType.ERROR -> {
            ColorType(default = NestTheme.colors.RN._200)
        }
    }

data class ColorType<T>(
    val default: T,
    val light: T = default,
    val dark: T = default,
    val disabled: T = default
)

@Composable
private fun getBackgroundColor(tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            ColorType(default = NestTheme.colors.BN._50)
        }
        TickerType.WARNING -> {
            ColorType(default = NestTheme.colors.YN._50)
        }
        TickerType.ERROR -> {
            ColorType(default = NestTheme.colors.RN._50)
        }
    }

@Preview(name = "Ticker")
@Composable
fun NestTickerLoosePreview() {
    NestTicker(
        Modifier,
        tickerTitle = "Maaf ada gangguan",
        tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
        onDismissed = {},
        closeButtonVisibility = true
    )
}

@Preview(name = "Ticker")
@Composable
fun NestTickerFullPreview() {
    NestTheme(darkTheme = true) {
        NestTicker(
            Modifier,
            tickerType = TickerType.WARNING,
            tickerVariant = TickerVariant.FULL,
            tickerTitle = "Maaf ada gangguan",
            tickerDescription = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            closeButtonVisibility = true
        )
    }
}
