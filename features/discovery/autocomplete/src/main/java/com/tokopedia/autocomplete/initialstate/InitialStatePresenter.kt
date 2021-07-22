package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.initialstate.chips.InitialStateChipWidgetDataView
import com.tokopedia.autocomplete.initialstate.chips.InitialStateChipWidgetTitleDataView
import com.tokopedia.autocomplete.initialstate.chips.convertToInitialStateChipWidgetDataView
import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.curatedcampaign.convertToCuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocomplete.initialstate.dynamic.convertDynamicInitialStateSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshInitialStateUseCase
import com.tokopedia.autocomplete.initialstate.popularsearch.convertPopularSearchToVisitableList
import com.tokopedia.autocomplete.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocomplete.initialstate.productline.convertToListInitialStateProductListDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.*
import com.tokopedia.autocomplete.initialstate.recentview.convertRecentViewSearchToVisitableList
import com.tokopedia.autocomplete.util.getShopIdFromApplink
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.discovery.common.utils.UrlParamUtils
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

    private var listVisitable = mutableListOf<Visitable<*>>()
    private var recentSearchList: MutableList<InitialStateItem>? = null
    private var searchParameter = HashMap<String, String>()

    var recentSearchPosition = -1
        private set

    var seeMoreButtonPosition = -1
        private set

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

    private fun isTokoNow(): Boolean {
        return UrlParamUtils.isTokoNow(searchParameter)
    }

    override fun getInitialStateData() {
        val warehouseId = view?.chooseAddressData?.warehouse_id ?: ""
        initialStateUseCase.execute(
                InitialStateUseCase.getParams(
                        searchParameter,
                        userSession.deviceId,
                        userSession.userId,
                        warehouseId
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
            val initialStateDataView = InitialStateDataView()

            for (initialStateData in list) {
                if (initialStateData.items.isNotEmpty()) initialStateDataView.addList(initialStateData)
            }

            listVisitable = getInitialStateResult(initialStateDataView.list)
            view?.showInitialStateResult(listVisitable)
        }
    }

    private fun <E: Any, T: Collection<E>> T.withNotEmpty(func: T.() -> Unit): Unit {
        if (this.isNotEmpty()) with (this) { func() }
    }

    private fun onRecentViewImpressed(list: List<InitialStateItem>) {
        list.withNotEmpty{
            view?.onRecentViewImpressed(getDataLayerForRecentView(this))
        }
    }

    private fun getDataLayerForRecentView(list: List<InitialStateItem>): List<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForRecentView(position))
        }
        return dataLayerList
    }

    private fun onImpressSeeMoreRecentSearch() {
        view?.onSeeMoreRecentSearchImpressed(getUserId())
    }

    private fun onRecentSearchImpressed(list: List<Any>) {
        list.withNotEmpty{
            view?.onRecentSearchImpressed(this)
        }
    }

    private fun getDataLayerForPromo(list: List<InitialStateItem>): List<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForPromo(position))
        }
        return dataLayerList
    }

    private fun onPopularSearchImpressed(data: InitialStateData) {
        data.items.withNotEmpty{
            val dynamicInitialStateItemTrackingModel = createDynamicInitialStateItemTrackingModel(
                    getUserId(), data.header, data.featureId, getDataLayerForPromo(this)
            )
            view?.onPopularSearchImpressed(dynamicInitialStateItemTrackingModel)
        }
    }

    private fun createDynamicInitialStateItemTrackingModel(userId: String, title: String, type: String, list: List<Any>): DynamicInitialStateItemTrackingModel {
        return DynamicInitialStateItemTrackingModel(
                userId = userId,
                title = title,
                type = type,
                list = list
        )
    }

    private fun onDynamicSectionImpressed(data: InitialStateData) {
        data.items.withNotEmpty{
            val dynamicInitialStateItemTrackingModel = createDynamicInitialStateItemTrackingModel(
                    getUserId(), data.header, data.featureId, getDataLayerForPromo(this)
            )
            view?.onDynamicSectionImpressed(dynamicInitialStateItemTrackingModel)
        }
    }

    private fun getInitialStateResult(list: MutableList<InitialStateData>): MutableList<Visitable<*>> {
        val data = mutableListOf<Visitable<*>>()
        for (initialStateData in list) {
            when (initialStateData.id) {
                InitialStateData.INITIAL_STATE_CURATED_CAMPAIGN -> {
                    addCuratedCampaignCard(data, initialStateData)
                }
                InitialStateData.INITIAL_STATE_RECENT_SEARCH -> {
                    data.add(createTitleWithDeleteAll(initialStateData.header, initialStateData.labelAction))
                    addRecentSearchData(data, initialStateData.items)
                }
                InitialStateData.INITIAL_STATE_RECENT_VIEW -> {
                    onRecentViewImpressed(initialStateData.items)
                    data.addAll(
                            initialStateData.convertRecentViewSearchToVisitableList(getDimension90()).insertTitle(initialStateData.header)
                    )
                }
                InitialStateData.INITIAL_STATE_POPULAR_SEARCH -> {
                    onPopularSearchImpressed(initialStateData)
                    data.addAll(
                            initialStateData.convertPopularSearchToVisitableList(getDimension90()).insertTitleWithRefresh(
                                    initialStateData.featureId,
                                    initialStateData.header,
                                    initialStateData.labelAction
                            )
                    )
                }
                InitialStateData.INITIAL_STATE_LIST_PRODUCT_LINE -> {
                    data.addAll(
                            initialStateData.convertToListInitialStateProductListDataView(getDimension90()).insertProductListTitle(initialStateData.header)
                    )
                }
                InitialStateData.INITIAL_STATE_LIST_CHIPS -> {
                    data.addAll(
                            initialStateData.convertToInitialStateChipWidgetDataView(getDimension90()).insertChipWidgetTitle(initialStateData.header)
                    )
                }
                else -> {
                    onDynamicSectionImpressed(initialStateData)
                    data.addAll(
                            initialStateData.convertDynamicInitialStateSearchToVisitableList(getDimension90()).insertDynamicTitle(
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

    private fun addCuratedCampaignCard(listVisitable: MutableList<Visitable<*>>, initialStateData: InitialStateData) {
        val item = initialStateData.items.getOrNull(0) ?: return

        val curatedCampaignDataView = item.convertToCuratedCampaignDataView(initialStateData.featureId)
        listVisitable.add(curatedCampaignDataView)
        onImpressCuratedCampaignCard(curatedCampaignDataView)
    }

    private fun onImpressCuratedCampaignCard(curatedCampaignDataView: CuratedCampaignDataView) {
        val label = "${curatedCampaignDataView.title} - ${curatedCampaignDataView.applink}"
        view?.onCuratedCampaignCardImpressed(getUserId(), label, curatedCampaignDataView.type)
    }

    private fun addRecentSearchData(listVisitable: MutableList<Visitable<*>>, listInitialStateItem: List<InitialStateItem>) {
        if (listInitialStateItem.size <= RECENT_SEARCH_SEE_MORE_LIMIT) {
            addRecentSearchDataWithoutSeeMoreButton(listVisitable, listInitialStateItem)
        }
        else {
            addRecentSearchDataWithSeeMoreButton(listVisitable, listInitialStateItem)
        }
    }

    private fun addRecentSearchDataWithoutSeeMoreButton(listVisitable: MutableList<Visitable<*>>, listInitialStateItem: List<InitialStateItem>) {
        onRecentSearchImpressed(getDataLayerForPromo(listInitialStateItem))

        listVisitable.add(listInitialStateItem.convertToRecentSearchDataView(getDimension90()))
        recentSearchPosition = listVisitable.lastIndex
    }

    private fun addRecentSearchDataWithSeeMoreButton(listVisitable: MutableList<Visitable<*>>, listInitialStateItem: List<InitialStateItem>) {
        recentSearchList = listInitialStateItem as MutableList<InitialStateItem>

        val recentSearchToBeShown = listInitialStateItem.take(RECENT_SEARCH_SEE_MORE_LIMIT)
        onRecentSearchImpressed(getDataLayerForPromo(recentSearchToBeShown))

        listVisitable.add(recentSearchToBeShown.convertToRecentSearchDataView(getDimension90()))
        recentSearchPosition = listVisitable.lastIndex

        listVisitable.add(createRecentSearchSeeMoreButton())
        seeMoreButtonPosition = listVisitable.lastIndex
        onImpressSeeMoreRecentSearch()
    }

    //dimension90 = pageSource
    private fun getDimension90(): String {
        return Dimension90Utils.getDimension90(searchParameter)
    }

    private fun MutableList<Visitable<*>>.insertTitle(title: String): List<Visitable<*>> {
        val titleSearch = RecentViewTitleDataView(title)
        this.add(0, titleSearch)
        return this
    }

    private fun createTitleWithDeleteAll(title: String, labelAction: String): RecentSearchTitleDataView {
        return RecentSearchTitleDataView(title, labelAction)
    }

    private fun MutableList<Visitable<*>>.insertTitleWithRefresh(featureId: String, title: String, labelAction: String): List<Visitable<*>> {
        if (title.isEmpty()) return this

        val titleSearch = PopularSearchTitleDataView(featureId, title, labelAction)
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertDynamicTitle(featureId: String, title: String, labelAction: String): List<Visitable<*>> {
        if (title.isEmpty()) return this

        val titleSearch = DynamicInitialStateTitleDataView(featureId, title, labelAction)
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertProductListTitle(title: String): List<Visitable<*>> {
        val titleSearch = InitialStateProductLineTitleDataView(title)
        this.add(0, titleSearch)
        return this
    }

    private fun MutableList<Visitable<*>>.insertChipWidgetTitle(title: String): List<Visitable<*>> {
        val titleSearch = InitialStateChipWidgetTitleDataView(title)
        this.add(0, titleSearch)
        return this
    }

    private fun createRecentSearchSeeMoreButton(): RecentSearchSeeMoreDataView {
        return RecentSearchSeeMoreDataView()
    }

    override fun refreshPopularSearch(featureId: String) {
        if (isTokoNow()) view?.onRefreshTokoNowPopularSearch()
        else view?.onRefreshPopularSearch()

        val warehouseId = view?.chooseAddressData?.warehouse_id ?: ""

        refreshInitialStateUseCase.unsubscribe()
        refreshInitialStateUseCase.execute(
                RefreshInitialStateUseCase.getParams(
                        searchParameter,
                        userSession.deviceId,
                        userSession.userId,
                        warehouseId
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

            var refreshIndex = -1

            listVisitable.forEachIndexed { index, visitable ->
                if (visitable is PopularSearchDataView && visitable.featureId == featureId) {
                    visitable.list = refreshedPopularSearchData
                    if (listVisitable[index - 1] is PopularSearchTitleDataView) refreshIndex = index - 1
                }
            }

            if (refreshIndex != -1) view.refreshViewWithPosition(refreshIndex)
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
        val warehouseId = view?.chooseAddressData?.warehouse_id ?: ""

        refreshInitialStateUseCase.unsubscribe()
        refreshInitialStateUseCase.execute(
                RefreshInitialStateUseCase.getParams(
                        searchParameter,
                        userSession.deviceId,
                        userSession.userId,
                        warehouseId
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

            var refreshIndex = -1

            listVisitable.forEachIndexed { index, visitable ->
                if (visitable is DynamicInitialStateSearchDataView && visitable.featureId == featureId) {
                    visitable.list = dynamicInitialStateData
                    if (listVisitable[index - 1] is DynamicInitialStateTitleDataView) refreshIndex = index - 1
                }
            }

            if (refreshIndex != -1) view.refreshViewWithPosition(refreshIndex)
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
                val recentSearchDataVisitable: RecentSearchDataView = listVisitable.find { it is RecentSearchDataView } as RecentSearchDataView

                if (recentSearchDataVisitable.list.size == 1) {
                    removeRecentSearchTitle()
                    removeRecentSearch()
                }
                else {
                    recentSearchList?.let{
                        deleteRecentSearchWithSeeMoreButton(keyword, it, recentSearchDataVisitable)
                    } ?: run {
                        deleteRecentSearchWithoutSeeMoreButton(keyword, recentSearchDataVisitable)
                    }
                }

                view?.showInitialStateResult(listVisitable)
            }
        }
    }

    private fun deleteRecentSearchWithSeeMoreButton(keyword: String, recentSearchList: MutableList<InitialStateItem>, recentSearchDataVisitable: RecentSearchDataView) {
        val deleted = recentSearchList.find { item -> item.title == keyword }
        recentSearchList.remove(deleted)

        val recentSearchDataView = recentSearchList.convertToRecentSearchDataView(getDimension90())
        if (recentSearchDataView.list.size <= RECENT_SEARCH_SEE_MORE_LIMIT) {
            recentSearchDataVisitable.list = recentSearchDataView.list
            removeSeeMoreRecentSearch()
        }
        else {
            recentSearchDataVisitable.list = recentSearchDataView.list.take(RECENT_SEARCH_SEE_MORE_LIMIT) as MutableList<BaseItemInitialStateSearch>
        }
    }

    private fun deleteRecentSearchWithoutSeeMoreButton(keyword: String, recentSearchDataVisitable: RecentSearchDataView) {
        val deleted = recentSearchDataVisitable.list.find { it.title == keyword }
        recentSearchDataVisitable.list.remove(deleted)
    }

    private fun removeRecentSearchTitle() {
        val titleDataView = listVisitable.filterIsInstance<RecentSearchTitleDataView>()
        listVisitable.removeAll(titleDataView)
    }

    private fun removeRecentSearch() {
        val recentSearchDataView = listVisitable.filterIsInstance<RecentSearchDataView>()
        listVisitable.removeAll(recentSearchDataView)
    }

    private fun removeSeeMoreRecentSearch() {
        val recentSearchSeeMoreDataView = listVisitable.filterIsInstance<RecentSearchSeeMoreDataView>()
        listVisitable.removeAll(recentSearchSeeMoreDataView)
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
                removeSeeMoreRecentSearch()
                view?.showInitialStateResult(listVisitable)
            }
        }
    }

    override fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch) {
        trackEventItemClicked(item)

        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    private fun trackEventItemClicked(item: BaseItemInitialStateSearch) {
        when(item.type) {
            TYPE_SHOP -> view?.trackEventClickRecentShop(getRecentShopLabelForTracking(item), getUserId(), item.dimension90)
            else -> view?.trackEventClickRecentSearch(getItemEventLabelForTracking(item), item.dimension90)
        }
    }

    private fun getRecentShopLabelForTracking(item: BaseItemInitialStateSearch): String {
        return getShopIdFromApplink(item.applink) + " - keyword: " + item.title
    }

    private fun getItemEventLabelForTracking(item: BaseItemInitialStateSearch): String {
        return "value: ${item.title} - po: ${item.position} - applink: ${item.applink}"
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

    override fun recentSearchSeeMoreClicked() {
        removeSeeMoreRecentSearch()

        recentSearchList?.let { recentSearchList ->
            val recentSearchToImpress = getDataLayerForPromo(recentSearchList)
            onRecentSearchImpressed(recentSearchToImpress.takeLast(recentSearchList.size - RECENT_SEARCH_SEE_MORE_LIMIT))

            val recentSearchDataView = recentSearchList.convertToRecentSearchDataView(getDimension90())

            val recentSearchDataVisitable: RecentSearchDataView = listVisitable.find { it is RecentSearchDataView } as RecentSearchDataView
            recentSearchDataVisitable.list = recentSearchDataView.list

            this.recentSearchList = null

            view?.trackEventClickSeeMoreRecentSearch(getUserId())
            view?.dropKeyBoard()
            view?.renderCompleteRecentSearch(recentSearchDataVisitable)
        }
    }

    override fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch) {
        if (isTokoNow()) trackEventClickTokoNowDynamicSectionItem(item)
        else trackEventClickDynamicSectionItem(item)

        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    private fun trackEventClickTokoNowDynamicSectionItem(item: BaseItemInitialStateSearch) {
        val label = "value: ${item.title} - po: ${item.position} - page: ${item.applink}"
        view?.trackEventClickTokoNowDynamicSectionItem(label)
    }

    private fun trackEventClickDynamicSectionItem(item: BaseItemInitialStateSearch) {
        val label = "value: ${item.title} - title: ${item.header} - po: ${item.position}"
        view?.trackEventClickDynamicSectionItem(getUserId(), label, item.featureId, item.dimension90)
    }

    override fun onCuratedCampaignCardClicked(curatedCampaignDataView: CuratedCampaignDataView) {
        val label = "${curatedCampaignDataView.title} - ${curatedCampaignDataView.applink}"
        view?.trackEventClickCuratedCampaignCard(getUserId(), label, curatedCampaignDataView.type)

        view?.route(curatedCampaignDataView.applink, searchParameter)
        view?.finish()
    }

    override fun onRecentViewClicked(item: BaseItemInitialStateSearch) {
        val label = "po: ${item.position} - applink: ${item.applink}"
        view?.trackEventClickRecentView(item, label)

        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    override fun onProductLineClicked(item: BaseItemInitialStateSearch) {
        val label = "po: ${item.position} - applink: ${item.applink}"
        view?.trackEventClickProductLine(item, getUserId(), label)

        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    override fun onChipClicked(item: BaseItemInitialStateSearch) {
        val label = "value: ${item.title} - title: ${item.header} - po: ${item.position}"
        view?.trackEventClickChip(getUserId(), label, item.featureId, item.dimension90)

        view?.route(item.applink, searchParameter)
        view?.finish()
    }
}