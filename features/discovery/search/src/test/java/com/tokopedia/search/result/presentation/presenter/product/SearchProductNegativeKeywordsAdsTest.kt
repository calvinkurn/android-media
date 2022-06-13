package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

internal class SearchProductNegativeKeywordsAdsTest : ProductListPresenterTestFixtures() {
    private val searchProductNegativeKeywordsResponseJSON = "searchproduct/negativekeywords/with-negative-keywords.json"

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    private fun `Given search product API will success`(searchProductModel: SearchProductModel) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given view will set and add product list`() {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify that ads is not loaded`() {
        Assert.assertTrue(
            visitableList.filterIsInstance<ProductItemDataView>().all { !it.isTopAds })
    }

    @Test
    fun `Product list with negative keywords will show no ads`() {
        val searchProductModel = searchProductNegativeKeywordsResponseJSON.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mapOf<String, Any>(
            SearchApiConst.Q to "tv -samsung -lg -realme -android -smart",
        )

        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When Load Data`(searchParameter)

        `Then verify that ads is not loaded`()
    }
}