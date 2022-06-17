package com.tokopedia.recommendation_widget_common.widget.tokonowutil

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData

/**
 * Created by yfsx on 06/10/21.
 */
object TokonowQuantityUpdater {

    fun updateRecomWithMinicartData(
        data: RecommendationCarouselData,
        miniCart: MutableMap<MiniCartItemKey, MiniCartItem>?
    ) {
        val newdata = mutableListOf<RecommendationItem>()
        data.recommendationData.recommendationItemList.forEach {
            val recomItem = it.copy()
            if (recomItem.isRecomProductShowVariantAndCart) {
                recomItem.setDefaultCurrentStock()
                miniCart?.let { cartData ->
                    recomItem.updateItemCurrentStock(
                        when {
                            recomItem.isProductHasParentID() -> {
                                cartData.getMiniCartItemParentProduct(recomItem.parentID.toString())?.totalQuantity ?: 0
                            }
                            cartData.containsKey(MiniCartItemKey(recomItem.productId.toString())) -> {
                                cartData.getMiniCartItemProduct(recomItem.productId.toString())?.quantity ?: 0
                            }
                            else -> 0
                        }
                    )
                }
            }
            newdata.add(recomItem)
        }
        data.recommendationData.recommendationItemList = newdata
    }

    fun updateCurrentQuantityRecomItem(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem
    ) {
        val newdata = mutableListOf<RecommendationItem>()
        data.recommendationData.recommendationItemList.forEach { item ->
            if (item.productId == recomItem.productId) {
                val copyItem = item.copy()
                copyItem.onCardQuantityChanged(recomItem.currentQuantity)
                newdata.add(copyItem)
            } else {
                newdata.add(item)
            }
        }
    }

    fun resetFailedRecomTokonowCard(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem
    ) {
        val newdata = mutableListOf<RecommendationItem>()
        data.recommendationData.recommendationItemList.forEach { item ->
            if (item.productId == recomItem.productId) {
                val copyItem = item.copy()
                copyItem.onFailedUpdateCart()
                newdata.add(copyItem)
            } else {
                newdata.add(item)
            }
        }
        data.recommendationData.recommendationItemList = newdata
    }

}