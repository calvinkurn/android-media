package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel

/**
 * Created By @ilhamsuaib on 09/07/20
 */

class BarChartViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<BarChartWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_bar_chart_widget
    }

    override fun bind(element: BarChartWidgetUiModel) {
        with(itemView) {

        }
    }

    interface Listener : BaseViewHolderListener
}