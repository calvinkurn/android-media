package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.text.InputFilter
import android.text.InputType
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.CurrencyIdrTextWatcher
import kotlinx.android.synthetic.main.item_product_manage_variant.view.*

class ProductVariantViewHolder(itemView: View): AbstractViewHolder<ProductVariant>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_variant

        private const val MAXIMUM_EDIT_PRICE_INPUT_LENGTH = 11
    }

    override fun bind(variant: ProductVariant) {
        itemView.name.text = variant.name
        setupEditPriceTextField(variant.price)
    }

    private fun setupEditPriceTextField(price: Int) {
        itemView.priceTextField.apply {
            setupClearIcon()
            setPriceTextFieldMaxLength()
            setPriceTextFieldListeners()
            setPriceTextFieldValue(price)
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
        textFieldInput.setText(priceTxt)
    }

    private fun TextFieldUnify.setPriceTextFieldListeners() {
        val idrTextWatcher = CurrencyIdrTextWatcher(textFieldInput)
        textFieldInput.addTextChangedListener(idrTextWatcher)
    }

    private fun TextFieldUnify.hidePriceTextFieldError() {
        setError(false)
        setMessage("")
    }
}