package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.action.FeedSearchResultAction
import com.tokopedia.feedplus.browse.presentation.model.state.FeedSearchResultPageState
import com.tokopedia.feedplus.browse.presentation.model.state.FeedSearchResultUiState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

internal class FeedSearchResultViewModel @AssistedInject constructor(
    @Assisted private val searchKeyword: String,
    private val repo: FeedBrowseRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            searchKeyword: String
        ): FeedSearchResultViewModel
    }

    private val _pageState = MutableStateFlow<FeedSearchResultPageState>(FeedSearchResultPageState.Unknown)

    val uiState = combine(
        _pageState,
        _pageState
    ) { pageState, _ ->
        FeedSearchResultUiState(
            searchKeyword = searchKeyword,
            pageState = pageState,
        )
    }

    fun submitAction(action: FeedSearchResultAction) {
        when (action) {
            is FeedSearchResultAction.LoadResult -> handleLoadResult()
        }
    }

    private fun handleLoadResult() {
        viewModelScope.launchCatchError(block = {

            if (_pageState.value is FeedSearchResultPageState.Loading) return@launchCatchError

            _pageState.update { FeedSearchResultPageState.Loading }
            
            val response = repo.getWidgetContentSlot(
                extraParam = WidgetRequestModel(
                    group = "content_browse_search",
                    sourceType = "",
                    sourceId = "0",
                    searchKeyword = searchKeyword,
                )
            )

            _pageState.update { FeedSearchResultPageState.Success }

        }) {
            _pageState.update { FeedSearchResultPageState.Loading }
        }
    }
}

// Temp data model
data class SearchTempDataModel (
    val resultList: List<CardDetail>?,
    val uiState: FeedSearchResultUiState
) {
    data class CardDetail(
        val title: String,
        val imgUrl: String
    )
}
