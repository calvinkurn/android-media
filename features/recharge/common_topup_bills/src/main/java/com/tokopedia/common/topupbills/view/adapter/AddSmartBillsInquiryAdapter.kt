package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import kotlinx.android.synthetic.main.item_add_sbm_inquiry.view.*

class AddSmartBillsInquiryAdapter: RecyclerView.Adapter<AddSmartBillsInquiryAdapter.SmartBillsInquiryViewHolder>() {

    var listInquiry = emptyList<TopupBillsEnquiryMainInfo>()

    inner class SmartBillsInquiryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(inquiry: TopupBillsEnquiryMainInfo) {
            with(itemView) {
                tv_sbm_title_inquiry.text = inquiry.label
                tv_sbm_desc_inquiry.text = inquiry.value
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_add_sbm_inquiry, parent, false)
        return SmartBillsInquiryViewHolder(itemView)
    }
}