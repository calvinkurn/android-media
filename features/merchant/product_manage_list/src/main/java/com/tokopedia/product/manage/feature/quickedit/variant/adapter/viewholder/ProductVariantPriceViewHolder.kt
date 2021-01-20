package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_PRICE_LENGTH
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_PRICE
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.item_product_manage_variant.view.*

class ProductVariantPriceViewHolder(
    itemView: View,
    private val priceMap: MutableMap<String, Int>,
    private val listener: ProductVariantListener
): AbstractViewHolder<ProductVariant>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_variant
    }

    private var priceTextWatcher: TextWatcher? = null

    override fun bind(variant: ProductVariant) {
        itemView.textProductName.text = variant.name
        setupEditTextFieldPrice(variant)
    }

    override fun onViewRecycled() {
        removeTextFieldPriceListeners()
        super.onViewRecycled()
    }

    private fun setupEditTextFieldPrice(variant: ProductVariant) {
        setInputType()
        setupClearIcon()
        setPriceTextWatcher(variant)
        setTextFieldPriceMaxLength()
        removeTextFieldPriceListeners()
        setTextFieldPriceValue(variant)
        setTextFieldPriceListeners()
    }

    private fun setupClearIcon() {
        itemView.textFieldPrice.apply {
            setFirstIcon(com.tokopedia.unifyprinciples.R.drawable.ic_system_action_close_normal_24)
            getFirstIcon().setOnClickListener {
                textFieldInput.text.clear()
            }
        }
    }

    private fun setTextFieldPriceMaxLength() {
        itemView.textFieldPrice.apply {
            val maxLength = LengthFilter(MAXIMUM_PRICE_LENGTH)
            textFieldInput.filters = arrayOf(maxLength)
        }
    }

    private fun setTextFieldPriceValue(variant: ProductVariant) {
       itemView.textFieldPrice.apply {
           val price = priceMap.getOrElse(variant.id, { variant.price }).toString()
           val priceRupiah = CurrencyFormatHelper.convertToRupiah(price)
           val priceTxt = CurrencyFormatHelper.removeCurrencyPrefix(priceRupiah)
           val prefixTxt = itemView.context.getString(R.string.product_manage_quick_edit_currency)

           prependText(prefixTxt)
           textFieldInput.setText(priceTxt)

           val length = textFieldInput.length()
           textFieldInput.setSelection(length)
       }
    }

    private fun setTextFieldPriceListeners() {
        itemView.textFieldPrice.apply {
            textFieldInput.addTextChangedListener(priceTextWatcher)

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

    private fun showMinPriceError() {
        itemView.textFieldPrice.apply {
           context?.let {
               val message = it.getString(R.string.product_manage_quick_edit_min_price_error)
               setMessage(message)
               setError(true)
           }
        }
    }

    private fun hidePriceError() {
        itemView.textFieldPrice.apply {
            setMessage("")
            setError(false)
        }
    }

    private fun setPriceTextWatcher(variant: ProductVariant) {
        itemView.textFieldPrice.run {
            priceTextWatcher = object: TextWatcher {
                override fun afterTextChanged(input: Editable) {
                    val price = CurrencyFormatHelper.convertRupiahToInt(input.toString())
                    priceMap[variant.id] = price

                    removeTextFieldPriceListeners()
                    CurrencyFormatHelper.setToRupiahCheckPrefix(textFieldInput)
                    setTextFieldPriceListeners()
                    showHidePriceError(price)

                    listener.onPriceChanged(variant.id, price)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }
        }
    }

    private fun removeTextFieldPriceListeners() {
        itemView.textFieldPrice.apply {
            textFieldInput.removeTextChangedListener(priceTextWatcher)
        }
    }

    private fun showHidePriceError(price: Int) {
        if (price < MINIMUM_PRICE) {
            showMinPriceError()
        } else {
            hidePriceError()
        }
    }

    interface ProductVariantListener {
        fun onPriceChanged(variantId: String, price: Int)
    }
}