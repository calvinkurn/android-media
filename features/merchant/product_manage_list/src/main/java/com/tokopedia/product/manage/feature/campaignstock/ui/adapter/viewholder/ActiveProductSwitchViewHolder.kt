package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import kotlinx.android.synthetic.main.item_campaign_stock_active_product_switch.view.*

class ActiveProductSwitchViewHolder(itemView: View?,
                                    private val onActiveStockChanged: (Boolean) -> Unit): AbstractViewHolder<ActiveProductSwitchUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_active_product_switch
    }

    override fun bind(element: ActiveProductSwitchUiModel) {
        itemView.switch_campaign_stock_active?.run {
            isChecked = element.isActive
            setOnCheckedChangeListener { _, isChecked ->
                onActiveStockChanged(isChecked)
                ProductManageTracking.eventClickAllocationProductStatus(isVariant = false, isOn = isChecked)
            }
            isEnabled = element.access?.editProduct == true
        }
    }
}