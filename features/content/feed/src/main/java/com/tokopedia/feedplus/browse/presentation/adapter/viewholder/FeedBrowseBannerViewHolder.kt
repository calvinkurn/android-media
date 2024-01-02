package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseBannerItemBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseBannerPlaceholderBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by kenny.hadisaputra on 18/09/23
 */
internal class FeedBrowseBannerViewHolder private constructor() {

    class Item private constructor(
        private val binding: ItemFeedBrowseBannerItemBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {

        init {
            binding.root.animateOnPress = CardUnify2.ANIMATE_BOUNCE
        }

        fun bind(item: FeedBrowseItemListModel.Banner.Item) {
            binding.imgBanner.loadImage(item.banner.imageUrl) {
                centerCrop()
            }

            binding.tvInspiration.text = item.banner.title

            binding.root.addImpressionListener {
                listener.onBannerImpressed(this, item)
            }

            binding.root.setOnClickListener {
                listener.onBannerClicked(this, item)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Item {
                return Item(
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
            fun onBannerImpressed(
                viewHolder: Item,
                item: FeedBrowseItemListModel.Banner.Item
            )
            fun onBannerClicked(
                viewHolder: Item,
                item: FeedBrowseItemListModel.Banner.Item
            )

            companion object {
                val Default get() = object : Listener {
                    override fun onBannerImpressed(
                        viewHolder: Item,
                        item: FeedBrowseItemListModel.Banner.Item
                    ) {}

                    override fun onBannerClicked(
                        viewHolder: Item,
                        item: FeedBrowseItemListModel.Banner.Item
                    ) {}
                }
            }
        }
    }

    class Placeholder private constructor(
        binding: ItemFeedBrowseBannerPlaceholderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Placeholder {
                return Placeholder(
                    ItemFeedBrowseBannerPlaceholderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}
