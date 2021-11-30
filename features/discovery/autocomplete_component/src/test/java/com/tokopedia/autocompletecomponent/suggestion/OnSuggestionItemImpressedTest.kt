package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"

internal class OnSuggestionItemImpressedTest: SuggestionPresenterTestFixtures() {

    private val keywordTypedByUser = "samsung"
    private val searchParameter : Map<String, String> = mutableMapOf(
        SearchApiConst.Q to keywordTypedByUser
    )

    @Test
    fun `test impress suggestion item type light`() {
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val item = visitableList.findDoubleLine(TYPE_LIGHT)

        `when suggestion item impressed` (item.data)
        `then verify view interaction for impression type light is correct`(item.data)
    }

    private fun `when suggestion item impressed`(item: BaseSuggestionDataView) {
        suggestionPresenter.onSuggestionItemImpressed(item)
    }

    private fun `then verify view interaction for impression type light is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
            "keyword: $keywordTypedByUser " +
                    "- product: ${item.subtitle} " +
                    "- po: ${item.position} " +
                    "- page: ${item.applink}"

        verifyOrder {
            suggestionView.trackEventImpressCurated(
                expectedEventLabel,
                item.trackingCode,
                item.dimension90,
                item,
            )
        }
    }

    @Test
    fun `test impress suggestion item type curated`() {
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val item = visitableList.findSingleLine(TYPE_CURATED)

        `when suggestion item impressed` (item.data)
        `then verify view interaction for impression type curated is correct`(item.data)
    }

    private fun `then verify view interaction for impression type curated is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
            "keyword: $keywordTypedByUser " +
                    "- product: ${item.title} " +
                    "- po: ${item.position} " +
                    "- page: ${item.applink}"

        verifyOrder {
            suggestionView.trackEventImpressCurated(
                expectedEventLabel,
                item.trackingCode,
                item.dimension90,
                item,
            )
        }
    }

    @Test
    fun `test impress suggestion item not light and not curated`() {
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val item = visitableList.findSingleLine(TYPE_RECENT_KEYWORD)

        `when suggestion item impressed` (item.data)

        `Then verify impression tracking is called`(item)
    }

    private fun `Then verify impression tracking is called`(item: SuggestionSingleLineDataDataView) {
        verify {
            suggestionView.trackEventImpression(item.data)
        }
    }
}