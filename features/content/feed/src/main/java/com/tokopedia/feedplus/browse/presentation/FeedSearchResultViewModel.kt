package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.action.FeedSearchResultAction
import com.tokopedia.feedplus.browse.presentation.model.exception.RestrictedKeywordException
import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent
import com.tokopedia.feedplus.browse.presentation.model.state.FeedSearchResultPageState
import com.tokopedia.feedplus.browse.presentation.model.state.FeedSearchResultUiState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

internal class FeedSearchResultViewModel @AssistedInject constructor(
    @Assisted val searchKeyword: String,
    private val repo: FeedBrowseRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            searchKeyword: String
        ): FeedSearchResultViewModel
    }

    private val _cursor = MutableStateFlow("")

    private val _pageState = MutableStateFlow(FeedSearchResultPageState.UNKNOWN)
    private val _contents = MutableStateFlow<List<FeedSearchResultContent>>(emptyList())
    private val _hasNextPage = MutableStateFlow(true)

    val uiState = combine(
        _pageState,
        _contents,
        _hasNextPage,
    ) { pageState, contents, hasNextPage  ->
        FeedSearchResultUiState(
            searchKeyword = searchKeyword,
            pageState = pageState,
            contents = contents,
            hasNextPage = hasNextPage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedSearchResultUiState.empty(searchKeyword),
    )

    fun submitAction(action: FeedSearchResultAction) {
        when (action) {
            is FeedSearchResultAction.LoadResult -> handleLoadResult()
        }
    }

    private fun handleLoadResult() {
        viewModelScope.launchCatchError(block = {

            if (_pageState.value == FeedSearchResultPageState.LOADING ||
                !_hasNextPage.value
            ) return@launchCatchError

            _pageState.update { FeedSearchResultPageState.LOADING }
            
            val response = repo.getWidgetContentSlot(
                extraParam = WidgetRequestModel(
                    group = FEED_LOCAL_SEARCH_GROUP,
                    sourceType = "",
                    sourceId = "0",
                    cursor = _cursor.value,
                    searchKeyword = searchKeyword,
                )
            )

            when (response) {
                is ContentSlotModel.ChannelBlock -> {
                    if (response.channels.isEmpty() && _contents.value.isEmpty()) {
                        _pageState.update { FeedSearchResultPageState.NOT_FOUND }
                        _hasNextPage.update { false }
                        return@launchCatchError
                    }

                    val newContents = response.channels.map {
                        FeedSearchResultContent.Channel(it, response.config)
                    }

                    _contents.update {
                        val prevContents = it.ifEmpty {
                            listOf(FeedSearchResultContent.Title(response.title))
                        }
                        prevContents + newContents
                    }
                    _cursor.update { response.nextCursor }
                    _hasNextPage.update { response.nextCursor.isNotEmpty() }
                    _pageState.update { FeedSearchResultPageState.SUCCESS }
                }
                is ContentSlotModel.NoData -> {
                    _hasNextPage.update { false }

                    if (_contents.value.isEmpty()) {
                        _pageState.update { FeedSearchResultPageState.NOT_FOUND }
                    } else {
                        _pageState.update { FeedSearchResultPageState.SUCCESS }
                    }
                }
                else -> {
                    throw Exception("not handled")
                }
            }

        }) { throwable ->
            _pageState.update {
                if (throwable is RestrictedKeywordException)
                    FeedSearchResultPageState.RESTRICTED
                else
                    FeedSearchResultPageState.INTERNAL_ERROR
            }
        }
    }

    companion object {
        private const val FEED_LOCAL_SEARCH_GROUP = "content_browse_search"
    }
}
