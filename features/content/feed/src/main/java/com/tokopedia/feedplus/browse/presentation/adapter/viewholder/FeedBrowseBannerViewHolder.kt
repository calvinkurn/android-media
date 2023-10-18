package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseBannerItemBinding
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by kenny.hadisaputra on 18/09/23
 */
internal class FeedBrowseBannerViewHolder private constructor(
    private val binding: ItemFeedBrowseBannerItemBinding,
    private val listener: Listener
) : BaseViewHolder(binding.root) {

    init {
        binding.root.animateOnPress = CardUnify2.ANIMATE_BOUNCE
    }

    fun bind(item: FeedBrowseUiModel.Banner) {
        binding.imgBanner.setImageUrl(item.imageUrl)
        binding.tvInspiration.text = item.title
    }

    fun bind(item: FeedBrowseItemListModel.Banner) {
        binding.imgBanner.setImageUrl(item.banner.imageUrl)
        binding.tvInspiration.text = item.banner.title

        binding.root.setOnClickListener {
            listener.onBannerClicked(this, item)
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: Listener): FeedBrowseBannerViewHolder {
            return FeedBrowseBannerViewHolder(
                ItemFeedBrowseBannerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {
        fun onBannerClicked(
            viewHolder: FeedBrowseBannerViewHolder,
            item: FeedBrowseItemListModel.Banner
        )
    }
}
