package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class PieChartViewHolder(
        itemView: View?,
        listener: Listener
) : AbstractViewHolder<PieChartWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_pie_chart_widget
    }

    override fun bind(element: PieChartWidgetUiModel) {
        with(itemView) {

        }
    }

    interface Listener : BaseViewHolderListener {

    }
}