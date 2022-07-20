package com.tokopedia.campaignlist.page.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaignlist.databinding.CampaignTypeItemLayoutBinding
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.campaignlist.page.presentation.viewholder.CampaignTypeViewHolder

@SuppressLint("NotifyDataSetChanged")
class CampaignTypeListAdapter :
        RecyclerView.Adapter<CampaignTypeViewHolder>(),
        CampaignTypeViewHolder.OnListItemClickListener {

    private var campaignTypeSelections: List<CampaignTypeSelection> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignTypeViewHolder {
        val binding = CampaignTypeItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CampaignTypeViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CampaignTypeViewHolder, position: Int) {
        holder.bindData(campaignTypeSelections[position])
    }

    override fun getItemCount(): Int {
        return campaignTypeSelections.size
    }

    override fun onListItemClicked(position: Int) {
        val selectedCampaignType = campaignTypeSelections[position]
        campaignTypeSelections.filter { campaignTypeSelection ->
            campaignTypeSelection.campaignTypeId != selectedCampaignType.campaignTypeId
        }.forEach { selection -> selection.isSelected = false }
        campaignTypeSelections[position].isSelected = true
        notifyDataSetChanged()
    }

    fun setCampaignTypeSelections(campaignTypeSelections: List<CampaignTypeSelection>) {
        this.campaignTypeSelections = campaignTypeSelections
        notifyDataSetChanged()
    }

    fun getSelectedCampaignType(): CampaignTypeSelection? {
        val selectedCampaignType = campaignTypeSelections.find { campaignTypeSelection ->
            campaignTypeSelection.isSelected
        }
        return selectedCampaignType
    }
}