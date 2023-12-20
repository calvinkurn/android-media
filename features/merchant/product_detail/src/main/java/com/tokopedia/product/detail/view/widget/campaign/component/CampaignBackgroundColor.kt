package com.tokopedia.product.detail.view.widget.campaign.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Created by yovi.putra on 19/12/23"
 * Project name: android-unify
 **/

private const val CAMPAIGN_COLOR = 0xFFD72C2C

@Composable
internal fun Modifier.backgroundColor(colors: List<Color>) = this.then(
    when (colors.size) {
        0 -> background(color = Color(CAMPAIGN_COLOR))
        1 -> background(color = colors.first())
        else -> {
            val brush = Brush.horizontalGradient(colors)
            background(brush)
        }
    }
)

@Composable
internal fun Modifier.backgroundColor(colorString: String): Modifier {
    val color = remember(colorString) {
        derivedStateOf {
            val listOfColor = colorString.split(",")
            listOfColor
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .map { parseColor(it) }
        }
    }

    return backgroundColor(colors = color.value)
}

private fun parseColor(colorString: String): Color = runCatching {
    val colorSafe = if (!colorString.startsWith("#")) {
        "#$colorString"
    } else {
        colorString
    }

    Color(android.graphics.Color.parseColor(colorSafe))
}.getOrDefault(Color(CAMPAIGN_COLOR))
