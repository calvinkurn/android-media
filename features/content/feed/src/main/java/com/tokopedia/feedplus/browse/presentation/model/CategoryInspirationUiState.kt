package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 31/10/23
 */
internal data class CategoryInspirationData(
    val menu: WidgetMenuModel,
    val items: FeedBrowseChannelListState<PlayWidgetChannelUiModel>
)
internal typealias CategoryInspirationMap = Map<String, CategoryInspirationData>
internal data class CategoryInspirationUiState(
    val title: String,
    val items: CategoryInspirationMap,
    val selectedMenuId: String,
    val state: ResultState
) {

    companion object {
        fun empty(state: ResultState): CategoryInspirationUiState {
            return CategoryInspirationUiState(
                title = "",
                items = emptyMap(),
                selectedMenuId = "",
                state = state
            )
        }
    }
}
