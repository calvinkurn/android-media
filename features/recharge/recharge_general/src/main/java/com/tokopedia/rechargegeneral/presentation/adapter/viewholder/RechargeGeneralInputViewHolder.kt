package com.tokopedia.rechargegeneral.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import java.util.regex.Pattern

class RechargeGeneralInputViewHolder(val view: View, val listener: OnInputListener, val isAddSBM: Boolean = false) : AbstractViewHolder<RechargeGeneralProductInput>(view) {

    override fun bind(enquiryData: RechargeGeneralProductInput) {
        val inputView = itemView as TopupBillsInputFieldWidget
        inputView.resetState()
        inputView.setLabel(enquiryData.text)
        inputView.setInputType(enquiryData.style)
        if (!isAddSBM) {
            inputView.isCustomInput = enquiryData.isFavoriteNumber
        }
        // Add delay to reduce tracking events
        inputView.setDelayTextChanged(1000)

        inputView.actionListener = object : TopupBillsInputFieldWidget.ActionListener{
            override fun onFinishInput(input: String) {
                var inputData = input
                // Reset input if it is not valid
                if (inputData.isEmpty() || verifyField(enquiryData.validations, input)) {
                    inputView.hideErrorMessage()
                } else {
                    inputView.setErrorMessage(getString(R.string.input_error_message))
                    inputData = ""
                }

                val isManual = inputView.hasFocus()
                listener.onFinishInput(enquiryData.name, inputData, adapterPosition, isManual)
            }

            // Setup favorite number input
            override fun onCustomInputClick() {
                listener.onCustomInputClick(inputView, enquiryData, position = position)
            }

            override fun onTextChangeInput() {
                listener.onTextChangeInput()
            }
        }
        if (enquiryData.help.isNotEmpty()) {
            inputView.infoListener = object : TopupBillsInputFieldWidget.InfoListener {
                override fun onInfoClick() {
                    listener.onInfoClick(enquiryData.help)
                }
            }
        }

        // Set item data
        if (enquiryData.value.isNotEmpty() && !isAddSBM) {
            inputView.setInputText(enquiryData.value, false)
            listener.onFinishInput(enquiryData.name, enquiryData.value, adapterPosition)
        }
    }

    private fun verifyField(fieldValidation: List<CatalogProductInput.Validation>,
                            input: String): Boolean {
        for (validation in fieldValidation) {
            if (validation.rule.isNotEmpty() && !Pattern.matches(validation.rule, input)) {
                return false
            }
        }
        return true
    }

}