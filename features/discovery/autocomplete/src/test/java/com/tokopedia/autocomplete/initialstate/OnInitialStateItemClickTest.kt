package com.tokopedia.autocomplete.initialstate

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifyOrder
import org.junit.Test
import rx.Subscriber

internal class OnInitialStateItemClickTest: InitialStatePresenterTestFixtures(){

    private val keyword = "sepatu"
    private val applink = "tokopedia://search?q=$keyword&source=universe&st=product"
    private val shopId = "8384142"
    private val shopName = "MizanBookCorner"
    private val applinkShop = "tokopedia://shop/$shopId?source=universe&st=product"

    @Test
    fun `test click recent search item`() {
        val item = BaseItemInitialStateSearch(
                applink = applink,
                title = keyword
        )

        `given initial state use case capture request params`(initialStateCommonData)
        `when recent search item clicked` (item, 0)
        `then verify view interaction is correct`(item)
    }

    private fun `given initial state use case capture request params`(list: List<InitialStateData>) {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(list)
        }
    }

    private fun `when recent search item clicked`(item: BaseItemInitialStateSearch, position: Int) {
        initialStatePresenter.onRecentSearchItemClicked(item, position)
    }

    private fun `then verify view interaction is correct`(item: BaseItemInitialStateSearch) {
        verifyOrder {
            initialStateView.onClickRecentSearch(item)
        }

        confirmVerified(initialStateView)
    }

    private fun InitialStateContract.View.onClickRecentSearch(item: BaseItemInitialStateSearch) {
        trackEventClickRecentSearch(getItemEventLabelForTracking(item, 0), 0)
        route(item.applink, initialStatePresenter.getSearchParameter())
        finish()
    }

    private fun getItemEventLabelForTracking(item: BaseItemInitialStateSearch, adapterPosition: Int): String {
        return String.format(
                "value: %s - po: %s - applink: %s",
                item.title,
                (adapterPosition + 1).toString(),
                item.applink
        )
    }

    @Test
    fun `test click recent shop item`() {
        val item = BaseItemInitialStateSearch(
                applink = applinkShop,
                title = shopName,
                type = TYPE_SHOP,
                productId = shopId
        )

        `given initial state use case capture request params`(initialStateCommonData)
        `when recent search item clicked` (item, 0)
        `then verify view interaction is correct for recent shop`(item)
    }

    private fun `then verify view interaction is correct for recent shop`(item: BaseItemInitialStateSearch) {
        verifyOrder {
            initialStateView.onClickRecentShop(item)
        }

        confirmVerified(initialStateView)
    }

    private fun InitialStateContract.View.onClickRecentShop(item: BaseItemInitialStateSearch) {
        trackEventClickRecentShop(getRecentShopLabelForTracking(item), "0")
        route(item.applink, initialStatePresenter.getSearchParameter())
        finish()
    }

    private fun getRecentShopLabelForTracking(item: BaseItemInitialStateSearch): String {
        return item.productId + " - keyword: " + item.title
    }
}