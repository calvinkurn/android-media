package com.tokopedia.rechargegeneral.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import java.util.regex.Pattern

class RechargeGeneralInputViewHolder(val view: View, val listener: OnInputListener) : AbstractViewHolder<RechargeGeneralProductInput>(view) {

    override fun bind(enquiryData: RechargeGeneralProductInput) {
        val inputView = itemView as TopupBillsInputFieldWidget
        inputView.resetState()
        inputView.setLabel(enquiryData.text)
        inputView.setHint("")
        inputView.setInputType(enquiryData.style)
        inputView.setActionListener(object : TopupBillsInputFieldWidget.ActionListener{
            override fun onFinishInput(input: String) {
                if (input.isEmpty() || verifyField(enquiryData.validations, input)) {
                    inputView.hideErrorMessage()
                    listener.onFinishInput(enquiryData.name, input, adapterPosition)
                } else {
                    inputView.setErrorMessage("Input tidak sesuai")
                }
            }

            override fun onCustomInputClick() {

            }
        })

        // Set recent item data
        if (enquiryData.value.isNotEmpty()) inputView.setInputText(enquiryData.value)
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