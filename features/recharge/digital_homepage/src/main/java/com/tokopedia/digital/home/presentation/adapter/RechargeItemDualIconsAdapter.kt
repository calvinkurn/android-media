package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeTrustmarkItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.media.loader.loadImage

class RechargeItemDualIconsAdapter(val items: List<RechargeHomepageSections.Item>, val listener: RechargeHomepageItemListener) :
    RecyclerView.Adapter<RechargeItemDualIconsAdapter.RechargeItemTrustMarkViewHolder>() {

    override fun onBindViewHolder(viewHolder: RechargeItemTrustMarkViewHolder, position: Int) {
        viewHolder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeItemTrustMarkViewHolder {
        val view = LayoutDigitalHomeTrustmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RechargeItemTrustMarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RechargeItemTrustMarkViewHolder(val binding: LayoutDigitalHomeTrustmarkItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: RechargeHomepageItemListener) {
            with(binding) {
                trustmarkImage.loadImage(element.mediaUrl)
                trustmarkName.text = element.title
                root.setOnClickListener {
                    onItemBindListener.onRechargeSectionItemClicked(element)
                }
            }
        }
    }
}
