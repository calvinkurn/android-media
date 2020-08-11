package com.tokopedia.product.addedit.variant.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import kotlinx.android.synthetic.main.add_edit_product_variant_custom_input_layout.view.*
import kotlinx.android.synthetic.main.add_edit_product_variant_custom_input_layout.view.buttonSave

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
        setupTextChangedListener(textWatcher = createTextWatcher())
        setupButtonSaveClickListener(layoutPosition, selectedVariantUnit, variantUnitValues, selectedVariantUnitValues)

        textFieldUnifyCustomValue.textFieldInput.afterTextChanged {
            buttonSave.isEnabled = it.isNotEmpty()
        }
    }

    private fun setupTextChangedListener(textWatcher: TextWatcher) {
        textFieldUnifyCustomValue?.textFieldInput?.addTextChangedListener(textWatcher)
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val input = editable?.toString() ?: ""
                buttonSave.isEnabled = input.isNotBlank()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setupButtonSaveClickListener(layoutPosition: Int,
                                             selectedVariantUnit: Unit,
                                             variantUnitValues: List<UnitValue>,
                                             selectedVariantUnitValues: MutableList<UnitValue>) {
        buttonSave.setOnClickListener {
            val customVariantUnitValueName = textFieldUnifyCustomValue.getText().trim()
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
                onCustomVariantUnitAddListener?.onCustomVariantUnitValueAdded(
                        layoutPosition,
                        selectedVariantUnit,
                        customVariantUnitValue,
                        selectedVariantUnitValues)
            }
        }
    }

    interface OnCustomVariantUnitAddListener {
        fun onCustomVariantUnitValueAdded(layoutPosition: Int,
                                          currentSelectedVariantUnit: Unit,
                                          customVariantUnitValue: UnitValue,
                                          currentSelectedVariantUnitValues: MutableList<UnitValue>)
    }
}