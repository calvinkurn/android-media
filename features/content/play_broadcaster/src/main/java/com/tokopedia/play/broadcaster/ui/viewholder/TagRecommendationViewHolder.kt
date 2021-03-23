package com.tokopedia.play.broadcaster.ui.viewholder

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemTagRecommendationBinding

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationViewHolder(
        private val binding: ItemTagRecommendationBinding,
        private val listener: Listener
) : BaseViewHolder(binding.root) {

    fun bind(item: String) {
        binding.tvTagName.text = item
        binding.tvTagName.setOnClickListener {
            listener.onTagClicked(item)
        }
    }

    companion object {

        val LAYOUT = R.layout.item_tag_recommendation
    }

    interface Listener {

        fun onTagClicked(tag: String)
    }
}