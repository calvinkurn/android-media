package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by kenny.hadisaputra on 16/10/23
 */
data class FeedBrowseChannelListState<T>(
    val items: List<T>,
    val state: ResultState,
    val nextCursor: String,
    val hasNextPage: Boolean,
    val config: PlayWidgetConfigUiModel
) {

    companion object {
        fun <T> init(state: ResultState): FeedBrowseChannelListState<T> {
            return FeedBrowseChannelListState(
                emptyList(),
                state,
                "",
                true,
                PlayWidgetConfigUiModel.Empty
            )
        }

        fun <T> initLoading(): FeedBrowseChannelListState<T> {
            return init(ResultState.Loading)
        }

        fun <T> initFail(error: Throwable): FeedBrowseChannelListState<T> {
            return init(ResultState.Fail(error))
        }

        fun <T> initSuccess(
            items: List<T>,
            nextCursor: String = "",
            hasNextPage: Boolean = true,
            config: PlayWidgetConfigUiModel = PlayWidgetConfigUiModel.Empty
        ): FeedBrowseChannelListState<T> {
            return FeedBrowseChannelListState(items, ResultState.Success, nextCursor, hasNextPage, config)
        }
    }
}
internal fun <T> FeedBrowseChannelListState<T>.isNotEmpty(): Boolean {
    return items.isNotEmpty()
}

internal fun <T> FeedBrowseChannelListState<T>.isEmpty(): Boolean {
    return items.isEmpty()
}

internal fun <T> FeedBrowseChannelListState<T>?.orInitLoading(): FeedBrowseChannelListState<T> {
    return this ?: FeedBrowseChannelListState.initLoading()
}

internal val <T> FeedBrowseChannelListState<T>.isLoading: Boolean
    get() = this.state == ResultState.Loading
