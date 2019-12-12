package com.tokopedia.topchat.chatsearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSearchViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val getSearchQueryUseCase: GetSearchQueryUseCase
) : BaseViewModel(dispatcher) {

    val hasNext: Boolean get() = getSearchQueryUseCase.hasNext

    var loadInitialData = MutableLiveData<Boolean>()
    var showEmpty = MutableLiveData<Boolean>()

    private var _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResult: LiveData<List<SearchResult>>
        get() = _searchResults

    var query: String = ""
    var page: Int = 1

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == query) return
        query = newQuery
        page = 1
        if (query.isEmpty()) {
            showEmpty.postValue(true)
            return
        }
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        loadInitialData.postValue(true)
        doSearch()
    }

    fun loadNextPage(nextPage: Int) {
        if (nextPage > page && getSearchQueryUseCase.hasNext) {
            page = nextPage
            doSearch()
        }
    }

    private fun doSearch() {
        getSearchQueryUseCase.doSearch(::onSuccessDoSearch, ::onErrorDoSearch, query, page)
    }

    private fun onSuccessDoSearch(response: GetChatSearchResponse) {
        val searchResults = response.chatSearch.contact.searchResults
        _searchResults.postValue(searchResults)
    }

    private fun onErrorDoSearch(throwable: Throwable) {
        Log.d("DO_SEARCH", "query: $query, page: $page")
    }
}