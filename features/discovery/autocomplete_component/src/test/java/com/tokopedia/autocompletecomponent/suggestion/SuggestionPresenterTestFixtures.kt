package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.complete
import com.tokopedia.autocompletecomponent.initialstate.TestException
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipWidgetDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocompletecomponent.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import rx.Subscriber

internal open class SuggestionPresenterTestFixtures {
    protected val suggestionView = mockk<SuggestionContract.View>(relaxed = true)

    protected val getSuggestionUseCase = mockk<UseCase<SuggestionUniverse>>(relaxed = true)
    protected val suggestionTrackerUseCase = mockk<UseCase<Void?>>(relaxed = true)

    protected val userSession = mockk<UserSessionInterface>(relaxed = true)

    protected lateinit var suggestionPresenter: SuggestionPresenter

    protected val slotVisitableList = slot<List<Visitable<*>>>()
    protected val visitableList by lazy { slotVisitableList.captured }

    protected val testException = TestException("Error")

    @Before
    fun setUp() {
        suggestionPresenter = SuggestionPresenter(
            getSuggestionUseCase,
            suggestionTrackerUseCase,
            userSession,
        )

        suggestionPresenter.attachView(suggestionView)
    }

    protected fun SuggestionContract.View.onClickSuggestion(applink: String) {
        dropKeyBoard()
        route(applink, suggestionPresenter.getSearchParameter())
        finish()
    }

    protected fun `Given View already load data`(responseJSON: String, searchParameter: Map<String, String>) {
        val suggestionUniverse = responseJSON.jsonToObject<SuggestionUniverse>()

        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse)
        `Given view already get suggestion`(searchParameter)
    }

    private fun `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().complete(suggestionUniverse)
        }
    }

    private fun `Given view already get suggestion`(
        searchParameter: Map<String, String> = mapOf()
    ) {
        every { suggestionView.showSuggestionResult(capture(slotVisitableList)) } just runs

        suggestionPresenter.getSuggestion(searchParameter)
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
}