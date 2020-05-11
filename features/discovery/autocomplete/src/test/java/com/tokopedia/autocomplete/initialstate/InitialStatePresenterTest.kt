package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel
import com.tokopedia.autocomplete.initialstate.recentview.ReecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateCommonResponse
import com.tokopedia.autocomplete.initialstate.testinstance.popularSearchCommonResponse
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Subscriber

internal class InitialStatePresenterTest {
    private val initialStateView = mockk<InitialStateContract.View>(relaxed = true)

    private val getInitialStateUseCase = mockk<UseCase<List<InitialStateData>>>(relaxed = true)
    private val deleteRecentSearchUseCase = mockk<DeleteRecentSearchUseCase>(relaxed = true)
    private val popularSearchUseCase = mockk<RefreshPopularSearchUseCase>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val slotVisitableList = slot<List<Visitable<*>>>()
    private val slotDeletedVisitableList = slot<List<Visitable<*>>>()
    private val slotRefreshVisitableList = slot<List<Visitable<*>>>()

    private lateinit var initialStatePresenter: InitialStatePresenter

    private val testException = TestException("Error")

    private val deleteKeyword = "samsung"
    private val isSuccessful = true

    @Before
    fun setUp() {
        initialStatePresenter = InitialStatePresenter(
                getInitialStateUseCase,
                deleteRecentSearchUseCase,
                popularSearchUseCase,
                userSession)
        initialStatePresenter.attachView(initialStateView)
    }

    @Test
    fun `test get initial state data`() {
        `given initial state use case capture request params`()
        `when presenter get initial state data`()
        `then verify initial state API is called`()
        `then verify initial state view will call showInitialStateResult behavior`()
        `then verify visitable list`()
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

    private fun `then verify initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }

    private fun `then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `then verify visitable list`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is RecentSearchTitleViewModel)
        Assert.assertTrue(visitableList[1] is RecentSearchViewModel)
        Assert.assertTrue(visitableList[2] is ReecentViewTitleViewModel)
        Assert.assertTrue(visitableList[3] is RecentViewViewModel)
        Assert.assertTrue(visitableList[4] is PopularSearchTitleViewModel)
        Assert.assertTrue(visitableList[5] is PopularSearchViewModel)
        Assert.assertTrue(visitableList.size == 6)
    }

    @Test
    fun `test fail to get initial state data`() {
        `given initial state API will return error`()
        `when presenter get initial state data`()
        `then verify initial state API is called`()
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
                (refreshVisitableList[5] as PopularSearchViewModel).list.size == (visitableList[5] as PopularSearchViewModel).list.size
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
    fun `test delete recent search data`() {
        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given delete recent search API will return data`()
        `when presenter delete recent search`()
        `then verify deleteRecentSearch API is called`()
        `then verify initial state view called deleteRecentSearch`()
        `then verify visitable list doesnt have keyword samsung in recent search`()
    }

    private fun `given delete recent search API will return data`() {
        every { deleteRecentSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onNext(isSuccessful)
        }
    }

    private fun `when presenter delete recent search`() {
        initialStatePresenter.deleteRecentSearchItem(deleteKeyword)
    }

    private fun `then verify deleteRecentSearch API is called`() {
        verify { deleteRecentSearchUseCase.execute(any(), any()) }
    }

    private fun `then verify initial state view called deleteRecentSearch`() {
        verify {
            initialStateView.deleteRecentSearch(capture(slotDeletedVisitableList))
        }
    }

    private fun `then verify visitable list doesnt have keyword samsung in recent search`() {
        val newList = slotDeletedVisitableList.captured
        Assert.assertTrue(newList[1] is RecentSearchViewModel)
        Assert.assertFalse(
                (newList[1] as RecentSearchViewModel).list.contains(
                        BaseItemInitialStateSearch(
                                template = "list_single_line",
                                applink = "tokopedia://search?q=sepatu&source=universe&st=product",
                                url = "/search?q=sepatu&source=universe&st=product",
                                title = "sepatu",
                                label = "keyword",
                                shortcutImage = "https://shortcut"
                        )
                )
        )
    }

    @Test
    fun `test fail to delete recent search data`() {
        `given initial state use case capture request params`()
        `given presenter get initial state data`()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given delete recent search API will return error`()
        `when presenter delete recent search`()
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
}