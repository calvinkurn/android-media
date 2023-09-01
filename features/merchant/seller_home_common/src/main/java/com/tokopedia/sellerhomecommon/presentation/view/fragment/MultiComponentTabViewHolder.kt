package com.tokopedia.sellerhomecommon.presentation.view.fragment

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.BarChartMetricValue
import com.tokopedia.charts.model.StackedBarChartData
import com.tokopedia.charts.model.StackedBarChartMetric
import com.tokopedia.charts.model.StackedBarChartMetricValue
import com.tokopedia.sellerhomecommon.databinding.ShcMultiComponentViewBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MultiComponentAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentLegendModel
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.MultiComponentTabAdapter
import com.tokopedia.utils.view.binding.viewBinding

class MultiComponentTabViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), MultiComponentTabFragmentInterface {

    private var viewBinding: ShcMultiComponentViewBinding? by viewBinding()

    private val tabAdapter by lazy {
        MultiComponentTabAdapter(MultiComponentAdapterFactoryImpl())
    }

    override fun onSetData(tab: MultiComponentTab) {
        if (tab.isLoaded) {
            if (tab.isError) {
                // TODO: Populate error
            }
        }
    }

    fun bind(tab: MultiComponentTab) {
        viewBinding?.rvShcMultiComponentView?.run {
            adapter = tabAdapter
            context?.let {
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
            isNestedScrollingEnabled = false
        }

        tabAdapter.setData(
            listOf(
                BarMultiComponentUiModel(
                    title = "Detail potensi penjualan",
                    description = "Bebas Ongkir bantu tingkatkan 5% potensi penjualan",
                    stackedBarChartData = StackedBarChartData(
                        yAxis = listOf(
                            AxisLabel(0f, ""),
                            AxisLabel(20f, "20jt"),
                            AxisLabel(40f, "40jt"),
                            AxisLabel(60f, "60jt"),
                            AxisLabel(80f, "80jt"),
                            AxisLabel(100f, "100jt")
                        ),
                        xAxisLabels = listOf(),
                        metrics = listOf(
                            StackedBarChartMetric(
                                values = listOf(
                                    StackedBarChartMetricValue(
                                        values = listOf(
                                            BarChartMetricValue(
                                                value = 4.25f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#C9FDE0"
                                            ),
                                            BarChartMetricValue(
                                                value = 1.25f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#4FE397"
                                            ),
                                            BarChartMetricValue(
                                                value = 1f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#00AA5B"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    legends = listOf(
                        BarMultiComponentLegendModel(
                            Color.parseColor("#00AA5B"),
                            "Bebas Ongkir",
                            "6.029"
                        ),
                        BarMultiComponentLegendModel(
                            Color.parseColor("#4FE397"),
                            "Bebas Ongkir Dilayani Tokopedia",
                            "10.053"
                        ),
                        BarMultiComponentLegendModel(
                            Color.parseColor("#C9FDE0"),
                            "Non Bebas Ongkir",
                            "4.250"
                        )
                    )
                ),BarMultiComponentUiModel(
                    title = "Detail potensi penjualan",
                    description = "Bebas Ongkir bantu tingkatkan 5% potensi penjualan",
                    stackedBarChartData = StackedBarChartData(
                        yAxis = listOf(
                            AxisLabel(0f, ""),
                            AxisLabel(20f, "20jt"),
                            AxisLabel(40f, "40jt"),
                            AxisLabel(60f, "60jt"),
                            AxisLabel(80f, "80jt"),
                            AxisLabel(100f, "100jt")
                        ),
                        xAxisLabels = listOf(),
                        metrics = listOf(
                            StackedBarChartMetric(
                                values = listOf(
                                    StackedBarChartMetricValue(
                                        values = listOf(
                                            BarChartMetricValue(
                                                value = 4.25f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#C9FDE0"
                                            ),
                                            BarChartMetricValue(
                                                value = 1.25f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#4FE397"
                                            ),
                                            BarChartMetricValue(
                                                value = 1f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#00AA5B"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    legends = listOf(
                        BarMultiComponentLegendModel(
                            Color.parseColor("#00AA5B"),
                            "Bebas Ongkir",
                            "6.029"
                        ),
                        BarMultiComponentLegendModel(
                            Color.parseColor("#4FE397"),
                            "Bebas Ongkir Dilayani Tokopedia",
                            "10.053"
                        ),
                        BarMultiComponentLegendModel(
                            Color.parseColor("#C9FDE0"),
                            "Non Bebas Ongkir",
                            "4.250"
                        )
                    )
                )
            )
        )
    }

}

interface MultiComponentTabFragmentInterface {

    fun onSetData(tab: MultiComponentTab)

}


