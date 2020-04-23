package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
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

        private const val MAXIMUM_INPUT_LENGTH = 11
    }

    override fun bind(variant: ProductVariant) {
        itemView.textProductName.text = variant.name
        setupEditTextFieldPrice(variant)
    }

    private fun setupEditTextFieldPrice(variant: ProductVariant) {
        setInputType()
        setupClearIcon()
        setTextFieldPriceMaxLength()
        setTextFieldPriceValue(variant.price)
        setTextFieldPriceListeners(variant.id)
    }

    private fun setupClearIcon() {
        itemView.textFieldPrice.apply {
            setFirstIcon(R.drawable.ic_system_action_close_normal_24)
            getFirstIcon().setOnClickListener {
                textFieldInput.text.clear()
                hideTextFieldPriceError()
            }
        }
    }

    private fun setTextFieldPriceMaxLength() {
        itemView.textFieldPrice.apply {
            val maxLength = LengthFilter(MAXIMUM_INPUT_LENGTH)
            textFieldInput.filters = arrayOf(maxLength)
        }
    }

    private fun setTextFieldPriceValue(price: Int) {
       itemView.textFieldPrice.apply {
           val priceRupiah = CurrencyFormatHelper.convertToRupiah(price.toString())
           val priceTxt = CurrencyFormatHelper.removeCurrencyPrefix(priceRupiah)
           val prefixTxt = itemView.context.getString(R.string.product_manage_quick_edit_currency)
           textFieldInput.setText(priceTxt)
           prependText(prefixTxt)
       }
    }

    private fun setTextFieldPriceListeners(productId: String) {
        itemView.textFieldPrice.apply {
            val idrTextWatcher = CurrencyIdrTextWatcher(textFieldInput)
            textFieldInput.addTextChangedListener(idrTextWatcher)
            textFieldInput.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val input = textFieldInput.text.toString()
                    val price = CurrencyFormatHelper.convertRupiahToInt(input)
                    listener.onPriceChanged(productId, price)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            setOnFocusChangeListener { _, hasFocus ->
               if(hasFocus) {
                   val length = textFieldInput.length()
                   textFieldInput.setSelection(length)
               }
            }
        }
    }

    private fun setInputType() {
        itemView.textFieldPrice.setInputType(InputType.TYPE_CLASS_NUMBER)
    }

    private fun hideTextFieldPriceError() {
        itemView.textFieldPrice.apply {
            setError(false)
            setMessage("")
        }
    }

    interface ProductVariantListener {
        fun onPriceChanged(variantId: String, price: Int)
    }
}