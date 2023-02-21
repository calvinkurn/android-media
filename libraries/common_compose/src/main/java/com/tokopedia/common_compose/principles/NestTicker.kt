package com.tokopedia.common_compose.principles

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTicker(
    modifier: Modifier = Modifier,
    title: CharSequence,
    description: CharSequence,
    onDismissed: () -> Unit = {},
    type: TickerType
) {

   val style = when(type) {
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

            Column(
                modifier = Modifier.weight(4f),
                verticalArrangement = Arrangement.Center
            ) {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    NestTypography(
                        text = title.toString(),
                        textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950)
                    )
                    NestTypography(
                        text = description.toString(),
                        textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950)
                    )
                } else if (title.isNotEmpty() && description.isEmpty()) {
                    NestTypography(
                        text = title.toString(),
                        textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950)
                    )
                } else if (title.isEmpty() && description.isNotEmpty()) {
                    NestTypography(
                        modifier = Modifier.fillMaxWidth(),
                        text = description.toString(),
                        textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950)
                    )

                }

            }

            Spacer(modifier = Modifier.weight(0.1f))

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
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            type = TickerType.ANNOUNCEMENT
        )
    }
}

@Preview(name = "Ticker Announcement [Dark]", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NestTickerAnnouncementDarkPreview() {
    NestTheme {
        NestTicker(
            modifier = Modifier,
            title = "Info",
            description = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
            onDismissed = {},
            type = TickerType.ANNOUNCEMENT
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
            type = TickerType.ANNOUNCEMENT
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
            type = TickerType.WARNING
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
            type = TickerType.ERROR
        )
    }
}
