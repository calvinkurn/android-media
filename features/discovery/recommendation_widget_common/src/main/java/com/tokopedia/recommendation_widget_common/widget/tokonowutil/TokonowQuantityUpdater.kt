package com.tokopedia.recommendation_widget_common.widget.tokonowutil

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData

/**
 * Created by yfsx on 06/10/21.
 */
object TokonowQuantityUpdater {

    fun updateRecomWithMinicartData(
        data: RecommendationCarouselData,
        miniCart: MutableMap<String, MiniCartItem>?
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
                                getTotalQuantityVariantBasedOnParentID(recomItem, miniCart)
                            }
                            cartData.containsKey(recomItem.productId.toString()) -> {
                                cartData[recomItem.productId.toString()]?.quantity ?: 0
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

    private fun getTotalQuantityVariantBasedOnParentID(
        recomItem: RecommendationItem,
        miniCart: MutableMap<String, MiniCartItem>
    ): Int {
        var variantTotalItems = 0
        miniCart.values.forEach { miniCartItem ->
            if (miniCartItem.productParentId == recomItem.parentID.toString()) {
                variantTotalItems += miniCartItem.quantity
            }
        }
        return variantTotalItems
    }

}