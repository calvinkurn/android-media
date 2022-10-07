package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.domain.model.ProductTopAdsModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductTypoCorrectionUseCase
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Observable

internal class SearchProductTopAdsTypoCorrectionFirstPageTest : ProductListPresenterTestFixtures() {
    private val searchProductResponseCode3 = "searchproduct/typocorrection/response-code-3.json"
    private val topAdsTypoCorrected = "searchproduct/typocorrection/topads-typo-corrected.json"

    private val requestParamsSlot = slot<RequestParams>()
    private val modifiedRequestParamsSlot = slot<RequestParams>()
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList
        get() = visitableListSlot.captured

    override val searchFirstPageUseCase: UseCase<SearchProductModel>
        get() = SearchProductTypoCorrectionUseCase(
            searchProductFirstPageUseCase,
            searchProductTopAdsUseCase,
            { performanceMonitoring },
            testSchedulersProvider,
        )

    @Test
    fun `Response Code 3 will call TopAds GQL again`() {
        val searchProductModel = searchProductResponseCode3.jsonToObject<SearchProductModel>()
        val expectedRelatedKeyword = searchProductModel.searchProduct.data.related.relatedKeyword
        val expectedTopAds = topAdsTypoCorrected.jsonToObject<ProductTopAdsModel>().topAdsModel

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given TopAds API will return TopAdsModel`(expectedTopAds)

        `When Load Data`(mapOf(SearchApiConst.Q to "samsung"))

        `Then verify TopAds use case is executed`()
        `Then verify topAds request params query replaced with relatedKeywords`(
            expectedRelatedKeyword
        )
        `Then verify view will set product list`()
        `Then verify topAds products is replaced with typo correction`(expectedTopAds)
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.createObservable(capture(requestParamsSlot)) }
            .returns(Observable.just(searchProductModel))
    }

    private fun `Given TopAds API will return TopAdsModel`(topAdsModel: TopAdsModel) {
        every { searchProductTopAdsUseCase.createObservable(capture(modifiedRequestParamsSlot)) }
            .returns(Observable.just(topAdsModel))
    }

    private fun `Given TopAds API will return error`() {
        every { searchProductTopAdsUseCase.createObservable(capture(modifiedRequestParamsSlot)) }
            .returns(Observable.error(Throwable()))
    }


    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify TopAds use case is executed`() {
        verify {
            searchProductTopAdsUseCase.createObservable(any())
        }
    }

    private fun `Then verify view will set product list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify topAds request params query replaced with relatedKeywords`(
        expectedQuery: String
    ) {
        val requestParams = modifiedRequestParamsSlot.captured
        requestParams.getString(SearchApiConst.Q, "") shouldBe expectedQuery
        requestParams.getBoolean(SearchApiConst.IS_TYPO_CORRECTED, false) shouldBe true
    }

    private fun `Then verify topAds products is replaced with typo correction`(
        expectedTopAds: TopAdsModel
    ) {
        visitableList.filter { it is ProductItemDataView && it.isTopAds }
            .forEachIndexed { index, visitable ->
                visitable.assertTopAdsProduct(expectedTopAds.data[index], index + 1)
            }
    }

    @Test
    fun `Response TopAds error will remove all TopAds products`() {
        val searchProductModel = searchProductResponseCode3.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given TopAds API will return error`()

        `When Load Data`(mapOf(SearchApiConst.Q to "samsung"))

        `Then verify view will set product list`()

        visitableList.any {
            it is ProductItemDataView && it.isTopAds
        } shouldBe false
    }

}
