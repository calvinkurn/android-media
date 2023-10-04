package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

internal class SearchProductLoadMoreTrackLastProductTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Load More params will have last clicked product id from regular product card`() {
        `Given View already load data`(searchProductFirstPageJSON)

        val productPosition = 2
        val productItem = getProductItemFromVisitableList(productPosition)
        `Given a product is clicked`(productItem, productPosition)
        `Given Search Product Load More API will be successful`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Load More Data`(loadMoreSearchParameter)

        `Then verify load more use case request params with last clicked correct id`(
            productItem.productID
        )
    }

    private fun `Given View already load data`(responseJSON: String) {
        val searchProductModel = responseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given className from view`()
        `Given view already load data`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given className from view`() {
        every { productListView.className } returns className
    }

    private fun `Given view already load data`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs

        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }
        productListPresenter.loadData(searchParameter)
    }

    private fun getProductItemFromVisitableList(position: Int): ProductItemDataView {
        val visitableList = visitableListSlot.captured

        return visitableList[position] as ProductItemDataView
    }

    private fun `Given a product is clicked`(productItem : ProductItemDataView, position: Int) {
        productListPresenter.onProductClick(productItem, position)
    }

    private fun `Given Search Product Load More API will be successful`() {
        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) } just runs
    }

    private fun createLoadMoreSearchParameter() : Map<String, Any> = mutableMapOf<String, Any>().also {
        it[SearchApiConst.Q] = "samsung"
        it[SearchApiConst.START] = productListPresenter.startFrom
        it[SearchApiConst.UNIQUE_ID] = "unique_id"
        it[SearchApiConst.USER_ID] = productListPresenter.userId
    }

    private fun `When Load More Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadMoreData(searchParameter)
    }

    private fun `Then verify load more use case request params with last clicked correct id`(
        expectedProductId: String,
    ) {
        val requestParams = requestParamsSlot.captured

        val params = requestParams.getSearchProductParams()

        params[SearchApiConst.LAST_CLICK] shouldBe expectedProductId
    }

    @Test
    fun `load more params will have last clicked product id from seamless carousel`() {
        val seamlessInspirationCarouselJSON =
            "searchproduct/seamlessinspiration/seamless-inspiration-product.json"
        `Given View already load data`(seamlessInspirationCarouselJSON)

        val visitableList = visitableListSlot.captured
        val targetProduct = visitableList
            .find { it is InspirationProductItemDataView } as InspirationProductItemDataView

        `Given seamless inspiration carousel product is clicked`(targetProduct)
        `Given Search Product Load More API will be successful`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Load More Data`(loadMoreSearchParameter)

        `Then verify load more use case request params with last clicked correct id`(
            targetProduct.id
        )
    }

    private fun `Given seamless inspiration carousel product is clicked`(targetProduct: InspirationProductItemDataView) {
        productListPresenter.onInspirationProductItemClick(targetProduct)
    }
}
