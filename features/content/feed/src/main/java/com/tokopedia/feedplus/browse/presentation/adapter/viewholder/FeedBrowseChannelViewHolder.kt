package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.CenterScrollLayoutManager
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowseErrorView
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.feedplus.R as feedplusR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChannelViewHolder(
    private val binding: ItemFeedBrowseChannelBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

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
        override fun onChipImpressed(model: FeedBrowseChipUiModel, position: Int) {
            val widgetData = mData ?: return
            listener.onChipImpressed(model, widgetData, position, bindingAdapterPosition)
        }

        override fun onChipClicked(model: FeedBrowseChipUiModel) {
            val widgetData = mData ?: return
            listener.onChipClicked(model, widgetData)
        }

        override fun onChipSelected(model: FeedBrowseChipUiModel, position: Int) {
            val widgetData = mData ?: return
            listener.onChipSelected(model, widgetData, position, bindingAdapterPosition)
        }
    }
    private val chipAdapter by lazy { FeedBrowseChipAdapter(chipListener) }
    private val chipItemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacingHorizontal = feedplusR.dimen.feed_space_2,
        spacingTop = feedplusR.dimen.feed_space_12
    )

    private val cardListener = object : FeedBrowseCardViewHolder.Listener {
        override fun onCardImpressed(item: PlayWidgetChannelUiModel, position: Int) {
            val widgetData = mData ?: return
            val config = (widgetData.channelUiState as? ChannelUiState.Data)?.config ?: return
            listener.onCardImpressed(item, config, widgetData, position, bindingAdapterPosition)
        }

        override fun onCardClicked(item: PlayWidgetChannelUiModel, position: Int) {
            val widgetData = mData ?: return
            val config = (widgetData.channelUiState as? ChannelUiState.Data)?.config ?: return
            listener.onCardClicked(item, config, widgetData, position, bindingAdapterPosition)
        }
    }
    private val cardAdapter by lazy { FeedBrowseCardAdapter(cardListener) }
    private val cardItemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacingHorizontal = feedplusR.dimen.feed_space_8,
        spacingTop = unifyprinciplesR.dimen.layout_lvl0
    )

    private var mData: FeedBrowseUiModel.Channel? = null

    init {
        recyclerViewCard.adapter = cardAdapter
        recyclerViewCard.addItemDecoration(cardItemDecoration)

        recyclerViewChip.adapter = chipAdapter
        recyclerViewChip.addItemDecoration(chipItemDecoration)
        recyclerViewChip.layoutManager = CenterScrollLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun updateItem(item: FeedBrowseUiModel.Channel) {
        mData = item
    }

    fun bind(item: FeedBrowseUiModel.Channel) {
        updateItem(item)
        setupTitle(item.title)
        setupContent(item)
    }

    fun bindChipUiState(chipUiState: ChipUiState) {
        when (chipUiState) {
            ChipUiState.Placeholder -> showPlaceholderView()
            is ChipUiState.Data -> setupChips(chipUiState)
        }
    }

    fun bindChannelUiState(channelUiState: ChannelUiState, item: FeedBrowseUiModel.Channel) {
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
                    onRetryClicked(extraParams, item)
                }
            }
        }
    }

    private fun setupContent(item: FeedBrowseUiModel.Channel) {
        val chipUiState = item.chipUiState
        val channelUiState = item.channelUiState

        errorView.stop()

        bindChipUiState(chipUiState)
        bindChannelUiState(channelUiState, item)
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
        chipAdapter.setItemsAndAnimateChanges(chip.items)
        autoScrollChip(chip)
    }

    private fun autoScrollChip(chip: ChipUiState.Data) {
        val selectedIndex = chip.items.indexOfFirst { it.isSelected }
        if (selectedIndex in 0 until chip.items.size) {
            recyclerViewChip.scrollToPosition(selectedIndex)
        }
    }

    private fun setupCards(channel: ChannelUiState.Data) {
        cardAdapter.setItemsAndAnimateChanges(channel.items)
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

    private fun onRetryClicked(extraParams: Map<String, Any>, widgetModel: FeedBrowseUiModel.Channel) {
        errorView.startAnimating()
        listener.onRetryClicked(extraParams, widgetModel)
    }

    interface Listener {
        fun onRetryClicked(extraParams: Map<String, Any>, widgetModel: FeedBrowseUiModel.Channel)

        fun onCardImpressed(
            channelModel: PlayWidgetChannelUiModel,
            config: PlayWidgetConfigUiModel,
            widgetModel: FeedBrowseUiModel.Channel,
            channelPositionInList: Int,
            verticalWidgetPosition: Int
        )

        fun onCardClicked(
            channelModel: PlayWidgetChannelUiModel,
            config: PlayWidgetConfigUiModel,
            widgetModel: FeedBrowseUiModel.Channel,
            channelPositionInList: Int,
            verticalWidgetPosition: Int
        )

        fun onChipImpressed(
            chipModel: FeedBrowseChipUiModel,
            widgetModel: FeedBrowseUiModel.Channel,
            chipPositionInList: Int,
            verticalWidgetPosition: Int
        )

        fun onChipClicked(
            chipModel: FeedBrowseChipUiModel,
            widgetModel: FeedBrowseUiModel.Channel
        )

        fun onChipSelected(
            chipModel: FeedBrowseChipUiModel,
            widgetModel: FeedBrowseUiModel.Channel,
            chipPositionInList: Int,
            verticalWidgetPosition: Int
        )
    }

    companion object {

        const val NOTIFY_CHANNEL_STATE = "NotifyChannelState"
        const val NOTIFY_CHIP_STATE = "NotifyChipState"
    }
}
