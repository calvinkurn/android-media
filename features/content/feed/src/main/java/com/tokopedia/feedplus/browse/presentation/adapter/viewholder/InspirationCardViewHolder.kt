package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseInspirationCardBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseInspirationPlaceholderBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
internal class InspirationCardViewHolder private constructor() {

    class Item private constructor(
        private val binding: ItemFeedBrowseInspirationCardBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.animateOnPress = CardUnify2.ANIMATE_BOUNCE
        }

        fun bind(item: FeedBrowseItemListModel.InspirationCard.Item) {
            binding.tvTitle.text = item.item.title
            binding.totalView.setTotalWatch(item.item.totalView.totalViewFmt)

            binding.imgCover.scaleType = ImageView.ScaleType.FIT_CENTER
            binding.imgCover.loadImage(item.item.video.coverUrl) {
                listener(
                    onSuccess = { _, _ -> binding.imgCover.scaleType = ImageView.ScaleType.CENTER_CROP }
                )
            }

            binding.tvPartnerName.text = item.item.partner.name
            binding.imgAvatar.loadImageCircle(item.item.partner.avatarUrl)
            binding.imgBadge.loadImage(item.item.partner.badgeUrl)
            binding.imgBadge.showWithCondition(item.item.partner.badgeUrl.isNotBlank())

            binding.root.addImpressionListener {
                listener.onImpressed(this, item)
            }

            binding.root.setOnClickListener {
                listener.onClicked(this, item)
            }
        }

        fun recycle() {
            binding.imgCover.setImageResource(0)
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Item {
                return Item(
                    ItemFeedBrowseInspirationCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener
                )
            }
        }

        interface Listener {

            fun onImpressed(viewHolder: Item, model: FeedBrowseItemListModel.InspirationCard.Item)
            fun onClicked(viewHolder: Item, model: FeedBrowseItemListModel.InspirationCard.Item)
        }
    }

    class Placeholder private constructor(
        binding: ItemFeedBrowseInspirationPlaceholderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Placeholder {
                return Placeholder(
                    ItemFeedBrowseInspirationPlaceholderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}
