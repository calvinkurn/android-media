package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionTrackerUseCase
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.autocompletecomponent.util.getShopIdFromApplink
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.RequestParams
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"
private const val suggestionShopAdsResponse = "autocomplete/suggestion/shopads/suggestion-shop-ads-response.json"

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

        val item = visitableList.findDoubleLine(TYPE_SHOP)

        `when suggestion item clicked` (item.data)
        `then verify view interaction is correct`(item.data)
        `then verify url tracker is hit`(item.data)
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

        val item = visitableList.findSingleLine(TYPE_KEYWORD)

        `when suggestion item clicked` (item.data)
        `then verify view interaction is correct`(item.data)
        `then verify url tracker is not hit`()
    }

    private fun `then verify url tracker is not hit`() {
        confirmVerified(suggestionTrackerUseCase)
    }

    @Test
    fun `test tracking click suggestion item keyword`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = visitableList.findSingleLine(TYPE_KEYWORD)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item keyword is correct`(item.data)
    }

    private fun `then verify view tracking click item keyword is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: ${item.title} " + // in item type keyword, event label `keyword` is the item title
                        "- value: $keywordTypedByUser " + // and event label `value` is the keyword typed by user
                        "- po: ${item.position} " +
                        "- applink: ${item.applink}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickKeyword(expectedEventLabel, item.dimension90, item)
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `test click suggestion item keyword on MPS selected keyword`() {
        `given suggestion tracker use case capture request params`()
        val activeKeyword = SearchBarKeyword(keyword = keywordTypedByUser, isSelected = true)
        `Given View already load data`(
            suggestionCommonResponse,
            searchParameter as HashMap<String, String>,
            activeKeyword,
        )

        val item = visitableList.findSingleLine(TYPE_KEYWORD)

        `when suggestion item clicked`(item.data)

        `then verify view apply suggestion to selected keyword`(item.data.title, activeKeyword)
    }

    private fun `then verify view apply suggestion to selected keyword`(
        expectedSuggestedText: String,
        expectedActiveKeyword: SearchBarKeyword,
    ) {
        verify {
            suggestionView.applySuggestionToSelectedKeyword(expectedSuggestedText, expectedActiveKeyword)
        }
    }

    @Test
    fun `test tracking click suggestion item curated`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = visitableList.findSingleLine(TYPE_CURATED)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item curated is correct`(item.data)
    }

    private fun `then verify view tracking click item curated is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: $keywordTypedByUser " +
                        "- product: ${item.title} " +
                        "- po: ${item.position} " +
                        "- page: ${item.applink}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickCurated(
                expectedEventLabel,
                item.trackingCode,
                item.dimension90,
                item,
            )
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `test tracking click suggestion item shop`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter as HashMap<String, String>)

        val item = visitableList.findDoubleLine(TYPE_SHOP)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item shop is correct`(item.data)
    }

    private fun `then verify view tracking click item shop is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                getShopIdFromApplink(item.applink) +
                        " - keyword: $keywordTypedByUser" +
                        " - shop: ${item.title}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickShop(expectedEventLabel, item.dimension90, item)
            suggestionView.onClickSuggestion(item.applink)
        }
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
            suggestionView.trackEventClickProfile(expectedEventLabel, item)
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

        val item = visitableList.findSingleLine(TYPE_RECENT_KEYWORD)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item recent keyword is correct`(item.data)
    }

    private fun `then verify view tracking click item recent keyword is correct`(item: BaseSuggestionDataView) {
        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickRecentKeyword(item.title, item.dimension90, item)
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `test tracking click suggestion item tokonow keyword`() {
        val searchParameter = mutableMapOf(
            SearchApiConst.Q to keywordTypedByUser,
            SearchApiConst.NAVSOURCE to "tokonow"
        )
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val item = visitableList.findSingleLine(TYPE_KEYWORD)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item tokonow keyword is correct`(item.data)
    }

    private fun `then verify view tracking click item tokonow keyword is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: ${item.title} " + // in item type keyword, event label `keyword` is the item title
                        "- value: $keywordTypedByUser " + // and event label `value` is the keyword typed by user
                        "- po: ${item.position} " +
                        "- page: ${item.applink}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackTokoNowEventClickKeyword(expectedEventLabel, item)
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `test tracking click suggestion item tokonow curated`() {
        val searchParameter = mutableMapOf(
            SearchApiConst.Q to keywordTypedByUser,
            SearchApiConst.NAVSOURCE to "tokonow"
        )

        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val item = visitableList.findSingleLine(TYPE_CURATED)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item tokonow curated is correct`(item.data)
    }

    private fun `then verify view tracking click item tokonow curated is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
                "keyword: $keywordTypedByUser " +
                        "- product: ${item.title} " +
                        "- po: ${item.position} " +
                        "- page: ${item.applink}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackTokoNowEventClickCurated(expectedEventLabel, item)
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `test tracking click suggestion item tokonow not keyword or curated`() {
        val searchParameter = mutableMapOf(
            SearchApiConst.Q to keywordTypedByUser,
            SearchApiConst.NAVSOURCE to "tokonow"
        )

        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val item = visitableList.findSingleLine(TYPE_RECENT_KEYWORD)

        `when suggestion item clicked`(item.data)

        `Then verify track click event tokonow`(item.data)
    }

    private fun `Then verify track click event tokonow`(
        item: BaseSuggestionDataView,
    ) {
        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClick(item)
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `test tracking click suggestion chip widget`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val baseSuggestionDataView = visitableList.findChip().data
        val item = baseSuggestionDataView.childItems[0]

        `when suggestion chip clicked`(baseSuggestionDataView, item)
        `then verify view tracking click chip widget`(baseSuggestionDataView, item)
    }

    private fun `when suggestion chip clicked`(
        baseSuggestionDataView: BaseSuggestionDataView,
        item: BaseSuggestionDataView.ChildItem,
    ) {
        suggestionPresenter.onSuggestionChipClicked(baseSuggestionDataView, item)
    }

    private fun `then verify view tracking click chip widget`(
        baseSuggestionDataView: BaseSuggestionDataView,
        childItem: BaseSuggestionDataView.ChildItem,
    ) {
        val expectedEventLabel =
                "keyword: ${childItem.title} " +
                        "- value: $keywordTypedByUser " +
                        "- po: ${childItem.position} " +
                        "- page: ${childItem.applink}"

        verify {
            suggestionView.trackClickChip(
                expectedEventLabel,
                childItem.dimension90,
                childItem,
            )
            suggestionView.dropKeyBoard()
            suggestionView.route(
                childItem.applink,
                suggestionPresenter.getSearchParameter(),
                suggestionPresenter.getActiveKeyword(),
            )
            suggestionView.finish()
        }
    }

    @Test
    fun `test tracking click suggestion item light`() {
        `given suggestion tracker use case capture request params`()
        `Given View already load data`(suggestionCommonResponse, searchParameter)

        val item = visitableList.findDoubleLine(TYPE_LIGHT)

        `when suggestion item clicked`(item.data)
        `then verify view tracking click item light is correct`(item.data)
    }

    private fun `then verify view tracking click item light is correct`(item: BaseSuggestionDataView) {
        val expectedEventLabel =
            "keyword: $keywordTypedByUser " +
                    "- product: ${item.subtitle} " +
                    "- po: ${item.position} " +
                    "- page: ${item.applink}"

        verify {
            suggestionView.showSuggestionResult(any())
            suggestionView.trackEventClickCurated(
                expectedEventLabel,
                item.trackingCode,
                item.dimension90,
                item,
            )
            suggestionView.onClickSuggestion(item.applink)
        }
    }

    @Test
    fun `tracking click shop ads`() {
        `Given View already load data`(suggestionShopAdsResponse, searchParameter)

        val item = visitableList.findShopAds()

        `when suggestion item clicked`(item.data)

        `Then verify shop ads click`(item)
    }

    private fun `Then verify shop ads click`(item: SuggestionDoubleLineDataDataView) {
        val shopAdsDataView = item.data.shopAdsDataView!!

        verify {
            topAdsUrlHitter.hitClickUrl(
                suggestionView.className,
                shopAdsDataView.clickUrl,
                any(),
                any(),
                shopAdsDataView.imageUrl,
            )
        }
    }
}
