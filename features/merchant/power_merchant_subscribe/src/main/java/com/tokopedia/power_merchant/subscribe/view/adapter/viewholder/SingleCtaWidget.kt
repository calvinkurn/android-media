package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetSingleCtaUiModel
import kotlinx.android.synthetic.main.widget_pm_single_cta.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class SingleCtaWidget(itemView: View) : AbstractViewHolder<WidgetSingleCtaUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_single_cta
    }

    override fun bind(element: WidgetSingleCtaUiModel) {
        with(itemView) {
            tvPmSingleCta.text = element.ctaText
            setOnClickListener {
                RouteManager.route(context, element.urlOrAppLink)
            }
        }
    }
}