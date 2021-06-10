package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.model.ExpandableItemUiModel
import kotlinx.android.synthetic.main.item_pm_expandable_widget_item.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableItemViewHolder(
        itemView: View,
        private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<ExpandableItemUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_expandable_widget_item
    }

    override fun bind(element: ExpandableItemUiModel) {
        with(itemView) {
            tvPmExpandableItemText.text = element.text

            if (element.urlOrAppLink.isNotBlank()) {
                icPmExpandableItemIcon.visible()
                setOnClickListener {
                    RouteManager.route(context, element.urlOrAppLink)
                    powerMerchantTracking.sendEventClickPowerMerchantBenefitItem(element.text)
                }
            } else {
                icPmExpandableItemIcon.gone()
            }
        }
    }
}