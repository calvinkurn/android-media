package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailFieldsUiModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import java.text.NumberFormat
import java.util.*

class VariantDetailFieldsViewHolder(itemView: View?,
                                    onStatusSwitchCheckedChangeListener: OnStatusSwitchCheckedChangeListener,
                                    onPriceInputTextChangedListener: OnPriceInputTextChangedListener,
                                    onStockInputTextChangedListener: OnStockInputTextChangedListener,
                                    onSkuInputTextChangedListener: OnSkuInputTextChangedListener) :
        AbstractViewHolder<VariantDetailFieldsUiModel>(itemView) {

    interface OnStatusSwitchCheckedChangeListener {
        fun onCheckedChanged(isChecked: Boolean, adapterPosition: Int)
    }

    interface OnPriceInputTextChangedListener {
        fun onPriceInputTextChanged(priceInput: String, adapterPosition: Int): VariantDetailInputLayoutModel
    }

    interface OnStockInputTextChangedListener {
        fun onStockInputTextChanged(stockInput: String, adapterPosition: Int): VariantDetailInputLayoutModel
    }

    interface OnSkuInputTextChangedListener {
        fun onSkuInputTextChanged(skuInput: String, adapterPosition: Int)
    }

    private var unitValueLabel: AppCompatTextView? = null
    private var statusSwitch: SwitchUnify? = null
    private var priceField: TextFieldUnify? = null
    private var stockField: TextFieldUnify? = null
    private var skuField: TextFieldUnify? = null

    private var isRendered = false
    private var visitablePosition = 0
    private var isPriceFieldEdited = false

    init {
        unitValueLabel = itemView?.findViewById(com.tokopedia.product.addedit.R.id.tv_unit_value_label)
        statusSwitch = itemView?.findViewById(com.tokopedia.product.addedit.R.id.su_variant_status)
        priceField = itemView?.findViewById(com.tokopedia.product.addedit.R.id.tfu_price_field)
        stockField = itemView?.findViewById(com.tokopedia.product.addedit.R.id.tfu_stock_field)
        skuField = itemView?.findViewById(com.tokopedia.product.addedit.R.id.tfu_sku_field)

        statusSwitch?.setOnClickListener {
            val isChecked = statusSwitch?.isChecked ?: false
            onStatusSwitchCheckedChangeListener.onCheckedChanged(isChecked, visitablePosition)
        }

        priceField?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                isPriceFieldEdited = start != after
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (isRendered && isPriceFieldEdited) {
                    // clean any kind of number formatting here
                    var priceInput = charSequence?.toString()?.replace(".", "") ?: ""
                    // remove scientific notation e.g. 20E7
                    priceInput.format("%f")
                    // handle the price input
                    val validatedInputModel = onPriceInputTextChangedListener.onPriceInputTextChanged(priceInput, visitablePosition)
                    priceField?.setError(validatedInputModel.isPriceError)
                    priceField?.setMessage(validatedInputModel.priceFieldErrorMessage)
                    // format the price with period delimiter
                    priceField?.textFieldInput?.let {
                        // remove the listener to prevent recursive callback
                        it.removeTextChangedListener(this)
                        // add the period delimiters
                        if (priceInput.isNotBlank()) {
                            priceInput = NumberFormat.getNumberInstance(Locale.US).format(priceInput.toBigDecimal()).replace(",", ".")
                        }
                        // set the text
                        it.setText(priceInput)
                        it.setSelection(priceInput.length)
                        // reset the listener
                        it.addTextChangedListener(this)
                    }
                } else if (isRendered && !isPriceFieldEdited) {
                    // handle the price input if field is cleared
                    val validatedInputModel = onPriceInputTextChangedListener.onPriceInputTextChanged(charSequence.toString(), visitablePosition)
                    priceField?.setError(validatedInputModel.isPriceError)
                    priceField?.setMessage(validatedInputModel.priceFieldErrorMessage)
                }
            }
        })

        stockField?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (isRendered) {
                    // clean any kind of number formatting here
                    var stockInput = charSequence?.toString()?.replace(".", "") ?: ""
                    // remove scientific notation e.g. 20E7
                    stockInput.format("%f")
                    // handle the stock input
                    val validatedInputModel = onStockInputTextChangedListener.onStockInputTextChanged(stockInput, visitablePosition)
                    stockField?.setError(validatedInputModel.isStockError)
                    stockField?.setMessage(validatedInputModel.stockFieldErrorMessage)
                    // format the stock with period delimiter
                    stockField?.textFieldInput?.let {
                        // remove the listener to prevent recursive callback
                        it.removeTextChangedListener(this)
                        // add the period delimiters
                        if (stockInput.isNotBlank()) {
                            stockInput = NumberFormat.getNumberInstance(Locale.US).format(stockInput.toBigDecimal()).replace(",", ".")
                        }
                        // set the text
                        it.setText(stockInput)
                        it.setSelection(stockInput.length)
                        // reset the listener
                        it.addTextChangedListener(this)
                    }
                }
            }
        })

        skuField?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (isRendered) {
                    val skuInput = charSequence?.toString() ?: ""
                    onSkuInputTextChangedListener.onSkuInputTextChanged(skuInput, visitablePosition)
                }
            }
        })
    }

    override fun bind(element: VariantDetailFieldsUiModel?) {
        element?.run {
            val variantDetailInputLayoutModel = this.variantDetailInputLayoutModel
            // update visitable position before bind the data
            visitablePosition = variantDetailInputLayoutModel.visitablePosition
            // render input data to
            unitValueLabel?.text = variantDetailInputLayoutModel.unitValueLabel
            statusSwitch?.isChecked = variantDetailInputLayoutModel.isActive
            priceField?.textFieldInput?.setText(variantDetailInputLayoutModel.price)
            priceField?.setError(variantDetailInputLayoutModel.isPriceError)
            priceField?.setMessage(variantDetailInputLayoutModel.priceFieldErrorMessage)
            stockField?.textFieldInput?.setText(variantDetailInputLayoutModel.stock)
            stockField?.setError(variantDetailInputLayoutModel.isStockError)
            stockField?.setMessage(variantDetailInputLayoutModel.stockFieldErrorMessage)
            skuField?.textFieldInput?.setText(variantDetailInputLayoutModel.sku)
            // show / hide sku field
            if (variantDetailInputLayoutModel.isSkuFieldVisible) skuField?.show()
            else skuField?.hide()
            // enable / disable priceField
            priceField?.textFieldInput?.isEnabled = variantDetailInputLayoutModel.priceEditEnabled
            // flag to prevent exception from
            isRendered = true
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_variant_detail_fields_layout
    }
}