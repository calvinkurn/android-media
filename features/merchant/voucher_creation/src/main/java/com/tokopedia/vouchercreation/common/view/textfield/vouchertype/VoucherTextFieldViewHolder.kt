package com.tokopedia.vouchercreation.common.view.textfield.vouchertype

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import kotlinx.android.synthetic.main.mvc_textfield.view.*

class VoucherTextFieldViewHolder(itemView: View) : AbstractViewHolder<VoucherTextFieldUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_textfield
    }

    private var minAlertErrorMessage: String = ""
    private var maxAlertErrorMessage: String = ""
    private var extraValidationErrorMessage: String = ""

    override fun bind(element: VoucherTextFieldUiModel) {
        itemView.textField?.run {
            setLabelStatic(true)
            setInputType(InputType.TYPE_CLASS_NUMBER)

            element.labelRes?.let { labelRes ->
                textFiedlLabelText.text = context?.resources?.getString(labelRes).toBlankOrString()
            }

            element.extraValidationRes?.let { errorRes ->
                extraValidationErrorMessage = context?.resources?.getString(errorRes).toBlankOrString()
            }

            element.currentErrorPair?.let { errorPair ->
                setError(errorPair.first)
                setMessage(errorPair.second)
            }

            textFieldInput.run {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        selectAll()
                    }
                }
            }

            when(element.type) {
                VoucherTextFieldType.CURRENCY -> {
                    element.currentValue?.let { value ->
                        if (value > 0) {
                            textFieldInput.setText(CurrencyFormatHelper.convertToRupiah(value.toString()))
                        }
                    }
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
                                        maxValue = element.maxValue,
                                        promotionType = element.promotionType,
                                        onValueChanged = element.onValueChanged,
                                        onSetErrorMessage = element.onSetErrorMessage,
                                        extraValidation = element.extraValidation)
                            }
                        })
                    }
                }
                VoucherTextFieldType.QUANTITY -> {
                    element.currentValue?.let { value ->
                        if(value > 0) {
                            textFieldInput.setText(value.toString())
                        }
                    }
                    minAlertErrorMessage = String.format(context.resources.getString(element.minAlertRes, element.minValue.toString()))
                    maxAlertErrorMessage = String.format(context.resources.getString(element.maxAlertRes, element.maxValue.toString()))

                    textFieldInput.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            val value = s.toString().toIntOrZero()
                            this@run.validateValue(
                                    textFieldType = VoucherTextFieldType.QUANTITY,
                                    currentValue = value,
                                    minValue = element.minValue,
                                    maxValue = element.maxValue,
                                    promotionType = element.promotionType,
                                    onValueChanged = element.onValueChanged,
                                    onSetErrorMessage = element.onSetErrorMessage,
                                    extraValidation = element.extraValidation)
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })
                }
                VoucherTextFieldType.PERCENTAGE -> {
                    element.currentValue?.let { value ->
                        if(value > 0) {
                            textFieldInput.setText(value.toString())
                        }
                    }
                    minAlertErrorMessage = "${String.format(context.resources.getString(element.minAlertRes, element.minValue.toString()))}%"
                    maxAlertErrorMessage = "${String.format(context.resources.getString(element.maxAlertRes, element.maxValue.toString()))}%"

                    appendText(context.resources.getString(R.string.mvc_percent).toBlankOrString())
                    textFieldInput.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            val value = s.toString().toIntOrZero()
                            this@run.validateValue(
                                    textFieldType = VoucherTextFieldType.PERCENTAGE,
                                    currentValue = value,
                                    minValue = element.minValue,
                                    maxValue = element.maxValue,
                                    promotionType = element.promotionType,
                                    onValueChanged = element.onValueChanged,
                                    onSetErrorMessage = element.onSetErrorMessage,
                                    extraValidation = element.extraValidation)
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })
                }
            }
        }
    }

    private fun TextFieldUnify.validateValue(textFieldType: VoucherTextFieldType,
                                             currentValue: Int,
                                             minValue: Int,
                                             maxValue: Int,
                                             promotionType: PromotionType,
                                             onValueChanged: (Int?, PromotionType) -> Unit,
                                             onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit,
                                             extraValidation: (Int, String) -> Pair<Boolean, String>) {
        onValueChanged(currentValue, promotionType)
        when {
            currentValue < minValue -> {
                setError(true)
                setMessage(minAlertErrorMessage)
                onSetErrorMessage(true, minAlertErrorMessage, promotionType)
            }
            currentValue > maxValue -> {
                setError(true)
                setMessage(maxAlertErrorMessage)
                onSetErrorMessage(true, maxAlertErrorMessage, promotionType)
            }
            else -> {
                val pairResult = extraValidation(currentValue, extraValidationErrorMessage)
                if (pairResult.first) {
                    setError(false)
                    if (textFieldType == VoucherTextFieldType.QUANTITY) {
                        setMessage(maxAlertErrorMessage)
                        onSetErrorMessage(false, maxAlertErrorMessage, promotionType)
                    } else {
                        setMessage("")
                        onSetErrorMessage(false, "", promotionType)
                    }
                } else {
                    val errorMessage = pairResult.second
                    setError(true)
                    setMessage(errorMessage)
                    onSetErrorMessage(true, errorMessage, promotionType)
                }
            }
        }

    }
}