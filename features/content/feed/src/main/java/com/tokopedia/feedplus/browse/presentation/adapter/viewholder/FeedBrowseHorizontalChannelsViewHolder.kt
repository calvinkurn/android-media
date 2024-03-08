package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChannelAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseHorizontalChannelsItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseHorizontalChannelsBinding
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetAutoRefreshCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class FeedBrowseHorizontalChannelsViewHolder private constructor(
    private val binding: ItemFeedBrowseHorizontalChannelsBinding,
    pool: RecyclerView.RecycledViewPool,
    private val scope: CoroutineScope,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private var retryJob: Job? = null

    private var mData: FeedBrowseItemListModel.HorizontalChannels? = null

    private val autoRefreshCoordinator = PlayWidgetAutoRefreshCoordinator(
        scope,
        listener = object : PlayWidgetAutoRefreshCoordinator.Listener {
            override fun onWidgetShouldRefresh() {
                val data = mData ?: return
                listener.onRefresh(
                    this@FeedBrowseHorizontalChannelsViewHolder,
                    data.slotInfo.id,
                    data.menu
                )
            }
        }
    )

    private val adapter = FeedBrowseChannelAdapter(
        object : FeedBrowseChannelViewHolder.Channel.Listener {
            override fun onCardImpressed(item: PlayWidgetChannelUiModel, position: Int) {
                val data = mData ?: return
                listener.onCardImpressed(
                    this@FeedBrowseHorizontalChannelsViewHolder,
                    data,
                    item,
                    position
                )
            }

            override fun onCardClicked(item: PlayWidgetChannelUiModel, position: Int) {
                val data = mData ?: return
                listener.onCardClicked(
                    this@FeedBrowseHorizontalChannelsViewHolder,
                    data,
                    item,
                    position
                )
            }
        }
    )

    init {
        binding.rvChannels.adapter = adapter
        binding.rvChannels.setRecycledViewPool(pool)
        binding.rvChannels.setHasFixedSize(true)
        binding.rvChannels.addItemDecoration(
            FeedBrowseHorizontalChannelsItemDecoration(binding.rvChannels.resources)
        )

        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                retryJob?.cancel()
            }
        })
    }

    fun bind(item: FeedBrowseItemListModel.HorizontalChannels) {
        mData = item

        binding.errorView.stop()

        when (item.itemState.state) {
            ResultState.Loading -> {
                showContent(true)
                adapter.setLoading()
            }
            is ResultState.Success -> {
                showContent(true)
                adapter.submitList(item.itemState.items) {
                    binding.rvChannels.doOnLayout {
                        binding.rvChannels.scrollToPosition(0)
                    }
                    binding.rvChannels.invalidateItemDecorations()
                }
                autoRefreshCoordinator.configureAutoRefresh(item.itemState.config)
            }
            is ResultState.Fail -> {
                showContent(false)
                binding.errorView.setOnClickListener { retry(item) }
            }
        }
    }

    private fun showContent(shouldShow: Boolean) {
        binding.rvChannels.showWithCondition(shouldShow)
        binding.errorView.showWithCondition(!shouldShow)
    }

    private fun retry(item: FeedBrowseItemListModel.HorizontalChannels) {
        if (retryJob?.isActive == true) return
        retryJob = scope.launch {
            binding.errorView.startAnimating()
            delay(1.seconds)
            listener.onRetry(
                this@FeedBrowseHorizontalChannelsViewHolder,
                item.slotInfo.id,
                item.menu
            )
        }
    }

    fun bindPayloads(item: FeedBrowseItemListModel.HorizontalChannels, payloads: FeedBrowsePayloads) {
        if (payloads.isChannelItemsChanged()) bind(item)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            pool: RecyclerView.RecycledViewPool,
            scope: CoroutineScope,
            listener: Listener
        ): FeedBrowseHorizontalChannelsViewHolder {
            return FeedBrowseHorizontalChannelsViewHolder(
                ItemFeedBrowseHorizontalChannelsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                pool,
                scope,
                listener
            )
        }
    }

    interface Listener {
        fun onRetry(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            slotId: String,
            menu: WidgetMenuModel
        )

        fun onRefresh(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            slotId: String,
            menu: WidgetMenuModel
        )

        fun onCardImpressed(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalChannels,
            channel: PlayWidgetChannelUiModel,
            channelPosition: Int
        )

        fun onCardClicked(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalChannels,
            channel: PlayWidgetChannelUiModel,
            channelPosition: Int
        )

        companion object {
            val Default get() = object : Listener {
                override fun onRetry(
                    viewHolder: FeedBrowseHorizontalChannelsViewHolder,
                    slotId: String,
                    menu: WidgetMenuModel
                ) {}

                override fun onRefresh(
                    viewHolder: FeedBrowseHorizontalChannelsViewHolder,
                    slotId: String,
                    menu: WidgetMenuModel
                ) {}

                override fun onCardImpressed(
                    viewHolder: FeedBrowseHorizontalChannelsViewHolder,
                    widgetModel: FeedBrowseItemListModel.HorizontalChannels,
                    channel: PlayWidgetChannelUiModel,
                    channelPosition: Int
                ) {}

                override fun onCardClicked(
                    viewHolder: FeedBrowseHorizontalChannelsViewHolder,
                    widgetModel: FeedBrowseItemListModel.HorizontalChannels,
                    channel: PlayWidgetChannelUiModel,
                    channelPosition: Int
                ) {}
            }
        }
    }
}
