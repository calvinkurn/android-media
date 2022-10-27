package com.tokopedia.common_compose.principles

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestBN200
import com.tokopedia.common_compose.ui.NestBN400
import com.tokopedia.common_compose.ui.NestBN50
import com.tokopedia.common_compose.ui.NestNN900
import com.tokopedia.common_compose.ui.NestRN200
import com.tokopedia.common_compose.ui.NestRN400
import com.tokopedia.common_compose.ui.NestRN50
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.common_compose.ui.NestYN200
import com.tokopedia.common_compose.ui.NestYN400
import com.tokopedia.common_compose.ui.NestYN50

@Composable
fun NestTicker(
    modifier: Modifier = Modifier,
    title: CharSequence,
    description: CharSequence,
    onDismissed: () -> Unit = {},
    style: TickerStyle = TickerStyle.Default
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = style.backgroundColor,
        border = BorderStroke(1.dp, style.strokeColor)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Information Icon",
                    tint = style.iconColor
                )
            }


            Column(
                modifier = Modifier.weight(4f),
                verticalArrangement = Arrangement.Center
            ) {
                if (title.isNotEmpty()) {
                    NestTypography(
                        text = title.toString(),
                        textStyle = NestTheme.typography.heading5
                    )
                }

                if (description.isNotEmpty()) {
                    NestTypography(
                        text = description.toString(),
                        textStyle = NestTheme.typography.paragraph3
                    )
                }
            }

            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    modifier = Modifier
                        .size(19.dp)
                        .clickable { onDismissed() },
                    contentDescription = "Close Icon",
                    tint = style.closeIconColor
                )
            }

        }
    }

}

@Immutable
class TickerStyle internal constructor(
     val backgroundColor: Color,
     val strokeColor: Color,
     val iconColor: Color,
     val closeIconColor: Color
) {

    companion object {
        @Stable
        val Announcement = TickerStyle(
            backgroundColor = NestBN50,
            strokeColor = NestBN200,
            iconColor = NestBN400,
            closeIconColor = NestNN900
        )
        @Stable
        val Error = TickerStyle(
            backgroundColor = NestRN50,
            strokeColor = NestRN200,
            iconColor = NestRN400,
            closeIconColor = NestNN900
        )
        @Stable
        val Warning = TickerStyle(
            backgroundColor = NestYN50,
            strokeColor = NestYN200,
            iconColor = NestYN400,
            closeIconColor = NestNN900
        )

        @Stable
        val Default = Warning
    }

    fun copy(
        backgroundColor: Color = this.backgroundColor,
        strokeColor: Color = this.strokeColor,
        iconColor: Color = this.iconColor,
        closeIconColor: Color = this.closeIconColor
    ): TickerStyle {
        return TickerStyle(backgroundColor, strokeColor, iconColor, closeIconColor)
    }
}




@Preview(name = "Ticker Announcement")
@Composable
fun NestTickerAnnouncementPreview() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            style = TickerStyle.Announcement
        )
    }
}

@Preview(name = "Ticker Announcement [Dark]", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NestTickerDarkAnnouncementPreview() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            style = TickerStyle.Announcement
        )
    }
}

@Preview(name = "Ticker Warning")
@Composable
fun NestTickerWarningPreview() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            style = TickerStyle.Warning
        )
    }
}

@Preview(name = "Ticker Warning [Dark]", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NestTickerDarkWarningPreview() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            style = TickerStyle.Warning
        )
    }
}

@Preview(name = "Ticker Error")
@Composable
fun NestTickerErrorPreview() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            style = TickerStyle.Error
        )
    }
}


@Preview(name = "Ticker Error [Dark]", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NestTickerErrorDarkPreview() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            style = TickerStyle.Error
        )
    }

}

@Preview(name = "Ticker [Description Only]")
@Composable
fun DescriptionOnlyTicker() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            style = TickerStyle.Announcement
        )
    }
}
