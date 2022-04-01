package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GLOBAL_NAV
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val globalNavWidgetAndShowTopAdsTrue = "searchproduct/globalnavwidget/show-topads-true.json"
private const val globalNavWidgetAndShowTopAdsFalse = "searchproduct/globalnavwidget/show-topads-false.json"
private const val noGlobalNavWidget = "searchproduct/globalnavwidget/no-global-nav-widget.json"
private const val searchProductPage2WithHeadlineAds = "searchproduct/with-topads-and-headline-ads.json"

internal class SearchProductGlobalNavWidgetTest: ProductListPresenterTestFixtures() {

    private val requestParamsSlot = slot<RequestParams>()
    private val requestParams by lazy { requestParamsSlot.captured }
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Showing both Global Nav Widget and CPM`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is true`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has global nav widget and CPM`()
    }

    private fun `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is true`() {
        val searchProductModel = globalNavWidgetAndShowTopAdsTrue.jsonToObject<SearchProductModel>()

        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        `Given top ads headline helper will process headline ads`(searchProductModel)
    }

    private fun `When Load Data`() {
        val searchParameter = createMapParameterFirstPage()

        productListPresenter.loadData(searchParameter)
    }

    private fun createMapParameterFirstPage() =
        createMapParameter(SearchApiConst.START to "0")

    private fun createMapParameter(vararg additionalParams: Pair<String, Any>) = mapOf(
        SearchApiConst.Q to "pulsa xl",
        SearchApiConst.UNIQUE_ID to "unique_id",
        SearchApiConst.USER_ID to productListPresenter.userId,
        *additionalParams
    )

    private fun `Then verify view set product list`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has global nav widget and CPM`() {
        val visitableList = visitableListSlot.captured

        visitableList[1].shouldBeInstanceOf<GlobalNavDataView>()
        visitableList[2].shouldBeInstanceOf<CpmDataView>()
    }

    @Test
    fun `Hide headline ads with Global Nav Widget`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list has global nav widget and no CPM`(visitableListSlot.captured)
    }

    private fun `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false`() {
        val searchProductModel = globalNavWidgetAndShowTopAdsFalse.jsonToObject<SearchProductModel>()

        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        `Given top ads headline helper will process headline ads`(searchProductModel)
    }

    private fun `Then verify visitable list has global nav widget and no CPM`(
        visitableList: List<Visitable<*>>
    ) {
        visitableList[1].shouldBeInstanceOf<GlobalNavDataView>()

        for(i in 2 until visitableList.size) {
            visitableList[i].shouldBeInstanceOf<ProductItemDataView>()
        }
    }

    @Test
    fun `Hide headline ads on page 2 with Global Nav Widget`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false`()
        `Given view already load data`()
        `Given search more product API will have headline ads`()

        `When view load more data`()

        val visitableList = mutableListOf<Visitable<*>>()
        `Then verify view set and add product list`(visitableList)
        `Then verify visitable list has global nav widget and no CPM`(visitableList)
    }

    private fun `Given search more product API will have headline ads`() {
        val searchProductModel = searchProductPage2WithHeadlineAds.jsonToObject<SearchProductModel>()

        every { searchProductLoadMoreUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        `Given top ads headline helper will process headline ads`(searchProductModel, 2)
    }

    private fun `Given view already load data`() {
        val searchParameter = createMapParameterFirstPage()

        productListPresenter.loadData(searchParameter)
    }

    private fun `When view load more data`() {
        val searchParameter = createMapParameterLoadMore()

        productListPresenter.loadMoreData(searchParameter)
    }

    private fun createMapParameterLoadMore() =
        createMapParameter(SearchApiConst.START to "8")

    private fun `Then verify view set and add product list`(visitableList: MutableList<Visitable<*>>) {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }

        visitableList.addAll(visitableListSlot.captured)

        verify {
            productListView.addProductList(capture(visitableListSlot))
        }

        visitableList.addAll(visitableListSlot.captured)
    }

    @Test
    fun `Showing only CPM, without Global Nav Widget`() {
        `Given Search Product API will return SearchProductModel without Global Nav Widget`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify visitable list not showing global nav widget and still show CPM`()
    }

    private fun `Given Search Product API will return SearchProductModel without Global Nav Widget`() {
        val searchProductModel = noGlobalNavWidget.jsonToObject<SearchProductModel>()

        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        `Given top ads headline helper will process headline ads`(searchProductModel)
    }

    private fun `Then verify visitable list not showing global nav widget and still show CPM`() {
        val visitableList = visitableListSlot.captured

        visitableList[1].shouldBeInstanceOf<CpmDataView>()

        for (i in 2 until visitableList.size) {
            visitableList[i].shouldBeInstanceOf<ProductItemDataView>()
        }
    }

    @Test
    fun `Search Result Page is landing page and should not show Global Nav Widget`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is false`()
        `Given Search Result Page is landing page`()

        `When Load Data`()

        `Then verify view set product list`()

        `Then verify visitable list not showing global nav widget and still show CPM`()
    }

    private fun `Given Search Result Page is landing page`() {
        every { productListView.isLandingPage } returns true
    }

    @Test
    fun `Do not show global nav widget there is filter`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is true`()
        `Given product list view has active filter`()

        `When Load Data`()

        `Then verify request params to skip global nav`()
        `Then verify view set product list`()
        `Then verify visitable list does not have global nav widget`()
    }

    private fun `Given product list view has active filter`() {
        every { productListView.isAnyFilterActive } returns true
    }

    private fun `Then verify request params to skip global nav`() {
        requestParams.getBoolean(SEARCH_PRODUCT_SKIP_GLOBAL_NAV, false) shouldBe true
    }

    private fun `Then verify visitable list does not have global nav widget`() {
        val visitableList = visitableListSlot.captured

        visitableList.any { it is GlobalNavDataView } shouldBe false
    }

    @Test
    fun `Do not show global nav widget there is sort`() {
        `Given Search Product API will return SearchProductModel with Global Nav Widget and CPM, and showTopAds is true`()
        `Given product list view has active sort`()

        `When Load Data`()

        `Then verify request params to skip global nav`()
        `Then verify view set product list`()
        `Then verify visitable list does not have global nav widget`()
    }

    private fun `Given product list view has active sort`() {
        every { productListView.isAnySortActive } returns true
    }
}