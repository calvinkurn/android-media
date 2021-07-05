package com.tokopedia.vouchercreation.common.view.textfield.vouchertype

import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
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

    override fun bind(element: VoucherTextFieldUiModel) {
        itemView.mvcInputTextField?.run {
            // Fix blank color when dark mode activated.
            textFiedlLabelText.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
            textFieldInput.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700))

            setLabelStatic(true)
            setInputType(InputType.TYPE_CLASS_NUMBER)

            element.labelRes?.let { labelRes ->
                textFiedlLabelText.text = context?.resources?.getString(labelRes).toBlankOrString()
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
                filters = arrayOf(InputFilter.LengthFilter(element.type.maxLength))
                imeOptions = EditorInfo.IME_ACTION_DONE
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
                }
                VoucherTextFieldType.QUANTITY -> {
                    element.currentValue?.let { value ->
                        if(value > 0) {
                            textFieldInput.setText(value.toString())
                        }
                    }
                    minAlertErrorMessage = String.format(context.resources.getString(element.minAlertRes, element.minValue.toString()))
                    maxAlertErrorMessage = String.format(context.resources.getString(element.maxAlertRes, element.maxValue.toString()))

                    setError(false)
                    setMessage(maxAlertErrorMessage)
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
                }
            }

            textFieldInput.let { editText ->
                editText.addTextChangedListener(object : NumberTextWatcher(editText) {
                    override fun onNumberChanged(number: Double) {
                        super.onNumberChanged(number)
                        this@run.validateValue(
                                textFieldType = element.type,
                                currentValue = number.toInt(),
                                minValue = element.minValue,
                                maxValue = element.maxValue,
                                promotionType = element.promotionType,
                                onValueChanged = element.onValueChanged,
                                onSetErrorMessage = element.onSetErrorMessage)
                    }
                })
            }
        }
    }

    private fun TextFieldUnify.validateValue(textFieldType: VoucherTextFieldType,
                                             currentValue: Int,
                                             minValue: Int,
                                             maxValue: Int,
                                             promotionType: PromotionType,
                                             onValueChanged: (Int?, PromotionType) -> Unit,
                                             onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit) {
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
                setError(false)
                if (textFieldType == VoucherTextFieldType.QUANTITY) {
                    setMessage(maxAlertErrorMessage)
                    onSetErrorMessage(false, maxAlertErrorMessage, promotionType)
                } else {
                    setMessage("")
                    onSetErrorMessage(false, "", promotionType)
                }
            }
        }

    }
}