package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductBannedProductsTest: ProductListPresenterTestFixtures() {

    private val bannedProductsVisitableListSlot = slot<List<Visitable<*>>>()
    private val bannedProductsVisitableList by lazy { bannedProductsVisitableListSlot.captured }

    @Test
    fun `Test show banned products message`() {
        val searchProductModel = "searchproduct/bannedproducts/banned-products.json".jsonToObject<SearchProductModel>()

        `Given search product API will return empty search with error message`(searchProductModel)

        `When load data`()

        `Then verify view interaction for banned products`()
        `Then verify banned products view model`(searchProductModel)
    }

    private fun `Given search product API will return empty search with error message`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf(SearchApiConst.Q to "rokok"))
    }

    private fun `Then verify view interaction for banned products`() {
        verify {
            productListView.removeLoading()
            productListView.setBannedProductsErrorMessage(capture(bannedProductsVisitableListSlot))
            productListView.trackEventImpressionBannedProducts(true)
        }
    }

    private fun `Then verify banned products view model`(searchProductModel: SearchProductModel) {
        bannedProductsVisitableList.size shouldBe 1

        val bannedProductsViewModel = bannedProductsVisitableList[0] as BannedProductsEmptySearchDataView

        bannedProductsViewModel.errorMessage shouldBe searchProductModel.searchProduct.header.errorMessage
    }
}