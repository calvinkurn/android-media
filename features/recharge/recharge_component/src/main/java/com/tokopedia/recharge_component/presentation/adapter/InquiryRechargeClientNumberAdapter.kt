package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.recharge_component.databinding.ViewClientNumberWidgetInquiryBinding
import com.tokopedia.recharge_component.presentation.adapter.viewholder.InquiryRechargeClientNumberViewHolder

class InquiryRechargeClientNumberAdapter :  RecyclerView.Adapter<InquiryRechargeClientNumberViewHolder>(){
    private var listMainInfo = mutableListOf<TopupBillsEnquiryMainInfo>()

    override fun getItemCount(): Int = listMainInfo.size

    override fun onBindViewHolder(holder: InquiryRechargeClientNumberViewHolder, position: Int) {
        holder.bind(listMainInfo[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InquiryRechargeClientNumberViewHolder {
        val binding = ViewClientNumberWidgetInquiryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return InquiryRechargeClientNumberViewHolder(binding)
    }

    fun setListMainInfo(listMainInfo: List<TopupBillsEnquiryMainInfo>){
        this.listMainInfo = listMainInfo.toMutableList()
        notifyDataSetChanged()
    }
}