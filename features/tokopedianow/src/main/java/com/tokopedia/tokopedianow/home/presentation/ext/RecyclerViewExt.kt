package com.tokopedia.tokopedianow.home.presentation.ext

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRecentPurchaseViewHolder

object RecyclerViewExt {

    fun RecyclerView.submitProduct(data: HomeLayoutListUiModel) {
        submitRecentPurchase(data)
        submitProductRecom(data)
    }

    private fun RecyclerView.submitRecentPurchase(data: HomeLayoutListUiModel) {
        val layoutList = data.items
        val layoutItem = layoutList.firstOrNull { it.layout is TokoNowRecentPurchaseUiModel }

        layoutItem?.let {
            val index = layoutList.indexOf(layoutItem)
            val viewHolder = findViewHolderForAdapterPosition(index) as? TokoNowRecentPurchaseViewHolder
            val recentPurchase = layoutItem.layout as? TokoNowRecentPurchaseUiModel
            viewHolder?.submitList(recentPurchase)
        }
    }

    private fun RecyclerView.submitProductRecom(data: HomeLayoutListUiModel) {
        val layoutList = data.items
        val layoutItem = layoutList.firstOrNull { it.layout is HomeProductRecomUiModel }

        layoutItem?.let {
            val index = layoutList.indexOf(layoutItem)
            val viewHolder = findViewHolderForAdapterPosition(index) as? HomeProductRecomViewHolder
            val productRecom = layoutItem.layout as? HomeProductRecomUiModel
            viewHolder?.submitList(productRecom)
        }
    }
}