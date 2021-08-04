package com.tokopedia.shop.score.performance.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.performance.presentation.model.BaseShopPerformance
import com.tokopedia.shop.score.performance.presentation.model.ItemShopPerformanceErrorUiModel

class ShopPerformanceAdapter(
        shopPerformanceAdapterTypeFactory: ShopPerformanceAdapterTypeFactory
): BaseAdapter<ShopPerformanceAdapterTypeFactory>(shopPerformanceAdapterTypeFactory) {

    fun setShopPerformanceData(data: List<BaseShopPerformance>) {
        visitables.clear()
        visitables.addAll(data)
        notifyDataSetChanged()
    }

    fun setShopPerformanceError(item: ItemShopPerformanceErrorUiModel) {
        if (visitables.getOrNull(lastIndex) !is ItemShopPerformanceErrorUiModel) {
            visitables.add(item)
            notifyItemInserted(lastIndex)
        }
    }
}