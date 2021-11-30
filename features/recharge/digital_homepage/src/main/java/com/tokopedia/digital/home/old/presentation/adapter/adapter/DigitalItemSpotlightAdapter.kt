package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeSpotlightItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SPOTLIGHT_BANNER_CLICK
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.home_component.util.loadImage

class DigitalItemSpotlightAdapter(val items: List<DigitalHomePageSectionModel.Item>, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemSpotlightAdapter.DigitalItemSpotlightViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSpotlightViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemSpotlightViewHolder {
        val view = LayoutDigitalHomeSpotlightItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigitalItemSpotlightViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemSpotlightViewHolder(val binding: LayoutDigitalHomeSpotlightItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: DigitalHomePageSectionModel.Item, onItemBindListener: OnItemBindListener) {
            binding.spotlightImage.loadImage(element.mediaUrl)
            binding.spotlightName.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onSectionItemClicked(element, adapterPosition, SPOTLIGHT_BANNER_CLICK)
            }
        }

    }
}
