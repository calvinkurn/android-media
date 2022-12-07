package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import io.mockk.every
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val headlineAdsSingleFirstPage = "searchproduct/headlineads/headline-ads-single-first-page.json"
private const val headlineAdsMultipleFirstPage = "searchproduct/headlineads/headline-ads-multiple-first-page.json"
private const val headlineAdsMultipleSecondPage = "searchproduct/headlineads/headline-ads-multiple-second-page.json"

internal class SearchProductHeadlineAdsTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList = mutableListOf<Visitable<*>>()

    @Test
    fun `Single headline ads at second top`() {
        val searchProductModel = headlineAdsSingleFirstPage.jsonToObject<SearchProductModel>()
        `Given search product API will return search product model`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        val expectedCpmModel = searchProductModel.cpmModel
        `Then verify CPM at the second top of list`(expectedCpmModel, expectedCpmModel.data.first())
    }

    private fun `Given search product API will return search product model`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        `Given top ads headline helper will process headline ads`(searchProductModel)
    }

    private fun `Given visitable list will be captured`() {
        every { productListView.setProductList(capture(visitableListSlot)) } answers {
            visitableList.addAll(visitableListSlot.captured)
        }

        every { productListView.addProductList(capture(visitableListSlot)) } answers {
            visitableList.addAll(visitableListSlot.captured)
        }
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify CPM at the second top of list`(
        expectedCpmModel: CpmModel,
        expectedCpmData: CpmData,
    ) {
        visitableList[1].assertCpmModel(expectedCpmModel, expectedCpmData, VerticalSeparator.None)
    }

    private fun Visitable<*>.assertCpmModel(
        expectedCpmModel: CpmModel,
        expectedCpmData: CpmData,
        expectedSeparator: VerticalSeparator,
    ) {
        this.shouldBeInstanceOf<CpmDataView>()

        val cpmDataView = this as CpmDataView
        val actualCpmModel = cpmDataView.cpmModel

        actualCpmModel.header.shouldBe(expectedCpmModel.header)
        actualCpmModel.status.shouldBe(expectedCpmModel.status)
        actualCpmModel.error.shouldBe(expectedCpmModel.error)
        actualCpmModel.data.shouldBe(listOf(expectedCpmData))
        cpmDataView.verticalSeparator.shouldBe(expectedSeparator)
    }

    @Test
    fun `Multiple headline ads in page 1`() {
        val searchProductModel = headlineAdsMultipleFirstPage.jsonToObject<SearchProductModel>()

        `Given search product API will return search product model`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        val expectedCpmModel = searchProductModel.cpmModel
        `Then verify CPM at the second top of list`(expectedCpmModel, expectedCpmModel.data.first())
        `Then verify CPM after last product cards`(expectedCpmModel, expectedCpmModel.data[1])
    }

    private fun `Then verify CPM after last product cards`(
        expectedCpmModel: CpmModel,
        expectedCpmData: CpmData,
    ) {
        val firstSeparatorIndex = visitableList.indexOfLast { it is ProductItemDataView } + 1

        visitableList[firstSeparatorIndex].assertCpmModel(
            expectedCpmModel,
            expectedCpmData,
            VerticalSeparator.Both
        )
    }

    @Test
    fun `headline ads at end of page 2`() {
        val searchProductModel = headlineAdsMultipleFirstPage.jsonToObject<SearchProductModel>()
        val searchProductModelPage2 = headlineAdsMultipleSecondPage.jsonToObject<SearchProductModel>()

        `Given search product API will return search product model`(searchProductModel)
        `Given visitable list will be captured`()
        `Given view already load data`()
        `Given search product load more API will return search product model`(searchProductModelPage2)

        `When load more data`()

        val expectedCpmModel = searchProductModelPage2.cpmModel
        val expectedCpmData = expectedCpmModel.data.first()
        `Then verify CPM after last product cards`(expectedCpmModel, expectedCpmData)
    }

    private fun `Given search product load more API will return search product model`(
        searchModelPage2: SearchProductModel,
    ) {
        every { searchProductLoadMoreUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchModelPage2)
        }

        `Given top ads headline helper will process headline ads`(searchModelPage2, 2)
    }

    private fun `Given view already load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `When load more data`() {
        productListPresenter.loadMoreData(mapOf())
    }

    @Test
    fun `Single headline ads at top`() {
        val searchProductModel = headlineAdsSingleFirstPage.jsonToObject<SearchProductModel>()
        `Given search product API will return search product model`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        `Then verify CPM at the top of list`()
    }

    private fun `Then verify CPM at the top of list`(
    ) {
        visitableList[0].shouldBeInstanceOf<ChooseAddressDataView>()
        visitableList[1].shouldBeInstanceOf<CpmDataView>()
    }
}