package com.tokopedia.recharge_component.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.recharge_component.databinding.ViewClientNumberWidgetInquiryBinding


class InquiryRechargeClientNumberViewHolder(
    private val binding: ViewClientNumberWidgetInquiryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(element: TopupBillsEnquiryMainInfo) {
        with(binding) {
           tgClientNumberWidgetInquiryTitle.text = element.label
            tgClientNumberWidgetInquiryValue.text = element.value
        }
    }
}