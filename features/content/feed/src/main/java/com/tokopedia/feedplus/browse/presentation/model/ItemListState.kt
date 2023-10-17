package com.tokopedia.feedplus.browse.presentation.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by kenny.hadisaputra on 16/10/23
 */
internal sealed interface ItemListState<out T> {
    object Loading : ItemListState<Nothing>
    data class HasContent<T>(val items: List<T>) : ItemListState<T>
}

@OptIn(ExperimentalContracts::class)
internal fun <T> ItemListState<T>.hasContent(): Boolean {
    contract {
        returns(true) implies (this@hasContent is ItemListState.HasContent)
    }
    return this is ItemListState.HasContent
}

internal fun <T> ItemListState<T>.hasContentAndNotEmpty(): Boolean {
    return this is ItemListState.HasContent && items.isNotEmpty()
}

internal val <T> ItemListState<T>.isLoading: Boolean
    get() = this == ItemListState.Loading

internal object LoadingModel
