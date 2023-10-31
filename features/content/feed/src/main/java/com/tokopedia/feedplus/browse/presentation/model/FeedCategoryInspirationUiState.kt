package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 31/10/23
 */
internal typealias FeedCategoryInspirationItems = Map<WidgetMenuModel, ItemListState<PlayWidgetChannelUiModel>>
internal data class FeedCategoryInspirationUiState(
    val title: String,
    val items: FeedCategoryInspirationItems,
    val selectedMenuId: String,
    val state: ResultState,
) {

    companion object {
        fun empty(state: ResultState): FeedCategoryInspirationUiState {
            return FeedCategoryInspirationUiState(
                title = "",
                items = emptyMap(),
                selectedMenuId = "",
                state = state,
            )
        }
    }
}
