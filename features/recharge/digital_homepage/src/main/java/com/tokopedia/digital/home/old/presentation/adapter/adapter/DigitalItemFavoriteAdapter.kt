package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeCategoryItemSubmenuFrameBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_CLICK
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.loadImage

class DigitalItemFavoriteAdapter(val items: List<DigitalHomePageSectionModel.Item>, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemFavoriteAdapter.DigitalItemFavoriteViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemFavoriteViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemFavoriteViewHolder {
        val view = LayoutDigitalHomeCategoryItemSubmenuFrameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigitalItemFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemFavoriteViewHolder(val binding: LayoutDigitalHomeCategoryItemSubmenuFrameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: DigitalHomePageSectionModel.Item, onItemBindListener: OnItemBindListener) {
            binding.categoryFrameImage.cardImage.loadImage(element.mediaUrl)
            binding.categoryFrameName.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onSectionItemClicked(element, adapterPosition, BEHAVIORAL_CATEGORY_CLICK)
            }
        }

    }
}
