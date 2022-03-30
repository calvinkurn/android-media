package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.setModeToNumberInput
import com.tokopedia.product.addedit.common.util.setRecyclerViewEditorActionListener
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailFieldsUiModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify

class VariantDetailFieldsViewHolder(
    itemView: View?,
    variantDetailFieldsViewHolderListener: VariantDetailFieldsViewHolderListener
): AbstractViewHolder<VariantDetailFieldsUiModel>(itemView) {

    interface VariantDetailFieldsViewHolderListener {
        fun onStatusSwitchChanged(isChecked: Boolean, adapterPosition: Int)
        fun onPriceInputTextChanged(priceInput: String, adapterPosition: Int): VariantDetailInputLayoutModel
        fun onStockInputTextChanged(stockInput: String, adapterPosition: Int): VariantDetailInputLayoutModel
        fun onSkuInputTextChanged(skuInput: String, adapterPosition: Int)
        fun onWeightInputTextChanged(weightInput: String, adapterPosition: Int): VariantDetailInputLayoutModel
    }

    private var unitValueLabel: AppCompatTextView? = null
    private var statusSwitch: SwitchUnify? = null
    private var priceField: TextFieldUnify? = null
    private var stockField: TextFieldUnify? = null
    private var skuField: TextFieldUnify? = null
    private var weightField: TextFieldUnify? = null

    private var visitablePosition = 0
    private var isPriceFieldEdited = false

    init {
        unitValueLabel = itemView?.findViewById(R.id.tv_unit_value_label)
        statusSwitch = itemView?.findViewById(R.id.su_variant_status)
        priceField = itemView?.findViewById(R.id.tfu_price_field)
        stockField = itemView?.findViewById(R.id.tfu_stock_field)
        skuField = itemView?.findViewById(R.id.tfu_sku_field)
        weightField = itemView?.findViewById(R.id.tfu_weight_field)

        // handle action listener for null view error handling
        priceField?.setRecyclerViewEditorActionListener()
        stockField?.setRecyclerViewEditorActionListener()
        skuField?.setRecyclerViewEditorActionListener()
        weightField?.setRecyclerViewEditorActionListener()

        // setup listeners
        setupPriceFieldListener(variantDetailFieldsViewHolderListener)
        setupStockFieldListener(variantDetailFieldsViewHolderListener)
        setupWeightFieldListener(variantDetailFieldsViewHolderListener)
        setupSkuFieldListener(variantDetailFieldsViewHolderListener)
        setupStatusSwitchListener(variantDetailFieldsViewHolderListener)
    }

    private fun setupPriceFieldListener(variantDetailFieldsViewHolderListener: VariantDetailFieldsViewHolderListener) {
        priceField?.setModeToNumberInput()
        priceField?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                isPriceFieldEdited = start != after
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (count.isMoreThanZero() && isPriceFieldEdited) {
                    // clean any kind of number formatting here
                    val priceInput = charSequence?.toString()?.replace(".", "") ?: ""
                    // remove scientific notation e.g. 20E7
                    priceInput.format("%f")
                    // handle the price input
                    val validatedInputModel = variantDetailFieldsViewHolderListener.onPriceInputTextChanged(priceInput, visitablePosition)
                    priceField?.setError(validatedInputModel.isPriceError)
                    priceField?.setMessage(validatedInputModel.priceFieldErrorMessage)
                } else if (count.isMoreThanZero() && !isPriceFieldEdited) {
                    // handle the price input if field is cleared
                    val validatedInputModel = variantDetailFieldsViewHolderListener.onPriceInputTextChanged(charSequence.toString(), visitablePosition)
                    priceField?.setError(validatedInputModel.isPriceError)
                    priceField?.setMessage(validatedInputModel.priceFieldErrorMessage)
                }
            }
        })
    }

    private fun setupStockFieldListener(variantDetailFieldsViewHolderListener: VariantDetailFieldsViewHolderListener) {
        stockField?.setModeToNumberInput()
        stockField?.textFieldInput?.doOnTextChanged { text, _, count, _ ->
            if (count.isMoreThanZero()) {
                val stockInput = text.toString().replace(".", "")
                stockInput.format("%f")
                val validatedInputModel = variantDetailFieldsViewHolderListener
                    .onStockInputTextChanged(stockInput, visitablePosition)
                stockField?.setError(validatedInputModel.isStockError)
                stockField?.setMessage(validatedInputModel.stockFieldErrorMessage)
            }
        }
    }

    private fun setupWeightFieldListener(variantDetailFieldsViewHolderListener: VariantDetailFieldsViewHolderListener) {
        weightField?.setModeToNumberInput()
        weightField?.textFieldInput?.doOnTextChanged { text, _, count, _ ->
            if (count.isMoreThanZero()) {
                val weightInput = text.toString().replace(".", "")
                weightInput.format("%f")
                val validatedInputModel = variantDetailFieldsViewHolderListener
                    .onWeightInputTextChanged(weightInput, visitablePosition)
                weightField?.setError(validatedInputModel.isWeightError)
                weightField?.setMessage(validatedInputModel.weightFieldErrorMessage)
            }
        }
    }

    private fun setupSkuFieldListener(variantDetailFieldsViewHolderListener: VariantDetailFieldsViewHolderListener) {
        skuField?.textFieldInput?.doOnTextChanged { text, _, count, _ ->
            if (count.isMoreThanZero()) {
                variantDetailFieldsViewHolderListener.onSkuInputTextChanged(text.toString(), visitablePosition)
            }
        }
    }

    private fun setupStatusSwitchListener(variantDetailFieldsViewHolderListener: VariantDetailFieldsViewHolderListener) {
        statusSwitch?.setOnClickListener {
            val isChecked = statusSwitch?.isChecked ?: false
            variantDetailFieldsViewHolderListener.onStatusSwitchChanged(isChecked, visitablePosition)
        }
    }

    private fun setSkuFieldVisibility(isVisible: Boolean) {
        skuField?.isVisible = isVisible
        weightField?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = if (isVisible) R.id.guideline else priceField?.id.orZero()
        }
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
            stockField?.textFieldInput?.setText(variantDetailInputLayoutModel.stock?.toString().orEmpty())
            stockField?.setError(variantDetailInputLayoutModel.isStockError)
            stockField?.setMessage(variantDetailInputLayoutModel.stockFieldErrorMessage)
            skuField?.textFieldInput?.setText(variantDetailInputLayoutModel.sku)
            weightField?.textFieldInput?.setText(variantDetailInputLayoutModel.weight?.toString().orEmpty())
            weightField?.setError(variantDetailInputLayoutModel.isWeightError)
            weightField?.setMessage(variantDetailInputLayoutModel.weightFieldErrorMessage)
            // show / hide sku field
            setSkuFieldVisibility(variantDetailInputLayoutModel.isSkuFieldVisible)
            // enable / disable priceField
            priceField?.textFieldInput?.isEnabled = variantDetailInputLayoutModel.priceEditEnabled
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_variant_detail_fields_layout
    }
}