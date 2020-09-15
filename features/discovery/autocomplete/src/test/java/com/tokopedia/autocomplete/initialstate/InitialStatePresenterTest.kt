package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel
import com.tokopedia.autocomplete.initialstate.recentview.ReecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateCommonResponse
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateEmptyDataResponse
import com.tokopedia.autocomplete.initialstate.testinstance.popularSearchCommonResponse
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-show-more-recent-search.json"

internal class InitialStatePresenterTest: InitialStatePresenterTestFixtures() {
    private val isSuccessful = true

    private fun `Test Initial State Data`(list: List<InitialStateData>) {
        `Given initial state use case capture request params`(list)
        `When presenter get initial state data`()
        `Then verify initial state API is called`()
        `Then verify initial state view will call showInitialStateResult behavior`()
    }

    private fun `When presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    private fun `Then verify initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    @Test
    fun `Test get initial state data without Show More Recent Search`() {
        `Test Initial State Data`(initialStateCommonData)

        `Then verify visitable list has no RecentSearchSeeMoreViewModel`()
    }

    private fun `Then verify visitable list has no RecentSearchSeeMoreViewModel`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is ReecentViewTitleViewModel)
        Assert.assertTrue(visitableList[1] is RecentViewViewModel)
        Assert.assertTrue(visitableList[2] is RecentSearchTitleViewModel)
        Assert.assertTrue(visitableList[3] is RecentSearchViewModel)
        Assert.assertTrue(visitableList[4] is PopularSearchTitleViewModel)
        Assert.assertTrue(visitableList[5] is PopularSearchViewModel)
        Assert.assertTrue(visitableList.size == 6)
    }

    @Test
    fun `Test get initial state data with Show More Recent Search`() {
        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
        `Test Initial State Data`(initialStateData)

        `Then verify visitable list has RecentSearchSeeMoreViewModel`()
    }

    private fun `Then verify visitable list has RecentSearchSeeMoreViewModel`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is ReecentViewTitleViewModel)
        Assert.assertTrue(visitableList[1] is RecentViewViewModel)
        Assert.assertTrue(visitableList[2] is RecentSearchTitleViewModel)
        Assert.assertTrue(visitableList[3] is RecentSearchViewModel)
        Assert.assertTrue(visitableList[4] is RecentSearchSeeMoreViewModel)
        Assert.assertTrue(visitableList[5] is PopularSearchTitleViewModel)
        Assert.assertTrue(visitableList[6] is PopularSearchViewModel)
        Assert.assertTrue(visitableList.size == 7)
    }

    private fun `given initial state use case capture request params`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateCommonResponse)
        }
    }

    private fun `when presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    @Test
    fun `test fail to get initial state data`() {
        `given initial state API will return error`()
        `when presenter get initial state data`()
        `Then verify initial state API is called`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given initial state API will return error`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onError(testException)
        }
    }

    private fun `then verify initial state view do nothing behavior`() {
        confirmVerified(initialStateView)
    }

    @Test
    fun `test refresh popular search data`() {
        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given refresh popular search API will return data`()
        `when presenter refresh popular search`()
        `then verify popularSearch API is called`()
        `then verify initial state view behavior`()
        `then verify visitable list after refresh popular search`()
    }

    private fun `given presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    private fun `given initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }

    private fun `given initial state view called showInitialStateResult behavior`() {
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `given refresh popular search API will return data`() {
        every { popularSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(popularSearchCommonResponse)
        }
    }

    private fun `when presenter refresh popular search`() {
        initialStatePresenter.refreshPopularSearch()
    }

    private fun `then verify popularSearch API is called`() {
        verify { popularSearchUseCase.execute(any(), any()) }
    }

    private fun `then verify initial state view behavior`() {
        verify {
            initialStateView.refreshPopularSearch(capture(slotRefreshVisitableList))
        }
    }

    private fun `then verify visitable list after refresh popular search`() {
        val refreshVisitableList = slotRefreshVisitableList.captured
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(
                (refreshVisitableList[6] as PopularSearchViewModel).list.size == (visitableList[6] as PopularSearchViewModel).list.size
        )
    }

    @Test
    fun `test fail to refresh popular search data`() {
        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given refresh popular search API will return error`()
        `when presenter refresh popular search`()
        `then verify popularSearch API is called`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given refresh popular search API will return error`() {
        every { popularSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateItem>>>().onStart()
            secondArg<Subscriber<List<InitialStateItem>>>().onError(testException)
        }
    }

    @Test
    fun `test delete recent search data with type keyword`() {
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

        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given delete recent search API will return data`()
        `when presenter delete recent search`(item)
        `then verify deleteRecentSearch API is called`()
        `then verify initial state view called deleteRecentSearch`()
        `then verify visitable list doesnt have keyword samsung in recent search`(item)
    }

    private fun `given delete recent search API will return data`() {
        every { deleteRecentSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onNext(isSuccessful)
        }
    }

    private fun `when presenter delete recent search`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.deleteRecentSearchItem(item)
    }

    private fun `then verify deleteRecentSearch API is called`() {
        verify { deleteRecentSearchUseCase.execute(any(), any()) }
    }

    private fun `then verify initial state view called deleteRecentSearch`() {
        verify {
            initialStateView.deleteRecentSearch(capture(slotDeletedVisitableList))
        }
    }

    private fun `then verify visitable list doesnt have keyword samsung in recent search`(item: BaseItemInitialStateSearch) {
        val newList = slotDeletedVisitableList.captured
        assert(newList[1] is RecentSearchViewModel) {
            "Should be RecentSearchViewModel"
        }
        assert(!(newList[1] as RecentSearchViewModel).list.contains(item)) {
            "Recent Search ${item.title} should be deleted"
        }
    }

    @Test
    fun `test delete recent search data with type shop`() {
        val item = BaseItemInitialStateSearch(
                template = "list_double_line",
                applink = "tokopedia://shop/8384142?source=universe&st=product",
                url = "/mizanbookc?source=universe&st=product",
                title = "MizanBookCorner",
                label = "Toko",
                shortcutImage = "https://shortcut",
                type = "shop"
        )

        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given delete recent search API will return data`()
        `when presenter delete recent search`(item)
        `then verify deleteRecentSearch API is called`()
        `then verify initial state view called deleteRecentSearch`()
        `then verify visitable list doesnt have shop in recent search`(item)
    }

    private fun `then verify visitable list doesnt have shop in recent search`(item: BaseItemInitialStateSearch) {
        val newList = slotDeletedVisitableList.captured
        assert(newList[1] is RecentSearchViewModel) {
            "Should be RecentSearchViewModel"
        }
        assert(!(newList[1] as RecentSearchViewModel).list.contains(item)) {
            "Recent Search ${item.title} should be deleted"
        }
    }

    @Test
    fun  `test fail to delete recent search data`() {
        val item = BaseItemInitialStateSearch(
                title = "samsung",
                type = "keyword",
                productId = "0"
        )

        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given delete recent search API will return error`()
        `when presenter delete recent search`(item)
        `then verify deleteRecentSearch API is called`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given delete recent search API will return error`() {
        every { deleteRecentSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onError(testException)
        }
    }

    @Test
    fun `test delete all recent search data`() {
        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given delete recent search API will return data`()
        `when presenter delete all recent search`()
        `then verify deleteRecentSearch API is called`()
        `then verify initial state view called deleteRecentSearch`()
        `then verify visitable list doesnt have recent search anymore`()
    }

    private fun `when presenter delete all recent search`() {
        initialStatePresenter.deleteAllRecentSearch()
    }

    private fun `then verify visitable list doesnt have recent search anymore`() {
        val list = slotDeletedVisitableList.captured
        Assert.assertTrue(list[0] is ReecentViewTitleViewModel)
        Assert.assertTrue(list[1] is RecentViewViewModel)
        Assert.assertTrue(list[2] is PopularSearchTitleViewModel)
        Assert.assertTrue(list[3] is PopularSearchViewModel)
        Assert.assertTrue(list.size == 4)
    }

    @Test
    fun `test fail to delete all recent search data`() {
        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given delete recent search API will return error`()
        `when presenter delete all recent search`()
        `then verify deleteRecentSearch API is called`()
        `then verify initial state view do nothing behavior`()
    }

    @Test
    fun `test get initial state impression`() {
        `given initial state use case capture request params`()
        `when presenter get initial state data`()
        `Then verify initial state API is called`()
        `then verify initial state impression is called`()
        `then verify list impression data`()
    }

    private fun `then verify initial state impression is called`() {
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
        }
    }

    private fun `then verify list impression data`() {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.captured
        val popularSearchItemList = slotPopularSearchItemList.captured

        val recentSearchListResponse = getDataLayerForPromo(initialStateCommonResponse[0].items)
        val recentViewListResponse = getDataLayerForRecentView(initialStateCommonResponse[1].items)
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
    fun `test initial state impression with empty items`() {
        `given initial state use case get empty data response`()
        `when presenter get initial state data`()
        `Then verify initial state API is called`()
        `Then verify initial state view will call showInitialStateResult behavior`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given initial state use case get empty data response`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateEmptyDataResponse)
        }
    }
}