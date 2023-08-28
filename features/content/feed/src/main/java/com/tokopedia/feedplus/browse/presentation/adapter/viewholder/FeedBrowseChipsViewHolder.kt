package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipsBinding

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChipsViewHolder(
    private val binding: ItemFeedBrowseChipsBinding,
): RecyclerView.ViewHolder(binding.root) {

    private val recyclerView = binding.feedBrowseChips

    private val adapter by lazy { FeedBrowseChipAdapter() }

    private val itemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacing = com.tokopedia.feedplus.R.dimen.feed_space_2
    )

    init {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(itemDecoration)
    }

    fun bind(item: FeedBrowseUiModel.Chips) {
        binding.feedBrowseTitle.text = item.title

        val chips = item.chips.keys.toList()
        adapter.setItemsAndAnimateChanges(chips)
    }
}
