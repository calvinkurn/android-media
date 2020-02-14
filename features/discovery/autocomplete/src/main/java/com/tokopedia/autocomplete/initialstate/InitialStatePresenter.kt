package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.InitialStateViewModel
import com.tokopedia.autocomplete.initialstate.newfiles.*
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchUseCase
import com.tokopedia.autocomplete.initialstate.popularsearch.convertPopularSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.convertRecentSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.recentview.ReecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.convertRecentViewSearchToVisitableList
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
    lateinit var initialStateUseCaseFlow: InitialStateUseCaseFlow

    @Inject
    lateinit var initialStateUseCase: InitialStateUseCase
//    lateinit var autoCompleteUseCase: AutoCompleteUseCase

    @Inject
    lateinit var deleteRecentSearchUseCase: DeleteRecentSearchUseCaseNew
//    lateinit var deleteRecentSearchUseCase: DeleteRecentSearchUseCase

    @Inject
    lateinit var popularSearchUseCase: PopularSearchUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    private fun getInitialStateSubscriber(): Subscriber<List<InitialStateData>> = object : Subscriber<List<InitialStateData>>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(list: List<InitialStateData>) {
            val initialStateViewModel = InitialStateViewModel()
            initialStateViewModel.searchTerm = querySearch

            for (initialStateData in list) {
                if (initialStateData.items.isNotEmpty()) {
                    when (initialStateData.id) {
                        RECENT_SEARCH, RECENT_VIEW, POPULAR_SEARCH -> {
                            initialStateViewModel.addList(initialStateData)
                        }
                    }
                }
            }

            view.showInitialStateResult(initialStateViewModel)
        }
    }

    override fun search(searchParameter: SearchParameter) {
//        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
//            initialStateUseCaseFlow.getInitialStateDataFlow(
//                    searchParameter.getSearchParameterHashMap(),
//                    userSession.deviceId,
//                    userSession.userId
//            ).collect{
//                val initialStateViewModel = InitialStateViewModel()
//                initialStateViewModel.searchTerm = querySearch
//
//                for (initialStateData in it) {
//                    if (initialStateData.items.isNotEmpty()) {
//                        when (initialStateData.id) {
//                            RECENT_SEARCH, RECENT_VIEW, POPULAR_SEARCH -> {
//                                initialStateViewModel.addList(initialStateData)
//                            }
//                        }
//                    }
//                }
//                view.showInitialStateResult(initialStateViewModel)
//            }
//        }) {
//            it.printStackTrace()
//        }

        initialStateUseCase.execute(
                InitialStateUseCase.getParams(
                        searchParameter.getSearchParameterMap(),
                        userSession.deviceId,
                        userSession.userId
                ),
                getInitialStateSubscriber()
        )
    }

    override fun deleteRecentSearchItem(keyword: String) {
        val params = DeleteRecentSearchUseCaseNew.getParams(
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
        val params = DeleteRecentSearchUseCaseNew.getParams(
                userSession.deviceId,
                userSession.userId
        )
        deleteRecentSearchUseCase.execute(
                params,
                getInitialStateSubscriber()
        )
    }

    override fun refreshPopularSearch(searchParameter: SearchParameter) {
        val params = PopularSearchUseCase.getParams(
                searchParameter.getSearchParameterMap(),
                userSession.deviceId,
                userSession.userId
        )
    }

    override fun detachView() {
        super.detachView()
        initialStateUseCase.unsubscribe()
    }

    override fun attachView(view: InitialStateContract.View) {
        super.attachView(view)
    }

    override fun getInitialStateResult(list: MutableList<InitialStateData>, searchTerm: String): List<Visitable<*>> {
        val data = mutableListOf<Visitable<*>>()
        for (initialStateData in list) {
            when (initialStateData.id) {
                InitialStateData.INITIAL_STATE_RECENT_SEARCH -> {
                    data.addAll(
                            initialStateData.convertRecentSearchToVisitableList().insertTitleWithDeleteAll(initialStateData.header)
                    )
                }
                InitialStateData.INITIAL_STATE_RECENT_VIEW -> {
                    data.addAll(
                            initialStateData.convertRecentViewSearchToVisitableList().insertTitle(initialStateData.header)
                    )
                }
                InitialStateData.INITIAL_STATE_POPULAR_SEARCH -> {
                    data.addAll(
                            initialStateData.convertPopularSearchToVisitableList().insertTitleWithRefresh(initialStateData.header)
                    )
                }
            }
        }
        return data
    }

    private fun MutableList<Visitable<*>>.insertTitle(name: String): List<Visitable<*>> {
        val titleSearch = ReecentViewTitleViewModel()
        titleSearch.title = name
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertTitleWithDeleteAll(name: String): List<Visitable<*>> {
        val titleSearch = RecentSearchTitleViewModel(true)
        titleSearch.title = name
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertTitleWithRefresh(name: String): List<Visitable<*>> {
        val titleSearch = PopularSearchTitleViewModel(true)
        titleSearch.title = name
        this.add(0, titleSearch)
        return this
    }
}