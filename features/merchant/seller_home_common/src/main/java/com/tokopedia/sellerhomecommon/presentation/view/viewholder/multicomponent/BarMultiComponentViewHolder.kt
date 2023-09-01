package com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.charts.config.BarChartConfig
import com.tokopedia.charts.model.StackedBarChartData
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcMultiComponentItemStackedBarBinding
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentLegendModel
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.MultiComponentLegendAdapter
import com.tokopedia.sellerhomecommon.utils.ChartXAxisLabelFormatter
import com.tokopedia.sellerhomecommon.utils.ChartYAxisLabelFormatter
import com.tokopedia.utils.view.binding.viewBinding

class BarMultiComponentViewHolder(itemView: View): AbstractViewHolder<BarMultiComponentUiModel>(itemView) {

    private val binding: ShcMultiComponentItemStackedBarBinding? by viewBinding()

    override fun bind(element: BarMultiComponentUiModel) {
        setupText(element.title, element.description)
        setupBarChart(element.stackedBarChartData)
        setupRecyclerView(element.legends)
    }

    private fun setupText(
        title: String,
        description: String
    ) {
        binding?.tvShcStackedBarTitle?.text = title
        binding?.tvShcStackedBarDesc?.text = description
    }

    private fun setupBarChart(data: StackedBarChartData) {
        binding?.barChartShcStackedBar?.run {
            init(
                BarChartConfig.create {
                    stackedBar { true }
                    xAxis {
                        labelFormatter {
                            ChartXAxisLabelFormatter(listOf(String.EMPTY))
                        }
                        gridEnabled {
                            false
                        }
                    }
                    yAxis {
                        val yAxisLabels = data.yAxis
                        textColor { itemView.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600) }
                        labelFormatter {
                            ChartYAxisLabelFormatter(yAxisLabels)
                        }
                        labelCount { yAxisLabels.size }
                    }
                }
            )
            setData(data)
        }
    }

    private fun setupRecyclerView(legends: List<BarMultiComponentLegendModel>) {
        binding?.rvShcStackedBar?.run {
            adapter = MultiComponentLegendAdapter(legends)
            context?.let {
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_component_item_stacked_bar
    }

}
