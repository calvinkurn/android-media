package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardsBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
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

    private val chipDividerItemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacing = com.tokopedia.feedplus.R.dimen.feed_space_2
    )

    private val cardDividerItemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacing = com.tokopedia.feedplus.R.dimen.feed_space_8
    )

    init {
        binding.feedBrowseChips.adapter = chipsAdapter
        binding.feedBrowseCards.adapter = cardsAdapter

        binding.feedBrowseChips.addItemDecoration(chipDividerItemDecoration)
        binding.feedBrowseCards.addItemDecoration(cardDividerItemDecoration)
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
        if (chips.isEmpty()) {
            binding.feedBrowseChips.gone()
        } else {
            binding.feedBrowseChips.visible()
            chipsAdapter.setItems(chips)
            chipsAdapter.notifyDataSetChanged()
        }
    }

    private fun setupContents(content: PlayWidgetUiModel) {
        val items = content.items.filterIsInstance<PlayWidgetChannelUiModel>()
        cardsAdapter.setItems(items)
        cardsAdapter.notifyDataSetChanged()
    }
}
