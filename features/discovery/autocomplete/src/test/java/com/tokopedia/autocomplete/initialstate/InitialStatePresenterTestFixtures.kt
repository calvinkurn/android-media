package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.complete
import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import rx.Subscriber

internal open class InitialStatePresenterTestFixtures {
    protected val initialStateView = mockk<InitialStateContract.View>(relaxed = true)

    protected val getInitialStateUseCase = mockk<UseCase<List<InitialStateData>>>(relaxed = true)
    protected val deleteRecentSearchUseCase = mockk<UseCase<Boolean>>(relaxed = true)
    protected val refreshInitialStateUseCase = mockk<UseCase<List<InitialStateData>>>(relaxed = true)

    protected val userSession = mockk<UserSessionInterface>(relaxed = true)

    protected val slotVisitableList = slot<List<Visitable<*>>>()
    protected val slotDeletedVisitableList = slot<List<Visitable<*>>>()
    protected val slotRefreshVisitableList = slot<List<Visitable<*>>>()

    protected val slotRecentViewItemList = slot<MutableList<Any>>()
    protected val slotRecentSearchItemList = slot<MutableList<Any>>()
    protected val slotPopularSearchTrackingModel = slot<DynamicInitialStateItemTrackingModel>()
    protected val slotDynamicSectionTrackingModel = slot<DynamicInitialStateItemTrackingModel>()

    protected lateinit var initialStatePresenter: InitialStatePresenter

    protected val testException = TestException("Error")

    protected val initialStateCommonResponse = "autocomplete/initialstate/common-response.json"
    protected val initialStateCommonData = initialStateCommonResponse.jsonToObject<InitialStateUniverse>().data

    protected val ID_POPULAR_SEARCH = "popular_search"
    protected val ID_NEW_SECTION = "new_section"

    @Before
    fun setUp() {
        initialStatePresenter = InitialStatePresenter(
                getInitialStateUseCase,
                deleteRecentSearchUseCase,
                refreshInitialStateUseCase,
                userSession)
        initialStatePresenter.attachView(initialStateView)
    }

    protected fun `Given view already get initial state`(list: List<InitialStateData>) {
        `Given initial state use case capture request params`(list)
        `Given presenter get initial state data`()
    }

    protected fun `Given initial state use case capture request params`(list: List<InitialStateData>) {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().complete(list)
        }
    }

    private fun `Given presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    protected fun `Given view already get initial state`(responseJSON: String) {
        val initialStateUniverse = responseJSON.jsonToObject<InitialStateUniverse>()

        `Given initial state use case capture request params`(initialStateUniverse.data)
        `Given view will show initial state result`()
        `Given presenter get initial state data`()
    }

    private fun `Given view will show initial state result`() {
        every { initialStateView.showInitialStateResult(capture(slotVisitableList)) } just runs
    }

    protected fun `Then verify initial state view behavior is correct`() {
        verifyOrder {
            initialStateView.chooseAddressData
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(capture(slotDynamicSectionTrackingModel))
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
        confirmVerified(initialStateView)
    }

    protected fun `Then verify view interaction for load data failed with exception`() {
        verify {
            initialStateView.chooseAddressData
        }
        confirmVerified(initialStateView)
    }

    protected fun `Given refresh popular search API will return data`(list: List<InitialStateData>) {
        every { refreshInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(list)
        }
    }

    protected fun `Then verify popularSearch API is called`() {
        verify { refreshInitialStateUseCase.execute(any(), any()) }
    }

    protected fun `Then verify refreshPopularSearch view behavior`(refreshedPosition: Int) {
        verifyOrder {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchTrackingModel))

            //This showInitialStateResult is called the first time it loads initial state data
            initialStateView.showInitialStateResult(capture(slotVisitableList))

            //This showInitialStateResult is called when refresh popular search is clicked
            initialStateView.refreshViewWithPosition(refreshedPosition)
        }
    }

    protected fun `Then verify refreshPopularSearch view behavior with no new refreshed data`() {
        verify(exactly = 1) {
            //This showInitialStateResult is only called once when it loads initial state data
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    protected inline fun <reified T> findDataView(): T {
        val visitableList = slotVisitableList.captured

        return visitableList.find { it is T} as T
    }

    protected fun List<BaseItemInitialStateSearch>.findByType(type: String = ""): BaseItemInitialStateSearch {
        return this.find { it.type == type } as BaseItemInitialStateSearch
    }
}