package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryItemSubmenuFrameBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.media.loader.loadImage

class RechargeItemFavoriteAdapter(val items: List<RechargeHomepageSections.Item>, val listener: RechargeHomepageItemListener) :
    RecyclerView.Adapter<RechargeItemFavoriteAdapter.DigitalItemFavoriteViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemFavoriteViewHolder, position: Int) {
        viewHolder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemFavoriteViewHolder {
        val view = LayoutDigitalHomeCategoryItemSubmenuFrameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigitalItemFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemFavoriteViewHolder(val binding: LayoutDigitalHomeCategoryItemSubmenuFrameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: RechargeHomepageItemListener) {
            with(binding) {
                categoryFrameImage.cardImage.loadImage(element.mediaUrl)
                categoryFrameName.text = element.title
                binding.root.setOnClickListener {
                    onItemBindListener.onRechargeSectionItemClicked(element)
                }
            }
        }
    }
}
