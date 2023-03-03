package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test

internal class OnSuggestionTopShopClickTest: SuggestionPresenterTestFixtures() {

    private val keywordTopShop = "ps4"

    @Test
    fun `test click top shop card`() {
        val searchParameter = mutableMapOf(SearchApiConst.Q to keywordTopShop)
        val activeKeyword = SearchBarKeyword(keyword = keywordTopShop)

        val card = SuggestionTopShopCardDataView(
                type = "top_shop",
                id = "428718",
                applink = "tokopedia://shop/428718/etalase/all?keyword=ps4",
                urlTracker = "https://ace-staging.tokopedia.com/tracker/v1?id=428718&user_id=0&device=&unique_id=d41d8cd98f00b204e9800998ecf8427e&type=shop"
        )

        `given view already get suggestion`(searchParameter, activeKeyword)
        `when suggestion top shop card clicked` (card)
        `then verify view tracking click top shop card is correct`(card)
    }

    private fun `given view already get suggestion`(
        searchParameter: Map<String, String>,
        activeKeyword: SearchBarKeyword,
    ) {
        suggestionPresenter.getSuggestion(searchParameter, activeKeyword)
    }

    private fun `when suggestion top shop card clicked`(cardData: SuggestionTopShopCardDataView) {
        suggestionPresenter.onTopShopCardClicked(cardData)
    }

    private fun `then verify view tracking click top shop card is correct`(cardData: SuggestionTopShopCardDataView) {
        val expectedEventLabel = "${cardData.id} - keyword: $keywordTopShop"

        verify {
            suggestionView.trackEventClickTopShopCard(expectedEventLabel)
            suggestionView.onClickSuggestion(cardData.applink)
        }
    }

    @Test
    fun `test click top shop see more`() {
        val searchParameter = mutableMapOf(SearchApiConst.Q to keywordTopShop)
        val activeKeyword = SearchBarKeyword(keyword = keywordTopShop)

        val card = SuggestionTopShopCardDataView(
                type = "top_shop_all",
                applink = "tokopedia://search?q=ps4&st=shop"
        )

        `given view already get suggestion`(searchParameter, activeKeyword)
        `when suggestion top shop card clicked` (card)
        `then verify url tracker is not hit`()
        `then verify view tracking click top shop see more is correct`(card)
    }

    private fun `then verify view tracking click top shop see more is correct`(cardData: SuggestionTopShopCardDataView) {
        val expectedEventLabel = "keyword: $keywordTopShop"

        verify {
            suggestionView.trackEventClickTopShopSeeMore(expectedEventLabel)
            suggestionView.onClickSuggestion(cardData.applink)
        }
    }

    private fun `then verify url tracker is not hit`() {
        confirmVerified(suggestionTrackerUseCase)
    }
}
