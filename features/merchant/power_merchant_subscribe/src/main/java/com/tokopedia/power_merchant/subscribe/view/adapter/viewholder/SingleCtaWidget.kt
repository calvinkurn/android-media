package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmSingleCtaBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetSingleCtaUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class SingleCtaWidget(itemView: View) : AbstractViewHolder<WidgetSingleCtaUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_single_cta
    }

    private val binding: WidgetPmSingleCtaBinding? by viewBinding()

    override fun bind(element: WidgetSingleCtaUiModel) {
        binding?.run {
            tvPmSingleCta.text = element.ctaText
            root.setOnClickListener {
                RouteManager.route(root.context, element.urlOrAppLink)
            }
        }
    }
}