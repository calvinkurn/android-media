package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardsBinding
import com.tokopedia.play.widget.ui.PlayWidgetMediumView

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardsViewHolder(
    private val binding: ItemFeedBrowseCardsBinding,
): RecyclerView.ViewHolder(binding.root) {

    private val view: PlayWidgetMediumView = binding.feedBrowseCards

    private val chipsAdapter by lazy { FeedBrowseChipAdapter() }

    init {
        binding.feedBrowseChips.adapter = chipsAdapter
    }

    fun bind(item: FeedBrowseUiModel.Cards) {
        setupHeader(item.title)
        setupChips(item.chips)
        view.setData(item.model)
    }

    private fun setupHeader(title: String) {
        binding.feedBrowseTitle.text = title
    }

    private fun setupChips(chips: List<FeedBrowseChipUiModel>) {
        chipsAdapter.setItems(chips)
        chipsAdapter.notifyDataSetChanged()
    }
}
