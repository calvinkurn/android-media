package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"
internal class OnSuggestionItemClickMps : SuggestionPresenterTestFixtures()  {

    private val capturedUrlTrackerParams = slot<RequestParams>()
    private val searchParameter : Map<String, String> = mutableMapOf<String, String>().also {
        it[SearchApiConst.ACTIVE_TAB] = SearchApiConst.ACTIVE_TAB_MPS
        it[SearchApiConst.Q1] = "apple"
        it[SearchApiConst.Q2] = "samsung"
    }

    @Test
    fun `mps suggestion test`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)
        val item = visitableList.findDoubleLine(TYPE_SHOP)
        `when suggestion item clicked` (item.data)
        `Then verify is listener of MPS is called`()
    }

    private fun `Then verify is listener of MPS is called`() {
        verify { suggestionView.addToMPSKeyword(any()) }
    }

    private fun `given suggestion tracker use case capture request params`() {
        every { suggestionTrackerUseCase.execute(capture(capturedUrlTrackerParams), any()) } just runs
    }

    private fun `when suggestion item clicked`(item: BaseSuggestionDataView) {
        suggestionPresenter.onSuggestionItemClicked(item)
    }

}
