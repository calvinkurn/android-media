package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

private const val initialStateWith4DataSeeMoreRecentSearch = "autocomplete/initialstate/with-4-data-show-more-recent-search.json"
private const val initialStateWith5DataSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"

internal class DeleteRecentSearchTest: InitialStatePresenterTestFixtures() {

    private val isSuccessful = true

    private fun `Test Delete Recent Search Data`(initialStateData: List<InitialStateData>, item: BaseItemInitialStateSearch) {
        `Given view already get initial state`(initialStateData)

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
        verify { deleteRecentSearchUseCase.execute(any(), any()) }
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
        val recentSearchViewModel = slotDeletedVisitableList.captured.find { it is RecentSearchViewModel } as RecentSearchViewModel
        assert(!recentSearchViewModel.list.contains(item)) {
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

        `Test Delete Recent Search Data`(initialStateCommonData, item)
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
    fun `Test delete recent search data without removing see more button`() {
        val initialStateData = initialStateWith5DataSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
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
        `Then verify visitable list still have RecentSearchSeeMoreViewModel`()
    }

    private fun `Then verify visitable list still have RecentSearchSeeMoreViewModel`() {
        val seeMoreRecentSearchViewModel = slotDeletedVisitableList.captured.find { it is RecentSearchSeeMoreViewModel }
        assert(seeMoreRecentSearchViewModel != null) {
            "Visitable list should have SeeMoreRecentSearchViewModel"
        }
    }

    @Test
    fun `Test delete recent search data and remove see more button`() {
        val initialStateData = initialStateWith4DataSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
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
        `Then verify visitable list doesnt have RecentSearchSeeMoreViewModel`()
    }

    private fun `Then verify visitable list doesnt have RecentSearchSeeMoreViewModel`() {
        val seeMoreRecentSearchViewModel = slotDeletedVisitableList.captured.find { it is RecentSearchSeeMoreViewModel }
        assert(seeMoreRecentSearchViewModel == null) {
            "Visitable list should not have SeeMoreRecentSearchViewModel"
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
    fun `Test fail to delete all recent search data`() {
        `Given view already get initial state`(initialStateCommonData)

        `Given delete recent search API will return error`()
        `When presenter delete all recent search`()
        `Then verify deleteRecentSearch API is called`()
        `Then verify initial state view behavior is correct`()
    }
}