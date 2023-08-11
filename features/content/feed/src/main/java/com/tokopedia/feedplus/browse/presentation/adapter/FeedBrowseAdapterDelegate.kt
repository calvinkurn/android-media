package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseCardsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowsePlaceholderViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardsBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowsePlaceholderBinding

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseAdapterDelegate private constructor() {

    internal class Cards:
        TypedAdapterDelegate<FeedBrowseUiModel.Cards, FeedBrowseUiModel, FeedBrowseCardsViewHolder>(
            com.tokopedia.feedplus.R.layout.item_feed_browse_cards
        ) {

        override fun onBindViewHolder(
            item: FeedBrowseUiModel.Cards,
            holder: FeedBrowseCardsViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedBrowseCardsViewHolder {
            return FeedBrowseCardsViewHolder(
                ItemFeedBrowseCardsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }

    internal class Placeholder:
        TypedAdapterDelegate<FeedBrowseUiModel.Placeholder, FeedBrowseUiModel, FeedBrowsePlaceholderViewHolder>(
            com.tokopedia.feedplus.R.layout.item_feed_browse_placeholder
        ) {
        override fun onBindViewHolder(
            item: FeedBrowseUiModel.Placeholder,
            holder: FeedBrowsePlaceholderViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedBrowsePlaceholderViewHolder {
            return FeedBrowsePlaceholderViewHolder(
                ItemFeedBrowsePlaceholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}
