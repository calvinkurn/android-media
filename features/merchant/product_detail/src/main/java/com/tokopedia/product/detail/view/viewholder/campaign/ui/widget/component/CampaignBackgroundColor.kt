package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Created by yovi.putra on 19/12/23"
 * Project name: android-unify
 **/

@Stable
class ImmutableList<T>(val values: List<T> = mutableListOf())

private const val CAMPAIGN_COLOR = 0xFFD72C2C

private fun Modifier.campaignBackgroundColor(colors: ImmutableList<Color>) = this.then(
    when (colors.values.size) {
        0 -> background(color = Color(CAMPAIGN_COLOR))
        1 -> background(color = colors.values.first())
        else -> {
            val brush = Brush.horizontalGradient(colors.values)
            background(brush)
        }
    }
)

@Composable
internal fun Modifier.campaignBackgroundColor(colorString: String): Modifier {
    val color = remember(colorString) {
        derivedStateOf {
            val listOfColor = colorString.split(",")
            listOfColor
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .map { parseColor(it) }
        }
    }

    return campaignBackgroundColor(colors = ImmutableList(color.value))
}

private fun parseColor(colorString: String): Color = runCatching {
    val colorSafe = if (!colorString.startsWith("#")) {
        "#$colorString"
    } else {
        colorString
    }

    Color(android.graphics.Color.parseColor(colorSafe))
}.getOrDefault(Color(CAMPAIGN_COLOR))
