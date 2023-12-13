package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalAuthorsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import kotlinx.coroutines.CoroutineScope

internal abstract class FeedBrowseItemAdapter<Input : Any>(
    private val scope: CoroutineScope,
    private val chipsListener: ChipsViewHolder.Listener,
    private val bannerListener: FeedBrowseBannerViewHolder.Item.Listener,
    private val channelListener: FeedBrowseHorizontalChannelsViewHolder.Listener,
    private val creatorListener: FeedBrowseHorizontalAuthorsViewHolder.Listener,
    private val spanSize: Int = 2
) : ListAdapter<FeedBrowseItemListModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<FeedBrowseItemListModel>() {
        override fun areItemsTheSame(
            oldItem: FeedBrowseItemListModel,
            newItem: FeedBrowseItemListModel
        ): Boolean {
            return oldItem.slotInfo.id == newItem.slotInfo.id && oldItem::class == newItem::class
        }

        override fun areContentsTheSame(
            oldItem: FeedBrowseItemListModel,
            newItem: FeedBrowseItemListModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: FeedBrowseItemListModel,
            newItem: FeedBrowseItemListModel
        ): Any? {
            val payloadBuilder = FeedBrowsePayloads.Builder()
            if (oldItem is FeedBrowseItemListModel.Chips && newItem is FeedBrowseItemListModel.Chips) {
                payloadBuilder.addChannelChipsChanged()
            }
            if (oldItem is FeedBrowseItemListModel.HorizontalChannels && newItem is FeedBrowseItemListModel.HorizontalChannels) {
                payloadBuilder.addChannelItemsChanged()
            }

            return payloadBuilder.build()
        }
    }
) {

    private val poolManager = FeedBrowseRecycledViewPoolManager()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CHIPS -> {
                ChipsViewHolder.create(parent, poolManager.menuRecycledViewPool, chipsListener)
            }
            TYPE_HORIZONTAL_CHANNELS -> {
                FeedBrowseHorizontalChannelsViewHolder.create(
                    parent,
                    poolManager.channelRecycledViewPool,
                    scope,
                    channelListener
                )
            }
            TYPE_BANNER -> {
                FeedBrowseBannerViewHolder.Item.create(parent, bannerListener)
            }
            TYPE_BANNER_PLACEHOLDER -> {
                FeedBrowseBannerViewHolder.Placeholder.create(parent)
            }
            TYPE_TITLE -> {
                FeedBrowseTitleViewHolder.create(parent)
            }
            TYPE_HORIZONTAL_CREATORS -> {
                FeedBrowseHorizontalAuthorsViewHolder.create(
                    parent,
                    poolManager.authorRecycledViewPool,
                    creatorListener
                )
            }
            else -> error("ViewType $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is ChipsViewHolder && item is FeedBrowseItemListModel.Chips -> {
                holder.bind(item)
            }
            holder is FeedBrowseHorizontalChannelsViewHolder && item is FeedBrowseItemListModel.HorizontalChannels -> {
                holder.bind(item)
            }
            holder is FeedBrowseBannerViewHolder.Item && item is FeedBrowseItemListModel.Banner.Item -> {
                holder.bind(item)
            }
            holder is FeedBrowseTitleViewHolder && item is FeedBrowseItemListModel.Title -> {
                holder.bind(item)
            }
            holder is FeedBrowseHorizontalAuthorsViewHolder && item is FeedBrowseItemListModel.HorizontalAuthors -> {
                holder.bind(item)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            return super.onBindViewHolder(holder, position, payloads)
        }
        val payload = payloads.filterIsInstance<FeedBrowsePayloads>().combine()
        val item = getItem(position)
        when {
            holder is ChipsViewHolder && item is FeedBrowseItemListModel.Chips -> {
                holder.bindPayloads(item, payload)
            }
            holder is FeedBrowseHorizontalChannelsViewHolder && item is FeedBrowseItemListModel.HorizontalChannels -> {
                holder.bindPayloads(item, payload)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is FeedBrowseItemListModel.Chips -> TYPE_CHIPS
            is FeedBrowseItemListModel.HorizontalChannels -> TYPE_HORIZONTAL_CHANNELS
            is FeedBrowseItemListModel.Banner.Item -> TYPE_BANNER
            is FeedBrowseItemListModel.Banner.Placeholder -> TYPE_BANNER_PLACEHOLDER
            is FeedBrowseItemListModel.Title -> TYPE_TITLE
            is FeedBrowseItemListModel.HorizontalAuthors -> TYPE_HORIZONTAL_CREATORS
            else -> error("Item $item is not supported")
        }
    }

    fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = getItem(position)
                return when (item::class) {
                    FeedBrowseItemListModel.Banner.Item::class,
                    FeedBrowseItemListModel.Banner.Placeholder::class -> 1
                    else -> spanSize
                }
            }
        }
    }

    fun setItems(input: Input, onCommit: () -> Unit = {}) {
        submitList(input.mapToItems())
    }

    abstract fun Input.mapToItems(): List<FeedBrowseItemListModel>

    companion object {
        internal const val TYPE_CHIPS = 0
        internal const val TYPE_HORIZONTAL_CHANNELS = 1
        internal const val TYPE_BANNER = 2
        internal const val TYPE_BANNER_PLACEHOLDER = 3
        internal const val TYPE_TITLE = 4
        internal const val TYPE_HORIZONTAL_CREATORS = 5
    }
}
