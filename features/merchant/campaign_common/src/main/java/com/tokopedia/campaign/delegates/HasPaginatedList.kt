package com.tokopedia.campaign.delegates

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView


interface HasPaginatedList {
    data class Config(
        val pageSize: Int,
        val onLoadNextPage: () -> Unit = {},
        val onLoadNextPageFinished: () -> Unit = {}
    )

    fun attachPaging(
        recyclerView: RecyclerView,
        config: Config,
        loadNextPage: (Int, Int) -> Unit
    )

    fun attachPagingWithNestedScrollView(
        nestedScrollView: NestedScrollView,
        config: Config,
        loadNextPage: () -> Unit
    )

    fun notifyLoadResult(hasNextPage: Boolean)
    fun resetPaging()
}