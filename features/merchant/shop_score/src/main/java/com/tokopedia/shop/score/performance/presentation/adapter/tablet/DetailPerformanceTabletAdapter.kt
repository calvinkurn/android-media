package com.tokopedia.shop.score.performance.presentation.adapter.tablet

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.diffutilscallback.DetailPerformanceDiffUtilCallback
import com.tokopedia.shop.score.performance.presentation.model.tablet.BaseParameterDetail

class DetailPerformanceTabletAdapter(
    detailPerformanceAdapterTabletTypeFactory: DetailPerformanceAdapterTabletTypeFactory
) : BaseAdapter<DetailPerformanceAdapterTabletTypeFactory>(detailPerformanceAdapterTabletTypeFactory) {

    fun setDetailPerformanceData(data: List<BaseParameterDetail>) {
        val diffCallback = DetailPerformanceDiffUtilCallback(visitables, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }
}