package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.databinding.ItemAddSbmInquiryBinding

class AddSmartBillsInquiryAdapter : RecyclerView.Adapter<AddSmartBillsInquiryAdapter.SmartBillsInquiryViewHolder>() {

    var listInquiry = emptyList<TopupBillsEnquiryMainInfo>()

    inner class SmartBillsInquiryViewHolder(
        private val binding: ItemAddSbmInquiryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(inquiry: TopupBillsEnquiryMainInfo) {
            with(binding) {
                tvSbmTitleInquiry.text = inquiry.label
                tvSbmDescInquiry.text = inquiry.value
            }
        }
    }
    override fun getItemCount(): Int {
        return listInquiry.size
    }

    override fun onBindViewHolder(holder: AddSmartBillsInquiryAdapter.SmartBillsInquiryViewHolder, position: Int) {
        holder.bind(listInquiry[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSmartBillsInquiryAdapter.SmartBillsInquiryViewHolder {
        val binding = ItemAddSbmInquiryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmartBillsInquiryViewHolder(binding)
    }
}
