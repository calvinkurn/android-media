package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.remoteconfig.RollenceKey
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

internal class SearchProductNegativeKeywordsABTestAdsTest : ProductListPresenterTestFixtures() {
    private val searchProductNegativeKeywordsResponseJSON = "searchproduct/negativekeywords/with-negative-keywords.json"
    private val searchProductWithTopAdsAndHeadlineAdsJSON = "searchproduct/with-topads-and-headline-ads.json"

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    private fun `Given view will return no ads ab test`() {
        every {
            productListView.abTestRemoteConfig?.getString(
                RollenceKey.SEARCH_ADVANCED_KEYWORD_ADV_NEG,
                any()
            )
        } answers {
            RollenceKey.SEARCH_ADVANCED_NEGATIVE_NO_ADS
        }
    }

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

    private fun `Given view will return with ads ab test`() {
        every {
            productListView.abTestRemoteConfig?.getString(
                RollenceKey.SEARCH_ADVANCED_KEYWORD_ADV_NEG,
                any()
            )
        } answers {
            ""
        }
    }

    private fun `Then verify that ads is loaded`() {
        Assert.assertTrue(visitableList.filterIsInstance<ProductItemDataView>().any { it.isTopAds })
    }



    @Test
    fun `Product list with negative keywords ab test return no ads will show no ads`() {
        val searchProductModel = searchProductNegativeKeywordsResponseJSON.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mapOf<String, Any>(
            SearchApiConst.Q to "tv -samsung -lg -realme -android -smart",
        )

        `Given view will return no ads ab test`()
        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When Load Data`(searchParameter)

        `Then verify that ads is not loaded`()
    }

    @Test
    fun `Product list with negative keywords ab test return show ads will show ads`() {
        val searchProductModel =
            searchProductNegativeKeywordsResponseJSON.jsonToObject<SearchProductModel>()
        val searchParameter: Map<String, Any> = mapOf<String, Any>(
            SearchApiConst.Q to "tv -samsung -lg -realme -android -smart",
        )

        `Given view will return with ads ab test`()
        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When Load Data`(searchParameter)

        `Then verify that ads is loaded`()
    }

    @Test
    fun `Product list with normal keywords ab test return no ads will show ads`() {
        val searchProductModel = searchProductWithTopAdsAndHeadlineAdsJSON.jsonToObject<SearchProductModel>()
        val searchParameter : Map<String, Any> = mapOf<String, Any>(
            SearchApiConst.Q to "samsung",
        )

        `Given view will return no ads ab test`()
        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When Load Data`(searchParameter)

        `Then verify that ads is loaded`()
    }
}