package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.TestException
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before

internal open class SuggestionPresenterTestFixtures {
    protected val suggestionView = mockk<SuggestionContract.View>(relaxed = true)

    protected val getSuggestionUseCase = mockk<UseCase<SuggestionUniverse>>(relaxed = true)
    protected val suggestionTrackerUseCase = mockk<UseCase<Void?>>(relaxed = true)

    protected val userSession = mockk<UserSessionInterface>(relaxed = true)

    protected lateinit var suggestionPresenter: SuggestionPresenter

    protected val slotVisitableList = slot<List<Visitable<*>>>()

    protected val testException = TestException("Error")

    @Before
    fun setUp() {
        suggestionPresenter = SuggestionPresenter()

        suggestionPresenter.attachView(suggestionView)

        suggestionPresenter.getSuggestionUseCase = getSuggestionUseCase
        suggestionPresenter.suggestionTrackerUseCase = suggestionTrackerUseCase
        suggestionPresenter.userSession = userSession
    }

    fun SuggestionContract.View.onClickSuggestion(applink: String) {
        dropKeyBoard()
        route(applink, suggestionPresenter.getSearchParameter())
        finish()
    }
}