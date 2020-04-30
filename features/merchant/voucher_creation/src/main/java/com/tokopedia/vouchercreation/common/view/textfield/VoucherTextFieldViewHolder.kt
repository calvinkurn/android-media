package com.tokopedia.vouchercreation.common.view.textfield

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.mvc_textfield.view.*

class VoucherTextFieldViewHolder(itemView: View) : AbstractViewHolder<VoucherTextFieldUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_textfield
    }

    private var minAlertErrorMessage: String = ""
    private var maxAlertErrorMessage: String = ""

    override fun bind(element: VoucherTextFieldUiModel) {
        itemView.textField?.run {
            setLabelStatic(true)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            textFieldInput.text.clear()

            element.labelRes?.let { labelRes ->
                textFiedlLabelText.text = context.resources.getString(labelRes).toBlankOrString()
            }

            when(element.type) {
                VoucherTextFieldType.CURRENCY -> {
                    minAlertErrorMessage = String.format(context.resources.getString(element.minAlertRes, CurrencyFormatHelper.convertToRupiah(element.minValue.toString())))
                    maxAlertErrorMessage = String.format(context.resources.getString(element.maxAlertRes, CurrencyFormatHelper.convertToRupiah(element.maxValue.toString())))

                    prependText(context.resources.getString(R.string.mvc_rp).toBlankOrString())
                    textFieldInput.let { editText ->
                        editText.addTextChangedListener(object : NumberTextWatcher(editText) {
                            override fun onNumberChanged(number: Double) {
                                super.onNumberChanged(number)
                                this@run.validateValue(
                                        textFieldType = VoucherTextFieldType.CURRENCY,
                                        currentValue = number.toInt(),
                                        minValue = element.minValue,
                                        maxValue = element.maxValue)
                            }
                        })
                    }
                }
                VoucherTextFieldType.QUANTITY -> {
                    minAlertErrorMessage = String.format(context.resources.getString(element.minAlertRes, element.minValue.toString()))
                    maxAlertErrorMessage = String.format(context.resources.getString(element.maxAlertRes, element.maxValue.toString()))

                    textFieldInput.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            this@run.validateValue(
                                    textFieldType = VoucherTextFieldType.QUANTITY,
                                    currentValue = s.toString().toIntOrZero(),
                                    minValue = element.minValue,
                                    maxValue = element.maxValue)
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })
                }
                VoucherTextFieldType.PERCENTAGE -> {
                    minAlertErrorMessage = "${String.format(context.resources.getString(element.minAlertRes, element.minValue.toString()))}%"
                    maxAlertErrorMessage = "${String.format(context.resources.getString(element.maxAlertRes, element.maxValue.toString()))}%"

                    appendText(context.resources.getString(R.string.mvc_percent).toBlankOrString())
                    textFieldInput.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            this@run.validateValue(
                                    textFieldType = VoucherTextFieldType.PERCENTAGE,
                                    currentValue = s.toString().toIntOrZero(),
                                    minValue = element.minValue,
                                    maxValue = element.maxValue)
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })
                }
            }
            if (element.isLastTextField) {
                textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
            }
        }
    }

    private fun TextFieldUnify.validateValue(textFieldType: VoucherTextFieldType,
                                             currentValue: Int,
                                             minValue: Int,
                                             maxValue: Int) {
        when {
            currentValue < minValue -> {
                setError(true)
                setMessage(minAlertErrorMessage)
            }
            currentValue > maxValue -> {
                setError(true)
                setMessage(maxAlertErrorMessage)
            }
            else -> {
                setError(false)
                if (textFieldType == VoucherTextFieldType.QUANTITY) {
                    setMessage(maxAlertErrorMessage)
                } else {
                    setMessage("")
                }
            }
        }

    }
}