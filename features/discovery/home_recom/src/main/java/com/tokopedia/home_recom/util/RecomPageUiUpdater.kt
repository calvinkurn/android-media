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

    fun appendFirstRecomData(listOfData: List<HomeRecommendationDataModel>) {
        dataList = mutableListOf()
        listOfData.forEach {
            when (it) {
                is RecommendationItemDataModel -> dataList.add(it)
                is ProductInfoDataModel -> dataList.add(it)
            }
        }
    }

    fun appendNextRecomData(listOfData: List<HomeRecommendationDataModel>) {
        listOfData.forEach {
            when (it) {
                is RecommendationItemDataModel -> dataList.add(it)
                is ProductInfoDataModel -> dataList.add(it)
            }
        }
    }

    fun updateRecomWithMinicartData(miniCart: MutableMap<String, MiniCartItem>?) {
        val newDataList = mutableListOf<HomeRecommendationDataModel>()
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
            newDataList.add(RecommendationItemDataModel(recomItem))
        }
        dataList = newDataList
    }

    fun updateCurrentQuantityRecomItem(recomItem: RecommendationItem) {
        dataList.forEach loop@{ item ->
            if (item is RecommendationItemDataModel && item.productItem.productId == recomItem.productId) {
                item.productItem.currentQuantity = recomItem.currentQuantity
                return@loop
            }
        }
    }

    fun resetFailedRecomTokonowCard(recomItem: RecommendationItem) {
        val newDataList = mutableListOf<HomeRecommendationDataModel>()
        dataList.forEach { item ->
            if (item is RecommendationItemDataModel && item.productItem.productId == recomItem.productId) {
                val copyItem = (item as RecommendationItemDataModel).copy()
                val recomItemCopy = copyItem.productItem.copy()
                recomItemCopy.onFailedUpdateCart()
                copyItem.productItem = recomItemCopy
                newDataList.add(copyItem)
            } else {
                newDataList.add(item)
            }
        }
        dataList = newDataList
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