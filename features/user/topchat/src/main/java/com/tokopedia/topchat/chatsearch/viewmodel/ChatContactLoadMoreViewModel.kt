package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import com.tokopedia.topchat.chatsearch.view.uimodel.ContactLoadMoreUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatContactLoadMoreViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val getSearchQueryUseCase: GetSearchQueryUseCase
) : BaseViewModel(dispatcher) {

    private var _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResult: LiveData<List<SearchResult>> get() = _searchResults

    fun loadSearchResult(page: Int, query: String, firstResponse: List<SearchResult>) {
        if (firstResponse.isEmpty() && page == 1) {
            getSearchQueryUseCase.doSearch(::onSuccessSearchContact, ::onErrorSearchContact, query, page)
        } else {
            _searchResults.value = firstResponse
        }
    }

    private fun onSuccessSearchContact(getChatSearchResponse: GetChatSearchResponse, contactLoadMoreUiModel: ContactLoadMoreUiModel?) {

    }

    private fun onErrorSearchContact(throwable: Throwable) {

    }

}