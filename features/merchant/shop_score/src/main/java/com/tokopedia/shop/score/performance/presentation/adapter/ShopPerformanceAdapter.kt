package com.tokopedia.shop.score.performance.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.score.performance.presentation.adapter.diffutilscallback.ShopPerformanceDiffUtilCallback
import com.tokopedia.shop.score.performance.presentation.model.BaseShopPerformance
import com.tokopedia.shop.score.performance.presentation.model.ItemShopPerformanceErrorUiModel
import com.tokopedia.shop.score.performance.presentation.model.TickerReactivatedUiModel

class ShopPerformanceAdapter(
    shopPerformanceAdapterTypeFactory: ShopPerformanceAdapterTypeFactory
) : BaseAdapter<ShopPerformanceAdapterTypeFactory>(shopPerformanceAdapterTypeFactory) {

    fun setShopPerformanceData(data: List<BaseShopPerformance>) {
        val diffCallback = ShopPerformanceDiffUtilCallback(visitables, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeShopPerformanceData() {
        val shopPerformanceDataCount = visitables.filterIsInstance<BaseShopPerformance>().count()
        if (shopPerformanceDataCount.isMoreThanZero()) {
            visitables.removeAll { it is BaseShopPerformance }
            notifyItemRangeRemoved(visitables.size, shopPerformanceDataCount)
        }
    }

    fun setShopPerformanceError(item: ItemShopPerformanceErrorUiModel) {
        if (visitables.getOrNull(lastIndex) !is ItemShopPerformanceErrorUiModel) {
            visitables.add(item)
            notifyItemInserted(lastIndex)
        }
    }

    fun removeShopPerformanceError() {
        if (visitables.getOrNull(lastIndex) is ItemShopPerformanceErrorUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun setShopPerformanceLoading(item: LoadingModel) {
        if (visitables.getOrNull(lastIndex) !is LoadingModel) {
            visitables.add(item)
            notifyItemInserted(lastIndex)
        }
    }

    fun removeShopPerformanceLoading() {
        if (visitables.getOrNull(lastIndex) is LoadingModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun removeTickerReactivated() {
        val tickerReactivatedIndex = visitables.indexOfFirst { it is TickerReactivatedUiModel }
        if (tickerReactivatedIndex != RecyclerView.NO_POSITION) {
            visitables.removeAt(tickerReactivatedIndex)
            notifyItemRemoved(tickerReactivatedIndex)
        }
    }
}