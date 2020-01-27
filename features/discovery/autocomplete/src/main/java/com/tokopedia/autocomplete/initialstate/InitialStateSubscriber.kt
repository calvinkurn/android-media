package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.InitialStateViewModel
import com.tokopedia.autocomplete.domain.model.SearchData
import rx.Subscriber

class InitialStateSubscriber(
        private var querySearch: String,
        private val initialStateViewModel: InitialStateViewModel,
        private val view: InitialStateContract.View
) : Subscriber<List<SearchData>>() {

    fun setQuerySearch(querySearch: String){
        this.querySearch = querySearch
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onNext(searchDatas: List<SearchData>) {
        for (searchData in searchDatas) {
            if (searchData.items.size > 0) {
                when (searchData.id) {
                    RECENT_SEARCH, RECENT_VIEW, POPULAR_SEARCH -> {
                        initialStateViewModel.searchTerm = querySearch
                        initialStateViewModel.addList(searchData)
                    }
                }
            }
        }
        view.showInitialStateResult(initialStateViewModel)
    }

    companion object {
        const val RECENT_SEARCH = "recent_search"
        const val RECENT_VIEW = "recent_view"
        const val POPULAR_SEARCH = "popular_search"
    }
}