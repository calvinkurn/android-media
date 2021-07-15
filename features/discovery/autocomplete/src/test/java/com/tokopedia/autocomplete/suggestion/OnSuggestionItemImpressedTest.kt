package com.tokopedia.autocomplete.suggestion

import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.verifyOrder
import org.junit.Test

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"

internal class OnSuggestionItemImpressedTest: SuggestionPresenterTestFixtures() {

    private val keywordTypedByUser = "samsung"
    private val searchParameter : Map<String, String> = mutableMapOf<String, String>().also {
        it[SearchApiConst.Q] = keywordTypedByUser
    }

    @Test
    fun `test click suggestion item type light`() {
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionDoubleLineDataDataView>(TYPE_LIGHT)

        `when suggestion item impressed` (item)
        `then verify view interaction is correct`(item)
    }

    private fun `when suggestion item impressed`(item: BaseSuggestionDataView) {
        suggestionPresenter.onSuggestionItemImpressed(item)
    }

    private fun `then verify view interaction is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
            "keyword: $keywordTypedByUser " +
                    "- product: ${item.title} " +
                    "- po: ${item.position} " +
                    "- page: ${item.applink}"

        verifyOrder {
            suggestionView.trackEventImpressCurated(expectedEventLabel, item.trackingCode, item.dimension90)
        }
    }

    @Test
    fun `test click suggestion item type curated`() {
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionSingleLineDataDataView>(TYPE_CURATED)

        `when suggestion item impressed` (item)
        `then verify view interaction is correct`(item)
    }
}