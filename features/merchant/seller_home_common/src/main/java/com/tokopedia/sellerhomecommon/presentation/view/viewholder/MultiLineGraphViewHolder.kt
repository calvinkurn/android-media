package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
) : AbstractViewHolder<MultiLineGraphWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_line_graph_widget
    }

    private val metricsAdapter by lazy { MultiLineMetricsAdapter() }

    override fun bind(element: MultiLineGraphWidgetUiModel) {
        val data = element.data
        when {
            data == null -> setOnLoadingState(element)
            data.error.isNotBlank() -> setOnErrorState(element)
            else -> setOnSuccessState(element)
        }
    }

    private fun setOnLoadingState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnErrorState(element: MultiLineGraphWidgetUiModel) {

    }

    private fun setOnSuccessState(element: MultiLineGraphWidgetUiModel) {
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

    }

    interface Listener : BaseViewHolderListener {

    }
}