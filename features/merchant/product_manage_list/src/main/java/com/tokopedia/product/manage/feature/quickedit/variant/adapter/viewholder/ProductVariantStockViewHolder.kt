package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.analytics.ProductManageTracking
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

    private var tempStock: Int? = null

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
        setAddButtonClickListener()
        setSubtractButtonClickListener()
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
        val quantityEditor = itemView.quantityEditorStock
        tempStock = quantityEditor.getValue()
        quantityEditor.apply {
            editText.afterTextChanged {
                val input = it
                val stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }
                toggleQuantityEditorBtn(stock)
                listener.onStockChanged(variant.id, stock)
            }
        }
        quantityEditor.editText.setOnFocusChangeListener { _, isFocus ->
            if(!isFocus) {
                val currentStock = quantityEditor.getValue()
                tempStock?.let { previousStock ->
                    // if previous stock is not the same as current stock, hit the tracker
                    if(previousStock != currentStock) {
                        ProductManageTracking.eventClickChangeAmountVariant()
                        tempStock = currentStock
                    }
                }
            }
        }
    }

    private fun setAddButtonClickListener() {
        itemView.quantityEditorStock.apply {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock++

                if(stock <= MAXIMUM_STOCK) {
                    tempStock = stock
                    editText.setText(stock.getNumberFormatted())
                    ProductManageTracking.eventClickChangeAmountVariant()
                }
            }
        }
    }

    private fun setSubtractButtonClickListener() {
        itemView.quantityEditorStock.apply {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock--

                if(stock >= MINIMUM_STOCK) {
                    tempStock = stock
                    editText.setText(stock.getNumberFormatted())
                    ProductManageTracking.eventClickChangeAmountVariant()
                }
            }
        }
    }

    private fun toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < MAXIMUM_STOCK
        val enableSubtractBtn = stock > MINIMUM_STOCK

        itemView.quantityEditorStock.apply {
            addButton.isEnabled = enableAddBtn
            subtractButton.isEnabled = enableSubtractBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }

    interface ProductVariantStockListener {
        fun onStockChanged(variantId: String, stock: Int)
        fun onStatusChanged(variantId: String, status: ProductStatus)
    }
}
