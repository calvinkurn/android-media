package com.tokopedia.product.addedit.variant.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import kotlinx.android.synthetic.main.add_edit_product_variant_custom_input_layout.view.*

class CustomVariantUnitValueForm : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, layoutPosition: Int, variantUnitValues: List<UnitValue>,
                onCustomVariantUnitAddListener: OnCustomVariantUnitAddListener) : super(context) {
        inflateVariantCustomInputLayout(context)
        this.layoutPosition = layoutPosition
        this.variantUnitValues = variantUnitValues
        this.onCustomVariantUnitAddListener = onCustomVariantUnitAddListener
    }

    private var layoutPosition: Int = 0
    private var variantUnitValues: List<UnitValue> = listOf()
    private var onCustomVariantUnitAddListener: OnCustomVariantUnitAddListener? = null

    private fun inflateVariantCustomInputLayout(context: Context) {
        View.inflate(context, R.layout.add_edit_product_variant_custom_input_layout, this)
    }

    fun setupVariantCustomInputLayout(selectedVariantUnit: Unit, selectedVariantUnitValues: MutableList<UnitValue>) {
        setupButtonSaveClickListener(layoutPosition, selectedVariantUnit, variantUnitValues, selectedVariantUnitValues)
    }

    @SuppressLint("DefaultLocale")
    private fun setupButtonSaveClickListener(layoutPosition: Int,
                                             selectedVariantUnit: Unit,
                                             variantUnitValues: List<UnitValue>,
                                             selectedVariantUnitValues: MutableList<UnitValue>) {
        buttonSave.setOnClickListener {
            val customVariantUnitValueName = textFieldUnifyCustomValue.getText()
            val isVariantUnitValueExist = variantUnitValues.any { variantUnitValue ->
                variantUnitValue.value.toLowerCase() == customVariantUnitValueName.toLowerCase()
            }
            if (isVariantUnitValueExist) {
                val errorMessage = context.getString(R.string.error_variant_exist)
                textFieldUnifyCustomValue.setMessage(errorMessage)
                textFieldUnifyCustomValue.setError(true)
                return@setOnClickListener
            } else {
                val customVariantUnitValue = UnitValue(value = customVariantUnitValueName)
                onCustomVariantUnitAddListener?.onCustomVariantUnitValueAdded(layoutPosition, selectedVariantUnit, customVariantUnitValue, selectedVariantUnitValues)
            }
        }
    }

    interface OnCustomVariantUnitAddListener {
        fun onCustomVariantUnitValueAdded(layoutPosition: Int,
                                          selectedVariantUnit: Unit,
                                          customVariantUnitValue: UnitValue,
                                          currentSelectedVariantUnitValues: MutableList<UnitValue>)
    }
}