package com.tokopedia.common_compose.principles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestTicker(
    modifier: Modifier = Modifier,
    text: CharSequence,
    onDismissed: () -> Unit = {}
) {
    //Implementation are specifically to cater ANNOUNCEMENT ticker type
    val backgroundColor = NestTheme.colors.BN._50
    val strokeColor =  NestTheme.colors.BN._200
    val iconColor = NestTheme.colors.BN._400
    val closeIconColor =  NestTheme.colors.NN._900

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, strokeColor)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = "Information Icon", tint = iconColor)
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            NestTypography(
                text = text.toString(),
                modifier = Modifier.width(250.dp),
                textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._950)
            )
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Icon(
                imageVector = Icons.Outlined.Close,
                modifier = Modifier.clickable { onDismissed() },
                contentDescription = "Close Icon",
                tint = closeIconColor
            )
        }
    }

}

@Preview(name = "Ticker")
@Composable
fun NestTickerPreview() {
    NestTicker(
        Modifier,
        text = "Sedang ada perbaikan hari ini. Cek lagi besok ya",
        onDismissed = {},
    )
}
