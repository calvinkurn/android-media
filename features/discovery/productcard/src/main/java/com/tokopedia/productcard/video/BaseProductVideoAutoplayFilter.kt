package com.tokopedia.productcard.video

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.utils.LayoutManagerUtil

abstract class BaseProductVideoAutoplayFilter : ProductVideoAutoplayFilter {
    abstract val recyclerView: RecyclerView?
    abstract val layoutManager: RecyclerView.LayoutManager?
    abstract val itemList: List<*>?

    override fun filterVisibleProductVideoPlayer(): List<ProductVideoPlayer> {
        val itemList = itemList ?: return emptyList()
        val firstVisibleItemIndex = LayoutManagerUtil.getFirstVisibleItemIndex(layoutManager, false)
        val lastVisibleItemIndex = LayoutManagerUtil.getLastVisibleItemIndex(layoutManager)
        if (itemList.isNotEmpty()
            && firstVisibleItemIndex != -1
            && lastVisibleItemIndex != -1
        ) {
            val visibleItems = getVisibleVideoItems(
                itemList,
                firstVisibleItemIndex,
                lastVisibleItemIndex
            )
            val currentlyVisibleViewHolders = getVisibleViewHolderList(itemList, visibleItems)
            return currentlyVisibleViewHolders
                .filterIsInstance<ProductVideoPlayer>()
        }
        return emptyList()
    }

    private fun getVisibleVideoItems(
        itemList: List<*>,
        firstVisibleItemIndex: Int,
        lastVisibleItemIndex: Int,
    ): List<ProductCardVideoItem> {
        val visibleItems = itemList.subList(
            firstVisibleItemIndex,
            lastVisibleItemIndex + 1
        )
        return filterProductVideoItem(visibleItems)
    }

    private fun filterProductVideoItem(itemList: List<*>): List<ProductCardVideoItem> {
        return itemList.filterIsInstance<ProductCardVideoItem>()
            .filter {
                it.hasProductVideo
            }
    }

    private fun getVisibleViewHolderList(
        itemList: List<*>,
        visibleItems: List<ProductCardVideoItem>,
    ): List<RecyclerView.ViewHolder> {
        return visibleItems.mapNotNull {
            val index = itemList.indexOf(it)
            if (index == -1) return@mapNotNull null
            recyclerView?.findViewHolderForAdapterPosition(index)
        }
    }
}