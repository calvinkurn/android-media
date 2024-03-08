package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.util.WindowSizeClass
import com.tokopedia.content.common.util.WindowWidthSizeClass
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationMap
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.feedplus.browse.presentation.model.isEmpty
import com.tokopedia.feedplus.browse.presentation.model.isLoading
import kotlinx.coroutines.CoroutineScope

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal class CategoryInspirationAdapter(
    scope: CoroutineScope,
    sizeClass: WindowSizeClass,
    chipsListener: ChipsViewHolder.Listener,
    cardListener: InspirationCardViewHolder.Item.Listener
) : FeedBrowseItemAdapter<CategoryInspirationUiState>(
    scope,
    chipsListener = chipsListener,
    inspirationCardListener = cardListener,
    spanCount = when (sizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 3
        WindowWidthSizeClass.Expanded -> 4
    }
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
                add(
                    FeedBrowseItemListModel.Title(
                        slotInfo = SlotInfo.Empty,
                        title = "Konten paling menarik",
                    )
                )
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
