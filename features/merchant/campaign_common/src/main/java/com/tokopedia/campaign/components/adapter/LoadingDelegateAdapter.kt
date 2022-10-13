package com.tokopedia.campaign.components.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.ItemLoadingCampaignBinding
import com.tokopedia.campaign.entity.LoadingItem
import com.tokopedia.kotlin.extensions.view.visible

class LoadingDelegateAdapter : DelegateAdapter<LoadingItem, LoadingDelegateAdapter.LoadingViewHolder>(
    LoadingItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemLoadingCampaignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadingViewHolder(binding)
    }

    override fun bindViewHolder(item: LoadingItem, viewHolder: LoadingViewHolder) {
        viewHolder.bind()
    }

    inner class LoadingViewHolder(private val binding : ItemLoadingCampaignBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.loader.visible()
        }
    }

}