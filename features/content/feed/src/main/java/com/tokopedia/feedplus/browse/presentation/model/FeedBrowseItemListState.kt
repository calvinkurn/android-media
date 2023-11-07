package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by kenny.hadisaputra on 16/10/23
 */
data class FeedBrowseItemListState<T>(
    val items: List<T>,
    val state: ResultState,
    val nextCursor: String,
    val hasNextPage: Boolean,
    val config: PlayWidgetConfigUiModel
) {

    companion object {
        fun <T> init(state: ResultState): FeedBrowseItemListState<T> {
            return FeedBrowseItemListState(
                emptyList(),
                state,
                "",
                true,
                PlayWidgetConfigUiModel.Empty
            )
        }

        fun <T> initLoading(): FeedBrowseItemListState<T> {
            return init(ResultState.Loading)
        }

        fun <T> initFail(error: Throwable): FeedBrowseItemListState<T> {
            return init(ResultState.Fail(error))
        }

        fun <T> initSuccess(
            items: List<T>,
            nextCursor: String = "",
            hasNextPage: Boolean = true,
            config: PlayWidgetConfigUiModel = PlayWidgetConfigUiModel.Empty
        ): FeedBrowseItemListState<T> {
            return FeedBrowseItemListState(items, ResultState.Success, nextCursor, hasNextPage, config)
        }
    }
}
internal fun <T> FeedBrowseItemListState<T>.isNotEmpty(): Boolean {
    return items.isNotEmpty()
}

internal fun <T> FeedBrowseItemListState<T>.isEmpty(): Boolean {
    return items.isEmpty()
}

internal fun <T> FeedBrowseItemListState<T>?.orInitLoading(): FeedBrowseItemListState<T> {
    return this ?: FeedBrowseItemListState.initLoading()
}

internal val <T> FeedBrowseItemListState<T>.isLoading: Boolean
    get() = this.state == ResultState.Loading

internal object LoadingModel
