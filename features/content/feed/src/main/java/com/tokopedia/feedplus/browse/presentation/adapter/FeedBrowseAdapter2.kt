package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel2
import com.tokopedia.feedplus.browse.presentation.model.ItemListState
import com.tokopedia.feedplus.browse.presentation.model.hasContent
import com.tokopedia.feedplus.browse.presentation.model.hasContentAndNotEmpty

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class FeedBrowseAdapter2(
    private val chipsListener: FeedBrowseChipsViewHolder.Listener,
    private val bannerListener: FeedBrowseBannerViewHolder.Listener,
) : ListAdapter<FeedBrowseItemListModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<FeedBrowseItemListModel>() {
        override fun areItemsTheSame(oldItem: FeedBrowseItemListModel, newItem: FeedBrowseItemListModel): Boolean {
            return oldItem.slotId == newItem.slotId && oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: FeedBrowseItemListModel, newItem: FeedBrowseItemListModel): Boolean {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CHIPS -> FeedBrowseChipsViewHolder.create(parent, chipsListener)
            TYPE_HORIZONTAL_CHANNELS -> FeedBrowseHorizontalChannelsViewHolder.create(parent)
            TYPE_BANNER -> FeedBrowseBannerViewHolder.create(parent, bannerListener)
            TYPE_TITLE -> FeedBrowseTitleViewHolder.create(parent)
            else -> error("ViewType $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is FeedBrowseChipsViewHolder && item is FeedBrowseItemListModel.Chips -> {
                holder.bind(item)
            }
            holder is FeedBrowseHorizontalChannelsViewHolder && item is FeedBrowseItemListModel.HorizontalChannels -> {
                holder.bind(item)
            }
            holder is FeedBrowseBannerViewHolder && item is FeedBrowseItemListModel.Banner -> {
                holder.bind(item)
            }
            holder is FeedBrowseTitleViewHolder && item is FeedBrowseItemListModel.Title -> {
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
            holder is FeedBrowseChipsViewHolder && item is FeedBrowseItemListModel.Chips -> {
                holder.bindPayloads(item, payload)
            }
            holder is FeedBrowseHorizontalChannelsViewHolder && item is FeedBrowseItemListModel.HorizontalChannels -> {
                holder.bindPayloads(item, payload)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedBrowseItemListModel.Chips -> TYPE_CHIPS
            is FeedBrowseItemListModel.HorizontalChannels -> TYPE_HORIZONTAL_CHANNELS
            is FeedBrowseItemListModel.Banner -> TYPE_BANNER
            is FeedBrowseItemListModel.Title -> TYPE_TITLE
        }
    }

    fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = getItem(position)
                return when (item::class) {
                    FeedBrowseItemListModel.Banner::class -> 1
                    else -> 2
                }
            }
        }
    }

    companion object {
        internal const val TYPE_CHIPS = 0
        internal const val TYPE_HORIZONTAL_CHANNELS = 1
        internal const val TYPE_BANNER = 2
        internal const val TYPE_TITLE = 3
    }

    fun setList(items: List<FeedBrowseUiModel2>) {
        submitList(items.mapToItems())
    }

    private fun List<FeedBrowseUiModel2>.mapToItems(): List<FeedBrowseItemListModel> {
        return flatMap {
            when (it.model) {
                is FeedBrowseModel.ChannelsWithMenus -> it.model.mapToItems(it.result)
                is FeedBrowseModel.InspirationBanner -> it.model.mapToItems()
            }
        }
    }

    private fun FeedBrowseModel.ChannelsWithMenus.mapToItems(
        state: ResultState,
    ): List<FeedBrowseItemListModel> {
        return buildList {
            if (state.isLoading) {
                add(FeedBrowseItemListModel.Title(slotId, title))
                add(FeedBrowseItemListModel.HorizontalChannels(slotId, ItemListState.Loading))
                return@buildList
            }

            val isMenuEmpty = menus.keys.isEmpty() || menus.keys.any { !it.isValid }
            val selectedMenu = menus.keys.firstOrNull { it.id == selectedMenuId } ?: menus.keys.firstOrNull()
            val itemsInSelectedMenu = menus[selectedMenu ?: menus.keys.firstOrNull()]

            if (!isMenuEmpty || itemsInSelectedMenu?.hasContentAndNotEmpty() == true) {
                add(FeedBrowseItemListModel.Title(slotId, title))
            }
            if (!isMenuEmpty) {
                add(
                    FeedBrowseItemListModel.Chips(
                        slotId,
                        menus.keys.toList().map {
                            FeedBrowseItemListModel.Chips.Model(it, it == selectedMenu)
                        }
                    )
                )
            }
            add(
                FeedBrowseItemListModel.HorizontalChannels(
                    slotId,
                    itemsInSelectedMenu ?: ItemListState.Loading
                )
            )
        }
    }

    private fun FeedBrowseModel.InspirationBanner.mapToItems(): List<FeedBrowseItemListModel> {
        return buildList {
            add(FeedBrowseItemListModel.Title(slotId, title))
            addAll(
                bannerList.map { FeedBrowseItemListModel.Banner(slotId, it) }
            )
        }
    }
}
