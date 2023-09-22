package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val searchProductThirdPageJSON = "searchproduct/loaddata/third-page.json"
private const val REQUEST_PARAMS_LAST_CLICK = "last_click"

internal class SearchProductLoadMoreTrackLastProductTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Load More Data Success with last clicked param from already Load Data`() {
        val searchProductModelSecondPage = searchProductSecondPageJSON.jsonToObject<SearchProductModel>()

        `Given View already load data`(searchProductFirstPageJSON)
        val productItemViewModel = getProductItemFromVisitableList(2)
        `Given product click`(productItemViewModel)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)


        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `Then verify load more use case request params with last clicked correct id`(productItemViewModel)
    }

    @Test
    fun `Load More Data Success without last clicked param from already Load Data`() {
        val searchProductModelSecondPage = searchProductSecondPageJSON.jsonToObject<SearchProductModel>()

        `Given View already load data`(searchProductFirstPageJSON)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `Then verify load more use case request params without last clicked params`()
    }

    @Test
    fun `Load More for third page should send last clicked product from second page`() {
        val searchProductModelSecondPage = searchProductSecondPageJSON.jsonToObject<SearchProductModel>()
        val searchProductModelThirdPage = searchProductThirdPageJSON.jsonToObject<SearchProductModel>()

        `Given View already load data`(searchProductFirstPageJSON)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)

        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelSecondPage)
        } andThenAnswer {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelThirdPage)
        }

        `Given Search Load Second Page`()
        val productItemViewModelClicked = getProductItemFromVisitableList(10)
        `When Product Card Clicked`(searchProductModelSecondPage, productItemViewModelClicked)
        `When Product List Presenter Load More Data`(createLoadMoreSearchParameter())

        `Then verify load more use case request params with last clicked correct id`(productItemViewModelClicked)
    }

    @Test
    fun `Load More for third page should not send last clicked product from first page product clicked`() {
        val searchProductModelSecondPage = searchProductSecondPageJSON.jsonToObject<SearchProductModel>()
        val searchProductModelThirdPage = searchProductThirdPageJSON.jsonToObject<SearchProductModel>()

        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)

        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelSecondPage)
        } andThenAnswer {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModelThirdPage)
        }

        `Given First Page Load and Clicked Product Card`()
        `Given Search Load Second Page`()
        `When Product List Presenter Load More Data`(createLoadMoreSearchParameter())

        `Then verify load more use case request params without last clicked params`()
    }

    private fun `Given Search Load Second Page`(){
        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)
    }

    private fun `Given First Page Load and Clicked Product Card`() {
        `Given View already load data`(searchProductFirstPageJSON)
        val productItemViewModel = getProductItemFromVisitableList(2)
        `Given product click`(productItemViewModel)
    }

    private fun `When Product Card Clicked`(searchProductModel: SearchProductModel,  productItemViewModel: ProductItemDataView) {
        `Given product click`(productItemViewModel)
        `Given Search Product Load More API will return SearchProductModel`(searchProductModel)
    }

    private fun getProductItemFromVisitableList(position: Int): ProductItemDataView {
        val visitableList = visitableListSlot.captured

        return visitableList[position] as ProductItemDataView
    }

    private fun `Given product click`(productItem : ProductItemDataView) {
        productListPresenter.trackLastProductClicked(productItem)
    }

    private fun `Given View already load data`(responseJSON: String) {
        val searchProductModel = responseJSON.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given className from view`()
        `Given view already load data`()
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

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun createLoadMoreSearchParameter() : Map<String, Any> = mutableMapOf<String, Any>().also {
        it[SearchApiConst.Q] = "samsung"
        it[SearchApiConst.START] = productListPresenter.startFrom
        it[SearchApiConst.UNIQUE_ID] = "unique_id"
        it[SearchApiConst.USER_ID] = productListPresenter.userId
    }

    private fun `When Product List Presenter Load More Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadMoreData(searchParameter)
    }

    private fun `Then verify load more use case request params with last clicked correct id`(clickedProduct: ProductItemDataView) {
        val requestParams = requestParamsSlot.captured

        val params = requestParams.getSearchProductParams()

        verifyRequestContainsLastClickedProductId(params, clickedProduct.productID)
    }

    private fun `Then verify load more use case request params with last clicked`() {
        val requestParams = requestParamsSlot.captured

        val params = requestParams.getSearchProductParams()

        verifyRequestContainsLastClickedProduct(params)
    }

    private fun `Then verify load more use case request params without last clicked params`() {
        val requestParams = requestParamsSlot.captured

        val params = requestParams.getSearchProductParams()

        verifyRequestNotContainsLastClickedProduct(params)
    }

    private fun `Given Search Product Load More API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun verifyRequestContainsLastClickedProductId(params: Map<String, Any>, clickProductId: String) {
        params[REQUEST_PARAMS_LAST_CLICK] shouldBe clickProductId
    }

    private fun verifyRequestNotContainsLastClickedProduct(params: Map<String, Any>) {
        params.containsKey(REQUEST_PARAMS_LAST_CLICK) shouldBe false
    }

    private fun verifyRequestContainsLastClickedProduct(params: Map<String, Any>) {
        params.containsKey(REQUEST_PARAMS_LAST_CLICK) shouldBe true
    }

}
