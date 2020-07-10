package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import kotlinx.android.synthetic.main.item_campaign_stock_variant_name.view.*

class VariantProductStockNameViewHolder(itemView: View?): AbstractViewHolder<VariantProductStockNameUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_variant_name
    }

    override fun bind(element: VariantProductStockNameUiModel) {
        with(itemView) {
            tv_campaign_stock_variant_name?.text = element.productName
            tv_campaign_stock_variant_count?.text = element.productStock
        }
    }
}