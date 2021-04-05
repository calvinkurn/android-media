package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.topads.sdk.domain.model.Cpm
import io.mockk.every
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

private const val searchProductHeadlineAdsTopJSON = "searchproduct/headlineads/headline-ads-top.json"
private const val searchProductHeadlineAdsAdditionalPosition14JSON = "searchproduct/headlineads/headline-ads-additional-position-14.json"
private const val searchProductHeadlineAdsAdditionalPosition24JSON = "searchproduct/headlineads/headline-ads-additional-position-24.json"
private const val searchProductHeadlineAdsAdditionalPositionNegativeJSON = "searchproduct/headlineads/headline-ads-additional-position-negative.json"

internal class SearchProductHeadlineAdsTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList = mutableListOf<Visitable<*>>()

    @Test
    fun `Single headline ads at top`() {
        val searchProductModel = searchProductHeadlineAdsTopJSON.jsonToObject<SearchProductModel>()
        `Given search product API will return search product model`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        `Then verify CPM at the top of list`()
    }

    private fun `Given search product API will return search product model`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
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

    private fun `Then verify CPM at the top of list`() {
        visitableList.first().shouldBeInstanceOf<CpmDataView>()
    }

    @Test
    fun `Additional headline ads in page 1`() {
        val searchProductModel = searchProductHeadlineAdsAdditionalPosition14JSON.jsonToObject<SearchProductModel>()
        val additionalCpmIndex = 16
        val expectedCpmPosition = 14
        val expectedAdditionalCpmLayout = 2

        `Given search product API will return search product model`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        `Then verify CPM at the top and at additional position between product cards`(additionalCpmIndex)
        `Then verify CPM data`(additionalCpmIndex, expectedCpmPosition, expectedAdditionalCpmLayout)
    }

    private fun `Then verify CPM at the top and at additional position between product cards`(additionalCpmIndex: Int) {
        visitableList.first().shouldBeInstanceOf<CpmDataView>()
        visitableList[additionalCpmIndex - 1].shouldBeInstanceOf<SeparatorDataView>()
        visitableList[additionalCpmIndex].shouldBeInstanceOf<CpmDataView>()
        visitableList[additionalCpmIndex + 1].shouldBeInstanceOf<SeparatorDataView>()

    }

    private fun `Then verify CPM data`(additionalCpmIndex: Int, expectedAdditionalCpmPosition: Int, expectedAdditionalCpmLayout: Int) {
        val firstCpm = (visitableList.first() as CpmDataView).getCpm()
        firstCpm.position shouldBe 1
        firstCpm.layout shouldBe 1

        val additionalCpm = (visitableList[additionalCpmIndex] as CpmDataView).getCpm()
        additionalCpm.position shouldBe expectedAdditionalCpmPosition
        additionalCpm.layout shouldBe expectedAdditionalCpmLayout
    }

    private fun CpmDataView.getCpm(): Cpm = this.cpmModel.data[0].cpm

    @Test
    fun `Additional headline ads in page 2`() {
        val searchProductModel = searchProductHeadlineAdsAdditionalPosition24JSON.jsonToObject<SearchProductModel>()
        val additionalCpmIndex = 26
        val expectedCpmPosition = 24
        val expectedAdditionalCpmLayout = 2

        `Given search product API will return search product model`(searchProductModel)
        `Given search product load more API will return search product model`()
        `Given visitable list will be captured`()
        `Given view already load data`()

        `When load more data`()

        `Then verify CPM at the top and at additional position between product cards`(additionalCpmIndex)
        `Then verify CPM data`(additionalCpmIndex, expectedCpmPosition, expectedAdditionalCpmLayout)
    }

    private fun `Given search product load more API will return search product model`() {
        every { searchProductLoadMoreUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete("searchproduct/with-topads.json".jsonToObject())
        }
    }

    private fun `Given view already load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `When load more data`() {
        productListPresenter.loadMoreData(mapOf())
    }

    @Test
    fun `Headline ads with invalid position`() {
        val searchProductModel = searchProductHeadlineAdsAdditionalPositionNegativeJSON.jsonToObject<SearchProductModel>()

        `Given search product API will return search product model`(searchProductModel)
        `Given visitable list will be captured`()

        `When load data`()

        `Then assert visitable list does not contain CPM`()
    }

    private fun `Then assert visitable list does not contain CPM`() {
        visitableList.any { it is CpmDataView } shouldBe false
    }
}