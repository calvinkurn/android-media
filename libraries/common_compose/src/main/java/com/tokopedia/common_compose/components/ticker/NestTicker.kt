package com.tokopedia.common_compose.components.ticker

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTicker(
    title: CharSequence,
    type: TickerType,
    description: CharSequence,
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit = {},
    closeButtonVisibility: Boolean = true
) {
    val style = when (type) {
        TickerType.WARNING -> TickerColor(
            backgroundColor = NestTheme.colors.YN._50,
            strokeColor = NestTheme.colors.YN._200,
            iconColor = NestTheme.colors.YN._400,
            closeIconColor = NestTheme.colors.NN._900
        )
        TickerType.ANNOUNCEMENT -> TickerColor(
            backgroundColor = NestTheme.colors.BN._50,
            strokeColor = NestTheme.colors.BN._200,
            iconColor = NestTheme.colors.BN._400,
            closeIconColor = NestTheme.colors.NN._900
        )
        TickerType.ERROR -> TickerColor(
            backgroundColor = NestTheme.colors.RN._50,
            strokeColor = NestTheme.colors.RN._200,
            iconColor = NestTheme.colors.RN._400,
            closeIconColor = NestTheme.colors.NN._900
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = style.backgroundColor,
        border = BorderStroke(1.dp, style.strokeColor)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(19.dp),
                imageVector = Icons.Outlined.Info,
                contentDescription = "Information Icon",
                tint = style.iconColor
            )

            Spacer(modifier = Modifier.weight(0.1f))

            TickerTextContent(
                modifier = Modifier.weight(4f),
                title = title,
                description = description
            )

            Spacer(modifier = Modifier.weight(0.1f))
            if (closeButtonVisibility) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    modifier = Modifier
                        .weight(0.25f)
                        .size(19.dp)
                        .clickable { onDismissed() },
                    contentDescription = "Close Icon",
                    tint = style.closeIconColor
                )
            }
        }
    }
}

@Composable
private fun TickerTextContent(modifier: Modifier = Modifier, title: CharSequence, description: CharSequence) {
    if (title.isNotEmpty() && description.isNotEmpty()) {
        TitleWithDescription(
            modifier = modifier,
            title = title,
            description = description
        )
    } else if (title.isNotEmpty() && description.isEmpty()) {
        TitleOnly(modifier = modifier, title = title)
    } else if (title.isEmpty() && description.isNotEmpty()) {
        DescriptionOnly(
            modifier = modifier,
            description = description
        )
    }
}

@Composable
private fun TitleWithDescription(
    modifier: Modifier = Modifier,
    title: CharSequence,
    description: CharSequence
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        NestTypography(
            text = title,
            textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950)
        )

        NestTypography(
            text = description,
            textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950)
        )
    }
}

@Composable
private fun TitleOnly(modifier: Modifier = Modifier, title: CharSequence) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        NestTypography(
            text = title,
            textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950)
        )
    }
}

@Composable
private fun DescriptionOnly(modifier: Modifier = Modifier, description: CharSequence) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        NestTypography(
            text = description,
            textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950)
        )
    }
}

data class TickerColor(
    val backgroundColor: Color,
    val strokeColor: Color,
    val iconColor: Color,
    val closeIconColor: Color
)
enum class TickerType {
    WARNING,
    ANNOUNCEMENT,
    ERROR
}

@Preview(name = "Ticker Announcement")
@Composable
fun NestTickerAnnouncementPreview() {
    NestTheme {
        NestTicker(
            title = "Info",
            type = TickerType.ANNOUNCEMENT,
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            modifier = Modifier,
            onDismissed = {}
        )
    }
}

@Preview(name = "Ticker Announcement [Dark]", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestTickerAnnouncementDarkPreview() {
    NestTheme {
        NestTicker(
            title = "Info",
            type = TickerType.ANNOUNCEMENT,
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            modifier = Modifier,
            onDismissed = {}
        )
    }
}

@Preview(name = "Ticker [Description Only]")
@Composable
fun DescriptionOnlyTicker() {
    NestTheme {
        NestTicker(
            title = "",
            type = TickerType.ANNOUNCEMENT,
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            modifier = Modifier,
            onDismissed = {}
        )
    }
}

@Preview(name = "Ticker Warning")
@Composable
fun NestTickerWarningPreview() {
    NestTheme {
        NestTicker(
            title = "Info",
            type = TickerType.WARNING,
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            modifier = Modifier,
            onDismissed = {}
        )
    }
}

@Preview(name = "Ticker Error")
@Composable
fun NestTickerErrorPreview() {
    NestTheme {
        NestTicker(
            title = "Info",
            type = TickerType.ERROR,
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            modifier = Modifier,
            onDismissed = {}
        )
    }
}
