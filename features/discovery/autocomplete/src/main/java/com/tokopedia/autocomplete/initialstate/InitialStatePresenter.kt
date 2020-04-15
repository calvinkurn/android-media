package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import com.tokopedia.autocomplete.initialstate.popularsearch.convertPopularSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.convertRecentSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.recentview.ReecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.convertRecentViewSearchToVisitableList
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class InitialStatePresenter @Inject constructor(
        private val initialStateUseCase: UseCase<List<InitialStateData>>,
        private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase,
        private val refreshPopularSearchUseCase: RefreshPopularSearchUseCase,
        private val userSession: UserSessionInterface
) : BaseDaggerPresenter<InitialStateContract.View>(), InitialStateContract.Presenter {

    companion object {
        const val RECENT_SEARCH = "recent_search"
        const val RECENT_VIEW = "recent_view"
        const val POPULAR_SEARCH = "popular_search"
    }

    private var querySearch = ""
    private var listVistable = mutableListOf<Visitable<*>>()
    private var searchParameter = HashMap<String, String>()

    fun setSearchParameter(searchParameter: HashMap<String, String>) {
        this.searchParameter = searchParameter
    }

    fun getSearchParameter(): Map<String, String> {
        return searchParameter
    }

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

            listVistable = getInitialStateResult(initialStateViewModel.list)
            view.showInitialStateResult(listVistable)
        }
    }

    private fun getPopularSearchSubscriber(): Subscriber<List<InitialStateData>> = object : Subscriber<List<InitialStateData>>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(listData: List<InitialStateData>) {
            val initialStateViewModel = InitialStateViewModel()
            initialStateViewModel.searchTerm = querySearch
            val newData: InitialStateData? = listData.find { it.id == POPULAR_SEARCH }
            var refreshedPopularSearchData = listOf<BaseItemInitialStateSearch>()
            newData?.let {
                refreshedPopularSearchData = convertListPopularSearchToBaseItemInitialStateSearch(it.items)
            }

            listVistable.forEachIndexed { _, visitable ->
                if (visitable is PopularSearchViewModel) {
                    visitable.list = refreshedPopularSearchData
                }
            }

            view.refreshPopularSearch(listVistable)
        }
    }

    private fun getDeleteRecentSearchSubscriber(keyword: String): Subscriber<Boolean> = object : Subscriber<Boolean>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(isSuccessful: Boolean) {
            if (isSuccessful) {
                var needDelete = false
                listVistable.forEachIndexed { _, visitable ->
                    if (visitable is RecentSearchViewModel) {
                        if (visitable.list.size == 1) {
                            needDelete = true
                        } else {
                            val deleted = visitable.list.find { it.title == keyword }
                            visitable.list.remove(deleted)
                        }
                    }
                }
                if (needDelete) {
                    removeRecentSearchTitle()
                    removeRecentSearch()
                }
                view.deleteRecentSearch(listVistable)
            }
        }
    }

    private fun removeRecentSearchTitle() {
        val titleViewModel = listVistable.filterIsInstance<RecentSearchTitleViewModel>()
        listVistable.removeAll(titleViewModel)
    }

    private fun removeRecentSearch() {
        val recentSearchViewModel = listVistable.filterIsInstance<RecentSearchViewModel>()
        listVistable.removeAll(recentSearchViewModel)
    }

    private fun getDeleteAllRecentSearchSubscriber(): Subscriber<Boolean> = object : Subscriber<Boolean>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(isSuccessful: Boolean) {
            if (isSuccessful) {
                removeRecentSearchTitle()
                removeRecentSearch()
                view.deleteRecentSearch(listVistable)
            }
        }
    }

    private fun getInitialStateResult(list: MutableList<InitialStateData>): MutableList<Visitable<*>> {
        val data = mutableListOf<Visitable<*>>()
        for (initialStateData in list) {
            when (initialStateData.id) {
                InitialStateData.INITIAL_STATE_RECENT_SEARCH -> {
                    data.addAll(
                            initialStateData.convertRecentSearchToVisitableList().insertTitleWithDeleteAll(initialStateData.header, initialStateData.labelAction)
                    )
                }
                InitialStateData.INITIAL_STATE_RECENT_VIEW -> {
                    data.addAll(
                            initialStateData.convertRecentViewSearchToVisitableList().insertTitle(initialStateData.header)
                    )
                }
                InitialStateData.INITIAL_STATE_POPULAR_SEARCH -> {
                    data.addAll(
                            initialStateData.convertPopularSearchToVisitableList().insertTitleWithRefresh(initialStateData.header, initialStateData.labelAction)
                    )
                }
            }
        }
        return data
    }

    private fun MutableList<Visitable<*>>.insertTitle(title: String): List<Visitable<*>> {
        val titleSearch = ReecentViewTitleViewModel()
        titleSearch.title = title
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertTitleWithDeleteAll(title: String, labelAction: String): List<Visitable<*>> {
        val titleSearch = RecentSearchTitleViewModel(true)
        titleSearch.title = title
        titleSearch.labelAction = labelAction
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertTitleWithRefresh(title: String, labelAction: String): List<Visitable<*>> {
        val titleSearch = PopularSearchTitleViewModel(true)
        titleSearch.title = title
        titleSearch.labelAction = labelAction
        this.add(0, titleSearch)
        return this
    }

    fun convertListPopularSearchToBaseItemInitialStateSearch(items: List<InitialStateItem>): List<BaseItemInitialStateSearch> {
        val childList = ArrayList<BaseItemInitialStateSearch>()
        for (item in items) {
            val model = BaseItemInitialStateSearch(
                    template = item.template,
                    imageUrl = item.imageUrl,
                    applink = item.applink,
                    url = item.url,
                    title = item.title,
                    subtitle = item.subtitle,
                    iconTitle = item.iconTitle,
                    iconSubtitle = item.iconSubtitle,
                    label = item.label,
                    labelType = item.labelType,
                    shortcutImage = item.shortcutImage,
                    productId = item.itemId
            )
            childList.add(model)
        }
        return childList
    }

    override fun getInitialStateData() {
        initialStateUseCase.execute(
                InitialStateGqlUseCase.getParams(
                        searchParameter,
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
                getDeleteRecentSearchSubscriber(keyword)
        )
    }

    override fun deleteAllRecentSearch() {
        val params = DeleteRecentSearchUseCase.getParams(
                userSession.deviceId,
                userSession.userId
        )
        deleteRecentSearchUseCase.execute(
                params,
                getDeleteAllRecentSearchSubscriber()
        )
    }

    override fun refreshPopularSearch() {
        refreshPopularSearchUseCase.execute(
                RefreshPopularSearchUseCase.getParams(
                        searchParameter,
                        userSession.deviceId,
                        userSession.userId
                ),
                getPopularSearchSubscriber()
        )
    }

    override fun detachView() {
        super.detachView()
        initialStateUseCase.unsubscribe()
    }

    override fun attachView(view: InitialStateContract.View) {
        super.attachView(view)
    }
}