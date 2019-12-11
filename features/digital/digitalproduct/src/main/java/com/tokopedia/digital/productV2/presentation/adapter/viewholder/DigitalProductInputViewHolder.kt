package com.tokopedia.digital.productV2.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.digital.productV2.model.DigitalProductInput

class DigitalProductInputViewHolder(val view: View, val listener: OnInputListener) : AbstractViewHolder<DigitalProductInput>(view) {

    override fun bind(enquiryData: DigitalProductInput) {
        val inputView = itemView as TopupBillsInputFieldWidget
        inputView.resetState()
        inputView.setLabel(enquiryData.text)
        inputView.setHint("")
        inputView.setInputType(enquiryData.style)
        inputView.setActionListener(object : TopupBillsInputFieldWidget.ActionListener{
            override fun onFinishInput(input: String) {
                listener.onFinishInput(input, adapterPosition)
            }

            override fun onCustomInputClick() {

            }
        })
    }

}