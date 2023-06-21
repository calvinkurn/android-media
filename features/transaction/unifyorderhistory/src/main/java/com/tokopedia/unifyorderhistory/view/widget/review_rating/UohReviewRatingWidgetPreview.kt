package com.tokopedia.unifyorderhistory.view.widget.review_rating

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifyorderhistory.data.model.UohListOrder

val previewConfig: MutableState<UohReviewRatingWidgetConfig> = mutableStateOf(createGoPayWithStars())

@Preview
@Composable
private fun UohReviewRatingWidgetPreview() {
    NestTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            UohReviewRatingWidget(modifier = Modifier.fillMaxWidth(), config = previewConfig.value)
            NestButton(
                text = "Show GoPay with stars",
                onClick = { previewConfig.value = createGoPayWithStars() }
            )
            NestButton(
                text = "Show interactive stars",
                onClick = { previewConfig.value = createInteractiveStars() }
            )
            NestButton(
                text = "Show GoPay without stars",
                onClick = { previewConfig.value = createGoPayWithoutStars() }
            )
            NestButton(
                text = "Hide",
                onClick = { previewConfig.value = previewConfig.value.copy(show = false) }
            )
        }
    }
}

private fun createGoPayWithStars() = UohReviewRatingWidgetConfig(
    show = true,
    componentData = UohListOrder.UohOrders.Order.Metadata.ExtraComponent(
        type = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS,
        action = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.Action(
            actionType = "link",
            appUrl = ""
        ),
        label = "Ulas untuk <b><font color='#28B9E1'>GoPay Coins</font></b>"
    )
)

private fun createInteractiveStars() = UohReviewRatingWidgetConfig(
    show = true,
    componentData = UohListOrder.UohOrders.Order.Metadata.ExtraComponent(
        type = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_INTERACTIVE_STARS,
        action = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.Action(
            actionType = "link",
            appUrl = ""
        ),
        label = "Ulas belanjaanmu, yuk!"
    )
)


private fun createGoPayWithoutStars() = UohReviewRatingWidgetConfig(
    show = true,
    componentData = UohListOrder.UohOrders.Order.Metadata.ExtraComponent(
        type = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS,
        action = UohListOrder.UohOrders.Order.Metadata.ExtraComponent.Action(
            actionType = "link",
            appUrl = ""
        ),
        label = "Bisa dapat <b><font color='#28B9E1'>GoPay Coins</font></b> kalau ulas produk ini!"
    )
)
