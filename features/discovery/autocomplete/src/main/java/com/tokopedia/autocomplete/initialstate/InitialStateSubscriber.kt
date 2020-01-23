package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.InitialStateViewModel
import com.tokopedia.autocomplete.domain.model.SearchData
import rx.Subscriber

class InitialStateSubscriber(
        private val querySearch: String,
        private val initialStateViewModel: InitialStateViewModel,
        private val view: InitialStateContract.View
) : Subscriber<List<SearchData>>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onNext(searchDatas: List<SearchData>) {
        loop@ for (searchData in searchDatas) {
            if (searchData.items.size > 0) {
                when (searchData.id) {
                    RECENT_SEARCH, RECENT_VIEW, POPULAR_SEARCH -> {
                        initialStateViewModel.searchTerm = querySearch
                        initialStateViewModel.addList(searchData)
                        continue@loop
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