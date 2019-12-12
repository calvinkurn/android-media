package com.tokopedia.topchat.chatsearch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSearchViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val getSearchQueryUseCase: GetSearchQueryUseCase
) : BaseViewModel(dispatcher) {

    var searchResult = MutableLiveData<String>()

    var query: String = ""
    var page: Int = 1

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == query || newQuery.isEmpty()) return
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        query = newQuery
        page = 1
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
        val searchResults = response.chatSearch.contact.data
        Log.d("DO_SEARCH", "query: $query, page: $page")
    }

    private fun onErrorDoSearch(throwable: Throwable) {
        Log.d("DO_SEARCH", "query: $query, page: $page")
    }
}