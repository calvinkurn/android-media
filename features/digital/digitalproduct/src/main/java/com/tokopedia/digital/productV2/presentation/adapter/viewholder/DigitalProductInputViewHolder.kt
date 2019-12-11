package com.tokopedia.digital.productV2.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.digital.productV2.model.DigitalProductInput
import java.util.regex.Pattern

class DigitalProductInputViewHolder(val view: View, val listener: OnInputListener) : AbstractViewHolder<DigitalProductInput>(view) {

    override fun bind(enquiryData: DigitalProductInput) {
        val inputView = itemView as TopupBillsInputFieldWidget
        inputView.resetState()
        inputView.setLabel(enquiryData.text)
        inputView.setHint("")
        inputView.setInputType(enquiryData.style)
        inputView.setActionListener(object : TopupBillsInputFieldWidget.ActionListener{
            override fun onFinishInput(input: String) {
                if (verifyField(enquiryData.validations, input)) {
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
        if (input.isEmpty()) return false
        for (validation in fieldValidation) {
            if (validation.rule.isNotEmpty() && !Pattern.matches(validation.rule, input)) {
                return false
            }
        }
        return true
    }

}