package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.databinding.ItemCampaignStockActiveProductSwitchBinding
import com.tokopedia.utils.view.binding.viewBinding

class ActiveProductSwitchViewHolder(itemView: View?,
                                    private val onActiveStockChanged: (Boolean) -> Unit,
                                    private val source: String,
                                    private val shopId: String): AbstractViewHolder<ActiveProductSwitchUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_active_product_switch
    }

    private val binding by viewBinding<ItemCampaignStockActiveProductSwitchBinding>()

    override fun bind(element: ActiveProductSwitchUiModel) {
        binding?.switchCampaignStockActive?.run {
            isChecked = element.isActive
            setOnCheckedChangeListener { _, isChecked ->
                onActiveStockChanged(isChecked)
                ProductManageTracking.eventClickAllocationProductStatus(
                    isVariant = false,
                    isOn = isChecked,
                    source = source,
                    productId = element.productId.orEmpty(),
                    shopId = shopId)
            }
            isEnabled = element.access?.editProduct == true
        }
    }
}