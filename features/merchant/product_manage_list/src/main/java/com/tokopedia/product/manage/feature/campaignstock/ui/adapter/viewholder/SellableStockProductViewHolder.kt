package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.text.InputFilter
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.quickedit.common.constant.EditProductConstant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.QuantityEditorUnify
import kotlinx.android.synthetic.main.item_campaign_stock_variant_editor.view.*

class SellableStockProductViewHolder(itemView: View?,
                                     private val onVariantStockChanged: (productId: String, stock: Int) -> Unit,
                                     private val onVariantStatusChanged: (productId: String, status: ProductStatus) -> Unit): AbstractViewHolder<SellableStockProductUIModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_variant_editor

        private const val MAXIMUM_LENGTH = 7
    }

    override fun bind(element: SellableStockProductUIModel) {
        with(itemView) {
            tv_campaign_stock_variant_editor_name?.text = element.productName
            qte_campaign_stock_variant_editor?.setElement(element)
            switch_campaign_stock_variant_editor?.run {
                isChecked = element.isActive
                setOnCheckedChangeListener { _, isChecked ->
                    val status =
                            if (isChecked) {
                                ProductStatus.ACTIVE
                            } else {
                                ProductStatus.INACTIVE
                            }
                    onVariantStatusChanged(element.productId, status)
                }
            }
        }
    }

    private fun QuantityEditorUnify.setElement(element: SellableStockProductUIModel) {
        val maxLength = InputFilter.LengthFilter(MAXIMUM_LENGTH)
        editText.filters = arrayOf(maxLength)
        minValue = EditProductConstant.MINIMUM_STOCK
        maxValue = EditProductConstant.MAXIMUM_STOCK

        setValue(element.stock.toIntOrZero())

        editText.afterTextChanged {
            val input = it
            val stock = if(input.isNotEmpty()) {
                input.toInt()
            } else {
                EditProductConstant.MINIMUM_STOCK
            }
            toggleQuantityEditorBtn(stock)
            onVariantStockChanged(element.productId, stock)
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