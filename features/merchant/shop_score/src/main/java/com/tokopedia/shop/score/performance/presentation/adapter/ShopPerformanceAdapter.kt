package com.tokopedia.shop.score.performance.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.performance.presentation.model.BaseShopPerformance

class ShopPerformanceAdapter(
        shopPerformanceAdapterTypeFactory: ShopPerformanceAdapterTypeFactory
): BaseAdapter<ShopPerformanceAdapterTypeFactory>(shopPerformanceAdapterTypeFactory) {

    fun setShopPerformanceData(data: List<BaseShopPerformance>) {
        visitables.clear()
        visitables.addAll(data)
        notifyDataSetChanged()
    }
}