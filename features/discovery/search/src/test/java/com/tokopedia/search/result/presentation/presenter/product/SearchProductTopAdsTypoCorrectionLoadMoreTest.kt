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

internal class SearchProductTopAdsTypoCorrectionLoadMoreTest : ProductListPresenterTestFixtures() {
    private val searchProductResponseCode3 = "searchproduct/typocorrection/response-code-3.json"
    private val searchProductResponseCode3SecondPage = "searchproduct/typocorrection/response-code-3-second-page.json"
    private val topAdsTypoCorrected = "searchproduct/typocorrection/topads-typo-corrected.json"
    private val topAdsTypoCorrectedSecondPage = "searchproduct/typocorrection/topads-typo-corrected-second-page.json"

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
    override val searchLoadMoreUseCase: UseCase<SearchProductModel>
        get() = SearchProductTypoCorrectionUseCase(
            searchProductLoadMoreUseCase,
            searchProductTopAdsUseCase,
            { performanceMonitoring },
            testSchedulersProvider,
        )

    @Test
    fun `Load More with Response Code 3 will call TopAds GQL again`() {
        val searchProductModelFirstPage = searchProductResponseCode3.jsonToObject<SearchProductModel>()
        val typoCorrectedTopAdsFirstPage = topAdsTypoCorrected.jsonToObject<ProductTopAdsModel>().topAdsModel
        val searchProductModelSecondPage = searchProductResponseCode3SecondPage.jsonToObject<SearchProductModel>()
        val relatedKeywordSecondPage = searchProductModelSecondPage.searchProduct.data.related.relatedKeyword
        val expectedTopAds = topAdsTypoCorrectedSecondPage.jsonToObject<ProductTopAdsModel>().topAdsModel

        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given TopAds API will return TopAdsModel`(typoCorrectedTopAdsFirstPage)
        `Given Product List Presenter already Load Data`()

        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)
        `Given TopAds API will return TopAdsModel`(expectedTopAds)

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `Then verify TopAds use case is executed`()
        `Then verify topAds request params query replaced with relatedKeywords`(
            relatedKeywordSecondPage
        )
        `Then verify view will add product list`()
        val topAdsIndexStart = typoCorrectedTopAdsFirstPage.data.size
        `Then verify topAds products is replaced with typo correction`(
            expectedTopAds,
            topAdsIndexStart
        )
    }

    private fun `Given Product List Presenter already Load Data`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
        }

        productListPresenter.loadData(searchParameter)
    }

    private fun createLoadMoreSearchParameter() : Map<String, Any> = mutableMapOf<String, Any>().also {
        it[SearchApiConst.Q] = "samsung"
        it[SearchApiConst.START] = productListPresenter.startFrom
    }

    private fun `When Product List Presenter Load More Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadMoreData(searchParameter)
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.createObservable(any()) }
            .returns(Observable.just(searchProductModel))
    }

    private fun `Given Search Product Load More API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductLoadMoreUseCase.createObservable(capture(requestParamsSlot)) }
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

    private fun `Then verify TopAds use case is executed`() {
        verify(exactly = 2) {
            searchProductTopAdsUseCase.createObservable(any())
        }
    }

    private fun `Then verify view will add product list`() {
        verify {
            productListView.addProductList(capture(visitableListSlot))
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
        expectedTopAds: TopAdsModel,
        topAdsPositionStart: Int = 0,
    ) {
        visitableList.filter { it is ProductItemDataView && it.isTopAds }
            .forEachIndexed { index, visitable ->
                val position = topAdsPositionStart + index + 1
                visitable.assertTopAdsProduct(expectedTopAds.data[index], position)
            }
    }

    @Test
    fun `Load More TopAds with error response will remove all TopAds products from current SearchProductModel`() {
        val searchProductModelFirstPage = searchProductResponseCode3.jsonToObject<SearchProductModel>()
        val typoCorrectedTopAdsFirstPage = topAdsTypoCorrected.jsonToObject<ProductTopAdsModel>().topAdsModel
        val searchProductModelSecondPage = searchProductResponseCode3SecondPage.jsonToObject<SearchProductModel>()

        `Given Search Product API will return SearchProductModel`(searchProductModelFirstPage)
        `Given TopAds API will return TopAdsModel`(typoCorrectedTopAdsFirstPage)
        `Given Product List Presenter already Load Data`()

        `Given Search Product Load More API will return SearchProductModel`(searchProductModelSecondPage)
        `Given TopAds API will return error`()

        val loadMoreSearchParameter = createLoadMoreSearchParameter()
        `When Product List Presenter Load More Data`(loadMoreSearchParameter)

        `Then verify view will add product list`()

        visitableList.any {
            it is ProductItemDataView && it.isTopAds
        } shouldBe false
    }

}
