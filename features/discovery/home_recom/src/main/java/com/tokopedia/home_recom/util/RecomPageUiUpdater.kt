package com.tokopedia.home_recom.util

import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by yfsx on 01/09/21.
 */
class RecomPageUiUpdater(var dataList: MutableList<HomeRecommendationDataModel>) {

    fun appendFirstData(listOfData: List<HomeRecommendationDataModel>) {
        dataList = mutableListOf()
        listOfData.forEach {
            when (it) {
                is RecommendationItemDataModel -> dataList.add(it)
                is ProductInfoDataModel -> dataList.add(it)
            }
        }
    }

    fun appendNextData(listOfData: List<HomeRecommendationDataModel>) {
        listOfData.forEach {
            when (it) {
                is RecommendationItemDataModel -> dataList.add(it)
                is ProductInfoDataModel -> dataList.add(it)
            }
        }
    }

    fun updateRecomWithMinicartData(miniCart: MutableMap<String, MiniCartItem>?) {
        dataList.filterIsInstance(RecommendationItemDataModel::class.java).forEach {
            val recomItem = it.productItem.copy()
            if (recomItem.isRecomProductShowVariantAndCart) {
                recomItem.setDefaultCurrentStock()
                miniCart?.let { cartData ->
                    recomItem.updateItemCurrentStock(when {
                        recomItem.isProductHasParentID() -> {
                            getTotalQuantityVariantBasedOnParentID(recomItem, miniCart)
                        }
                        cartData.containsKey(recomItem.productId.toString()) -> {
                            cartData[recomItem.productId.toString()]?.quantity ?: 0
                        }
                        else -> 0
                    })
                }
            }
        }
    }

    private fun getTotalQuantityVariantBasedOnParentID(recomItem: RecommendationItem, miniCart: MutableMap<String, MiniCartItem>): Int {
        var variantTotalItems = 0
        miniCart.values.forEach { miniCartItem ->
            if (miniCartItem.productParentId == recomItem.parentID.toString()) {
                variantTotalItems += miniCartItem.quantity
            }
        }
        return variantTotalItems
    }
}