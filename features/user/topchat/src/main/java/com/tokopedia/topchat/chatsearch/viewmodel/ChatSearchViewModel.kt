package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.data.GetMultiChatSearchResponse
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import com.tokopedia.topchat.chatsearch.view.uimodel.BigDividerUiModel
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchListHeaderUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSearchViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val getSearchQueryUseCase: GetSearchQueryUseCase
) : BaseViewModel(dispatcher) {

    val hasNext: Boolean get() = getSearchQueryUseCase.hasNext

    private var _loadInitialData = MutableLiveData<Boolean>()
    val loadInitialData: LiveData<Boolean> get() = _loadInitialData

    private var _emptyQuery = MutableLiveData<Boolean>()
    val emptyQuery: LiveData<Boolean> get() = _emptyQuery

    private var _triggerSearch = MutableLiveData<String>()
    val triggerSearch: LiveData<String> get() = _triggerSearch

    private var _errorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable> get() = _errorMessage

    private var _searchResults = MutableLiveData<List<Visitable<*>>>()
    val searchResults: LiveData<List<Visitable<*>>> get() = _searchResults

    var firstContactSearchResults: GetChatSearchResponse = GetChatSearchResponse()

    var query: String = ""
    private var page: Int = 1
    private var canRetry = false

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == query) return
        query = newQuery
        page = 1
        if (query.isEmpty()) {
            getSearchQueryUseCase.cancelRunningSearch()
            _emptyQuery.value = true
            return
        }
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        _loadInitialData.value = true
        doSearch()
    }

    fun loadPage(page: Int) {
        if (page > this.page && getSearchQueryUseCase.hasNext) {
            loadNextPage(page)
        } else if (page == this.page && canRetry) {
            retryLoadCurrentPage()
        }
    }

    fun resetLiveData() {
        _emptyQuery.value = false
        _searchResults.value = null
        _loadInitialData.value = false
        _errorMessage.value = null
        _triggerSearch.value = null
    }

    fun isFirstPage(): Boolean {
        return page == 1
    }

    private fun loadNextPage(nextPage: Int) {
        this.page = nextPage
        doSearch()
    }

    private fun retryLoadCurrentPage() {
        doSearch()
    }

    private fun doSearch() {
        _triggerSearch.value = query
        val isReplyOnly = !isFirstPage()
        getSearchQueryUseCase.doSearch(::onSuccessDoSearch, ::onErrorDoSearch, query, page, isReplyOnly)

    }

    private fun onSuccessDoSearch(
            response: GetMultiChatSearchResponse,
            searchListHeader: SearchListHeaderUiModel?,
            replyHeader: SearchListHeaderUiModel?
    ) {
        canRetry = false
        val searchContactResponse = GetChatSearchResponse(response.searchByName)
        if (isFirstPage()) {
            firstContactSearchResults = searchContactResponse
        }
        if (isFirstPage()) {
            val searchResults: MutableList<Visitable<*>> = ArrayList()
            if (searchListHeader != null) {
                searchResults.add(searchListHeader)
                if (searchContactResponse.searchResults.size > 5) {
                    searchResults.addAll(searchContactResponse.searchResults.subList(0, 5).toMutableList())
                } else {
                    searchResults.addAll(searchContactResponse.searchResults)
                }
            }
            if (replyHeader != null) {
                if (searchResults.isNotEmpty()) {
                    searchResults.add(BigDividerUiModel())
                }
                searchResults.add(replyHeader)
                searchResults.addAll(response.replySearchResults)
            }
            _searchResults.value = searchResults
        } else {
            _searchResults.value = response.replySearchResults
        }
    }

    private fun onErrorDoSearch(throwable: Throwable) {
        canRetry = true
        _errorMessage.value = throwable
    }
}