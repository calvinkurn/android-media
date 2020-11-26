package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

internal class DeleteRecentSearchTest: InitialStatePresenterTestFixtures() {

    private val isSuccessful = true

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

        `Given view already get initial state`(initialStateCommonData)

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

        `Given view already get initial state`(initialStateCommonData)

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