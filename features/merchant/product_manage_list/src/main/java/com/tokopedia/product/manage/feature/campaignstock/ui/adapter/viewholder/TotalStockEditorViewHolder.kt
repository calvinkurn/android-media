package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.text.InputFilter
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.TotalStockEditorUiModel
import com.tokopedia.unifycomponents.QuantityEditorUnify
import kotlinx.android.synthetic.main.item_campaign_stock_total_editor.view.*

class TotalStockEditorViewHolder(itemView: View?,
                                 private val onTotalStockChanged: (Int) -> Unit
): AbstractViewHolder<TotalStockEditorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_total_editor

        private const val MAXIMUM_LENGTH = 7
    }

    override fun bind(element: TotalStockEditorUiModel) {
        itemView.qte_campaign_stock_amount?.setElement(element)
    }

    private fun QuantityEditorUnify.setElement(element: TotalStockEditorUiModel) {
        val maxLength = InputFilter.LengthFilter(MAXIMUM_LENGTH)
        editText.filters = arrayOf(maxLength)
        minValue = EditProductConstant.MINIMUM_STOCK
        maxValue = EditProductConstant.MAXIMUM_STOCK

        setValue(element.totalStock)

        editText.afterTextChanged {
            val input = it
            val stock = if(input.isNotEmpty()) {
                input.toInt()
            } else {
                EditProductConstant.MINIMUM_STOCK
            }
            toggleQuantityEditorBtn(stock)
            onTotalStockChanged(stock)
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                ProductManageTracking.eventClickAllocationInputStock(isVariant = false)
            }
        }
        setAddClickListener {
            ProductManageTracking.eventClickAllocationIncreaseStock(isVariant = false)
        }
        setSubstractListener {
            ProductManageTracking.eventClickAllocationDecreaseStock(isVariant = false)
        }
    }

    private fun QuantityEditorUnify.toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < EditProductConstant.MAXIMUM_STOCK
        val enableSubtractBtn = stock > EditProductConstant.MINIMUM_STOCK

        addButton.isEnabled = enableAddBtn
        subtractButton.isEnabled = enableSubtractBtn
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }
}