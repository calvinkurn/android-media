package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import rx.Subscriber

internal open class InitialStatePresenterTestFixtures {

    protected val initialStateView = mockk<InitialStateContract.View>(relaxed = true)

    protected val getInitialStateUseCase = mockk<UseCase<List<InitialStateData>>>(relaxed = true)
    protected val deleteRecentSearchUseCase = mockk<UseCase<Boolean>>(relaxed = true)
    protected val popularSearchUseCase = mockk<RefreshPopularSearchUseCase>(relaxed = true)

    protected val userSession = mockk<UserSessionInterface>(relaxed = true)

    protected val slotVisitableList = slot<List<Visitable<*>>>()
    protected val slotDeletedVisitableList = slot<List<Visitable<*>>>()
    protected val slotRefreshVisitableList = slot<List<Visitable<*>>>()

    protected val slotRecentViewItemList = slot<MutableList<Any>>()
    protected val slotRecentSearchItemList = slot<MutableList<Any>>()
    protected val slotPopularSearchItemList = slot<MutableList<Any>>()

    protected lateinit var initialStatePresenter: InitialStatePresenter

    protected val testException = TestException("Error")

    private val initialStateCommonResponse = "autocomplete/initialstate/common-response.json"
    protected val initialStateCommonData = initialStateCommonResponse.jsonToObject<InitialStateUniverse>().data

    protected val ID_POPULAR_SEARCH = "popular_search"
    protected val ID_NEW_SECTION = "new_section"

    @Before
    fun setUp() {
        initialStatePresenter = InitialStatePresenter(
                getInitialStateUseCase,
                deleteRecentSearchUseCase,
                popularSearchUseCase,
                userSession)
        initialStatePresenter.attachView(initialStateView)
    }

    protected fun `Given view already get initial state`(list: List<InitialStateData>) {
        `Given initial state use case capture request params`(list)
        `Given presenter get initial state data`()
    }

    private fun `Given presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    protected fun `Given initial state use case capture request params`(list: List<InitialStateData>) {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(list)
        }
    }

    protected fun `Given initial state view called showInitialStateResult behavior`() {
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    protected fun `Then verify initial state view do nothing behavior`() {
        confirmVerified(initialStateView)
    }

    protected fun `Given refresh popular search API will return data`(list: List<InitialStateData>) {
        every { popularSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(list)
        }
    }

    protected fun `Then verify popularSearch API is called`() {
        verify { popularSearchUseCase.execute(any(), any()) }
    }

    protected fun `Then verify refreshPopularSearch view behavior`() {
        verifyOrder {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))

            //This showInitialStateResult is called the first time it loads initial state data
            initialStateView.showInitialStateResult(capture(slotVisitableList))

            //This showInitialStateResult is called when refresh popular search is clicked
            initialStateView.showInitialStateResult(capture(slotRefreshVisitableList))
        }
    }

    protected fun `Then verify refreshPopularSearch view behavior with no new refreshed data`() {
        verify(exactly = 1) {
            //This showInitialStateResult is only called once when it loads initial state data
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }
}