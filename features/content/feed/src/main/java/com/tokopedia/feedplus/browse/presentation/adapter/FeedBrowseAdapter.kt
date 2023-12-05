package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalCreatorsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseStatefulModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.feedplus.browse.presentation.model.isNotEmpty
import kotlinx.coroutines.CoroutineScope

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class FeedBrowseAdapter(
    private val scope: CoroutineScope,
    private val chipsListener: ChipsViewHolder.Listener,
    private val bannerListener: FeedBrowseBannerViewHolder.Item.Listener,
    private val channelListener: FeedBrowseHorizontalChannelsViewHolder.Listener,
    private val creatorListener: FeedBrowseHorizontalCreatorsViewHolder.Listener
) : ListAdapter<FeedBrowseItemListModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<FeedBrowseItemListModel>() {
        override fun areItemsTheSame(oldItem: FeedBrowseItemListModel, newItem: FeedBrowseItemListModel): Boolean {
            return oldItem.slotInfo.id == newItem.slotInfo.id && oldItem::class == newItem::class
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
            TYPE_CHIPS -> {
                ChipsViewHolder.create(parent, chipsListener)
            }
            TYPE_HORIZONTAL_CHANNELS -> {
                FeedBrowseHorizontalChannelsViewHolder.create(parent, scope, channelListener)
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
                FeedBrowseHorizontalCreatorsViewHolder.create(parent, creatorListener, scope)
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
            holder is FeedBrowseHorizontalCreatorsViewHolder && item is FeedBrowseItemListModel.HorizontalCreator -> {
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
            is FeedBrowseItemListModel.HorizontalCreator -> TYPE_HORIZONTAL_CREATORS
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
                    else -> 2
                }
            }
        }
    }

    companion object {
        internal const val TYPE_CHIPS = 0
        internal const val TYPE_HORIZONTAL_CHANNELS = 1
        internal const val TYPE_BANNER = 2
        internal const val TYPE_BANNER_PLACEHOLDER = 3
        internal const val TYPE_TITLE = 4
        internal const val TYPE_INSPIRATION_CARD = 5
        internal const val TYPE_HORIZONTAL_CREATORS = 6
    }

    fun setList(items: List<FeedBrowseStatefulModel>, onCommit: () -> Unit = {}) {
        submitList(items.mapToItems(), onCommit)
    }

    fun setPlaceholder() {
        submitList(getPlaceholders())
    }

    private fun List<FeedBrowseStatefulModel>.mapToItems(): List<FeedBrowseItemListModel> {
        return flatMapIndexed { index, item ->
            when (item.model) {
                is FeedBrowseSlotUiModel.ChannelsWithMenus -> item.model.mapToChannelBlocks(
                    item.result,
                    index
                )
                is FeedBrowseSlotUiModel.InspirationBanner -> item.model.mapToItems(index)
                is FeedBrowseSlotUiModel.Creators -> item.model.mapToItems(item.result, index)
            }
        }
    }

    private fun FeedBrowseSlotUiModel.ChannelsWithMenus.mapToChannelBlocks(
        state: ResultState,
        position: Int
    ): List<FeedBrowseItemListModel> {
        return buildList {
            val isMenuEmpty = menus.keys.isEmpty() || menus.keys.any { !it.isValid }
            val selectedMenu = if (menus.keys.isNotEmpty()) {
                menus.keys.firstOrNull { it.id == selectedMenuId } ?: menus.keys.first()
            } else {
                WidgetMenuModel.Empty
            }
            val itemsInSelectedMenu = menus[selectedMenu]

            val slotInfo = getSlotInfo(position)

            if (!isMenuEmpty || itemsInSelectedMenu?.isNotEmpty() == true || !state.isSuccess) {
                add(FeedBrowseItemListModel.Title(slotInfo, title))
            }
            if (!isMenuEmpty) {
                add(
                    FeedBrowseItemListModel.Chips.Item(
                        slotInfo,
                        menus.keys.toList().map {
                            ChipsModel(it, it == selectedMenu)
                        }
                    )
                )
            }

            add(
                FeedBrowseItemListModel.HorizontalChannels(
                    slotInfo,
                    selectedMenu,
                    itemsInSelectedMenu ?: FeedBrowseChannelListState.initLoading()
                )
            )
        }
    }

    private fun FeedBrowseSlotUiModel.InspirationBanner.mapToItems(
        position: Int
    ): List<FeedBrowseItemListModel> {
        val slotInfo = getSlotInfo(position)
        return buildList {
            add(FeedBrowseItemListModel.Title(slotInfo, title))
            addAll(
                if (isLoading) List(6) { FeedBrowseItemListModel.Banner.Placeholder }
                else bannerList.map { FeedBrowseItemListModel.Banner.Item(slotInfo, it) }
            )
        }
    }

    private fun FeedBrowseSlotUiModel.Creators.mapToItems(
        state: ResultState,
        position: Int
    ): List<FeedBrowseItemListModel> {
        val slotInfo = getSlotInfo(position)
        return buildList {
            add(FeedBrowseItemListModel.Title(slotInfo, title))
            add(FeedBrowseItemListModel.HorizontalCreator(slotInfo, creatorList))
        }
    }

    private fun getPlaceholders(): List<FeedBrowseItemListModel> {
        return listOf(
            FeedBrowseItemListModel.Title.Loading,
            FeedBrowseItemListModel.Chips.Loading,
            FeedBrowseItemListModel.HorizontalChannels.Loading,

            FeedBrowseItemListModel.Title.Loading,
            FeedBrowseItemListModel.HorizontalChannels.Loading,

            FeedBrowseItemListModel.Title.Loading,
            FeedBrowseItemListModel.HorizontalCreator.Loading,
        )
    }

    private fun FeedBrowseSlotUiModel.getSlotInfo(position: Int): SlotInfo {
        return SlotInfo(
            id = slotId,
            title = title,
            position = position
        )
    }
}
