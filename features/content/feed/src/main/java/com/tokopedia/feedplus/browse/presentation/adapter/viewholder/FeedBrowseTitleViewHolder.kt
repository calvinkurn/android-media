package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseTitleBinding

/**
 * Created by kenny.hadisaputra on 18/09/23
 */
internal class FeedBrowseTitleViewHolder(
    private val binding: ItemFeedBrowseTitleBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FeedBrowseUiModel.Title) {
        binding.root.text = item.title
    }

    fun bind(item: FeedBrowseItemListModel.Title) {
        binding.root.text = item.title
    }

    companion object {
        fun create(parent: ViewGroup): FeedBrowseTitleViewHolder {
            return FeedBrowseTitleViewHolder(
                ItemFeedBrowseTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
