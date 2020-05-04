package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import kotlinx.android.synthetic.main.item_product_variant_stock.view.*

class ProductVariantStockViewHolder(
    itemView: View,
    private val listener: ProductVariantStockListener
): AbstractViewHolder<ProductVariant>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_variant_stock

        private const val MAXIMUM_STOCK = 999999
        private const val MINIMUM_STOCK = 0
        private const val MAXIMUM_LENGTH = 7
    }

    override fun bind(variant: ProductVariant) {
        setProductName(variant)
        setupStockQuantityEditor(variant)
        setupStatusSwitch(variant)
        setupStatusLabel(variant)
    }

    private fun setProductName(variant: ProductVariant) {
        itemView.textProductName.text = variant.name
    }

    private fun setupStockQuantityEditor(variant: ProductVariant) {
        setStockMinMaxValue()
        setStockEditorValue(variant.stock)
        addStockEditorTextChangedListener(variant)
    }

    private fun setupStatusSwitch(variant: ProductVariant) {
        itemView.switchStatus.setOnCheckedChangeListener(null)
        itemView.switchStatus.isChecked = variant.isActive()
        itemView.switchStatus.setOnCheckedChangeListener { _, isChecked ->
            val status = if(isChecked) {
                ProductStatus.ACTIVE
            } else {
                ProductStatus.INACTIVE
            }
            listener.onStatusChanged(variant.id, status)
        }
    }

    private fun setupStatusLabel(variant: ProductVariant) {
        itemView.labelInactive.showWithCondition(variant.isNotActive())
    }

    private fun setStockMinMaxValue() {
        itemView.quantityEditorStock.apply {
            val maxLength = LengthFilter(MAXIMUM_LENGTH)
            editText.filters = arrayOf(maxLength)
            minValue = MINIMUM_STOCK
            maxValue = MAXIMUM_STOCK
        }
    }

    private fun setStockEditorValue(stock: Int) {
        itemView.quantityEditorStock.setValue(stock)
    }

    private fun addStockEditorTextChangedListener(variant: ProductVariant) {
        itemView.quantityEditorStock.apply {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editor: Editable?) {
                    val input = editor.toString()
                    val stock = if(input.isNotEmpty()) {
                        input.replace(".", "").toIntOrZero()
                    } else {
                        MINIMUM_STOCK
                    }
                    listener.onStockChanged(variant.id, stock)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    interface ProductVariantStockListener {
        fun onStockChanged(variantId: String, stock: Int)
        fun onStatusChanged(variantId: String, status: ProductStatus)
    }
}
