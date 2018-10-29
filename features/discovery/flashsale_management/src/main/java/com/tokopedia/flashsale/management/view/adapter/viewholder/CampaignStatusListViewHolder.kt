package com.tokopedia.flashsale.management.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.campaignlabel.CampaignStatus
import com.tokopedia.flashsale.management.ekstension.toCampaignStatusViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusListViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusViewModel
import com.tokopedia.flashsale.management.view.adapter.CampaignStatusListAdapter
import kotlinx.android.synthetic.main.item_list_campaign_status.view.*

class CampaignStatusListViewHolder(itemView: View,
                                   private val onCampaignStatusListViewHolderListener: OnCampaignStatusListViewHolderListener) : AbstractViewHolder<CampaignStatusListViewModel>(itemView), CampaignStatusListAdapter.OnCampaignStatusListAdapterListener{

    private val campaignStatusListAdapter: CampaignStatusListAdapter by lazy { CampaignStatusListAdapter(ArrayList(), this) }

    interface OnCampaignStatusListViewHolderListener {
        fun onCampaignStatusClicked(campaignStatusViewModel: CampaignStatusViewModel)
    }

    init {
        val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        itemView.recycler_view.layoutManager = layoutManager
        val animator = itemView.recycler_view.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        itemView.recycler_view.adapter = campaignStatusListAdapter
    }

    override fun bind(campaignStatusListViewModel: CampaignStatusListViewModel) {
        val listCampaignStatusViewModel = ArrayList<CampaignStatusViewModel>()
        for(campaignStatus : CampaignStatus in campaignStatusListViewModel.campaignStatusList){
            listCampaignStatusViewModel.add(campaignStatus.toCampaignStatusViewModel())
        }
        campaignStatusListAdapter.replaceData(listCampaignStatusViewModel)
    }

    override fun onCampaignStatusClicked(campaignStatusViewModel: CampaignStatusViewModel) {
        onCampaignStatusListViewHolderListener.onCampaignStatusClicked(campaignStatusViewModel)
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_list_campaign_status
    }
}
