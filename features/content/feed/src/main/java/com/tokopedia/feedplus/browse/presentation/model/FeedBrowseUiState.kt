package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.HeaderDataModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal sealed class FeedBrowseUiState {
    object Placeholder : FeedBrowseUiState()
    data class Error(val throwable: Throwable) : FeedBrowseUiState()
    data class Success(
        val headerData: HeaderDataModel,
        val widgets: List<FeedBrowseStatefulModel>
    ) : FeedBrowseUiState()
}
