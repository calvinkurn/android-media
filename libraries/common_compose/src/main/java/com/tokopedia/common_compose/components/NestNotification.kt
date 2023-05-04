package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestNotification(text: String, colorType: Color = Color.PRIMARY) {
    val color = getNotificationColor(colorType)
    var textFinal = text
    val isText = try {
        val temp = text.toInt()
        if (temp > 99) textFinal = "99+"
        false
    } catch (e: NumberFormatException) {
        true
    }

    val height = if (isText) {
        if (textFinal.isEmpty()) 8.dp else 12.dp
    } else {
        16.dp // unify has 10 for text
    }
    val rad = if (isText) {
        if (textFinal.isEmpty()) 10.dp else 3.dp
    } else {
        6.dp
    }
    val minWidth = if (textFinal.isEmpty()) 8.dp else 16.dp
    val style = NestTheme.typography.small.copy(
        fontWeight = FontWeight.Bold,
        color = androidx.compose.ui.graphics.Color.White
    ).let { if (isText) it.copy(fontSize = 7.sp) else it }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(rad))
            .background(color)
            .defaultMinSize(minWidth = minWidth)
            .height(height)
            .padding(2.dp)
    ) {
        NestTypography(
            modifier = Modifier.align(Alignment.Center),
            text = textFinal,
            textStyle = style
        )
    }
}

@Composable
private fun getNotificationColor(colorType: Color) =
    if (colorType == Color.PRIMARY) {
        if (isSystemInDarkTheme()) {
            NestTheme.colors.RN._400
        } else {
            NestTheme.colors.RN._600
        }
    } else {
        NestTheme.colors.GN._500
    }

enum class Color { PRIMARY, SECONDARY }

@Preview("Notification")
@Preview("Notification (Dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NotificationPreview() {
    NestTheme {
        Surface {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NestNotification("12")
                NestNotification(text = "122", colorType = Color.SECONDARY)
                NestNotification(text = "")
                NestNotification(text = "", colorType = Color.SECONDARY)
                NestNotification(text = "NEW")
                NestNotification(text = "PROMO")
            }
        }
    }
}
