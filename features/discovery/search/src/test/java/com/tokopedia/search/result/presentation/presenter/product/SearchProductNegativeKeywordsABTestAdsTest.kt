package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.every
import io.mockk.slot
import org.junit.Assert.assertTrue
import org.junit.Test
import rx.Subscriber

internal class SearchProductNegativeKeywordsABTestAdsTest : ProductListPresenterTestFixtures() {

    private val searchProductNegativeKeywordsResponseJSON = "searchproduct/negativekeywords/with-negative-keywords.json"

    private val tdn1 = TopAdsImageViewModel(position = 1, bannerName = "Position 1")
    private val tdn4 = TopAdsImageViewModel(position = 4, bannerName = "Position 4")
    private val tdn8 = TopAdsImageViewModel(position = 8, bannerName = "Position 8")

    private val tdnList = listOf(tdn1, tdn4, tdn8)

    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList = mutableListOf<Visitable<*>>()

    @Test
    fun `Test product list with negative keywords ab test will show no ads`() {
        val searchProductModel = searchProductNegativeKeywordsResponseJSON.jsonToObject<SearchProductModel>()
        searchProductModel.setTopAdsImageViewModelList(tdnList)
        val searchParameter : Map<String, Any> = mapOf<String, Any>(
            SearchApiConst.Q to "tv -samsung -lg -realme -android -smart",
        )

        `Given view will return no ads ab test`()
        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When Load Data`(searchParameter)

        `Then verify that ads is not loaded`()
    }

    private fun `Given view will return no ads ab test`() {
        every {
            productListView.abTestRemoteConfig?.getString(
                SearchConstant.ABTestRemoteConfigKey.AB_TEST_KEYWORD_ADV_NEG,
                any()
            )
        } answers{
            SearchConstant.ABTestRemoteConfigKey.AB_TEST_NEGATIVE_NO_ADS
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
        } answers {
            visitableList.clear()
            visitableList.addAll(visitableListSlot.captured)
        }

        every {
            productListView.addProductList(capture(visitableListSlot))
        } answers {
            visitableList.addAll(visitableListSlot.captured)
        }
    }

    private fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify that ads is not loaded`() {
        assertTrue(visitableList.filterIsInstance<SearchProductTopAdsImageDataView>().isEmpty())
    }

    @Test
    fun `Test product list with negative keywords ab test will show ads`() {
        val searchProductModel = searchProductNegativeKeywordsResponseJSON.jsonToObject<SearchProductModel>()
        searchProductModel.setTopAdsImageViewModelList(tdnList)
        val searchParameter : Map<String, Any> = mapOf<String, Any>(
            SearchApiConst.Q to "tv -samsung -lg -realme -android -smart",
        )

        `Given view will return with ads ab test`()
        `Given search product API will success`(searchProductModel)
        `Given view will set and add product list`()

        `When Load Data`(searchParameter)

        `Then verify that ads is loaded`()
    }

    private fun `Given view will return with ads ab test`() {
        every {
            productListView.abTestRemoteConfig?.getString(
                SearchConstant.ABTestRemoteConfigKey.AB_TEST_KEYWORD_ADV_NEG,
                any()
            )
        } answers{
            ""
        }
    }

    private fun `Then verify that ads is loaded`() {
        assertTrue(visitableList.filterIsInstance<SearchProductTopAdsImageDataView>().isNotEmpty())
    }
}