package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmExpandableWidgetItemBinding
import com.tokopedia.power_merchant.subscribe.view.model.ExpandableItemUiModel
import com.tokopedia.utils.view.binding.viewBinding

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

    private val binding: ItemPmExpandableWidgetItemBinding? by viewBinding()

    override fun bind(element: ExpandableItemUiModel) {
        binding?.run {
            tvPmExpandableItemText.text = element.text

            if (element.urlOrAppLink.isNotBlank()) {
                icPmExpandableItemIcon.visible()
                root.setOnClickListener {
                    RouteManager.route(root.context, element.urlOrAppLink)
                    powerMerchantTracking.sendEventClickPowerMerchantBenefitItem(element.text)
                }
            } else {
                icPmExpandableItemIcon.gone()
            }
        }
    }
}