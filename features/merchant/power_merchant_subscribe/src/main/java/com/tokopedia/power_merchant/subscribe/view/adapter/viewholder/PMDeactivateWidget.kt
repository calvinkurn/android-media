package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmDeactivateWidgetBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPMDeactivateUiModel
import com.tokopedia.utils.view.binding.viewBinding

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

    private val binding: WidgetPmDeactivateWidgetBinding? by viewBinding()

    override fun bind(element: WidgetPMDeactivateUiModel) {
        binding?.tvPmDeactivateCta?.setOnClickListener {
            listener.setOnDeactivatePMClickListener()
        }
    }

    interface PMDeactivateWidgetListener {

        fun setOnDeactivatePMClickListener()
    }
}