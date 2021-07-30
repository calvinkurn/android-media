package com.tokopedia.smartbills.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.smartbills.R
import kotlinx.android.synthetic.main.view_smart_bills_item_inquiry.view.*

class SmartBillsAdapterItemInquiry: RecyclerView.Adapter<SmartBillsAdapterItemInquiry.SmartBillsInquiryViewHolder>() {

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

    override fun onBindViewHolder(holder: SmartBillsAdapterItemInquiry.SmartBillsInquiryViewHolder, position: Int) {
        holder.bind(listInquiry[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartBillsAdapterItemInquiry.SmartBillsInquiryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_smart_bills_item_inquiry, parent, false)
        return SmartBillsInquiryViewHolder(itemView)
    }
}