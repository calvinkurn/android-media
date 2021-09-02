package com.tokopedia.tokopedianow.home.presentation.ext

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRecentPurchaseViewHolder

object RecyclerViewExt {

    fun RecyclerView.submitProduct(data: HomeLayoutListUiModel) {
        try {
            submitRecentPurchase(data)
            submitProductRecom(data)
        } catch (e: Exception) {
            // nothing to do
        }
    }

    private fun RecyclerView.submitRecentPurchase(data: HomeLayoutListUiModel) {
        val layoutList = data.items
        val layoutItem = layoutList.firstOrNull { it.layout is TokoNowRecentPurchaseUiModel }

        layoutItem?.let {
            val index = layoutList.indexOf(layoutItem)
            val viewHolder = findViewHolderForAdapterPosition(index) as? TokoNowRecentPurchaseViewHolder
            val recentPurchase = layoutItem.layout as? TokoNowRecentPurchaseUiModel
            if (!recentPurchase?.productList.isNullOrEmpty()) {
                viewHolder?.submitList(recentPurchase)
            }
        }
    }

    private fun RecyclerView.submitProductRecom(data: HomeLayoutListUiModel) {
        val layoutList = data.items
        layoutList.filter { it.layout is HomeProductRecomUiModel }.forEach {
            val index = layoutList.indexOf(it)
            val viewHolder = findViewHolderForAdapterPosition(index) as? HomeProductRecomViewHolder
            val productRecom = it.layout as? HomeProductRecomUiModel
            if (!productRecom?.recomWidget?.recommendationItemList.isNullOrEmpty()) {
                viewHolder?.submitList(productRecom)
            }
        }
    }
}