package com.tokopedia.rechargegeneral.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment.Companion.INPUT_TYPE_FAVORITE_NUMBER
import java.util.regex.Pattern

class RechargeGeneralInputViewHolder(val view: View, val listener: OnInputListener) : AbstractViewHolder<RechargeGeneralProductInput>(view) {

    override fun bind(enquiryData: RechargeGeneralProductInput) {
        val inputView = itemView as TopupBillsInputFieldWidget
        inputView.resetState()
        inputView.setLabel(enquiryData.text)
        inputView.setHint("")
        if (enquiryData.style == INPUT_TYPE_FAVORITE_NUMBER) {
            inputView.isCustomInput = true
            inputView.setInputType(TopupBillsInputFieldWidget.INPUT_NUMERIC)
        } else {
            inputView.isCustomInput = false
            inputView.setInputType(enquiryData.style)
        }

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
                listener.onFinishInput(enquiryData.name, inputData, adapterPosition)
            }

            // Setup favorite number input
            override fun onCustomInputClick() {
                listener.onCustomInputClick(inputView, adapterPosition)
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
        if (enquiryData.value.isNotEmpty()) {
            inputView.setInputText(enquiryData.value)
            // Hide dropdown icon
            inputView.toggleDropdownIcon(false)
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