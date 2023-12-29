package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.chips.convertToInitialStateChipWidgetDataView
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.convertToCuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateItem
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateRequestUtils
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.convertToDynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.mps.MpsDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.convertToPopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductLineTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.convertToListInitialStateProductListDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.*
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.convertToRecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.convertToSearchBarEducationDataView
import com.tokopedia.autocompletecomponent.util.getShopIdFromApplink
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.NAVSOURCE
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class InitialStatePresenter @Inject constructor(
    @Named(INITIAL_STATE_USE_CASE)
    private val initialStateUseCase: UseCase<InitialStateUniverse>,
    @Named(DELETE_RECENT_SEARCH_USE_CASE)
    private val deleteRecentSearchUseCase: UseCase<Boolean>,
    @Named(REFRESH_INITIAL_STATE_USE_CASE)
    private val refreshInitialStateUseCase: UseCase<List<InitialStateData>>,
    private val userSession: UserSessionInterface,
    private val reimagine: ReimagineRollence
) : BaseDaggerPresenter<InitialStateContract.View>(), InitialStateContract.Presenter {

    private var listVisitable = mutableListOf<Visitable<*>>()
    private var recentSearchList: MutableList<InitialStateItem>? = null
    private var searchParameter = mutableMapOf<String, String>()

    override var recentSearchPosition = -1
        private set

    override var seeMoreButtonPosition = -1
        private set

    override fun getQueryKey(): String {
        return searchParameter[SearchApiConst.Q] ?: ""
    }

    override fun getSearchParameter(): Map<String, String> = searchParameter

    private fun getUserId(): String {
        return if (userSession.isLoggedIn) userSession.userId else "0"
    }

    private fun isTokoNow(): Boolean {
        return UrlParamUtils.isTokoNow(searchParameter)
    }

    private fun getNavSource() = searchParameter[NAVSOURCE] ?: ""

    override fun showInitialState(searchParameter: Map<String, String>) {
        this.searchParameter = searchParameter.toMutableMap()

        initialStateUseCase.execute(
            createInitialStateRequestParams(),
            getInitialStateSubscriber()
        )
    }

    private fun createInitialStateRequestParams(): RequestParams {
        val chooseAddressData = view?.chooseAddressData ?: LocalCacheModel()

        return InitialStateRequestUtils.getParams(
            searchParameter,
            userSession,
            chooseAddressData,
        )
    }

    private fun getInitialStateSubscriber(): Subscriber<InitialStateUniverse> =
        object : Subscriber<InitialStateUniverse>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(initialStateUniverse: InitialStateUniverse) {
                onGetInitialStateSuccess(initialStateUniverse)
            }
        }

    private fun onGetInitialStateSuccess(initialStateUniverse: InitialStateUniverse) {
        val initialStateDataView = InitialStateDataView()

        for (initialStateData in initialStateUniverse.data)
            if (initialStateData.items.isNotEmpty())
                initialStateDataView.addList(initialStateData)

        listVisitable = getInitialStateResult(initialStateDataView.list)

        val view = view ?: return
        view.showInitialStateResult(listVisitable)
    }

    private fun <E: Any, T: Collection<E>> T.withNotEmpty(func: T.() -> Unit): Unit {
        if (this.isNotEmpty()) with (this) { func() }
    }

    private fun onRecentViewImpressed(
        recentViewDataView: RecentViewDataView,
        list: List<InitialStateItem>,
    ) {
        list.withNotEmpty{
            view?.onRecentViewImpressed(recentViewDataView, getDataLayerForRecentView(this))
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

    private fun onRecentSearchImpressed(
        recentSearchList: List<BaseItemInitialStateSearch>,
        list: List<Any>,
    ) {
        list.withNotEmpty{
            view?.onRecentSearchImpressed(recentSearchList, this)
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

    private fun onPopularSearchImpressed(
        popularSearchDataView: PopularSearchDataView,
        data: InitialStateData
    ) {
        data.items.withNotEmpty{
            val dynamicInitialStateItemTrackingModel = DynamicInitialStateItemTrackingModel(
                userId = getUserId(),
                title = data.header,
                type = data.featureId,
                list = getDataLayerForPromo(this)
            )
            view?.onPopularSearchImpressed(
                popularSearchDataView,
                dynamicInitialStateItemTrackingModel
            )
        }
    }

    private fun onDynamicSectionImpressed(
        dynamicInitialStateSearchDataView: DynamicInitialStateSearchDataView,
        data: InitialStateData,
    ) {
        data.items.withNotEmpty{
            val dynamicInitialStateItemTrackingModel = DynamicInitialStateItemTrackingModel(
                userId = getUserId(),
                title = data.header,
                type = data.featureId,
                list = getDataLayerForPromo(this),
            )

            view?.onDynamicSectionImpressed(
                dynamicInitialStateSearchDataView,
                dynamicInitialStateItemTrackingModel
            )
        }
    }

    private fun getInitialStateResult(list: MutableList<InitialStateData>): MutableList<Visitable<*>> {
        val data = mutableListOf<Visitable<*>>()
        val keyword = getQueryKey()

        for (initialStateData in list) {
            when (initialStateData.id) {
                InitialStateData.INITIAL_STATE_CURATED_CAMPAIGN -> {
                    addCuratedCampaignCard(data, initialStateData)
                }
                InitialStateData.INITIAL_STATE_RECENT_SEARCH -> {
                    if(initialStateData.header.isNotEmpty()) {
                        val title = RecentSearchTitleDataView(
                            initialStateData.header,
                            initialStateData.labelAction
                        )
                        data.add(title)
                    }
                    addRecentSearchData(data, initialStateData.items, initialStateData.trackingOption)
                }
                InitialStateData.INITIAL_STATE_RECENT_VIEW -> {
                    if(initialStateData.header.isNotEmpty()) {
                        val title = RecentViewTitleDataView(initialStateData.header)
                        data.add(title)
                    }

                    val recentViewDataView = initialStateData
                        .convertToRecentViewDataView(getDimension90(), keyword)

                    data.addAll(listOf(recentViewDataView))

                    onRecentViewImpressed(recentViewDataView, initialStateData.items)
                }
                InitialStateData.INITIAL_STATE_POPULAR_SEARCH -> {
                    if (initialStateData.header.isNotEmpty()) {
                        val titlePopularSearch = PopularSearchTitleDataView(
                            initialStateData.featureId,
                            initialStateData.header,
                            initialStateData.labelAction
                        )
                        data.add(titlePopularSearch)
                    }

                    val popularSearchDataView = initialStateData
                        .convertToPopularSearchDataView(getDimension90(), keyword)

                    data.add(popularSearchDataView)

                    onPopularSearchImpressed(popularSearchDataView, initialStateData)
                }
                InitialStateData.INITIAL_STATE_LIST_PRODUCT_LINE -> {
                    data.addAll(
                        initialStateData
                            .convertToListInitialStateProductListDataView(
                                getDimension90(),
                                keyword,
                            )
                            .insertProductListTitle(initialStateData.header)
                    )
                }
                InitialStateData.INITIAL_STATE_LIST_CHIPS -> {
                    data.addAll(
                        initialStateData
                            .convertToInitialStateChipWidgetDataView(
                                getDimension90(),
                                keyword,
                            )
                            .insertChipWidgetTitle(initialStateData.header)
                    )
                }
                InitialStateData.INITIAL_STATE_SEARCHBAR_EDUCATION -> {
                    initialStateData.convertToSearchBarEducationDataView(
                        getDimension90(),
                        keyword,
                    )?.let {
                        data.add(it)
                    }
                }
                InitialStateData.INITIAL_STATE_MPS -> {
                    MpsDataView.create(
                        initialStateData,
                        getDimension90(),
                        keyword,
                        searchParameter,
                    )?.let {
                        data.add(it)
                    }
                }
                else -> {
                    if (initialStateData.header.isNotEmpty()) {
                        val titleDynamicInitialState =
                            DynamicInitialStateTitleDataView(
                                initialStateData.featureId,
                                initialStateData.header,
                                initialStateData.labelAction
                            )
                        data.add(titleDynamicInitialState)
                    }

                    val dynamicInitialStateSearchDataView = initialStateData
                        .convertToDynamicInitialStateSearchDataView(
                            getDimension90(),
                            keyword,
                        )

                    data.add(dynamicInitialStateSearchDataView)

                    onDynamicSectionImpressed(dynamicInitialStateSearchDataView, initialStateData)
                }
            }
        }
        return data
    }

    private fun addCuratedCampaignCard(
        listVisitable: MutableList<Visitable<*>>,
        initialStateData: InitialStateData
    ) {
        val item = initialStateData.items.getOrNull(0) ?: return

        val curatedCampaignDataView = item.convertToCuratedCampaignDataView(
            initialStateData.featureId,
            getDimension90(),
            getQueryKey(),
            initialStateData.trackingOption,
        )
        listVisitable.add(curatedCampaignDataView)
        onImpressCuratedCampaignCard(curatedCampaignDataView)
    }

    private fun onImpressCuratedCampaignCard(curatedCampaignDataView: CuratedCampaignDataView) {
        val label = getCuratedCampaignEventLabel(curatedCampaignDataView)
        view?.onCuratedCampaignCardImpressed(
            getUserId(),
            label,
            curatedCampaignDataView.baseItemInitialState,
            curatedCampaignDataView.baseItemInitialState.type,
            curatedCampaignDataView.baseItemInitialState.campaignCode
        )
    }

    private fun getCuratedCampaignEventLabel(curatedCampaignDataView: CuratedCampaignDataView) =
        "${curatedCampaignDataView.baseItemInitialState.title} - " +
            curatedCampaignDataView.baseItemInitialState.applink

    private fun addRecentSearchData(
        listVisitable: MutableList<Visitable<*>>,
        listInitialStateItem: List<InitialStateItem>,
        trackingOption: Int,
    ) {
        if (listInitialStateItem.size <= RECENT_SEARCH_SEE_MORE_LIMIT || !isReimagineVariantControl())
            addRecentSearchDataWithoutSeeMoreButton(listVisitable, listInitialStateItem, trackingOption)
        else
            addRecentSearchDataWithSeeMoreButton(listVisitable, listInitialStateItem, trackingOption)
    }

    private fun addRecentSearchDataWithoutSeeMoreButton(
        listVisitable: MutableList<Visitable<*>>,
        listInitialStateItem: List<InitialStateItem>,
        trackingOption: Int,
    ) {
        val recentSearchItems = listInitialStateItem.convertToRecentSearchDataView(
            getDimension90(),
            trackingOption,
            getQueryKey(),
        )

        onRecentSearchImpressed(recentSearchItems, getDataLayerForPromo(listInitialStateItem))

        listVisitable.add(RecentSearchDataView(recentSearchItems, trackingOption))
        recentSearchPosition = listVisitable.lastIndex
    }

    private fun addRecentSearchDataWithSeeMoreButton(
        listVisitable: MutableList<Visitable<*>>,
        listInitialStateItem: List<InitialStateItem>,
        trackingOption: Int,
    ) {
        recentSearchList = listInitialStateItem as MutableList<InitialStateItem>

        val recentSearchToBeShown = listInitialStateItem.take(RECENT_SEARCH_SEE_MORE_LIMIT)
        addRecentSearchDataWithoutSeeMoreButton(listVisitable, recentSearchToBeShown, trackingOption)

        listVisitable.add(createRecentSearchSeeMoreButton())
        seeMoreButtonPosition = listVisitable.lastIndex
        onImpressSeeMoreRecentSearch()
    }

    //dimension90 = pageSource
    private fun getDimension90(): String {
        return Dimension90Utils.getDimension90(searchParameter)
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

        refreshInitialStateUseCase.unsubscribe()
        refreshInitialStateUseCase.execute(
            createInitialStateRequestParams(),
            getPopularSearchSubscriber(featureId)
        )
    }

    private fun getPopularSearchSubscriber(featureId: String): Subscriber<List<InitialStateData>> =
        object : Subscriber<List<InitialStateData>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(listData: List<InitialStateData>) {
                onRefreshPopularSuccess(featureId, listData)
            }
        }

    private fun onRefreshPopularSuccess(
        featureId: String,
        listData: List<InitialStateData>,
    ) {
        val refreshData = getRefreshedData(featureId, listData)

        if (refreshData.isEmpty()) return

        var refreshIndex = -1

        listVisitable.forEachIndexed { index, visitable ->
            if (visitable is PopularSearchDataView && visitable.featureId == featureId) {
                visitable.list = refreshData
                if (listVisitable[index - 1] is PopularSearchTitleDataView)
                    refreshIndex = index - 1
            }
        }

        if (refreshIndex != -1)
            view.refreshViewWithPosition(refreshIndex)
    }

    private fun getRefreshedData(
        featureId: String,
        listData: List<InitialStateData>,
    ): List<BaseItemInitialStateSearch> {
        val newData: InitialStateData? = listData.find { it.featureId == featureId }
        var refreshedData = listOf<BaseItemInitialStateSearch>()
        newData?.let {
            refreshedData = it.items.convertToBaseItemInitialStateSearch()
        }
        return refreshedData
    }

    override fun refreshDynamicSection(featureId: String) {
        refreshInitialStateUseCase.unsubscribe()
        refreshInitialStateUseCase.execute(
            createInitialStateRequestParams(),
            getRefreshDynamicSectionSubscriber(featureId)
        )
    }

    private fun getRefreshDynamicSectionSubscriber(
        featureId: String
    ): Subscriber<List<InitialStateData>> =
        object : Subscriber<List<InitialStateData>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(listData: List<InitialStateData>) {
                onRefreshDynamicSectionSuccess(
                    featureId,
                    listData,
                )
            }
        }

    private fun onRefreshDynamicSectionSuccess(
        featureId: String,
        listData: List<InitialStateData>,
    ) {
        val refreshData = getRefreshedData(featureId, listData)

        if (refreshData.isEmpty()) return

        var refreshIndex = -1

        listVisitable.forEachIndexed { index, visitable ->
            if (visitable is DynamicInitialStateSearchDataView && visitable.featureId == featureId) {
                visitable.list = refreshData

                if (listVisitable[index - 1] is DynamicInitialStateTitleDataView)
                    refreshIndex = index - 1
            }
        }

        if (refreshIndex != -1)
            view.refreshViewWithPosition(refreshIndex)
    }

    override fun deleteRecentSearchItem(item: BaseItemInitialStateSearch) {
        val params = DeleteRecentSearchUseCase.getParams(
            registrationId = userSession.deviceId,
            userId = userSession.userId,
            item = item,
            navSource = getNavSource()
        )
        deleteRecentSearchUseCase.execute(
                params,
                getDeleteRecentSearchSubscriber(item.title)
        )
    }

    private fun getDeleteRecentSearchSubscriber(keyword: String): Subscriber<Boolean> =
        object : Subscriber<Boolean>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(isSuccess: Boolean) {
                onDeleteRecentSearchSuccess(isSuccess, keyword)
            }
        }

    private fun onDeleteRecentSearchSuccess(isSuccess: Boolean, keyword: String) {
        if (!isSuccess) return

        val recentSearchDataVisitable =
            listVisitable.find { it is RecentSearchDataView } as RecentSearchDataView

        if (recentSearchDataVisitable.list.size == 1) {
            removeRecentSearchTitle()
            removeRecentSearch()
        } else {
            recentSearchList?.let {
                deleteRecentSearchWithSeeMoreButton(keyword, it, recentSearchDataVisitable)
            } ?: run {
                deleteRecentSearchWithoutSeeMoreButton(keyword, recentSearchDataVisitable)
            }
        }

        view?.showInitialStateResult(listVisitable)
    }

    private fun deleteRecentSearchWithSeeMoreButton(
        keyword: String,
        recentSearchList: MutableList<InitialStateItem>,
        recentSearchDataVisitable: RecentSearchDataView,
    ) {
        val deleted = recentSearchList.find { item -> item.title == keyword }
        recentSearchList.remove(deleted)

        val recentSearchItems = recentSearchList.convertToRecentSearchDataView(
            getDimension90(),
            recentSearchDataVisitable.trackingOption,
            getQueryKey(),
        )
        if (recentSearchItems.size <= RECENT_SEARCH_SEE_MORE_LIMIT) {
            recentSearchDataVisitable.list = recentSearchItems
            removeSeeMoreRecentSearch()
        }
        else {
            recentSearchDataVisitable.list =
                recentSearchItems.take(RECENT_SEARCH_SEE_MORE_LIMIT)
                    as MutableList<BaseItemInitialStateSearch>
        }
    }

    private fun deleteRecentSearchWithoutSeeMoreButton(
        keyword: String,
        recentSearchDataVisitable: RecentSearchDataView
    ) {
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
        val recentSearchSeeMoreDataView =
            listVisitable.filterIsInstance<RecentSearchSeeMoreDataView>()

        listVisitable.removeAll(recentSearchSeeMoreDataView)
    }

    override fun deleteAllRecentSearch() {
        val params = DeleteRecentSearchUseCase.getParams(
            registrationId = userSession.deviceId,
            userId = userSession.userId,
            navSource = getNavSource()
        )
        deleteRecentSearchUseCase.execute(
                params,
                getDeleteAllRecentSearchSubscriber()
        )
    }

    private fun getDeleteAllRecentSearchSubscriber(): Subscriber<Boolean> =
        object : Subscriber<Boolean>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(isSuccess: Boolean) {
                onDeleteAllRecentSearchSuccess(isSuccess)
            }
        }

    private fun onDeleteAllRecentSearchSuccess(isSuccess: Boolean) {
        if (!isSuccess) return

        removeRecentSearchTitle()
        removeRecentSearch()
        removeSeeMoreRecentSearch()
        view?.showInitialStateResult(listVisitable)
    }

    override fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch) {
        trackEventItemClicked(item)
        val view = view ?: return

        view.route(item.applink, searchParameter)
        view.finish()
    }

    private fun trackEventItemClicked(item: BaseItemInitialStateSearch) {
        val view = view ?: return
        when(item.type) {
            TYPE_SHOP ->
                view.trackEventClickRecentShop(
                    item,
                    getRecentShopLabelForTracking(item),
                    getUserId(),
                )
            else ->
                view.trackEventClickRecentSearch(
                    item,
                    getItemEventLabelForTracking(item),
                )
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

        val recentSearchList = recentSearchList ?: return

        val recentSearchDataVisitable =
            listVisitable.find { it is RecentSearchDataView } as RecentSearchDataView

        val recentSearchItems = recentSearchList.convertToRecentSearchDataView(
            getDimension90(),
            recentSearchDataVisitable.trackingOption,
            getQueryKey(),
        )

        val recentSearchToImpress = getDataLayerForPromo(recentSearchList)
        val remainingRecentSearchCount = recentSearchList.size - RECENT_SEARCH_SEE_MORE_LIMIT
        onRecentSearchImpressed(
            recentSearchItems.takeLast(remainingRecentSearchCount),
            recentSearchToImpress.takeLast(remainingRecentSearchCount)
        )

        recentSearchDataVisitable.list = recentSearchItems

        this.recentSearchList = null

        val view = view ?: return

        view.trackEventClickSeeMoreRecentSearch(getUserId())
        view.dropKeyBoard()
        view.renderCompleteRecentSearch(recentSearchDataVisitable)
    }

    override fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch) {
        if (isTokoNow()) trackEventClickTokoNowDynamicSectionItem(item)
        else trackEventClickDynamicSectionItem(item)

        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    private fun trackEventClickTokoNowDynamicSectionItem(item: BaseItemInitialStateSearch) {
        val label = "value: ${item.title} - po: ${item.position} - page: ${item.applink}"
        view?.trackEventClickTokoNowDynamicSectionItem(label, item)
    }

    private fun trackEventClickDynamicSectionItem(item: BaseItemInitialStateSearch) {
        val label = "value: ${item.title} - title: ${item.header} - po: ${item.position}"
        view?.trackEventClickDynamicSectionItem(
            getUserId(),
            label,
            item,
            item.featureId,
            item.dimension90,
        )
    }

    override fun onCuratedCampaignCardClicked(curatedCampaignDataView: CuratedCampaignDataView) {
        val view = view ?: return
        val label = getCuratedCampaignEventLabel(curatedCampaignDataView)
        val baseItemInitialState = curatedCampaignDataView.baseItemInitialState

        view.trackEventClickCuratedCampaignCard(
            getUserId(),
            label,
            baseItemInitialState,
            baseItemInitialState.type,
            baseItemInitialState.campaignCode
        )

        view.route(baseItemInitialState.applink, searchParameter)
        view.finish()
    }

    override fun onRecentViewClicked(item: BaseItemInitialStateSearch) {
        val view = view ?: return
        val label = "po: ${item.position} - applink: ${item.applink}"
        view.trackEventClickRecentView(item, label)

        view.route(item.applink, searchParameter)
        view.finish()
    }

    override fun onProductLineClicked(item: BaseItemInitialStateSearch) {
        val view = view ?: return
        val label = "po: ${item.position} - applink: ${item.applink}"
        view.trackEventClickProductLine(item, getUserId(), label)

        view.route(item.applink, searchParameter)
        view.finish()
    }

    override fun onChipClicked(item: BaseItemInitialStateSearch) {
        val view = view ?: return
        val label = "value: ${item.title} - title: ${item.header} - po: ${item.position}"
        view.trackEventClickChip(getUserId(), label, item, item.featureId, item.dimension90)

        view.route(item.applink, searchParameter)
        view.finish()
    }

    override fun onSearchBarEducationClick(item: BaseItemInitialStateSearch) {
        val view = view ?: return
        view.trackEventClickSearchBarEducation(item)

        view.route(item.applink, searchParameter)
        view.finish()
    }

    private fun isReimagineVariantControl() = reimagine.search1InstAuto() == Search1InstAuto.CONTROL
}
