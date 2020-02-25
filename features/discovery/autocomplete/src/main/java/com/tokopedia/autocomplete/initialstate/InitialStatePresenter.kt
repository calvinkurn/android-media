package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.InitialStateViewModel
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.usecase.AutoCompleteUseCase
import com.tokopedia.autocomplete.usecase.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.viewmodel.TitleSearch
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class InitialStatePresenter @Inject constructor() : BaseDaggerPresenter<InitialStateContract.View>(), InitialStateContract.Presenter {

    companion object {
        const val RECENT_SEARCH = "recent_search"
        const val RECENT_VIEW = "recent_view"
        const val POPULAR_SEARCH = "popular_search"
    }

    private var querySearch = ""

    @Inject
    lateinit var autoCompleteUseCase: AutoCompleteUseCase

    @Inject
    lateinit var deleteRecentSearchUseCase: DeleteRecentSearchUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    private fun getInitialStateSubscriber() = object : Subscriber<List<SearchData>>() {
        override fun onCompleted() { }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(searchDatas: List<SearchData>) {
            val initialStateViewModel = InitialStateViewModel()
            initialStateViewModel.searchTerm = querySearch

            for (searchData in searchDatas) {
                if (searchData.items.size > 0) {
                    when (searchData.id) {
                        RECENT_SEARCH, RECENT_VIEW, POPULAR_SEARCH -> {
                            initialStateViewModel.addList(searchData)
                        }
                    }
                }
            }

            view.showInitialStateResult(initialStateViewModel)
        }
    }

    override fun search(searchParameter: SearchParameter) {
        autoCompleteUseCase.execute(
                AutoCompleteUseCase.getParams(
                        searchParameter.getSearchParameterMap(),
                        userSession.deviceId,
                        userSession.userId
                ),
                getInitialStateSubscriber()
        )
    }

    override fun deleteRecentSearchItem(keyword: String) {
        val params = DeleteRecentSearchUseCase.getParams(
                keyword,
                userSession.deviceId,
                userSession.userId
        )
        deleteRecentSearchUseCase.execute(
                params,
                getInitialStateSubscriber()
        )
    }

    override fun deleteAllRecentSearch() {
        val params = DeleteRecentSearchUseCase.getParams(
                userSession.deviceId,
                userSession.userId
        )
        deleteRecentSearchUseCase.execute(
                params,
                getInitialStateSubscriber()
        )
    }

    override fun detachView() {
        super.detachView()
        autoCompleteUseCase.unsubscribe()
    }

    override fun attachView(view: InitialStateContract.View) {
        super.attachView(view)
    }

    override fun getInitialStateResult(list: MutableList<SearchData>, searchTerm: String): List<Visitable<*>>{
        val data = mutableListOf<Visitable<*>>()
        for (searchData in list) {
            when (searchData.id) {
                SearchData.AUTOCOMPLETE_RECENT_SEARCH -> {
                    data.addAll(
                            searchData.convertRecentSearchToVisitableList(searchTerm).insertTitleWithDeleteAll(searchData.name)
                    )
                }
                SearchData.AUTOCOMPLETE_RECENT_VIEW -> {
                    data.addAll(
                            searchData.convertRecentViewSearchToVisitableList(searchTerm).insertTitle(searchData.name)
                    )
                }
                SearchData.AUTOCOMPLETE_POPULAR_SEARCH -> {
                    data.addAll(
                            searchData.convertPopularSearchToVisitableList(searchTerm).insertTitle(searchData.name)
                    )
                }
            }
        }
        return data
    }

    private fun MutableList<Visitable<*>>.insertTitle(name: String): List<Visitable<*>> {
        val titleSearch = TitleSearch()
        titleSearch.title = name
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertTitleWithDeleteAll(name: String): List<Visitable<*>> {
        val titleSearch = TitleSearch(true)
        titleSearch.title = name
        this.add(0, titleSearch)
        return this
    }
}