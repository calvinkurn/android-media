package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.app.Activity
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.CurrencyIdrTextWatcher
import kotlinx.android.synthetic.main.item_product_manage_variant.view.*

class ProductVariantViewHolder(
    itemView: View,
    private val listener: ProductVariantListener
): AbstractViewHolder<ProductVariant>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_variant

        private const val MAXIMUM_EDIT_PRICE_INPUT_LENGTH = 11
    }

    override fun bind(variant: ProductVariant) {
        itemView.name.text = variant.name
        setupEditPriceTextField(variant)
    }

    private fun setupEditPriceTextField(variant: ProductVariant) {
        itemView.priceTextField.apply {
            setupClearIcon()
            setPriceTextFieldMaxLength()
            setPriceTextFieldValue(variant.price)
            setPriceTextFieldListeners(variant.id)
            setInputType(InputType.TYPE_CLASS_NUMBER)
        }
    }

    private fun TextFieldUnify.setupClearIcon() {
        setFirstIcon(R.drawable.ic_system_action_close_normal_24)
        getFirstIcon().setOnClickListener {
            textFieldInput.text.clear()
            hidePriceTextFieldError()
        }
    }

    private fun TextFieldUnify.setPriceTextFieldMaxLength() {
        textFieldInput.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_EDIT_PRICE_INPUT_LENGTH))
    }

    private fun TextFieldUnify.setPriceTextFieldValue(price: Int) {
        val priceRupiah = CurrencyFormatHelper.convertToRupiah(price.toString())
        val priceTxt = CurrencyFormatHelper.removeCurrencyPrefix(priceRupiah)
        val prefixTxt = itemView.context.getString(R.string.product_manage_quick_edit_currency)
        textFieldInput.setText(priceTxt)
        prependText(prefixTxt)
    }

    private fun TextFieldUnify.setPriceTextFieldListeners(productId: String) {
        val idrTextWatcher = CurrencyIdrTextWatcher(textFieldInput)
        textFieldInput.addTextChangedListener(idrTextWatcher)
        textFieldInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val price = CurrencyFormatHelper.convertRupiahToInt(textFieldInput.text.toString())
                listener.onPriceChanged(productId, price)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSoftKeyboard()
            } else {
                hideSoftKeyboard()
            }
        }
    }

    private fun TextFieldUnify.hidePriceTextFieldError() {
        setError(false)
        setMessage("")
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = itemView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = (itemView.context as? Activity)?.currentFocus ?: return
        val windowToken = currentFocus.windowToken ?: return

        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showSoftKeyboard() {
        val inputMethodManager = itemView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = (itemView.context as? Activity)?.currentFocus ?: return
        inputMethodManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT)
    }

    interface ProductVariantListener {
        fun onPriceChanged(variantId: String, price: Int)
    }
}