package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseInspirationCardBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
internal class FeedBrowseInspirationCardViewHolder private constructor(
    private val binding: ItemFeedBrowseInspirationCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedBrowseItemListModel.InspirationCard) {
        binding.tvTitle.text = item.item.title
        binding.imgCover.setImageUrl(item.item.video.coverUrl)

        binding.tvPartnerName.text = item.item.partner.name
        binding.imgAvatar.setImageUrl(item.item.partner.avatarUrl)
        binding.imgBadge.setImageUrl(item.item.partner.badgeUrl)
        binding.imgBadge.showWithCondition(item.item.partner.badgeUrl.isNotBlank())
    }

    companion object {
        fun create(parent: ViewGroup): FeedBrowseInspirationCardViewHolder {
            return FeedBrowseInspirationCardViewHolder(
                ItemFeedBrowseInspirationCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
