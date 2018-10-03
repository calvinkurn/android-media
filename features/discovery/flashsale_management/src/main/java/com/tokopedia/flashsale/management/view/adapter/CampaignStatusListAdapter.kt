package com.tokopedia.product.manage.item.video.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.ekstension.convertIdtoCommaString
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusViewModel
import kotlinx.android.synthetic.main.item_campaign_status.view.*

class CampaignStatusListAdapter(var campaignStatusViewModelList: ArrayList<CampaignStatusViewModel>,
                                val onCampaignStatusListAdapterListener: OnCampaignStatusListAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var selectedCampaignStatusId: String = ""

    interface OnCampaignStatusListAdapterListener {
        fun onCampaignStatusClicked(campaignStatusViewModel: CampaignStatusViewModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.text.text = campaignStatusViewModelList[position].labelName
        holder.itemView.text.isSelected = campaignStatusViewModelList[position].convertIdtoCommaString() == selectedCampaignStatusId
    }

    override fun getItemCount() = campaignStatusViewModelList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_campaign_status, parent, false)

        return CampaignStatusViewHolder(itemLayoutView)
    }

    fun replaceData(campaignStatusViewModelList: ArrayList<CampaignStatusViewModel>) {
        this.campaignStatusViewModelList = campaignStatusViewModelList
        notifyDataSetChanged()
    }


    inner class CampaignStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.text.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position < 0 || position >= campaignStatusViewModelList.size) {
                return
            }
            onCampaignStatusListAdapterListener.onCampaignStatusClicked(campaignStatusViewModelList[position])
            selectedCampaignStatusId = campaignStatusViewModelList[position].convertIdtoCommaString()
            notifyDataSetChanged()
        }
    }
}