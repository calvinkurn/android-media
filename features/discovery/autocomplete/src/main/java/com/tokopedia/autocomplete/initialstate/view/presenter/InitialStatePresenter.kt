package com.tokopedia.autocomplete.initialstate.view.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.InitialStateViewModel
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.initialstate.utils.convertPopularSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.utils.convertRecentSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.utils.convertRecentViewSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.view.InitialStateContract
import com.tokopedia.autocomplete.initialstate.view.subscriber.InitialStateSubscriber
import com.tokopedia.autocomplete.usecase.AutoCompleteUseCase
import com.tokopedia.autocomplete.usecase.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.viewmodel.TitleSearch
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InitialStatePresenter @Inject constructor() : BaseDaggerPresenter<InitialStateContract.View>(), InitialStateContract.Presenter {

    private var querySearch = ""

    @Inject
    lateinit var autoCompleteUseCase: AutoCompleteUseCase

    @Inject
    lateinit var deleteRecentSearchUseCase: DeleteRecentSearchUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun search(searchParameter: SearchParameter) {
        this.querySearch = searchParameter.getSearchQuery()
        autoCompleteUseCase.execute(
                AutoCompleteUseCase.getParams(
                        searchParameter.getSearchParameterMap(),
                        userSession.deviceId,
                        userSession.userId
                ),
                InitialStateSubscriber(querySearch,
                        InitialStateViewModel(),
                        view)
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
                InitialStateSubscriber(querySearch,
                        InitialStateViewModel(),
                        view)
        )
    }

    override fun deleteAllRecentSearch() {
        val params = DeleteRecentSearchUseCase.getParams(
                userSession.deviceId,
                userSession.userId
        )
        deleteRecentSearchUseCase.execute(
                params,
                InitialStateSubscriber("",
                        InitialStateViewModel(),
                        view)
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
        loop@ for (searchData in list) {
            when (searchData.id) {
                SearchData.AUTOCOMPLETE_RECENT_SEARCH -> {
                    data.addAll(
                            searchData.convertRecentSearchToVisitableList(searchTerm).insertTitleWithDeleteAll(searchData.name)
                    )
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_RECENT_VIEW -> {
                    data.addAll(
                            searchData.convertRecentViewSearchToVisitableList(searchTerm).insertTitle(searchData.name)
                    )
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_POPULAR_SEARCH -> {
                    data.addAll(
                            searchData.convertPopularSearchToVisitableList(searchTerm).insertTitle(searchData.name)
                    )
                    continue@loop
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