package com.tokopedia.unifyorderhistory.view.widget.review_rating

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.ui.NestColor
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifyorderhistory.data.model.UohListOrder

val previewConfig: MutableState<UohReviewRatingWidgetConfig> = mutableStateOf(createInteractiveStars())

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UohReviewRatingWidgetPreview() {
    val colors = NestTheme.colors
    NestTheme {
        Surface {
            Column(modifier = Modifier.fillMaxWidth()) {
                UohReviewRatingWidget(config = previewConfig.value)
                NestButton(
                    text = "Show GoPay with stars",
                    onClick = { previewConfig.value = createGoPayWithStars(colors) }
                )
                NestButton(
                    text = "Show interactive stars",
                    onClick = { previewConfig.value = createInteractiveStars() }
                )
                NestButton(
                    text = "Show GoPay without stars",
                    onClick = { previewConfig.value = createGoPayWithoutStars(colors) }
                )
                NestButton(
                    text = "Hide",
                    onClick = { previewConfig.value = previewConfig.value.copy(show = false) }
                )
            }
        }
    }
}

private fun createGoPayWithStars(colors: NestColor) = UohReviewRatingWidgetConfig(
    show = true,
    componentData = UohListOrder.UohOrders.Order.Metadata.ExtraComponent(
        type = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS,
        action = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.Action(appUrl = ""),
        label = "Ulas untuk <b><font color='${colorToHex(colors.BN._400)}'>GoPay Coins</font></b>"
    )
)

private fun createInteractiveStars() = UohReviewRatingWidgetConfig(
    show = true,
    componentData = UohListOrder.UohOrders.Order.Metadata.ExtraComponent(
        type = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_INTERACTIVE_STARS,
        action = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.Action(appUrl = ""),
        label = "Ulas belanjaanmu, yuk!"
    )
)


private fun createGoPayWithoutStars(colors: NestColor) = UohReviewRatingWidgetConfig(
    show = true,
    componentData = UohListOrder.UohOrders.Order.Metadata.ExtraComponent(
        type = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS,
        action = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.Action(appUrl = ""),
        label = "Bisa dapat <b><font color='${colorToHex(colors.BN._400)}'>GoPay Coins</font></b> kalau ulas produk ini!"
    )
)

private fun colorToHex(color: Color): String {
    // Extract individual color components (ranging from 0.0 to 1.0)
    val red = color.red
    val green = color.green
    val blue = color.blue

    // Convert each component to its hexadecimal representation (ranging from 00 to FF)
    val redHex = (red * 255).toInt().toString(16).padStart(2, '0')
    val greenHex = (green * 255).toInt().toString(16).padStart(2, '0')
    val blueHex = (blue * 255).toInt().toString(16).padStart(2, '0')

    // Combine the hexadecimal values into a single string
    return "#$redHex$greenHex$blueHex"
}
