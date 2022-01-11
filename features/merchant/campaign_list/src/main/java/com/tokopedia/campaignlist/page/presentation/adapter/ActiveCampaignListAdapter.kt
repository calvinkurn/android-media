package com.tokopedia.campaignlist.page.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaignlist.databinding.CampaignListItemLayoutBinding
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder.OnShareButtonClickListener

class ActiveCampaignListAdapter(
    private val clickListener: OnShareButtonClickListener
) : RecyclerView.Adapter<ActiveCampaignViewHolder>() {

    private var activeCampaignList: List<ActiveCampaign> = listOf()
    private var onImpressed : (ActiveCampaign) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveCampaignViewHolder {
        val binding = CampaignListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveCampaignViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ActiveCampaignViewHolder, position: Int) {
        holder.bindData(activeCampaignList[position], onImpressed)
    }

    override fun getItemCount(): Int {
        return activeCampaignList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setActiveCampaignList(activeCampaignList: List<ActiveCampaign>) {
        this.activeCampaignList = activeCampaignList
        notifyDataSetChanged()
    }

    fun setCampaignImpression(onImpressed : (ActiveCampaign) -> Unit) {
        this.onImpressed = onImpressed
    }
}