package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowseErrorView
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChannelViewHolder(
    private val binding: ItemFeedBrowseChannelBinding,
    private val listener: Listener,
): RecyclerView.ViewHolder(binding.root) {

    private val errorView: FeedBrowseErrorView = FeedBrowseErrorView(binding.root.context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private val placeholderView: FeedBrowsePlaceholderView = FeedBrowsePlaceholderView(binding.root.context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        show(FeedBrowsePlaceholderView.Type.Cards)
    }

    private val recyclerViewChip = binding.feedBrowseChips
    private val recyclerViewCard = binding.feedBrowseCards

    private val chipListener = object : FeedBrowseChipViewHolder.Listener {
    }
    private val chipAdapter by lazy { FeedBrowseChipAdapter(chipListener) }
    private val chipItemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacing = com.tokopedia.feedplus.R.dimen.feed_space_2
    )

    private val cardAdapter by lazy { FeedBrowseCardAdapter() }
    private val cardItemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacing = com.tokopedia.feedplus.R.dimen.feed_space_8
    )

    init {
        recyclerViewCard.adapter = cardAdapter
        recyclerViewCard.addItemDecoration(cardItemDecoration)

        recyclerViewChip.adapter = chipAdapter
        recyclerViewChip.addItemDecoration(chipItemDecoration)
    }

    fun bind(item: FeedBrowseUiModel.Channel) {
        setupTitle(item.title)
        setupContent(item)
    }

    private fun setupContent(item: FeedBrowseUiModel.Channel) {
        val chipUiState = item.chipUiState
        val channelUiState = item.channelUiState

        errorView.stop()

        when (chipUiState) {
            ChipUiState.Placeholder -> showPlaceholderView()
            is ChipUiState.Data -> setupChips(chipUiState)
        }

        when (channelUiState) {
            ChannelUiState.Placeholder -> showPlaceholderView()
            is ChannelUiState.Data -> {
                clearPlaceholderView()
                setupCards(channelUiState)
            }
            is ChannelUiState.Error -> {
                showErrorView()
                errorView.setOnClickListener {
                    val extraParams = channelUiState.extraParams ?: item.extraParams
                    onRetryClicked(extraParams, item.id)
                }
            }
        }
    }

    private fun showErrorView() {
        addToPlaceholderView(errorView)
    }

    private fun showPlaceholderView() {
        addToPlaceholderView(placeholderView)
    }

    private fun setupTitle(title: String) {
        if (title.isBlank()) {
            binding.feedBrowseTitle.hide()
        } else {
            binding.feedBrowseTitle.text = title
            binding.feedBrowseTitle.show()
        }
    }

    private fun setupChips(chip: ChipUiState.Data) {
        if (chip.items.isEmpty()) {
            recyclerViewChip.hide()
        } else {
            chipAdapter.setItemsAndAnimateChanges(chip.items)
            recyclerViewChip.show()
        }
    }

    private fun setupCards(channel: ChannelUiState.Data) {
        cardAdapter.setItemsAndAnimateChanges(channel.items)
        recyclerViewCard.show()
    }

    private fun addToPlaceholderView(view: View) {
        with(binding.feedBrowsePlaceholder) {
            if (childCount > 0) {
                val visibleView = getChildAt(0)
                if (visibleView != view) {
                    removeViewAt(0)
                    addView(view)
                }
            } else {
                addView(view)
            }
        }
    }

    private fun clearPlaceholderView() {
        binding.feedBrowsePlaceholder.removeAllViews()
    }

    private fun onRetryClicked(extraParams: Map<String, Any>, widgetId: String) {
        errorView.startAnimating()
        listener.onRetryClicked(extraParams, widgetId)
    }

    interface Listener {
        fun onRetryClicked(extraParams: Map<String, Any>, widgetId: String)
    }

}
