package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.content.common.types.ResultState
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by kenny.hadisaputra on 16/10/23
 */
data class ItemListState<T>(
    val items: List<T>,
    val state: ResultState,
    val nextCursor: String,
    val hasNextPage: Boolean,
) {

    companion object {
        fun <T> init(state: ResultState): ItemListState<T> {
            return ItemListState(emptyList(), state, "", true)
        }

        fun <T> initLoading(): ItemListState<T> {
            return init(ResultState.Loading)
        }

        fun <T> initSuccess(
            items: List<T>,
            nextCursor: String = "",
            hasNextPage: Boolean = true,
        ): ItemListState<T> {
            return ItemListState(items, ResultState.Success, nextCursor, hasNextPage)
        }
    }
}
internal fun <T> ItemListState<T>.isNotEmpty(): Boolean {
    return items.isNotEmpty()
}

internal fun <T> ItemListState<T>.isEmpty(): Boolean {
    return items.isEmpty()
}

internal fun <T> ItemListState<T>?.orInitLoading(): ItemListState<T> {
    return this ?: ItemListState.initLoading()
}

internal val <T> ItemListState<T>.isLoading: Boolean
    get() = this.state == ResultState.Loading

internal object LoadingModel
