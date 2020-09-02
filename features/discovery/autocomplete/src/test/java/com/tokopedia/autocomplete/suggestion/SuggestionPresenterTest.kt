package com.tokopedia.autocomplete.suggestion

import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewModel
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewModel
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleViewModel
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"
private const val suggestionTopShopResponse = "autocomplete/suggestion/suggestion-top-shop-response.json"

internal class SuggestionPresenterTest: SuggestionPresenterTestFixtures() {

    @Test
    fun `test get suggestion data`() {
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showInitialStateResult behavior`()
        `then verify visitable list`()
    }

    private fun `given suggestion use case capture request params`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onNext(suggestionUniverse)
        }
    }

    private fun `when presenter get suggestion data (search)`() {
        suggestionPresenter.search()
    }

    private fun `then verify suggestion API is called`() {
        verify { getSuggestionUseCase.execute(any(), any()) }
    }

    private fun `then verify suggestion view will call showInitialStateResult behavior`() {
        verify {
            suggestionView.showSuggestionResult(capture(slotVisitableList))
        }
    }

    private fun `then verify visitable list`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is SuggestionSingleLineViewModel)
        Assert.assertTrue(visitableList[1] is SuggestionSingleLineViewModel)
        Assert.assertTrue(visitableList[2] is SuggestionTitleViewModel)
        Assert.assertTrue(visitableList[3] is SuggestionDoubleLineViewModel)
        Assert.assertTrue(visitableList[4] is SuggestionDoubleLineViewModel)
        Assert.assertTrue(visitableList[5] is SuggestionTitleViewModel)
        Assert.assertTrue(visitableList[6] is SuggestionDoubleLineViewModel)
        Assert.assertTrue(visitableList[7] is SuggestionDoubleLineViewModel)
        Assert.assertTrue(visitableList.size == 8)
    }

    @Test
    fun `test fail to get initial state data`() {
        `given suggestion API will return error`()
        `when presenter get suggestion data (search)`()
        `then verify suggestion API is called`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given suggestion API will return error`() {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onError(testException)
        }
    }

    private fun `then verify initial state view do nothing behavior`() {
        confirmVerified(suggestionView)
    }

    @Test
    fun `test get suggestion data with top shop`() {
        val suggestionUniverse = suggestionTopShopResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params with top shop`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showInitialStateResult behavior`()
        `then verify visitable list with top shop`()
    }

    private fun `given suggestion use case capture request params with top shop`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onNext(suggestionUniverse)
        }
    }


    private fun `then verify visitable list with top shop`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is SuggestionSingleLineViewModel)
        Assert.assertTrue(visitableList[1] is SuggestionSingleLineViewModel)
        Assert.assertTrue(visitableList[2] is SuggestionSingleLineViewModel)
        Assert.assertTrue(visitableList[3] is SuggestionSingleLineViewModel)
        Assert.assertTrue(visitableList[4] is SuggestionSingleLineViewModel)
        Assert.assertTrue(visitableList[5] is SuggestionTitleViewModel)
        Assert.assertTrue(visitableList[6] is SuggestionDoubleLineViewModel)
        Assert.assertTrue(visitableList[7] is SuggestionDoubleLineViewModel)
        Assert.assertTrue(visitableList[8] is SuggestionDoubleLineViewModel)
        Assert.assertTrue(visitableList[9] is SuggestionTopShopWidgetViewModel)
        Assert.assertTrue(visitableList.size == 10)
    }
}