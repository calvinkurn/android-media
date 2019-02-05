package com.tokopedia.flashsale.management.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.campaignlabel.CampaignStatus
import com.tokopedia.flashsale.management.ekstension.convertIdtoCommaString
import kotlinx.android.synthetic.main.item_campaign_status.view.*

class CampaignStatusListAdapter(val campaignStatusList: MutableList<CampaignStatus> = mutableListOf(),
                                private val onSelected: (String) -> Unit) : RecyclerView.Adapter<CampaignStatusListAdapter.CampaignStatusViewHolder>(){

    private var selectedCampaignStatusId: String = ""

    override fun onBindViewHolder(holder: CampaignStatusViewHolder, position: Int) {
        holder.bind(campaignStatusList[position])
    }

    override fun getItemCount() = campaignStatusList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignStatusViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_campaign_status, parent, false)

        return CampaignStatusViewHolder(itemLayoutView)
    }

    fun replaceData(campaignStatusViewModelList: List<CampaignStatus>) {
        this.campaignStatusList.clear()
        this.campaignStatusList.addAll(campaignStatusViewModelList)
        notifyDataSetChanged()
    }

    fun resetFilter(){
        selectedCampaignStatusId = ""
        notifyDataSetChanged()
    }


    inner class CampaignStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.text.setOnClickListener(this)
        }

        fun bind(data: CampaignStatus){
            itemView.text.text = data.name
            itemView.text.isSelected = selectedCampaignStatusId.equals(data.convertIdtoCommaString())
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position < 0 || position >= itemCount) {
                return
            }

            if (selectedCampaignStatusId.equals(campaignStatusList[position].convertIdtoCommaString())){
                selectedCampaignStatusId = ""
            } else {
                selectedCampaignStatusId = campaignStatusList[position].convertIdtoCommaString()
            }
            onSelected(selectedCampaignStatusId)
            notifyDataSetChanged()
        }
    }
}