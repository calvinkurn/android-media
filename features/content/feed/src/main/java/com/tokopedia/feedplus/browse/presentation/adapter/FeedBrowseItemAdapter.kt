package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.producttag.view.adapter.viewholder.LoadingViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalAuthorsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.HorizontalStoriesViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.Banner
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.Chips
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.HorizontalAuthors
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.HorizontalChannels
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.HorizontalStories
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.InspirationCard
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.LoadingModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel.Title
import kotlinx.coroutines.CoroutineScope

internal abstract class FeedBrowseItemAdapter<Input : Any>(
    private val scope: CoroutineScope,
    private val chipsListener: ChipsViewHolder.Listener = ChipsViewHolder.Listener.Default,
    private val bannerListener: FeedBrowseBannerViewHolder.Item.Listener = FeedBrowseBannerViewHolder.Item.Listener.Default,
    private val channelListener: FeedBrowseHorizontalChannelsViewHolder.Listener = FeedBrowseHorizontalChannelsViewHolder.Listener.Default,
    private val creatorListener: FeedBrowseHorizontalAuthorsViewHolder.Listener = FeedBrowseHorizontalAuthorsViewHolder.Listener.Default,
    private val inspirationCardListener: InspirationCardViewHolder.Item.Listener = InspirationCardViewHolder.Item.Listener.Default,
    private val storyWidgetListener: HorizontalStoriesViewHolder.Listener = HorizontalStoriesViewHolder.Listener.Default,
    val spanCount: Int = 2,
) : ListAdapter<FeedBrowseItemListModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<FeedBrowseItemListModel>() {
        override fun areItemsTheSame(
            oldItem: FeedBrowseItemListModel,
            newItem: FeedBrowseItemListModel
        ): Boolean {
            if (oldItem.slotInfo.id != newItem.slotInfo.id) return false
            return when {
                oldItem is InspirationCard.Item && newItem is InspirationCard.Item -> {
                    oldItem.item.channelId == newItem.item.channelId
                }
                else -> {
                    oldItem::class == newItem::class
                }
            }
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
            if (oldItem is Chips && newItem is Chips) {
                payloadBuilder.addChannelChipsChanged()
            }
            if (oldItem is HorizontalChannels && newItem is HorizontalChannels) {
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
            TYPE_HORIZONTAL_AUTHORS -> {
                FeedBrowseHorizontalAuthorsViewHolder.create(
                    parent,
                    poolManager.authorRecycledViewPool,
                    creatorListener
                )
            }
            TYPE_INSPIRATION_CARD -> {
                InspirationCardViewHolder.Item.create(parent, inspirationCardListener)
            }
            TYPE_INSPIRATION_CARD_PLACEHOLDER -> {
                InspirationCardViewHolder.Placeholder.create(parent)
            }
            TYPE_LOADING -> {
                LoadingViewHolder.create(parent)
            }
            TYPE_HORIZONTAL_STORIES -> {
                HorizontalStoriesViewHolder.create(
                    parent,
                    poolManager.storyRecycledViewPool,
                    storyWidgetListener,
                )
            }
            else -> error("ViewType $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is ChipsViewHolder && item is Chips -> {
                holder.bind(item)
            }
            holder is FeedBrowseHorizontalChannelsViewHolder && item is HorizontalChannels -> {
                holder.bind(item)
            }
            holder is FeedBrowseBannerViewHolder.Item && item is Banner.Item -> {
                holder.bind(item)
            }
            holder is FeedBrowseTitleViewHolder && item is Title -> {
                holder.bind(item)
            }
            holder is FeedBrowseHorizontalAuthorsViewHolder && item is HorizontalAuthors -> {
                holder.bind(item)
            }
            holder is InspirationCardViewHolder.Item && item is InspirationCard.Item -> {
                holder.bind(item)
            }
            holder is HorizontalStoriesViewHolder && item is HorizontalStories -> {
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
            holder is ChipsViewHolder && item is Chips -> {
                holder.bindPayloads(item, payload)
            }
            holder is FeedBrowseHorizontalChannelsViewHolder && item is HorizontalChannels -> {
                holder.bindPayloads(item, payload)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is Chips -> TYPE_CHIPS
            is HorizontalChannels -> TYPE_HORIZONTAL_CHANNELS
            is Banner.Item -> TYPE_BANNER
            Banner.Placeholder -> TYPE_BANNER_PLACEHOLDER
            is Title -> TYPE_TITLE
            is HorizontalAuthors -> TYPE_HORIZONTAL_AUTHORS
            LoadingModel -> TYPE_LOADING
            is InspirationCard.Item -> TYPE_INSPIRATION_CARD
            is InspirationCard.Placeholder -> TYPE_INSPIRATION_CARD_PLACEHOLDER
            is HorizontalStories -> TYPE_HORIZONTAL_STORIES
            else -> error("Item $item is not supported")
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is InspirationCardViewHolder.Item -> holder.recycle()
        }
    }

    fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItem(position)) {
                    is Banner.Item,
                    Banner.Placeholder,
                    is InspirationCard -> 1
                    else -> spanCount
                }
            }
        }
    }

    fun setItems(input: Input, onCommit: () -> Unit = {}) {
        submitList(input.mapToItems(), onCommit)
    }

    abstract fun Input.mapToItems(): List<FeedBrowseItemListModel>

    companion object {
        internal const val TYPE_CHIPS = 0
        internal const val TYPE_HORIZONTAL_CHANNELS = 1
        internal const val TYPE_BANNER = 2
        internal const val TYPE_BANNER_PLACEHOLDER = 3
        internal const val TYPE_TITLE = 4
        internal const val TYPE_HORIZONTAL_AUTHORS = 5
        internal const val TYPE_INSPIRATION_CARD = 6
        internal const val TYPE_INSPIRATION_CARD_PLACEHOLDER = 7
        internal const val TYPE_HORIZONTAL_STORIES = 8
        internal const val TYPE_LOADING = 99
    }
}
