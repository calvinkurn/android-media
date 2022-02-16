package com.tokopedia.productcard.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

internal object LayoutManagerUtil {
    fun getFirstVisibleItemIndex(
        layoutManager: RecyclerView.LayoutManager?,
        isCompletelyVisible: Boolean
    ): Int {
        return when (layoutManager) {
            is StaggeredGridLayoutManager -> getFirstVisibleItemIndex(layoutManager, isCompletelyVisible)
            is GridLayoutManager -> getFirstVisibleItemIndex(layoutManager, isCompletelyVisible)
            is LinearLayoutManager -> getFirstVisibleItemIndex(layoutManager, isCompletelyVisible)
            else -> -1
        }
    }

    private fun getFirstVisibleItemIndex(
        layoutManager: LinearLayoutManager,
        isCompletelyVisible: Boolean
    ): Int {
        val firstCompleteVisibleItemIndex = layoutManager.findFirstCompletelyVisibleItemPosition()
        val firstVisibleItemIndex = layoutManager.findFirstVisibleItemPosition()
        return when {
            firstCompleteVisibleItemIndex != -1 && isCompletelyVisible -> firstCompleteVisibleItemIndex
            firstVisibleItemIndex != -1 -> firstVisibleItemIndex
            else -> -1
        }
    }

    private fun getFirstVisibleItemIndex(
        layoutManager: GridLayoutManager,
        isCompletelyVisible: Boolean
    ): Int {
        val firstCompleteVisibleItemIndex = layoutManager.findFirstCompletelyVisibleItemPosition()
        val firstVisibleItemIndex = layoutManager.findFirstVisibleItemPosition()
        return when {
            firstCompleteVisibleItemIndex != -1 && isCompletelyVisible -> firstCompleteVisibleItemIndex
            firstVisibleItemIndex != -1 -> firstVisibleItemIndex
            else -> -1
        }
    }

    private fun getFirstVisibleItemIndex(
        layoutManager: StaggeredGridLayoutManager,
        isCompletelyVisible: Boolean
    ): Int {
        val firstCompleteVisibleItemIndex = layoutManager
            .findFirstCompletelyVisibleItemPositions(null)
            .minOrNull() ?: -1
        val firstVisibleItemIndex = layoutManager
            .findFirstVisibleItemPositions(null)
            .minOrNull() ?: -1
        return when {
            firstCompleteVisibleItemIndex != -1 && isCompletelyVisible -> firstCompleteVisibleItemIndex
            else -> firstVisibleItemIndex
        }
    }

    fun getLastVisibleItemIndex(layoutManager: RecyclerView.LayoutManager?): Int {
        return when (layoutManager) {
            is StaggeredGridLayoutManager -> getLastVisibleItemIndex(layoutManager)
            is GridLayoutManager -> getLastVisibleItemIndex(layoutManager)
            is LinearLayoutManager -> getLastVisibleItemIndex(layoutManager)
            else -> -1
        }
    }

    private fun getLastVisibleItemIndex(layoutManager: StaggeredGridLayoutManager): Int {
        return layoutManager
            .findLastVisibleItemPositions(null)
            .maxOrNull() ?: -1
    }

    private fun getLastVisibleItemIndex(layoutManager: GridLayoutManager): Int {
        return layoutManager.findLastVisibleItemPosition()
    }

    private fun getLastVisibleItemIndex(layoutManager: LinearLayoutManager): Int {
        return layoutManager.findLastVisibleItemPosition()
    }
}