package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.adapter.CenterScrollLayoutManager
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseCardAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseConfigUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowseErrorView
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetAutoRefreshCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import kotlinx.coroutines.CoroutineScope
import com.tokopedia.feedplus.R as feedplusR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseChannelViewHolder(
    private val binding: ItemFeedBrowseChannelBinding,
    private val listener: Listener,
    lifecycleScope: CoroutineScope,
    coroutineDispatchers: CoroutineDispatchers
) : RecyclerView.ViewHolder(binding.root), PlayWidgetAutoRefreshCoordinator.Listener {

    private val dp12 = binding.root.context.resources.getDimensionPixelOffset(R.dimen.feed_space_12)

    private val errorView: FeedBrowseErrorView = FeedBrowseErrorView(binding.root.context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp12
        }
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
        spacingHorizontal = feedplusR.dimen.feed_space_4,
        spacingTop = feedplusR.dimen.feed_space_12
    )

    private val cardListener = object : FeedBrowseCardViewHolder.Listener {
        override fun onCardImpressed(item: PlayWidgetChannelUiModel, position: Int) {
            val widgetData = mData ?: return
            val config = (widgetData.channelUiState as? ChannelUiState.Data)?.config ?: return
            listener.onCardImpressed(item, config.data, widgetData, position, bindingAdapterPosition)
        }

        override fun onCardClicked(item: PlayWidgetChannelUiModel, position: Int) {
            val widgetData = mData ?: return
            val config = (widgetData.channelUiState as? ChannelUiState.Data)?.config ?: return
            listener.onCardClicked(item, config.data, widgetData, position, bindingAdapterPosition)
        }
    }
    private val cardAdapter by lazy { FeedBrowseCardAdapter(cardListener) }
    private val cardItemDecoration = FeedBrowseItemDecoration(
        context = binding.root.context,
        spacingHorizontal = feedplusR.dimen.feed_space_8,
        spacingTop = unifyprinciplesR.dimen.layout_lvl0
    )

    private val autoRefreshCoordinator = PlayWidgetAutoRefreshCoordinator(
        lifecycleScope,
        coroutineDispatchers.immediate,
        coroutineDispatchers.default,
        this@FeedBrowseChannelViewHolder
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
            ChannelUiState.Placeholder -> {
                recyclerViewCard.hide()
                showPlaceholderView()
            }
            is ChannelUiState.Data -> {
                clearPlaceholderView()
                recyclerViewCard.show()
                setupCards(channelUiState)
            }
            is ChannelUiState.Error -> {
                showErrorView()
                errorView.setOnClickListener {
                    val extraParam = channelUiState.extraParam ?: item.extraParam
                    onRetryClicked(extraParam, item)
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
        configureAutoRefresh(channel.config)
    }

    fun configureAutoRefresh(configUiModel: FeedBrowseConfigUiModel) {
        autoRefreshCoordinator.configureAutoRefresh(configUiModel.data)
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

    private fun onRetryClicked(extraParam: WidgetRequestModel, widgetModel: FeedBrowseUiModel.Channel) {
        errorView.startAnimating()
        listener.onRetryClicked(extraParam, widgetModel)
    }

    override fun onWidgetShouldRefresh() {
        val widgetData = mData ?: return
        val chipUiState = widgetData.chipUiState
        val extraParams = if (chipUiState is ChipUiState.Data) {
            val selectedChip = chipUiState.items.first { it.isSelected }
            selectedChip.extraParam
        } else {
            widgetData.extraParam
        }
        listener.onWidgetShouldRefresh(extraParams, widgetData)
    }

    interface Listener {
        fun onRetryClicked(extraParam: WidgetRequestModel, widgetModel: FeedBrowseUiModel.Channel)

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

        fun onWidgetShouldRefresh(
            extraParam: WidgetRequestModel,
            widgetModel: FeedBrowseUiModel.Channel
        )
    }

    companion object {

        const val NOTIFY_CHANNEL_STATE = "NotifyChannelState"
        const val NOTIFY_CHIP_STATE = "NotifyChipState"
        const val NOTIFY_AUTO_REFRESH = "NotifyAutoRefresh"
    }
}
