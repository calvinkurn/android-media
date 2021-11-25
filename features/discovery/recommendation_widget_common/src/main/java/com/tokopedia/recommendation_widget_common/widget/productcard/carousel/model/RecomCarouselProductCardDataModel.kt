package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCarouselDiffUtilComparable
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener

/**
 * Created by yfsx on 5/3/21.
 */
data class RecomCarouselProductCardDataModel(
        val productModel: ProductCardModel,
        val recomItem: RecommendationItem,
        val componentName: String = "",
        val listener: RecomCommonProductCardListener? = null
): Visitable<CommonRecomCarouselCardTypeFactory>, RecomCarouselDiffUtilComparable {
    override fun type(typeFactory: CommonRecomCarouselCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean {
        if (toCompare !is RecomCarouselProductCardDataModel) return false

        return recomItem.productId == toCompare.recomItem.productId
    }

    override fun areContentsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean {
        if (toCompare !is RecomCarouselProductCardDataModel) return false

        return productModel == toCompare.productModel && recomItem == toCompare.recomItem &&
                componentName == toCompare.componentName && listener === toCompare.listener
    }

    override fun getChangePayload(toCompare: RecomCarouselDiffUtilComparable): Map<String, Any> {
        if (toCompare !is RecomCarouselProductCardDataModel) return emptyMap()
        return mutableMapOf<String, Any>().apply {
            if (productModel != toCompare.productModel) {
                put(PAYLOAD_PRODUCT_MODEL, Unit)
            }
            if (recomItem != toCompare.recomItem) {
                put(PAYLOAD_RECOM_ITEM, Unit)
            }
            if (componentName != toCompare.componentName) {
                put(PAYLOAD_COMPONENT_NAME, Unit)
            }
            if (listener !== toCompare.listener) {
                put(PAYLOAD_IS_LISTENER_CHANGED, Unit)
            }
        }
    }

    companion object {
        const val PAYLOAD_PRODUCT_MODEL = "productModel"
        const val PAYLOAD_RECOM_ITEM = "recomItem"
        const val PAYLOAD_COMPONENT_NAME = "componentName"
        const val PAYLOAD_IS_LISTENER_CHANGED = "isListenerChanged"
    }
}