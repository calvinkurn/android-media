package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseInspirationCardBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseInspirationPlaceholderBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
internal class CategoryInspirationViewHolder private constructor() {

    class Card private constructor(
        private val binding: ItemFeedBrowseInspirationCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.animateOnPress = CardUnify2.ANIMATE_BOUNCE
        }

        fun bind(item: FeedBrowseItemListModel.InspirationCard.Item) {
            binding.tvTitle.text = item.item.title
            binding.totalView.setTotalWatch(item.item.totalView.totalViewFmt)
            binding.imgCover.setImageUrl(item.item.video.coverUrl)

            binding.tvPartnerName.text = item.item.partner.name
            binding.imgAvatar.setImageUrl(item.item.partner.avatarUrl)
            binding.imgBadge.setImageUrl(item.item.partner.badgeUrl)
            binding.imgBadge.showWithCondition(item.item.partner.badgeUrl.isNotBlank())
        }

        fun recycle() {
            binding.imgCover.setImageResource(0)
        }

        companion object {
            fun create(parent: ViewGroup): Card {
                return Card(
                    ItemFeedBrowseInspirationCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }

    class Placeholder private constructor(
        binding: ItemFeedBrowseInspirationPlaceholderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Placeholder {
                return Placeholder(
                    ItemFeedBrowseInspirationPlaceholderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }
}
