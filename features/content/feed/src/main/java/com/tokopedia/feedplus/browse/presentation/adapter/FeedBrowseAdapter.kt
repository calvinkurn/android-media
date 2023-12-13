package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalAuthorsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
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
    scope: CoroutineScope,
    chipsListener: ChipsViewHolder.Listener,
    bannerListener: FeedBrowseBannerViewHolder.Item.Listener,
    channelListener: FeedBrowseHorizontalChannelsViewHolder.Listener,
    creatorListener: FeedBrowseHorizontalAuthorsViewHolder.Listener
) : FeedBrowseItemAdapter<List<FeedBrowseStatefulModel>>(
    scope,
    chipsListener,
    bannerListener,
    channelListener,
    creatorListener,
    object : InspirationCardViewHolder.Item.Listener {
        override fun onImpressed(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
        }

        override fun onClicked(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
        }
    }
) {

    fun setPlaceholder() {
        submitList(getPlaceholders())
    }

    override fun List<FeedBrowseStatefulModel>.mapToItems(): List<FeedBrowseItemListModel> {
        return flatMapIndexed { index, item ->
            when (item.model) {
                is FeedBrowseSlotUiModel.ChannelsWithMenus -> item.model.mapToChannelBlocks(
                    item.result,
                    index
                )
                is FeedBrowseSlotUiModel.InspirationBanner -> item.model.mapToItems(item.result, index)
                is FeedBrowseSlotUiModel.Authors -> item.model.mapToItems(item.result, index)
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
        state: ResultState,
        position: Int
    ): List<FeedBrowseItemListModel> {
        if (state.isFail || (state.isSuccess && bannerList.isEmpty())) return emptyList()

        val slotInfo = getSlotInfo(position)
        return buildList {
            add(FeedBrowseItemListModel.Title(slotInfo, title))
            addAll(
                if (state.isLoading) {
                    List(6) { FeedBrowseItemListModel.Banner.Placeholder }
                } else {
                    bannerList.mapIndexed { index, banner ->
                        FeedBrowseItemListModel.Banner.Item(slotInfo, banner, index)
                    }
                }
            )
        }
    }

    private fun FeedBrowseSlotUiModel.Authors.mapToItems(
        state: ResultState,
        position: Int
    ): List<FeedBrowseItemListModel> {
        if (state.isFail || (state.isSuccess && authorList.isEmpty())) return emptyList()

        val slotInfo = getSlotInfo(position)
        return buildList {
            add(FeedBrowseItemListModel.Title(slotInfo, title))
            add(
                FeedBrowseItemListModel.HorizontalAuthors(slotInfo, authorList, isLoading = state.isLoading)
            )
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
            FeedBrowseItemListModel.HorizontalAuthors.Loading
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
