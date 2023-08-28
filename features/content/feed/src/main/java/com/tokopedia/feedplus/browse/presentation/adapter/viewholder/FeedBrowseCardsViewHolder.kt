package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardsBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardsViewHolder(
    private val binding: ItemFeedBrowseCardsBinding,
): RecyclerView.ViewHolder(binding.root) {

    private val recyclerView = binding.feedBrowseCards

    private val adapter by lazy { FeedBrowseCardAdapter() }
    private val itemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacing = com.tokopedia.feedplus.R.dimen.feed_space_2
    )

    init {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(itemDecoration)
    }

    fun bind(item: FeedBrowseUiModel.Cards) {
        setupTitle(item.title)
        setupContents(item.model)
    }

    private fun setupTitle(title: String) {
        if (title.isBlank()) {
            binding.feedBrowseTitle.hide()
        } else {
            binding.feedBrowseTitle.text = title
            binding.feedBrowseTitle.show()
        }
    }

    private fun setupContents(content: PlayWidgetUiModel) {
        val items = content.items.filterIsInstance<PlayWidgetChannelUiModel>()
        adapter.setItemsAndAnimateChanges(items)
    }
}
