package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineWithoutImageDataDataView
import com.tokopedia.autocompletecomponent.suggestion.productline.SuggestionProductLineDataDataView
import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.verify
import org.junit.Test

private const val suggestionCampaignResponse = "autocomplete/suggestion/local-global-response.json"

internal class OnSuggestionLocalItemClickTest: SuggestionPresenterTestFixtures() {

    private val campaign = "Waktu%20Indonesia%20Belanja"
    private val keywordLocalGlobalTypedByUser = "asus"
    private val searchParameter : Map<String, String> = mutableMapOf<String, String>().also {
        it[SearchApiConst.Q] = keywordLocalGlobalTypedByUser
        it[SearchApiConst.SRP_PAGE_TITLE] = campaign
    }

    @Test
    fun `Test tracking click suggestion item local keyword`() {
        `Given View already load data`(suggestionCampaignResponse, searchParameter)

        val item = visitableList.findDoubleLineWithoutImage(TYPE_LOCAL)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item local keyword is correct`(item.data)
    }

    private fun `when suggestion item clicked`(item: BaseSuggestionDataView) {
        suggestionPresenter.onSuggestionItemClicked(item)
    }

    private fun `then verify view tracking click item local keyword is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: ${item.title} " +
                        "- value: $keywordLocalGlobalTypedByUser " +
                        "- applink: ${item.applink} " +
                        "- campaign: $campaign"
        val userId = "0"

        verify {
            suggestionView.trackEventClickLocalKeyword(
                expectedEventLabel,
                userId,
                item.dimension90,
                item,
            )
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `Test tracking click suggestion item global keyword`() {
        `Given View already load data`(suggestionCampaignResponse, searchParameter)

        val item = visitableList.findDoubleLineWithoutImage(TYPE_GLOBAL)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item global keyword is correct`(item.data)
    }

    private fun `then verify view tracking click item global keyword is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: ${item.title} " +
                        "- value: $keywordLocalGlobalTypedByUser " +
                        "- applink: ${item.applink}"
        val userId = "0"

        verify {
            suggestionView.trackEventClickGlobalKeyword(
                expectedEventLabel,
                userId,
                item.dimension90,
                item,
            )
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `Test tracking click suggestion item product line`() {
        `Given View already load data`(suggestionCampaignResponse, searchParameter)

        val item = visitableList.findProductLine(TYPE_PRODUCT)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item product line is correct`(item.data)
    }

    private fun `then verify view tracking click item product line is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
            "keyword: ${item.title} " +
                    "- value: $keywordLocalGlobalTypedByUser " +
                    "- applink: ${item.applink}"
        val userId = "0"

        verify {
            suggestionView.trackEventClickProductLine(item, expectedEventLabel, userId)
            suggestionView.onClickSuggestion(item.applink)
        }
    }
}