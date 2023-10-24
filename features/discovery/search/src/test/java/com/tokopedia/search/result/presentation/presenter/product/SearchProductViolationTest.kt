package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.reimagine.Search3ProductCard
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.Violation
import com.tokopedia.search.result.product.emptystate.EmptyStateDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.search.result.product.violation.ViolationDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Test
import rx.Subscriber

internal class SearchProductViolationTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `Show violation products message`() {
        val searchProductModel = "searchproduct/violation/violation.json".jsonToObject<SearchProductModel>()

        `Given search product API will return empty search with error message`(searchProductModel)

        `When load data`("diethylether")

        `Then verify view interaction for violation products`()
        `Then verify violation products view model`(searchProductModel.searchProduct.data.violation)
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
            productListView.addProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify violation products view model`(violation: Violation) {
        visitableList.size shouldBe 1

        val violationDataView = visitableList[0] as ViolationDataView

        violationDataView.verify(violation)
    }

    private fun ViolationDataView.verify(violation: Violation) {
        headerText shouldBe violation.headerText
        descriptionText shouldBe violation.descriptionText
        violationButton.ctaUrl shouldBe violation.ctaUrl
        violationButton.text shouldBe violation.buttonText
        verticalSeparator shouldBe VerticalSeparator.Bottom
    }

    @Test
    fun `Show violation products message with global nav`() {
        val violationProductsJSON = "searchproduct/violation/violation-with-global-nav.json"
        val searchProductModel = violationProductsJSON.jsonToObject<SearchProductModel>()

        `Given search product API will return empty search with error message`(searchProductModel)

        `When load data`("avigan")

        `Then verify view interaction for violation products`()
        `Then verify visitable list contains global nav and violation products`(
            searchProductModel.searchProduct.data.violation
        )
    }

    private fun `Then verify visitable list contains global nav and violation products`(
        violation: Violation
    ) {
        val globalNavWidgetDataView = visitableList[0]
        globalNavWidgetDataView.shouldBeInstanceOf<GlobalNavDataView>()

        val violationDataView = visitableList[1] as ViolationDataView

        violationDataView.verify(violation)
    }

    @Test
    fun `Show empty product search when violation header and description is empty`() {
        val noViolationEmptyProductsJSON = "searchproduct/violation/no-violation.json"
        val searchProductModel = noViolationEmptyProductsJSON.jsonToObject<SearchProductModel>()

        `Given search product API will return empty search with error message`(searchProductModel)

        `When load data`("asdgasdfasdf")

        `Then verify view interaction for empty products search`()
        `Then assert visitable list contains empty state`()
    }

    private fun `Then verify view interaction for empty products search`() {
        verify {
            productListView.removeLoading()
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then assert visitable list contains empty state`() {
        assertTrue(visitableList.any { it is EmptyStateDataView })
    }

    @Test
    fun `Show violation products message for search reimagine`() {
        val searchProductModel = "searchproduct/violation/violation-reimagine.json"
            .jsonToObject<SearchProductModel>()

        `Given search reimagine rollence product card will return non control variant`()
        `Given search product API will return empty search with error message`(searchProductModel)

        `When load data`("diethylether")

        `Then verify view interaction for violation products`()
        `Then verify violation products view model`(searchProductModel.searchProductV5.data.violation)
    }
}
