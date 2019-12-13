package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.network.constant.ErrorNetMessage.MESSAGE_ERROR_DEFAULT
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
    var errorMessage = MutableLiveData<String>()

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
            getSearchQueryUseCase.cancelRunningSearch()
            showEmpty.postValue(true)
            return
        }
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        loadInitialData.postValue(true)
        doSearch()
    }

    fun loadNextPage(nextPage: Int) {
        if (nextPage >= page && getSearchQueryUseCase.hasNext) {
            page = nextPage
            doSearch()
        }
    }

    fun isFirstPage(): Boolean {
        return page == 1
    }

    private fun doSearch() {
        getSearchQueryUseCase.doSearch(::onSuccessDoSearch, ::onErrorDoSearch, query, page)
    }

    private fun onSuccessDoSearch(response: GetChatSearchResponse) {
        _searchResults.postValue(response.searchResults)
    }

    private fun onErrorDoSearch(throwable: Throwable) {
        errorMessage.postValue(MESSAGE_ERROR_DEFAULT)
    }
}