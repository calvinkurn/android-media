package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.usecase.GetSearchContactQueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatContactLoadMoreViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val searchContactQuery: GetSearchContactQueryUseCase
) : BaseViewModel(dispatcher) {

    private var _searchResults = MutableLiveData<GetChatSearchResponse>()
    val searchResult: LiveData<GetChatSearchResponse> get() = _searchResults

    private var _errorSearchResults = MutableLiveData<Throwable>()
    val errorSearchResults: LiveData<Throwable> get() = _errorSearchResults

    fun loadSearchResult(page: Int, query: String, firstResponse: GetChatSearchResponse) {
        searchContactQuery.doSearch(
                ::onSuccessSearchContact, ::onErrorSearchContact, query, page, firstResponse
        )
    }

    private fun onSuccessSearchContact(getChatSearchResponse: GetChatSearchResponse) {
        _searchResults.value = getChatSearchResponse
    }

    private fun onErrorSearchContact(throwable: Throwable) {
        _errorSearchResults.value = throwable
    }

}