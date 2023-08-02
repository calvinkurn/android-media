package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.TestSchedulersProvider
import com.tokopedia.autocompletecomponent.initialstate.TestException
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipWidgetDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocompletecomponent.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import org.junit.Before
import com.tokopedia.usecase.UseCase as RxUseCase

internal open class SuggestionPresenterTestFixtures {
    protected val suggestionView = mockk<SuggestionContract.View>(relaxed = true)

    protected val getSuggestionUseCase = mockk<UseCase<SuggestionUniverse>>(relaxed = true)
    protected val suggestionTrackerUseCase = mockk<RxUseCase<Void?>>(relaxed = true)
    protected val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)

    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val coachMarkLocalCache = mockk<CoachMarkLocalCache>(relaxed = true)

    protected lateinit var suggestionPresenter: SuggestionPresenter

    protected val slotVisitableList = slot<List<Visitable<*>>>()
    protected val visitableList by lazy { slotVisitableList.captured }

    protected val testException = TestException("Error")

    @Before
    open fun setUp() {
        suggestionPresenter = SuggestionPresenter(
            getSuggestionUseCase,
            suggestionTrackerUseCase,
            { topAdsUrlHitter },
            userSession,
            coachMarkLocalCache,
            TestSchedulersProvider,
        )

        suggestionPresenter.attachView(suggestionView)
    }

    protected fun SuggestionContract.View.onClickSuggestion(applink: String) {
        dropKeyBoard()
        route(applink, suggestionPresenter.getSearchParameter(), suggestionPresenter.getActiveKeyword())
        finish()
    }

    protected fun `Given View already load data`(
        responseJSON: String,
        searchParameter: Map<String, String>,
        activeKeyword: SearchBarKeyword = SearchBarKeyword(),
    ) {
        val suggestionUniverse = responseJSON.jsonToObject<SuggestionUniverse>()

        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse)
        `Given view already get suggestion`(searchParameter, activeKeyword)
    }

    protected fun `Given Suggestion API will return SuggestionUniverse`(
        suggestionUniverse: SuggestionUniverse,
        requestParamSlot: CapturingSlot<RequestParams> = slot(),
    ) {
        every {
            getSuggestionUseCase.execute(any(), any(), capture(requestParamSlot))
        } answers {
            firstArg<(SuggestionUniverse) -> Unit>().invoke(suggestionUniverse)
        }
    }

    private fun `Given view already get suggestion`(
        searchParameter: Map<String, String> = mapOf(),
        activeKeyword: SearchBarKeyword = SearchBarKeyword(),
    ) {
        every { suggestionView.showSuggestionResult(capture(slotVisitableList)) } just runs

        suggestionPresenter.getSuggestion(searchParameter, activeKeyword)
    }

    protected fun List<Visitable<*>>.findSingleLine(
        type: String = "",
    ): SuggestionSingleLineDataDataView =
        this.filterIsInstance<SuggestionSingleLineDataDataView>()
            .find { it.data.type == type }!!

    protected fun List<Visitable<*>>.findDoubleLine(
        type: String = "",
    ): SuggestionDoubleLineDataDataView =
        this.filterIsInstance<SuggestionDoubleLineDataDataView>()
            .find { it.data.type == type }!!

    protected fun List<Visitable<*>>.findDoubleLineWithoutImage(
        type: String = "",
    ): SuggestionDoubleLineWithoutImageDataDataView =
        this.filterIsInstance<SuggestionDoubleLineWithoutImageDataDataView>()
            .find { it.data.type == type }!!

    protected fun List<Visitable<*>>.findProductLine(
        type: String = "",
    ): SuggestionProductLineDataDataView =
        this.filterIsInstance<SuggestionProductLineDataDataView>()
            .find { it.data.type == type }!!

    protected fun List<Visitable<*>>.findChip(): SuggestionChipWidgetDataView =
        this.filterIsInstance<SuggestionChipWidgetDataView>().first()

    protected fun List<Visitable<*>>.findShopAds(): SuggestionDoubleLineDataDataView =
        this.filterIsInstance<SuggestionDoubleLineDataDataView>()
            .find { it.data.shopAdsDataView != null }!!
}
