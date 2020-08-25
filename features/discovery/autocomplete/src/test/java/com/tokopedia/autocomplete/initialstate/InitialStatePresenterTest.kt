package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateCommonResponse
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateEmptyDataResponse
import com.tokopedia.autocomplete.initialstate.testinstance.popularSearchCommonResponse
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

internal class InitialStatePresenterTest: InitialStatePresenterTestFixtures() {
    private val isSuccessful = true
    private val ID_POPULAR_SEARCH = "popular_search"

    private fun `Test Initial State Data`(list: List<InitialStateData>) {
        `Given initial state use case capture request params`(list)
        `When presenter get initial state data`()
        `Then verify initial state API is called`()
    }

    private fun `Given initial state use case capture request params`(list: List<InitialStateData>) {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(list)
        }
    }

    private fun `When presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    private fun `Then verify initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }

    @Test
    fun `Test get initial state data`() {
        `Test Initial State Data`(initialStateCommonResponse)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list`()
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `Then verify visitable list`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is RecentViewTitleViewModel)
        Assert.assertTrue(visitableList[1] is RecentViewViewModel)
        Assert.assertTrue(visitableList[2] is RecentSearchTitleViewModel)
        Assert.assertTrue(visitableList[3] is RecentSearchViewModel)
        Assert.assertTrue(visitableList[4] is PopularSearchTitleViewModel)
        Assert.assertTrue(visitableList[5] is PopularSearchViewModel)
        Assert.assertTrue(visitableList[6] is DynamicInitialStateTitleViewModel)
        Assert.assertTrue(visitableList[7] is DynamicInitialStateSearchViewModel)
        Assert.assertTrue(visitableList.size == 8)
    }

    @Test
    fun `Test fail to get initial state data`() {
        `Given initial state API will return error`()
        `When presenter get initial state data`()
        `Then verify initial state API is called`()
        `Then verify initial state view do nothing behavior`()
    }

    private fun `Given initial state API will return error`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onError(testException)
        }
    }

    private fun `Then verify initial state view do nothing behavior`() {
        confirmVerified(initialStateView)
    }

    @Test
    fun `tTest refresh popular search data`() {
        `Test Initial State Data`(initialStateCommonResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given refresh popular search API will return data`()
        `When presenter refresh popular search`(ID_POPULAR_SEARCH)
        `Then verify popularSearch API is called`()
        `Then verify initial state view behavior`()
        `Then verify visitable list after refresh popular search`()
    }

    private fun `Given initial state view called showInitialStateResult behavior`() {
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `Given refresh popular search API will return data`() {
        every { popularSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(popularSearchCommonResponse)
        }
    }

    private fun `When presenter refresh popular search`(id: String) {
        initialStatePresenter.refreshPopularSearch(id)
    }

    private fun `Then verify popularSearch API is called`() {
        verify { popularSearchUseCase.execute(any(), any()) }
    }

    private fun `Then verify initial state view behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotRefreshVisitableList))
        }
    }

    private fun `Then verify visitable list after refresh popular search`() {
        val refreshVisitableList = slotRefreshVisitableList.captured
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(
                (refreshVisitableList[5] as PopularSearchViewModel).list.size == (visitableList[5] as PopularSearchViewModel).list.size
        )
    }

    @Test
    fun `test fail to refresh popular search data`() {
        `Test Initial State Data`(initialStateCommonResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given refresh popular search API will return error`()
        `When presenter refresh popular search`(ID_POPULAR_SEARCH)
        `Then verify popularSearch API is called`()
        `Then verify initial state view do nothing behavior`()
    }

    private fun `Given refresh popular search API will return error`() {
        every { popularSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateItem>>>().onStart()
            secondArg<Subscriber<List<InitialStateItem>>>().onError(testException)
        }
    }

    @Test
    fun `Test delete recent search data with type keyword`() {
        val item = BaseItemInitialStateSearch(
                template = "list_single_line",
                applink = "tokopedia://search?q=sepatu&source=universe&st=product",
                url = "/search?q=sepatu&source=universe&st=product",
                title = "sepatu",
                label = "keyword",
                type = "keyword",
                productId = "0",
                shortcutImage = "https://shortcut"
        )

        `Test Initial State Data`(initialStateCommonResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given delete recent search API will return data`()
        `When presenter delete recent search`(item)
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view called deleteRecentSearch`()
        `Then verify visitable list doesnt have keyword samsung in recent search`(item)
    }

    private fun `Given delete recent search API will return data`() {
        every { deleteRecentSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onNext(isSuccessful)
        }
    }

    private fun `When presenter delete recent search`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.deleteRecentSearchItem(item)
    }

    private fun `Then verify deleteRecentSearch API is called`() {
        verify { deleteRecentSearchUseCase.execute(any(), any()) }
    }

    private fun `Then verify initial state view called deleteRecentSearch`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotDeletedVisitableList))
        }
    }

    private fun `Then verify visitable list doesnt have keyword samsung in recent search`(item: BaseItemInitialStateSearch) {
        val newList = slotDeletedVisitableList.captured
        assert(newList[3] is RecentSearchViewModel) {
            "Should be RecentSearchViewModel"
        }
        assert(!(newList[3] as RecentSearchViewModel).list.contains(item)) {
            "Recent Search ${item.title} should be deleted"
        }
    }

    @Test
    fun `Test delete recent search data with type shop`() {
        val item = BaseItemInitialStateSearch(
                template = "list_double_line",
                applink = "tokopedia://shop/8384142?source=universe&st=product",
                url = "/mizanbookc?source=universe&st=product",
                title = "MizanBookCorner",
                label = "Toko",
                shortcutImage = "https://shortcut",
                type = "shop"
        )

        `Test Initial State Data`(initialStateCommonResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given delete recent search API will return data`()
        `When presenter delete recent search`(item)
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view called deleteRecentSearch`()
        `Then verify visitable list doesnt have shop in recent search`(item)
    }

    private fun `Then verify visitable list doesnt have shop in recent search`(item: BaseItemInitialStateSearch) {
        val newList = slotDeletedVisitableList.captured
        assert(newList[3] is RecentSearchViewModel) {
            "Should be RecentSearchViewModel"
        }
        assert(!(newList[3] as RecentSearchViewModel).list.contains(item)) {
            "Recent Search ${item.title} should be deleted"
        }
    }

    @Test
    fun  `Test fail to delete recent search data`() {
        val item = BaseItemInitialStateSearch(
                title = "samsung",
                type = "keyword",
                productId = "0"
        )

        `Test Initial State Data`(initialStateCommonResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given delete recent search API will return error`()
        `When presenter delete recent search`(item)
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view do nothing behavior`()
    }

    private fun `Given delete recent search API will return error`() {
        every { deleteRecentSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onError(testException)
        }
    }

    @Test
    fun `Test delete all recent search data`() {
        `Test Initial State Data`(initialStateCommonResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given delete recent search API will return data`()
        `When presenter delete all recent search`()
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view called deleteRecentSearch`()
        `Then verify visitable list doesnt have recent search anymore`()
    }

    private fun `When presenter delete all recent search`() {
        initialStatePresenter.deleteAllRecentSearch()
    }

    private fun `Then verify visitable list doesnt have recent search anymore`() {
        val list = slotDeletedVisitableList.captured
        Assert.assertTrue(list[0] is RecentViewTitleViewModel)
        Assert.assertTrue(list[1] is RecentViewViewModel)
        Assert.assertTrue(list[2] is PopularSearchTitleViewModel)
        Assert.assertTrue(list[3] is PopularSearchViewModel)
        Assert.assertTrue(list[4] is DynamicInitialStateTitleViewModel)
        Assert.assertTrue(list[5] is DynamicInitialStateSearchViewModel)
        Assert.assertTrue(list.size == 6)
    }

    @Test
    fun `test fail to delete all recent search data`() {
        `Test Initial State Data`(initialStateCommonResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given delete recent search API will return error`()
        `When presenter delete all recent search`()
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view do nothing behavior`()
    }

    @Test
    fun `test get initial state impression`() {
        `Test Initial State Data`(initialStateCommonResponse)

        `Then verify initial state impression is called`()
        `Then verify list impression data`()
    }

    private fun `Then verify initial state impression is called`() {
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
        }
    }

    private fun `Then verify list impression data`() {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.captured
        val popularSearchItemList = slotPopularSearchItemList.captured

        val recentViewListResponse = getDataLayerForRecentView(initialStateCommonResponse[0].items)
        val recentSearchListResponse = getDataLayerForPromo(initialStateCommonResponse[1].items)
        val popularSearchListResponse = getDataLayerForPromo(initialStateCommonResponse[2].items)

        assert(recentViewItemList.containsAll(recentViewListResponse))
        assert(recentSearchItemList.containsAll(recentSearchListResponse))
        assert(popularSearchItemList.containsAll(popularSearchListResponse))
    }

    private fun getDataLayerForRecentView(list: List<InitialStateItem>): MutableList<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForRecentView(position))
        }
        return dataLayerList
    }

    private fun getDataLayerForPromo(list: List<InitialStateItem>): MutableList<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForPromo(position))
        }
        return dataLayerList
    }

    @Test
    fun `Test initial state impression with empty items`() {
        `Test Initial State Data`(initialStateEmptyDataResponse)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify initial state view do nothing behavior`()
    }
}