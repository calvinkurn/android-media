package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSearchViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val getSearchQueryUseCase: GetSearchQueryUseCase
) : BaseViewModel(dispatcher) {

    var searchResult = MutableLiveData<String>()

    var query: String = ""

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == query || newQuery.isEmpty()) return
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        query = newQuery
        getSearchQueryUseCase.doSearch(::onSuccessDoSearch, ::onErrorDoSearch)
    }

    private fun onSuccessDoSearch() {

    }

    private fun onErrorDoSearch(throwable: Throwable) {

    }
}