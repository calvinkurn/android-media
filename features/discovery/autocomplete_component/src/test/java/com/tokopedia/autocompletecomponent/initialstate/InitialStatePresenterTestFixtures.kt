package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.complete
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import rx.Subscriber

internal open class InitialStatePresenterTestFixtures {
    protected val initialStateView = mockk<InitialStateContract.View>(relaxed = true)

    protected val getInitialStateUseCase = mockk<UseCase<InitialStateUniverse>>(relaxed = true)
    protected val deleteRecentSearchUseCase = mockk<UseCase<Boolean>>(relaxed = true)
    protected val refreshInitialStateUseCase = mockk<UseCase<List<InitialStateData>>>(relaxed = true)

    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val reimagine = mockk<ReimagineRollence>(relaxed = true)

    protected val slotVisitableList = slot<List<Visitable<*>>>()
    protected val slotDeletedVisitableList = mutableListOf<List<Visitable<*>>>()

    protected val slotRecentViewItemList = slot<MutableList<Any>>()
    protected val slotRecentSearchItemList = mutableListOf<MutableList<Any>>()
    protected val slotPopularSearchTrackingModel = slot<DynamicInitialStateItemTrackingModel>()
    protected val slotDynamicSectionTrackingModel = slot<DynamicInitialStateItemTrackingModel>()
    protected val slotCuratedCampaignType = slot<String>()
    protected val slotCuratedCampaignLabel = slot<String>()
    protected val slotCuratedCampaignCode = slot<String>()

    protected lateinit var initialStatePresenter: InitialStatePresenter

    protected val testException = TestException("Error")

    protected val initialStateCommonResponse = "autocomplete/initialstate/common-response.json"
    protected val initialStateCommonData = initialStateCommonResponse.jsonToObject<InitialStateUniverse>()

    protected val ID_POPULAR_SEARCH = "popular_search"
    protected val ID_NEW_SECTION = "new_section"
    protected val keyword = "samsung"

    @Before
    fun setUp() {
        initialStatePresenter = InitialStatePresenter(
                getInitialStateUseCase,
                deleteRecentSearchUseCase,
                refreshInitialStateUseCase,
                userSession,
                reimagine
        )
        initialStatePresenter.attachView(initialStateView)
    }

    protected fun `Given view already get initial state`(
        initialStateUniverse: InitialStateUniverse
    ) {
        `Given initial state use case will be successful`(initialStateUniverse)
        `Given presenter get initial state data`()
    }

    protected fun `Given initial state use case will be successful`(
        initialStateUniverse: InitialStateUniverse
    ) {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<InitialStateUniverse>>().complete(initialStateUniverse)
        }
    }

    private fun `Given presenter get initial state data`(
        searchParameter: Map<String, String> = mapOf()
    ) {
        initialStatePresenter.showInitialState(searchParameter)
    }

    protected fun `Given view already get initial state`(
        responseJSON: String,
        searchParameter: Map<String, String> = mapOf(),
    ) {
        val initialStateUniverse = responseJSON.jsonToObject<InitialStateUniverse>()

        `Given initial state use case will be successful`(initialStateUniverse)
        `Given view will show initial state result`()
        `Given presenter get initial state data`(searchParameter)
    }

    private fun `Given view will show initial state result`() {
        every { initialStateView.showInitialStateResult(capture(slotVisitableList)) } just runs
    }

    protected fun `Then verify initial state view behavior is correct`() {
        verifyOrder {
            initialStateView.chooseAddressData
            initialStateView.onRecentViewImpressed(any(), capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(any(), capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(any(), capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(any(), capture(slotDynamicSectionTrackingModel))
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
        confirmVerified(initialStateView)
    }

    protected fun `Then verify initial state view behavior for failed refresh`() {
        verifyOrder {
            initialStateView.chooseAddressData
            initialStateView.onRecentViewImpressed(any(), capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(any(), capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(any(), capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(any(), capture(slotDynamicSectionTrackingModel))
            initialStateView.showInitialStateResult(capture(slotVisitableList))
            initialStateView.onRefreshPopularSearch()
            initialStateView.chooseAddressData
        }
        confirmVerified(initialStateView)
    }

    protected fun `Then verify initial state view behavior for failed refresh dynamic`() {
        verifyOrder {
            initialStateView.chooseAddressData
            initialStateView.onRecentViewImpressed(any(), capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(any(), capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(any(), capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(any(), capture(slotDynamicSectionTrackingModel))
            initialStateView.showInitialStateResult(capture(slotVisitableList))
            initialStateView.chooseAddressData
        }
        confirmVerified(initialStateView)
    }

    protected fun `Then verify view interaction for load data failed with exception`() {
        verify {
            initialStateView.chooseAddressData
        }
        confirmVerified(initialStateView)
    }
    protected fun `Then verify view interaction for load data with empty item`() {
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
            initialStateView.onRecentViewImpressed(any(), capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(any(), capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(any(), capture(slotPopularSearchTrackingModel))

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

    protected fun `Given rollance is off`(){
        every { reimagine.search1InstAuto() } returns Search1InstAuto.CONTROL
    }

    protected fun `Given rollance is on`(){
        every { reimagine.search1InstAuto() } returns Search1InstAuto.VARIANT_1
    }
}
