package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalAuthorsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationMap
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.feedplus.browse.presentation.model.isEmpty
import com.tokopedia.feedplus.browse.presentation.model.isLoading
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal class CategoryInspirationAdapter(
    chipsListener: ChipsViewHolder.Listener,
    cardListener: InspirationCardViewHolder.Item.Listener
) : FeedBrowseItemAdapter<CategoryInspirationUiState>(
    CoroutineScope(Dispatchers.Main),
    chipsListener,
    object : FeedBrowseBannerViewHolder.Item.Listener {
        override fun onBannerImpressed(
            viewHolder: FeedBrowseBannerViewHolder.Item,
            item: FeedBrowseItemListModel.Banner.Item
        ) {
        }

        override fun onBannerClicked(
            viewHolder: FeedBrowseBannerViewHolder.Item,
            item: FeedBrowseItemListModel.Banner.Item
        ) {
        }
    },
    object : FeedBrowseHorizontalChannelsViewHolder.Listener {
        override fun onRetry(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            slotId: String,
            menu: WidgetMenuModel
        ) {
        }

        override fun onRefresh(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            slotId: String,
            menu: WidgetMenuModel
        ) {
        }

        override fun onCardImpressed(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalChannels,
            channel: PlayWidgetChannelUiModel,
            channelPosition: Int
        ) {
        }

        override fun onCardClicked(
            viewHolder: FeedBrowseHorizontalChannelsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalChannels,
            channel: PlayWidgetChannelUiModel,
            channelPosition: Int
        ) {
        }
    },
    object : FeedBrowseHorizontalAuthorsViewHolder.Listener {
        override fun onWidgetImpressed(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        ) {
        }

        override fun onChannelClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        ) {
        }

        override fun onAuthorClicked(
            viewHolder: FeedBrowseHorizontalAuthorsViewHolder,
            widgetModel: FeedBrowseItemListModel.HorizontalAuthors,
            item: AuthorWidgetModel,
            authorWidgetPosition: Int
        ) {
        }
    },
    cardListener
) {

    fun setList(
        state: ResultState,
        items: CategoryInspirationMap,
        selectedMenuId: String,
        onCommit: () -> Unit = {}
    ) {
        submitList(
            if (state.isLoading) getPlaceholders() else items.mapToListItems(state, selectedMenuId),
            onCommit
        )
    }

    private fun getPlaceholders(): List<FeedBrowseItemListModel> {
        return buildList {
            add(FeedBrowseItemListModel.Chips.Placeholder)
            addAll(List(6) { FeedBrowseItemListModel.InspirationCard.Placeholder })
        }
    }

    override fun CategoryInspirationUiState.mapToItems(): List<FeedBrowseItemListModel> {
        return items.mapToListItems(state, selectedMenuId)
    }

    private fun CategoryInspirationMap.mapToListItems(
        pageState: ResultState,
        selectedMenuId: String
    ): List<FeedBrowseItemListModel> {
        return buildList {
            val isMenuEmpty = keys.isEmpty() || (keys.size == 1 && keys.first().isBlank())
            val selectedData = get(selectedMenuId) ?: values.firstOrNull()
            val menuItem = selectedData?.items ?: return@buildList

            if (!isMenuEmpty) {
                add(
                    FeedBrowseItemListModel.Chips.Item(
                        SlotInfo.Empty,
                        entries.map {
                            ChipsModel(it.value.menu, it.key == selectedMenuId)
                        }
                    )
                )
            }

            if (menuItem.isLoading && menuItem.isEmpty()) {
                addAll(List(6) { FeedBrowseItemListModel.InspirationCard.Placeholder })
            } else {
                addAll(
                    menuItem.items.mapIndexed { index, data ->
                        FeedBrowseItemListModel.InspirationCard.Item(
                            SlotInfo.Empty,
                            data,
                            menuItem.config,
                            index
                        )
                    }
                )
                if (menuItem.isLoading && !pageState.isLoading) { add(FeedBrowseItemListModel.LoadingModel) }
            }
        }
    }
}
