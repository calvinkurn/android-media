package com.tokopedia.search.result.presentation.presenter.product.negativekeywordsabtest

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.domain.model.SearchProductModel
import org.junit.Before
import org.junit.Test

internal class SearchProductNegativeKeywordsABTestNoAdsTest : NegativeKeywordsAbTestTestFixtures() {

    @Before
    override fun setUp() {
        `Given view will return no ads ab test`()
        super.setUp()
    }

    @Test
    fun `Product list with negative keywords ab test return no ads will show no ads`() {
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