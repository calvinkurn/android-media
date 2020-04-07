package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateCommonResponse
import com.tokopedia.autocomplete.initialstate.testinstance.popularSearchCommonResponse
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Subscriber

internal class InitialStatePresenterTest {
    private val initialStateView = mockk<InitialStateContract.View>(relaxed = true)

    private val getInitialStateUseCase = mockk<InitialStateUseCase>(relaxed = true)
    private val deleteRecentSearchUseCase = mockk<DeleteRecentSearchUseCase>(relaxed = true)
    private val popularSearchUseCase = mockk<RefreshPopularSearchUseCase>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val slotVisitableList = slot<List<Visitable<*>>>()
    private val slotRefreshVisitableList = slot<List<Visitable<*>>>()

    private lateinit var initialStatePresenter: InitialStatePresenter

    private val testException = TestException("Error")

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
        `when presenter get initial state data` ()
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
        Assert.assertTrue(visitableList[2] is PopularSearchTitleViewModel)
        Assert.assertTrue(visitableList[3] is PopularSearchViewModel)
        Assert.assertTrue(visitableList.size == 4)
    }

    @Test
    fun `test fail to get initial state data`() {
        `given initial state API will return error`()
        `when presenter get initial state data` ()
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
        `given presenter get initial state data` ()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given refresh popular search API will return data`()
        `when presenter refresh popular search `()
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

    private fun `when presenter refresh popular search `() {
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

    private fun `then verify visitable list after refresh popular search`(){
        val refreshVisitableList = slotRefreshVisitableList.captured
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(
                (refreshVisitableList[3] as PopularSearchViewModel).list.size == (visitableList[3] as PopularSearchViewModel).list.size
        )
    }

    @Test
    fun `test fail to refresh popular search data`() {
        `given initial state use case capture request params`()
        `given presenter get initial state data` ()
        `given initial state API is called`()
        `given initial state view called showInitialStateResult behavior`()
        `given refresh popular search API will return error`()
        `when presenter refresh popular search `()
        `then verify popularSearch API is called`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given refresh popular search API will return error`() {
        every { popularSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateItem>>>().onStart()
            secondArg<Subscriber<List<InitialStateItem>>>().onError(testException)
        }
    }
}