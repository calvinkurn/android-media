package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.TotalStockEditorUiModel
import com.tokopedia.product.manage.feature.quickedit.common.constant.EditProductConstant
import kotlinx.android.synthetic.main.item_campaign_stock_total_editor.view.*

class TotalStockEditorViewHolder(itemView: View?,
                                 private val onTotalStockChanged: (Int) -> Unit
): AbstractViewHolder<TotalStockEditorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_total_editor
    }

    override fun bind(element: TotalStockEditorUiModel) {
        itemView.qte_campaign_stock_amount?.run {
            maxValue = EditProductConstant.MAXIMUM_STOCK
            setValue(element.totalStock)
            setValueChangedListener { stock, _, _ ->
                onTotalStockChanged(stock)
            }
        }
    }
}