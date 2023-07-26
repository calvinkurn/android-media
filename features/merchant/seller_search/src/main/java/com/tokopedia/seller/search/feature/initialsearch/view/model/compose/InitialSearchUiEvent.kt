package com.tokopedia.seller.search.feature.initialsearch.view.model.compose

import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemHighlightInitialSearchUiModel

sealed class InitialSearchUiEvent {

    data class OnClearAllHistory(
        val titleList: List<String>
    ) : InitialSearchUiEvent()
    data class OnItemHistoryClicked(
        val itemTitle: String
    ) : InitialSearchUiEvent()

    data class OnItemRemoveClicked(
        val title: String,
        val position: Int
    ) : InitialSearchUiEvent()

    data class OnItemRecommendationClicked(
        val item: ItemHighlightInitialSearchUiModel,
        val position: Int
    ) : InitialSearchUiEvent()
}
