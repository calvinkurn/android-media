package com.tokopedia.flashsale.management.product.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.ekstension.convertIdtoCommaString
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusViewModel
import kotlinx.android.synthetic.main.item_campaign_status.view.*

class SellerStatusListAdapter(var modelList: ArrayList<String>,
                              val onSellerStatusListAdapterListener: OnSellerStatusListAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var selectedStatusId: String = ""

    interface OnSellerStatusListAdapterListener {
        fun onStatusClicked(string: String)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.text.text = modelList[position]
        holder.itemView.text.isSelected = selectedStatusId.equals(modelList[position])
    }

    override fun getItemCount() = modelList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_seller_status, parent, false)

        return CampaignStatusViewHolder(itemLayoutView)
    }

    fun setData(stringList: ArrayList<String>) {
        this.modelList = stringList
        notifyDataSetChanged()
    }


    inner class CampaignStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.text.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position < 0 || position >= modelList.size) {
                return
            }
            onSellerStatusListAdapterListener.onStatusClicked(modelList[position])
            selectedStatusId = modelList[position]
            notifyDataSetChanged()
        }
    }
}