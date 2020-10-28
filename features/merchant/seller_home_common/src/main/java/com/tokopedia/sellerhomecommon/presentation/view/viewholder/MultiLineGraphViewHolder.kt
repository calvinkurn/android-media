package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.charts.config.LineChartConfig
import com.tokopedia.charts.model.LineChartConfigModel
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.MultiLineMetricsAdapter
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import kotlinx.android.synthetic.main.shc_multi_line_graph_widget.view.*

/**
 * Created By @ilhamsuaib on 27/10/20
 */

class MultiLineGraphViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<MultiLineGraphWidgetUiModel>(itemView), MultiLineMetricsAdapter.MetricsListener {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_line_graph_widget
    }

    private val metricsAdapter by lazy { MultiLineMetricsAdapter(this) }
    private var lastSelectedMetric: MultiLineMetricUiModel? = null

    override fun bind(element: MultiLineGraphWidgetUiModel) {
        val data = element.data
        when {
            data == null -> setOnLoadingState(element)
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    override fun onItemClickListener(metric: MultiLineMetricUiModel, position: Int) {
        lastSelectedMetric = metric
        itemView.rvShcGraphMetrics.scrollToPosition(position)
    }

    private fun setOnLoadingState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnErrorState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnSuccessState(element: MultiLineGraphWidgetUiModel) {
        lastSelectedMetric = element.data?.metrics?.getOrNull(0)

        with(itemView) {
            tvShcMultiLineGraphTitle.text = element.title

            setupMetrics(element.data?.metrics.orEmpty())
            setupLineGraph(element)
        }
    }

    private fun setupMetrics(items: List<MultiLineMetricUiModel>) {
        with(itemView) {
            rvShcGraphMetrics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvShcGraphMetrics.adapter = metricsAdapter

            metricsAdapter.setItems(items)
            rvShcGraphMetrics.post {
                metricsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupLineGraph(element: MultiLineGraphWidgetUiModel) {
        with(itemView.chartViewShcMultiLine) {
            init(getLineGraphConfig(element))
        }
    }

    private fun getLineGraphConfig(element: MultiLineGraphWidgetUiModel): LineChartConfigModel {
        return LineChartConfig.create { }
    }

    interface Listener : BaseViewHolderListener {

    }
}