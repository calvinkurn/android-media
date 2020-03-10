package com.tokopedia.autocomplete.suggestion

import com.tokopedia.user.session.UserSessionInterface
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test

internal class OnSuggestionItemClickTest {

    private val suggestionView = mockk<SuggestionContract.View>(relaxed = true)
    private val getSuggestionUseCase = mockk<SuggestionUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private lateinit var suggestionPresenter: SuggestionPresenter

    private val applinkShopWithQueryParams = "tokopedia://shop/12345?source=universe&st=product"
    private val applinkShopWithoutQueryParams = "tokopedia://shop/12345"
    private val applinkProfileWithQueryParams = "tokopedia://people/12345?source=universe&st=product"
    private val applinkProfileWithoutQueryParams = "tokopedia://people/12345"
    private val keyword = "samsung"
    private val position = 1
    private val title = "Samsung"

    @Before
    fun setUp() {
        suggestionPresenter = SuggestionPresenter()
        suggestionPresenter.attachView(suggestionView)
        suggestionPresenter.getSuggestionUseCase = getSuggestionUseCase
        suggestionPresenter.userSession = userSession
    }

    @Test
    fun `test click suggestion item`() {
        val item = BaseSuggestionViewModel(applink = applinkShopWithQueryParams)

        `when suggestion item clicked` (item)
        `then verify view interaction is correct`(item)
    }

    private fun `when suggestion item clicked`(item: BaseSuggestionViewModel) {
        suggestionPresenter.onSuggestionItemClicked(item)
    }

    private fun `then verify view interaction is correct`(item: BaseSuggestionViewModel) {
        verifyOrder {
            suggestionView.onClickSuggestion(item)
        }

        confirmVerified(suggestionView)
    }

    private fun SuggestionContract.View.onClickSuggestion(item: BaseSuggestionViewModel) {
        dropKeyBoard()
        route(item.applink)
        finish()
    }

    @Test
    fun `test tracking click suggestion item keyword`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithQueryParams,
                type = TYPE_KEYWORD,
                searchTerm = keyword,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item keyword is correct`(item)
    }

    private fun `then verify view tracking click item keyword is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel = "keyword: $keyword - value: $title - po: $position - applink: $applinkShopWithQueryParams"

        verify {
            suggestionView.trackEventClickKeyword(expectedEventLabel)
            suggestionView.onClickSuggestion(item)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item curated`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithQueryParams,
                type = TYPE_CURATED,
                searchTerm = keyword,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item curated is correct`(item)
    }

    private fun `then verify view tracking click item curated is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel = "keyword: $keyword - product: $title - po: $position - page: $applinkShopWithQueryParams"

        verify {
            suggestionView.trackEventClickCurated(expectedEventLabel)
            suggestionView.onClickSuggestion(item)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item shop using applink with query params`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithQueryParams,
                type = TYPE_SHOP,
                searchTerm = keyword,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item shop is correct`(item)
    }

    private fun `then verify view tracking click item shop is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel = "12345 - keyword: $keyword - shop: $title"

        verify {
            suggestionView.trackEventClickShop(expectedEventLabel)
            suggestionView.onClickSuggestion(item)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item shop using applink without query params`() {
        val item = BaseSuggestionViewModel(
                applink = applinkShopWithoutQueryParams,
                type = TYPE_SHOP,
                searchTerm = keyword,
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
                searchTerm = keyword,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item profile is correct`(item)
    }

    private fun `then verify view tracking click item profile is correct`(item: BaseSuggestionViewModel) {
        val expectedEventLabel = "keyword: $keyword - profile: $title - profile id: 12345 - po: $position"

        verify {
            suggestionView.trackEventClickProfile(expectedEventLabel)
            suggestionView.onClickSuggestion(item)
        }

        confirmVerified(suggestionView)
    }

    @Test
    fun `test tracking click suggestion item profile using applink without query params`() {
        val item = BaseSuggestionViewModel(
                applink = applinkProfileWithoutQueryParams,
                type = TYPE_PROFILE,
                searchTerm = keyword,
                title = title,
                position = position
        )

        `when suggestion item clicked`(item)
        `then verify view tracking click item profile is correct`(item)
    }
}