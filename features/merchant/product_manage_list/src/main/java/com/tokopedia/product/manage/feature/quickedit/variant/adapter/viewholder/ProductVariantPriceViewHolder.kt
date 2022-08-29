package com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder

import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_PRICE_LENGTH
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_PRICE
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.databinding.ItemProductManageVariantBinding
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.view.binding.viewBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class ProductVariantPriceViewHolder(
    itemView: View,
    private val priceMap: MutableMap<String, Double>,
    private val listener: ProductVariantListener
): AbstractViewHolder<ProductVariant>(itemView) {

    companion object {
        private const val PRICE_DECIMAL_PRECISION = 0
        private const val LOCALE_LANGUAGE_ID = "in"
        private const val LOCALE_COUNTRY_ID = "ID"

        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_variant
    }

    private val binding by viewBinding<ItemProductManageVariantBinding>()
    private val priceDecimalFormatter by lazy(LazyThreadSafetyMode.NONE) {
        createPriceDecimalFormatter()
    }

    private var priceTextWatcher: TextWatcher? = null

    override fun bind(variant: ProductVariant) {
        binding?.textProductName?.text = variant.name
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
        binding?.textFieldPrice?.run {
            setFirstIcon(com.tokopedia.unifyprinciples.R.drawable.ic_system_action_close_normal_24)
            getFirstIcon().setOnClickListener {
                textFieldInput.text.clear()
            }
        }
    }

    private fun setTextFieldPriceMaxLength() {
        binding?.textFieldPrice?.run {
            val maxLength = LengthFilter(MAXIMUM_PRICE_LENGTH)
            textFieldInput.filters = arrayOf(maxLength)
        }
    }

    private fun setTextFieldPriceValue(variant: ProductVariant) {
       binding?.textFieldPrice?.run {
           val price = priceMap.getOrElse(variant.id) { variant.price }
           val priceTxt = priceDecimalFormatter.format(price).toString()
           val prefixTxt = itemView.context.getString(R.string.product_manage_quick_edit_currency)

           prependText(prefixTxt)
           textFieldInput.setText(priceTxt)

           val length = textFieldInput.length()
           textFieldInput.setSelection(length)
       }
    }

    private fun setTextFieldPriceListeners() {
        binding?.textFieldPrice?.run {
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
        binding?.textFieldPrice?.setInputType(InputType.TYPE_CLASS_NUMBER)
        binding?.textFieldPrice?.textFieldInput?.imeOptions = EditorInfo.IME_NULL
    }

    private fun showMinPriceError() {
        binding?.textFieldPrice?.run {
           context?.let {
               val message = it.getString(R.string.product_manage_quick_edit_min_price_error)
               setMessage(message)
               setError(true)
           }
        }
    }

    private fun hidePriceError() {
        binding?.textFieldPrice?.run {
            setMessage("")
            setError(false)
        }
    }

    private fun setPriceTextWatcher(variant: ProductVariant) {
        binding?.textFieldPrice?.run {
            priceTextWatcher = object: TextWatcher {
                override fun afterTextChanged(input: Editable) {
                    val price = CurrencyFormatHelper.convertRupiahToDouble(input.toString())
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
        binding?.textFieldPrice?.run {
            textFieldInput.removeTextChangedListener(priceTextWatcher)
        }
    }

    private fun showHidePriceError(price: Double) {
        if (price < MINIMUM_PRICE) {
            showMinPriceError()
        } else {
            hidePriceError()
        }
    }

    private fun createPriceDecimalFormatter(): NumberFormat {
        return DecimalFormat.getInstance(Locale(LOCALE_LANGUAGE_ID, LOCALE_COUNTRY_ID)).apply {
            maximumFractionDigits = PRICE_DECIMAL_PRECISION
        }
    }

    interface ProductVariantListener {
        fun onPriceChanged(variantId: String, price: Double)
    }
}