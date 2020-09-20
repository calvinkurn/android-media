package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.convertDynamicInitialStateSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshInitialStateUseCase
import com.tokopedia.autocomplete.initialstate.popularsearch.convertPopularSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.convertRecentSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.convertRecentViewSearchToVisitableList
import com.tokopedia.autocomplete.util.getShopIdFromApplink
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class InitialStatePresenter @Inject constructor(
        @Named(INITIAL_STATE_USE_CASE) private val initialStateUseCase: UseCase<List<InitialStateData>>,
        private val deleteRecentSearchUseCase: UseCase<Boolean>,
        @Named(REFRESH_INITIAL_STATE_USE_CASE) private val refreshInitialStateUseCase: UseCase<List<InitialStateData>>,
        private val userSession: UserSessionInterface
) : BaseDaggerPresenter<InitialStateContract.View>(), InitialStateContract.Presenter {

    companion object {
        const val RECENT_SEARCH = "recent_search"
        const val RECENT_VIEW = "recent_view"
        const val POPULAR_SEARCH = "popular_search"
    }

    private var listVisitable = mutableListOf<Visitable<*>>()
    private var searchParameter = HashMap<String, String>()

    override fun getQueryKey(): String {
        return searchParameter[SearchApiConst.Q] ?: ""
    }

    fun setSearchParameter(searchParameter: HashMap<String, String>) {
        this.searchParameter = searchParameter
    }

    fun getSearchParameter(): Map<String, String> {
        return searchParameter
    }

    private fun getUserId(): String {
        return if (userSession.isLoggedIn) userSession.userId else "0"
    }

    override fun getInitialStateData() {
        initialStateUseCase.execute(
                InitialStateUseCase.getParams(
                        searchParameter,
                        userSession.deviceId,
                        userSession.userId
                ),
                getInitialStateSubscriber()
        )
    }

    private fun getInitialStateSubscriber(): Subscriber<List<InitialStateData>> = object : Subscriber<List<InitialStateData>>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(list: List<InitialStateData>) {
            val initialStateViewModel = InitialStateViewModel()

            for (initialStateData in list) {
                if (initialStateData.items.isNotEmpty()) {
                    when (initialStateData.id) {
                        RECENT_SEARCH-> {
                            initialStateViewModel.addList(initialStateData)
                            initialStateData.items.withNotEmpty{
                                onRecentSearchImpressed(this)
                            }
                        }
                        RECENT_VIEW -> {
                            initialStateViewModel.addList(initialStateData)
                            initialStateData.items.withNotEmpty{
                                onRecentViewImpressed(this)
                            }
                        }
                        POPULAR_SEARCH -> {
                            initialStateViewModel.addList(initialStateData)
                            initialStateData.items.withNotEmpty{
                                onPopularSearchImpressed(this)
                            }
                        }
                        else -> {
                            initialStateViewModel.addList(initialStateData)
                        }
                    }
                }
            }

            listVisitable = getInitialStateResult(initialStateViewModel.list)
            view.showInitialStateResult(listVisitable)
        }
    }

    private fun <E: Any, T: Collection<E>> T.withNotEmpty(func: T.() -> Unit): Unit {
        if (this.isNotEmpty()) with (this) { func() }
    }

    private fun onRecentViewImpressed(list: List<InitialStateItem>) {
        view.onRecentViewImpressed(getDataLayerForRecentView(list))
    }

    private fun getDataLayerForRecentView(list: List<InitialStateItem>): MutableList<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForRecentView(position))
        }
        return dataLayerList
    }

    private fun onRecentSearchImpressed(list: List<InitialStateItem>) {
        view.onRecentSearchImpressed(getDataLayerForPromo(list))
    }

    private fun getDataLayerForPromo(list: List<InitialStateItem>): MutableList<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForPromo(position))
        }
        return dataLayerList
    }

    private fun onPopularSearchImpressed(list: List<InitialStateItem>) {
        view.onPopularSearchImpressed(getDataLayerForPromo(list))
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
                            initialStateData.convertPopularSearchToVisitableList().insertTitleWithRefresh(
                                    initialStateData.featureId,
                                    initialStateData.header,
                                    initialStateData.labelAction
                            )
                    )
                }
                else -> {
                    data.addAll(
                            initialStateData.convertDynamicInitialStateSearchToVisitableList().insertDynamicTitle(
                                    initialStateData.featureId,
                                    initialStateData.header,
                                    initialStateData.labelAction
                            )
                    )
                }
            }
        }
        return data
    }

    private fun MutableList<Visitable<*>>.insertTitle(title: String): List<Visitable<*>> {
        val titleSearch = RecentViewTitleViewModel(title)
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertTitleWithDeleteAll(title: String, labelAction: String): List<Visitable<*>> {
        val titleSearch = RecentSearchTitleViewModel(title, labelAction)
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertTitleWithRefresh(featureId: String, title: String, labelAction: String): List<Visitable<*>> {
        val titleSearch = PopularSearchTitleViewModel(featureId, title, labelAction)
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertDynamicTitle(featureId: String, title: String, labelAction: String): List<Visitable<*>> {
        val titleSearch = DynamicInitialStateTitleViewModel(featureId, title, labelAction)
        this.add(0, titleSearch)
        return this
    }

    override fun refreshPopularSearch(featureId: String) {
        refreshInitialStateUseCase.execute(
                RefreshInitialStateUseCase.getParams(
                        searchParameter,
                        userSession.deviceId,
                        userSession.userId
                ),
                getPopularSearchSubscriber(featureId)
        )
    }

    private fun getPopularSearchSubscriber(featureId: String): Subscriber<List<InitialStateData>> = object : Subscriber<List<InitialStateData>>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(listData: List<InitialStateData>) {
            val refreshedPopularSearchData = getRefreshedData(featureId, listData)

            if (refreshedPopularSearchData.isEmpty()) return

            listVisitable.forEachIndexed { _, visitable ->
                if (visitable is PopularSearchViewModel) {
                    visitable.list = refreshedPopularSearchData
                }
            }

            view.showInitialStateResult(listVisitable)
        }
    }

    private fun getRefreshedData(featureId: String, listData: List<InitialStateData>): List<BaseItemInitialStateSearch> {
        val newData: InitialStateData? = listData.find { it.featureId == featureId }
        var refreshedData = listOf<BaseItemInitialStateSearch>()
        newData?.let {
            refreshedData = it.items.convertToBaseItemInitialStateSearch()
        }
        return refreshedData
    }

    override fun refreshDynamicSection(featureId: String) {
        refreshInitialStateUseCase.execute(
                RefreshInitialStateUseCase.getParams(
                        searchParameter,
                        userSession.deviceId,
                        userSession.userId
                ),
                getRefreshDynamicSectionSubscriber(featureId)
        )
    }

    private fun getRefreshDynamicSectionSubscriber(featureId: String): Subscriber<List<InitialStateData>> = object : Subscriber<List<InitialStateData>>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(listData: List<InitialStateData>) {
            val dynamicInitialStateData = getRefreshedData(featureId, listData)

            if (dynamicInitialStateData.isEmpty()) return

            listVisitable.forEachIndexed { _, visitable ->
                if (visitable is DynamicInitialStateSearchViewModel) {
                    visitable.list = dynamicInitialStateData
                }
            }

            view.showInitialStateResult(listVisitable)
        }
    }

    override fun deleteRecentSearchItem(item: BaseItemInitialStateSearch) {
        val params = DeleteRecentSearchUseCase.getParams(
                userSession.deviceId,
                userSession.userId,
                item
        )
        deleteRecentSearchUseCase.execute(
                params,
                getDeleteRecentSearchSubscriber(item.title)
        )
    }

    private fun getDeleteRecentSearchSubscriber(keyword: String): Subscriber<Boolean> = object : Subscriber<Boolean>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(isSuccess: Boolean) {
            if (isSuccess) {
                var needDelete = false
                listVisitable.forEachIndexed { _, visitable ->
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
                view.showInitialStateResult(listVisitable)
            }
        }
    }

    private fun removeRecentSearchTitle() {
        val titleViewModel = listVisitable.filterIsInstance<RecentSearchTitleViewModel>()
        listVisitable.removeAll(titleViewModel)
    }

    private fun removeRecentSearch() {
        val recentSearchViewModel = listVisitable.filterIsInstance<RecentSearchViewModel>()
        listVisitable.removeAll(recentSearchViewModel)
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

    private fun getDeleteAllRecentSearchSubscriber(): Subscriber<Boolean> = object : Subscriber<Boolean>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(isSuccess: Boolean) {
            if (isSuccess) {
                removeRecentSearchTitle()
                removeRecentSearch()
                view.showInitialStateResult(listVisitable)
            }
        }
    }

    override fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int) {
        trackEventItemClicked(item, adapterPosition)

        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    private fun trackEventItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int) {
        when(item.type) {
            TYPE_SHOP -> view?.trackEventClickRecentShop(getRecentShopLabelForTracking(item), getUserId())
            else -> view?.trackEventClickRecentSearch(
                    getItemEventLabelForTracking(item, adapterPosition),
                    adapterPosition
            )
        }
    }

    private fun getRecentShopLabelForTracking(item: BaseItemInitialStateSearch): String {
        return getShopIdFromApplink(item.applink) + " - keyword: " + item.title
    }

    private fun getItemEventLabelForTracking(item: BaseItemInitialStateSearch, adapterPosition: Int): String {
        return "value: ${item.title} - po: ${adapterPosition +1} - applink: ${item.applink}"
    }

    override fun detachView() {
        super.detachView()
        initialStateUseCase.unsubscribe()
        deleteRecentSearchUseCase.unsubscribe()
        refreshInitialStateUseCase.unsubscribe()
    }

    override fun attachView(view: InitialStateContract.View) {
        super.attachView(view)
    }
}