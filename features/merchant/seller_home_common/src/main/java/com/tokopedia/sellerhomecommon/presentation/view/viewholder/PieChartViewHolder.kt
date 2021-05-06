package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.charts.config.PieChartConfig
import com.tokopedia.charts.model.PieChartEntry
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_loading.view.*
import kotlinx.android.synthetic.main.shc_pie_chart_widget.view.*

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class PieChartViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<PieChartWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_pie_chart_widget
    }

    override fun bind(element: PieChartWidgetUiModel) {
        with(itemView) {
            tvPieChartTitle.text = element.title
            setupTooltip(element)

            observeState(element)
        }
    }

    private fun observeState(element: PieChartWidgetUiModel) {
        val data = element.data

        when {
            data == null -> setOnLoading()
            data.error.isNotBlank() -> {
                setOnError()
                listener.setOnErrorWidget(adapterPosition, element, data.error)
            }
            else -> setOnSuccess(element)
        }
    }

    private fun setOnLoading() {
        with(itemView) {
            shimmerWidgetCommon.visible()
            commonWidgetErrorState.gone()
            pieChartShc.gone()
            tvPieChartValue.gone()
            tvPieChartSubValue.gone()
        }
    }

    private fun setOnSuccess(element: PieChartWidgetUiModel) = with(itemView) {
        shimmerWidgetCommon.gone()
        commonWidgetErrorState.gone()
        pieChartShc.visible()
        tvPieChartValue.visible()
        tvPieChartSubValue.visible()

        val data = element.data?.data
        tvPieChartValue.text = data?.summary?.valueFmt.orEmpty()
        tvPieChartSubValue.text = data?.summary?.diffPercentageFmt.orEmpty().parseAsHtml()

        pieChartShc.init(PieChartConfig.getDefaultConfig())
        pieChartShc.setData(getPieChartData(element))
        pieChartShc.invalidateChart()

        addOnImpressionListener(element.impressHolder) {
            listener.sendPieChartImpressionEvent(element)
        }
    }

    private fun setOnError() {
        with(itemView) {
            commonWidgetErrorState.visible()
            pieChartShc.gone()
            shimmerWidgetCommon.gone()
            tvPieChartValue.gone()
            tvPieChartSubValue.gone()

            ImageHandler.loadImageWithId(imgWidgetOnError, com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
        }
    }

    private fun setupTooltip(element: PieChartWidgetUiModel) = with(itemView) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            tvPieChartTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
            tvPieChartTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            tvPieChartTitle.clearUnifyDrawableEnd()
        }
    }

    private fun getPieChartData(element: PieChartWidgetUiModel): List<PieChartEntry> {
        return element.data?.data?.item?.map {
            PieChartEntry(
                    value = it.value,
                    valueFmt = it.valueFmt,
                    hexColor = it.color,
                    legend = it.legend
            )
        }.orEmpty()
    }

    interface Listener : BaseViewHolderListener {

        fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {}
    }
}