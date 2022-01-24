package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.GlobalNavDataView
import com.tokopedia.search.result.product.violation.ViolationDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductViolationTest: ProductListPresenterTestFixtures() {

    private val violationProductsVisitableListSlot = slot<List<Visitable<*>>>()
    private val violationProductsVisitableList by lazy { violationProductsVisitableListSlot.captured }

    @Test
    fun `Show violation products message`() {
        val searchProductModel = "searchproduct/violation/violation.json".jsonToObject<SearchProductModel>()

        `Given search product API will return empty search with error message`(searchProductModel)

        `When load data`("diethylether")

        `Then verify view interaction for violation products`()
        `Then verify violation products view model`(searchProductModel)
    }

    private fun `Given search product API will return empty search with error message`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When load data`(query: String) {
        productListPresenter.loadData(mapOf(SearchApiConst.Q to query))
    }

    private fun `Then verify view interaction for violation products`() {
        verify {
            productListView.removeLoading()
            productListView.setViolationProductsErrorMessage(capture(violationProductsVisitableListSlot))
        }
    }

    private fun `Then verify violation products view model`(searchProductModel: SearchProductModel) {
        violationProductsVisitableList.size shouldBe 2

        val violationProductsViewModel = violationProductsVisitableList[0] as ViolationDataView

        val violation = searchProductModel.searchProduct.data.violation!!

        violationProductsViewModel.headerText shouldBe violation.headerText
        violationProductsViewModel.descriptionText shouldBe violation.descriptionText
        violationProductsViewModel.violationButton.ctaUrl shouldBe violation.ctaUrl
        violationProductsViewModel.violationButton.text shouldBe violation.buttonText
    }

    @Test
    fun `Show violation products message with global nav`() {
        val violationProductsJSON = "searchproduct/violation/violation-with-global-nav.json"
        val searchProductModel = violationProductsJSON.jsonToObject<SearchProductModel>()

        `Given search product API will return empty search with error message`(searchProductModel)

        `When load data`("avigan")

        `Then verify view interaction for violation products`()
        `Then verify visitable list contains global nav and violation products`(searchProductModel)
    }

    private fun `Then verify visitable list contains global nav and violation products`(
        searchProductModel: SearchProductModel
    ) {
        val globalNavWidgetDataView = violationProductsVisitableList[0]
        globalNavWidgetDataView.shouldBeInstanceOf<GlobalNavDataView>()

        val violationProductsViewModel =
            violationProductsVisitableList[1] as ViolationDataView
        val violation = searchProductModel.searchProduct.data.violation!!

        violationProductsViewModel.headerText shouldBe violation.headerText
        violationProductsViewModel.descriptionText shouldBe violation.descriptionText
        violationProductsViewModel.violationButton.ctaUrl shouldBe violation.ctaUrl
        violationProductsViewModel.violationButton.text shouldBe violation.buttonText
    }
}