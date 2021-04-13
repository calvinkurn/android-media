package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPMDeactivateUiModel
import kotlinx.android.synthetic.main.widget_pm_deactivate_widget.view.*

/**
 * Created By @ilhamsuaib on 19/03/21
 */

class PMDeactivateWidget(
        itemView: View,
        private val listener: PMDeactivateWidgetListener
) : AbstractViewHolder<WidgetPMDeactivateUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_deactivate_widget
    }

    override fun bind(element: WidgetPMDeactivateUiModel) {
        itemView.tvPmDeactivateCta.setOnClickListener {
            listener.setOnDeactivatePMClickListener()
        }
    }

    interface PMDeactivateWidgetListener {

        fun setOnDeactivatePMClickListener()
    }
}