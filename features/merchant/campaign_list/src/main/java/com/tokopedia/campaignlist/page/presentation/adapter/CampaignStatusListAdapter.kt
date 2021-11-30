package com.tokopedia.campaignlist.page.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaignlist.databinding.CampaignStatusItemLayoutBinding
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.viewholder.CampaignStatusViewHolder

@SuppressLint("NotifyDataSetChanged")
class CampaignStatusListAdapter :
        RecyclerView.Adapter<CampaignStatusViewHolder>(),
        CampaignStatusViewHolder.OnListItemClickListener {

    private var campaignStatusSelections: List<CampaignStatusSelection> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignStatusViewHolder {
        val binding = CampaignStatusItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CampaignStatusViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CampaignStatusViewHolder, position: Int) {
        holder.bindData(campaignStatusSelections[position])
    }

    override fun getItemCount(): Int {
        return campaignStatusSelections.size
    }

    override fun onListItemClicked(position: Int) {
        val selectedCampaignStatus = campaignStatusSelections[position]
        campaignStatusSelections.filter { campaignStatusSelection ->
            campaignStatusSelection.statusId != selectedCampaignStatus.statusId
        }.forEach { selection -> selection.isSelected = false }
        campaignStatusSelections[position].isSelected = true
        notifyDataSetChanged()
    }

    fun setCampaignStatusSelections(campaignStatusSelections: List<CampaignStatusSelection>) {
        this.campaignStatusSelections = campaignStatusSelections
        notifyDataSetChanged()
    }

    fun getSelectedCampaignStatus(): CampaignStatusSelection? {
        val selectedCampaignStatus = campaignStatusSelections.find { campaignStatusSelection ->
            campaignStatusSelection.isSelected
        }
        return selectedCampaignStatus
    }
}