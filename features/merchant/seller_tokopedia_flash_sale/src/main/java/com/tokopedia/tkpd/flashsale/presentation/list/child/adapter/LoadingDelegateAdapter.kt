package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemLoadingBinding
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.LoadingItem

class LoadingDelegateAdapter : DelegateAdapter<LoadingItem, LoadingDelegateAdapter.LoadingViewHolder>(
    LoadingItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StfsItemLoadingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadingViewHolder(binding)
    }

    override fun bindViewHolder(item: LoadingItem, viewHolder: LoadingViewHolder) {
        viewHolder.bind()
    }

    inner class LoadingViewHolder(private val binding : StfsItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.loader.visible()
        }

    }

}
