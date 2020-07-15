package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.quickedit.common.constant.EditProductConstant
import kotlinx.android.synthetic.main.item_campaign_stock_variant_editor.view.*

class SellableStockProductViewHolder(itemView: View?): AbstractViewHolder<SellableStockProductUIModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_variant_editor
    }

    override fun bind(element: SellableStockProductUIModel) {
        with(itemView) {
            tv_campaign_stock_variant_editor_name?.text = element.productName
            qte_campaign_stock_variant_editor?.run {
                maxValue = EditProductConstant.MAXIMUM_STOCK
                setValue(element.stock.toIntOrZero())
            }
            switch_campaign_stock_variant_editor?.isChecked = element.isActive
        }
    }
}