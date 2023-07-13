package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

data class RecommendationVerticalProductCardModel(
    val productModel: ProductCardModel,
    val recomItem: RecommendationItem,
    val recomWidget: RecommendationWidget,
    val trackingModel: RecommendationWidgetTrackingModel,
    val componentName: String = ""
) : RecommendationVerticalVisitable {
    override fun type(typeFactory: RecommendationVerticalTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(toCompare: RecommendationVerticalVisitable): Boolean {
        if (toCompare !is RecommendationVerticalProductCardModel) return false

        return recomItem.productId == toCompare.recomItem.productId
    }

    override fun areContentsTheSame(toCompare: RecommendationVerticalVisitable): Boolean {
        if (toCompare !is RecommendationVerticalProductCardModel) return false

        return productModel == toCompare.productModel &&
            recomItem == toCompare.recomItem &&
            componentName == toCompare.componentName &&
            recomWidget == toCompare.recomWidget
    }

    override fun getChangePayload(toCompare: RecommendationVerticalVisitable): Map<String, Any> {
        if (toCompare !is RecommendationVerticalProductCardModel) return emptyMap()
        return mutableMapOf<String, Any>().apply {
            if (productModel != toCompare.productModel || recomItem != toCompare.recomItem) {
                put(PAYLOAD_FLAG_SHOULD_UPDATE_PRODUCT_CARD, Unit)
            }
            if (
                componentName != toCompare.componentName ||
                recomItem != toCompare.recomItem ||
                recomWidget != toCompare.recomWidget ||
                trackingModel != toCompare.trackingModel
            ) {
                put(PAYLOAD_FLAG_SHOULD_UPDATE_LISTENERS, Unit)
            }
        }
    }

    companion object {
        const val PAYLOAD_FLAG_SHOULD_UPDATE_PRODUCT_CARD = "updateProductCard"
        const val PAYLOAD_FLAG_SHOULD_UPDATE_LISTENERS = "updateListeners"
    }
}
