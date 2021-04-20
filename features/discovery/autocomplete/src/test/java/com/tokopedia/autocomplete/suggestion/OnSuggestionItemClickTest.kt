package com.tokopedia.autocomplete.suggestion

import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionTrackerUseCase
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineDataDataView
import com.tokopedia.autocomplete.util.getShopIdFromApplink
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"

internal class OnSuggestionItemClickTest: SuggestionPresenterTestFixtures() {

    private val id = "12345"
    private val applinkProfileWithQueryParams = "tokopedia://people/$id?source=universe&st=product"
    private val applinkProfileWithoutQueryParams = "tokopedia://people/$id"
    private val capturedUrlTrackerParams = slot<RequestParams>()
    private val keywordTypedByUser = "samsung"
    private val position = 1
    private val title = "Samsung"
    private val searchParameter : Map<String, String> = mutableMapOf<String, String>().also {
        it[SearchApiConst.Q] = keywordTypedByUser
    }

    @Test
    fun `test click suggestion item`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionDoubleLineDataDataView>(TYPE_SHOP)

        `when suggestion item clicked` (item)
        `then verify view interaction is correct`(item)
        `then verify url tracker is hit`(item)
    }

    private fun `given suggestion tracker use case capture request params`() {
        every { suggestionTrackerUseCase.execute(capture(capturedUrlTrackerParams), any()) } just runs
    }

    private fun `when suggestion item clicked`(item: BaseSuggestionDataView) {
        suggestionPresenter.onSuggestionItemClicked(item)
    }

    private fun `then verify view interaction is correct`(item: BaseSuggestionDataView) {
        verifyOrder {
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    private fun `then verify url tracker is hit`(item: BaseSuggestionDataView) {
        val requestParams = capturedUrlTrackerParams.captured

        val actualUrlTracker = requestParams.getString(SuggestionTrackerUseCase.URL_TRACKER, "")

        assert(actualUrlTracker.contains(item.urlTracker)) {
            "Assertion Failed, actual url tracker: $actualUrlTracker, expected url tracker: ${item.urlTracker}"
        }
    }

    @Test
    fun `test click suggestion item without tracker url`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionSingleLineDataDataView>(TYPE_KEYWORD)

        `when suggestion item clicked` (item)
        `then verify view interaction is correct`(item)
        `then verify url tracker is not hit`()
    }

    private fun `then verify url tracker is not hit`() {
        confirmVerified(suggestionTrackerUseCase)
    }

    @Test
    fun `test tracking click suggestion item keyword`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionSingleLineDataDataView>(TYPE_KEYWORD)

        `when suggestion item clicked`(item)
        `then verify view tracking click item keyword is correct`(item)
    }

    private fun `then verify view tracking click item keyword is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: ${item.title} " + // in item type keyword, event label `keyword` is the item title
                        "- value: $keywordTypedByUser " + // and event label `value` is the keyword typed by user
                        "- po: ${item.position} " +
                        "- applink: ${item.applink}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickKeyword(expectedEventLabel)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item curated`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionSingleLineDataDataView>(TYPE_CURATED)

        `when suggestion item clicked`(item)
        `then verify view tracking click item curated is correct`(item)
    }

    private fun `then verify view tracking click item curated is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: $keywordTypedByUser " +
                        "- product: ${item.title} " +
                        "- po: ${item.position} " +
                        "- page: ${item.applink}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickCurated(expectedEventLabel, item.trackingCode)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item shop`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionDoubleLineDataDataView>(TYPE_SHOP)

        `when suggestion item clicked`(item)
        `then verify view tracking click item shop is correct`(item)
    }

    private fun `then verify view tracking click item shop is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                getShopIdFromApplink(item.applink) +
                        " - keyword: $keywordTypedByUser" +
                        " - shop: ${item.title}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickShop(expectedEventLabel)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item profile using applink with query params`() {
        val item = BaseSuggestionDataView(
                applink = applinkProfileWithQueryParams,
                type = TYPE_PROFILE,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item profile is correct`(item)
    }

    private fun `then verify view tracking click item profile is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: $keywordTypedByUser " +
                        "- profile: ${item.title} " +
                        "- profile id: $id " +
                        "- po: ${item.position}"

        verify {
            suggestionView.trackEventClickProfile(expectedEventLabel)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item profile using applink without query params`() {
        val item = BaseSuggestionDataView(
                applink = applinkProfileWithoutQueryParams,
                type = TYPE_PROFILE,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item profile is correct`(item)
    }

    @Test
    fun `test tracking click suggestion item recent keyword`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = findDataView<SuggestionSingleLineDataDataView>(TYPE_RECENT_KEYWORD)

        `when suggestion item clicked`(item)
        `then verify view tracking click item recent keyword is correct`(item)
    }

    private fun `then verify view tracking click item recent keyword is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel = item.title

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickRecentKeyword(expectedEventLabel)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }
}