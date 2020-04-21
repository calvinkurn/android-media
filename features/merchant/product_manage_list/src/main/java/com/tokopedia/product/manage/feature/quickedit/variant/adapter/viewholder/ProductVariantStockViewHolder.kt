package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.QuantityEditorUnify
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
    }

    private fun setProductName(variant: ProductVariant) {
        itemView.textProductName.text = variant.name
    }

    private fun setupStockQuantityEditor(variant: ProductVariant) {
        itemView.quantityEditorStock.apply {
            setStockMinMaxValue()
            setStockEditorValue(variant.stock)
            setStockEditorOnActionListener()
            setStockEditorFocusChangeListener()
            addStockEditorTextChangedListener(variant)
        }
    }

    private fun setupStatusSwitch(variant: ProductVariant) {
        itemView.switchStatus.setOnCheckedChangeListener(null)
        itemView.switchStatus.isChecked = variant.status == ProductStatus.ACTIVE
        itemView.switchStatus.setOnCheckedChangeListener { _, isChecked ->
            val status = if(isChecked) {
                ProductStatus.ACTIVE
            } else {
                ProductStatus.INACTIVE
            }
            listener.onStatusChanged(variant.id, status)
        }
    }

    private fun QuantityEditorUnify.setStockEditorFocusChangeListener() {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSoftKeyboard()
            } else {
                hideSoftKeyboard()
            }
        }
    }

    private fun QuantityEditorUnify.setStockMinMaxValue() {
        minValue = MINIMUM_STOCK
        maxValue = MAXIMUM_STOCK
        editText.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_LENGTH))
    }

    private fun QuantityEditorUnify.setStockEditorValue(stock: Int) = setValue(stock)

    private fun QuantityEditorUnify.setStockEditorOnActionListener() {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onClickDoneStockEditor()
            }
            true
        }
    }

    private fun QuantityEditorUnify.onClickDoneStockEditor() {
        if (editText.text.isEmpty()) {
            setValue(MINIMUM_STOCK)
        }
        clearFocus()
        hideSoftKeyboard()
    }

    private fun QuantityEditorUnify.addStockEditorTextChangedListener(variant: ProductVariant) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editor: Editable?) {
                val input = editor.toString()

                if (input.isNotEmpty()) {
                    val stock = input.replace(".", "").toIntOrZero()
                    listener.onStockChanged(variant.id, stock)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun QuantityEditorUnify.showSoftKeyboard() {
        val imm = itemView.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun QuantityEditorUnify.hideSoftKeyboard() {
        val imm = itemView.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    interface ProductVariantStockListener {
        fun onStockChanged(variantId: String, stock: Int)
        fun onStatusChanged(variantId: String, status: ProductStatus)
    }
}
