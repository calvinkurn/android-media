package com.tokopedia.sellerhomecommon.presentation.model.multicomponent

import com.tokopedia.charts.model.StackedBarChartData
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactory

class BarMultiComponentUiModel(
    val title: String,
    val description: String,
    val stackedBarChartData: StackedBarChartData,
    val legends: List<BarMultiComponentLegendModel>
): MultiComponentItemUiModel {

    override fun type(typeFactory: MultiComponentAdapterFactory): Int {
        return typeFactory.type(this)
    }

}

data class BarMultiComponentLegendModel(
    val color: Int,
    val title: String,
    val value: String
)
