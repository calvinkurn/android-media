package com.tokopedia.autocomplete.suggestion

import com.tokopedia.autocomplete.suggestion.data.SuggestionUniverse
import com.tokopedia.autocomplete.suggestion.testinstance.suggestionTopShopResponse
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardViewModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import org.junit.Test
import rx.Subscriber

internal class OnSuggestionItemClickTest: SuggestionPresenterTestFixtures() {

    private val id = "12345"
    private val applinkShopWithQueryParams = "tokopedia://shop/$id?source=universe&st=product"
    private val applinkShopWithoutQueryParams = "tokopedia://shop/$id"
    private val applinkProfileWithQueryParams = "tokopedia://people/$id?source=universe&st=product"
    private val applinkProfileWithoutQueryParams = "tokopedia://people/$id"
    private val applinkRecentSearch = "tokopedia://search?q=jaket+pria&source=universe&st=product"
    private val urlTracker = "https://ace.tokopedia.com/tracker/v1?id=558518&user_id=0&device=desktop&unique_id=&type=shop"
    private val expectedUrlTracker = "https://ace.tokopedia.com/tracker/v1?id=558518&user_id=0&device=desktop&unique_id=&type=shop&device=android&source=searchbar&count=5&user_id=&unique_id=d41d8cd98f00b204e9800998ecf8427e&device_id="
    private val capturedUrlTrackerParams = slot<RequestParams>()
    private val keywordTypedByUser = "samsung"
    private val position = 1
    private val title = "Samsung"
    private val campaignCode = "123"
    private val keywordTopShop = "ps4"

    @Test
    fun `test click suggestion item`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithQueryParams,
                urlTracker = urlTracker
        )

        `given suggestion tracker use case capture request params`()
        `when suggestion item clicked` (item)
        `then verify view interaction is correct`(item)
        `then verify url tracker is hit`()
    }

    private fun `given suggestion tracker use case capture request params`() {
        every { suggestionTrackerUseCase.execute(capture(capturedUrlTrackerParams), any()) } just runs
    }

    private fun `when suggestion item clicked`(item: BaseSuggestionViewModel) {
        suggestionPresenter.onSuggestionItemClicked(item)
    }

    private fun `then verify view interaction is correct`(item: BaseSuggestionViewModel) {
        verifyOrder {
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    private fun SuggestionContract.View.onClickSuggestion(applink: String) {
        dropKeyBoard()
        route(applink, suggestionPresenter.getSearchParameter())
        finish()
    }

    private fun `then verify url tracker is hit`() {
        val requestParams = capturedUrlTrackerParams.captured

        val actualUrlTracker = requestParams.getString(SuggestionTrackerUseCase.URL_TRACKER, "")

        assert(actualUrlTracker == expectedUrlTracker) {
            "Assertion Failed, actual url tracker: $actualUrlTracker, expected url tracker: $expectedUrlTracker"
        }
    }

    @Test
    fun `test click suggestion item without tracker url`() {
        val item = BaseSuggestionViewModel(applink = applinkShopWithQueryParams)

        `when suggestion item clicked` (item)
        `then verify view interaction is correct`(item)
        `then verify url tracker is not hit`()
    }

    private fun `then verify url tracker is not hit`() {
        confirmVerified(suggestionTrackerUseCase)
    }

    @Test
    fun `test tracking click suggestion item keyword`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithQueryParams,
                type = TYPE_KEYWORD,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item keyword is correct`(item)
    }

    private fun `then verify view tracking click item keyword is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel =
                "keyword: ${item.title} " + // in item type keyword, event label `keyword` is the item title
                "- value: $keywordTypedByUser " + // and event label `value` is the keyword typed by user
                "- po: ${item.position} " +
                "- applink: ${item.applink}"

        verify {
            suggestionView.trackEventClickKeyword(expectedEventLabel)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item curated`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithQueryParams,
                type = TYPE_CURATED,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position,
                trackingCode = campaignCode
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item curated is correct`(item)
    }

    private fun `then verify view tracking click item curated is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel =
                "keyword: $keywordTypedByUser " +
                "- product: ${item.title} " +
                "- po: ${item.position} " +
                "- page: ${item.applink}"

        verify {
            suggestionView.trackEventClickCurated(expectedEventLabel, campaignCode)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item shop using applink with query params`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithQueryParams,
                type = TYPE_SHOP,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item shop is correct`(item)
    }

    private fun `then verify view tracking click item shop is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel =
                "$id " +
                "- keyword: $keywordTypedByUser " +
                "- shop: ${item.title}"

        verify {
            suggestionView.trackEventClickShop(expectedEventLabel)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item shop using applink without query params`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithoutQueryParams,
                type = TYPE_SHOP,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item shop is correct`(item)
    }

    @Test
    fun `test tracking click suggestion item profile using applink with query params`() {
        val item = BaseSuggestionViewModel(
                applink = applinkProfileWithQueryParams,
                type = TYPE_PROFILE,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item profile is correct`(item)
    }

    private fun `then verify view tracking click item profile is correct`(item: BaseSuggestionViewModel) {
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
        val item = BaseSuggestionViewModel(
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
        val item = BaseSuggestionViewModel(
                applink = applinkRecentSearch,
                type = TYPE_RECENT_KEYWORD,
                searchTerm = keywordTypedByUser,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item recent keyword is correct`(item)
    }

    private fun `then verify view tracking click item recent keyword is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel = item.title

        verify {
            suggestionView.trackEventClickRecentKeyword(expectedEventLabel)
            suggestionView.onClickSuggestion(item.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test click top shop card`() {
        val searchParameter : Map<String, String> = mutableMapOf<String, String>().also {
            it[SearchApiConst.Q] = "ps4"
        }

        val card = SuggestionTopShopCardViewModel(
                type = "top_shop",
                id = "428718",
                applink = "tokopedia://shop/428718/etalase/all?keyword=ps4",
                urlTracker = "https://ace-staging.tokopedia.com/tracker/v1?id=428718&user_id=0&device=&unique_id=d41d8cd98f00b204e9800998ecf8427e&type=shop"
        )

        `given search param keyword is ps4`(searchParameter as HashMap<String, String>)
        `when suggestion top shop card clicked` (card)
        `then verify view tracking click top shop card is correct`(card)
    }

    private fun `given search param keyword is ps4`(searchParameter: HashMap<String, String>) {
        suggestionPresenter.setSearchParameter(searchParameter)
    }

    private fun `when suggestion top shop card clicked`(card: SuggestionTopShopCardViewModel) {
        suggestionPresenter.onTopShopCardClicked(card)
    }

    private fun `then verify view tracking click top shop card is correct`(card: SuggestionTopShopCardViewModel) {
        val expectedEventLabel = "${card.id} - keyword: $keywordTopShop"

        verify {
            suggestionView.trackEventClickTopShopCard(expectedEventLabel)
            suggestionView.onClickSuggestion(card.applink)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test click top shop see more`() {
        val searchParameter : Map<String, String> = mutableMapOf<String, String>().also {
            it[SearchApiConst.Q] = "ps4"
        }

        val card = SuggestionTopShopCardViewModel(
                type = "top_shop_all",
                applink = "tokopedia://search?q=ps4&st=shop"
        )

        `given search param keyword is ps4`(searchParameter as HashMap<String, String>)
        `when suggestion top shop card clicked` (card)
        `then verify url tracker is not hit`()
        `then verify view tracking click top shop see more is correct`(card)
    }

    private fun `then verify view tracking click top shop see more is correct`(card: SuggestionTopShopCardViewModel) {
        val expectedEventLabel = "keyword: $keywordTopShop"

        verify {
            suggestionView.trackEventClickTopShopSeeMore(expectedEventLabel)
            suggestionView.onClickSuggestion(card.applink)
        }

        confirmVerified(suggestionView)
    }
}