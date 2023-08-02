package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchSeeMoreDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

private const val initialStateWith4DataSeeMoreRecentSearch = "autocomplete/initialstate/with-4-data-show-more-recent-search.json"
private const val initialStateWith5DataSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"
private const val singleRecentSearchResponse = "autocomplete/initialstate/single-recent-search-response.json"

internal class DeleteRecentSearchTest: InitialStatePresenterTestFixtures() {

    private val isSuccessful = true
    private val deleteRecentSearchRequestParams = slot<RequestParams>()

    private fun `Test Delete Recent Search Data`(
        initialStateUniverse: InitialStateUniverse,
        item: BaseItemInitialStateSearch
    ) {
        `Given view already get initial state`(initialStateUniverse)

        `Given delete recent search API will return data`()
        `When presenter delete recent search`(item)
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view called deleteRecentSearch`()
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
        verify {
            deleteRecentSearchUseCase.execute(capture(deleteRecentSearchRequestParams), any())
        }
    }

    private fun `Then verify initial state view called deleteRecentSearch`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotDeletedVisitableList))
        }
    }

    @Test
    fun `Test delete recent search data with type keyword`() {
        val item = BaseItemInitialStateSearch(
                template = "list_single_line",
                applink = "tokopedia://search?q=samsung&source=universe&st=product",
                url = "/search?q=samsung&source=universe&st=product",
                title = "samsung",
                label = "keyword",
                type = "keyword",
                productId = "0",
                shortcutImage = "https://shortcut"
        )

        `Test Delete Recent Search Data`(initialStateCommonData, item)
        `Then verify visitable list doesnt have the deleted keyword in recent search`(item)
    }

    private fun `Then verify visitable list doesnt have the deleted keyword in recent search`(item: BaseItemInitialStateSearch) {
        val recentSearchDataView = slotDeletedVisitableList.last().find { it is RecentSearchDataView } as RecentSearchDataView
        assert(!recentSearchDataView.list.contains(item)) {
            "Recent Search ${item.title} should be deleted"
        }
    }

    @Test
    fun `Test delete recent search data with type keyword on single recent search`() {
        val item = BaseItemInitialStateSearch(
            template = "list_single_line",
            applink = "tokopedia://search?q=samsung&source=universe&st=product",
            url = "/search?q=samsung&source=universe&st=product",
            title = "samsung",
            label = "keyword",
            type = "keyword",
            productId = "0",
            shortcutImage = "https://shortcut"
        )

        val singleRecentSearchData = singleRecentSearchResponse.jsonToObject<InitialStateUniverse>()

        `Test Delete Recent Search Data`(singleRecentSearchData, item)
        `Then verify visitable list does not have recent search`()
    }

    private fun `Then verify visitable list does not have recent search`() {
        slotDeletedVisitableList.last().none { it is RecentSearchDataView } shouldBe true
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

        `Test Delete Recent Search Data`(initialStateCommonData, item)
        `Then verify visitable list doesnt have shop in recent search`(item)
    }

    private fun `Then verify visitable list doesnt have shop in recent search`(item: BaseItemInitialStateSearch) {
        val newList = slotDeletedVisitableList.last()
        assert(newList[3] is RecentSearchDataView) {
            "Should be RecentSearchDataView"
        }
        assert(!(newList[3] as RecentSearchDataView).list.contains(item)) {
            "Recent Search ${item.title} should be deleted"
        }
    }

    @Test
    fun `Test delete recent search data without removing see more button`() {
        val initialStateData = initialStateWith5DataSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>()
        val item = BaseItemInitialStateSearch(
                template = "list_single_line",
                applink = "tokopedia://search?q=samsung&source=universe&st=product",
                url = "/search?q=samsung&source=universe&st=product",
                title = "samsung",
                label = "keyword",
                type = "keyword",
                productId = "0",
                shortcutImage = "https://shortcut"
        )

        `Test Delete Recent Search Data`(initialStateData, item)
        `Then verify visitable list doesnt have the deleted keyword in recent search`(item)
        `Then verify visitable list still have RecentSearchSeeMoreDataView`()
    }

    private fun `Then verify visitable list still have RecentSearchSeeMoreDataView`() {
        val recentSearchSeeMoreDataView = slotDeletedVisitableList.last().find { it is RecentSearchSeeMoreDataView }
        assert(recentSearchSeeMoreDataView != null) {
            "Visitable list should have SeeMoreRecentSearchDataView"
        }
    }

    @Test
    fun `Test delete recent search data and remove see more button`() {
        val initialStateData = initialStateWith4DataSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>()
        val item = BaseItemInitialStateSearch(
                template = "list_single_line",
                applink = "tokopedia://search?q=samsung&source=universe&st=product",
                url = "/search?q=samsung&source=universe&st=product",
                title = "samsung",
                label = "keyword",
                type = "keyword",
                productId = "0",
                shortcutImage = "https://shortcut"
        )

        `Test Delete Recent Search Data`(initialStateData, item)
        `Then verify visitable list doesnt have the deleted keyword in recent search`(item)
        `Then verify visitable list doesnt have RecentSearchSeeMoreDataView`()
    }

    private fun `Then verify visitable list doesnt have RecentSearchSeeMoreDataView`() {
        val recentSearchSeeMoreDataView = slotDeletedVisitableList.last().find { it is RecentSearchSeeMoreDataView }
        assert(recentSearchSeeMoreDataView == null) {
            "Visitable list should not have SeeMoreRecentSearchDataView"
        }
    }

    @Test
    fun  `Test fail to delete recent search data`() {
        val item = BaseItemInitialStateSearch(
                title = "samsung",
                type = "keyword",
                productId = "0"
        )

        `Given view already get initial state`(initialStateCommonData)

        `Given delete recent search API will return error`()
        `When presenter delete recent search`(item)
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view behavior is correct`()
    }

    private fun `Given delete recent search API will return error`() {
        every { deleteRecentSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onError(testException)
        }
    }

    @Test
    fun `Test delete all recent search data`() {
        `Given view already get initial state`(initialStateCommonData)

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
        val list = slotDeletedVisitableList.last()
        Assert.assertTrue(list[0] is RecentViewTitleDataView)
        Assert.assertTrue(list[1] is RecentViewDataView)
        Assert.assertTrue(list[2] is PopularSearchTitleDataView)
        Assert.assertTrue(list[3] is PopularSearchDataView)
        Assert.assertTrue(list[4] is DynamicInitialStateTitleDataView)
        Assert.assertTrue(list[5] is DynamicInitialStateSearchDataView)
        Assert.assertTrue(list.size == 6)
    }

    @Test
    fun `Test fail to delete all recent search data`() {
        `Given view already get initial state`(initialStateCommonData)

        `Given delete recent search API will return error`()
        `When presenter delete all recent search`()
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view behavior is correct`()
    }
}
