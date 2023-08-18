package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardsBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardsViewHolder(
    private val binding: ItemFeedBrowseCardsBinding,
): RecyclerView.ViewHolder(binding.root) {

    private val chipsAdapter by lazy { FeedBrowseChipAdapter() }
    private val cardsAdapter by lazy { FeedBrowseCardAdapter() }

    init {
        binding.feedBrowseChips.adapter = chipsAdapter
        binding.feedBrowseCards.adapter = cardsAdapter
    }

    fun bind(item: FeedBrowseUiModel.Cards) {
        setupHeader(item.title)
        setupChips(item.chips)
        setupContents(item.model)
    }

    private fun setupHeader(title: String) {
        binding.feedBrowseTitle.text = title
    }

    private fun setupChips(chips: List<FeedBrowseChipUiModel>) {
        chipsAdapter.setItems(chips)
        chipsAdapter.notifyDataSetChanged()
    }

    private fun setupContents(content: PlayWidgetUiModel) {
        val items = content.items.filterIsInstance<PlayWidgetChannelUiModel>()
        cardsAdapter.setItems(items)
        cardsAdapter.notifyDataSetChanged()
    }
}
