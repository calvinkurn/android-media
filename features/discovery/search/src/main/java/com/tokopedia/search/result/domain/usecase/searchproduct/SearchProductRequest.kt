package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.domain.model.AceSearchProductModel
import com.tokopedia.search.result.domain.model.AceSearchProductModelV5
import com.tokopedia.search.result.domain.model.HeadlineAdsModel
import com.tokopedia.search.result.domain.model.ProductTopAdsModel
import com.tokopedia.search.result.domain.usecase.searchproduct.query.AceSearchProduct
import com.tokopedia.search.result.domain.usecase.searchproduct.query.AceSearchProductV5
import com.tokopedia.search.result.domain.usecase.searchproduct.query.HeadlineAdsProduct
import com.tokopedia.search.result.domain.usecase.searchproduct.query.TopAdsProduct
import com.tokopedia.usecase.RequestParams

internal fun graphqlRequests(request: MutableList<GraphqlRequest>.() -> Unit) =
        mutableListOf<GraphqlRequest>().apply {
                request()
        }

internal fun MutableList<GraphqlRequest>.addAceSearchProductRequest(
    reimagineRollence: ReimagineRollence,
    params: String,
) {
    val request = if (reimagineRollence.search3ProductCard().isUseAceSearchProductV5())
        createAceSearchProductV5Request(params)
    else
        createAceSearchProductRequest(params)

    add(request)
}

internal fun createAceSearchProductRequest(params: String) =
        GraphqlRequest(
                AceSearchProduct.GQL_QUERY,
                AceSearchProductModel::class.java,
                mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )

internal fun createAceSearchProductV5Request(params: String) =
    GraphqlRequest(
        AceSearchProductV5(),
        AceSearchProductModelV5::class.java,
        mapOf(SearchConstant.GQL.KEY_PARAMS to params)
    )

internal fun MutableList<GraphqlRequest>.addProductAdsRequest(requestParams: RequestParams, params: String) {
        if (!requestParams.isSkipProductAds()) {
                add(createTopAdsProductRequest(params = params))
        }
}

internal fun createTopAdsProductRequest(params: String) =
        GraphqlRequest(
                TopAdsProduct.GQL_QUERY,
                ProductTopAdsModel::class.java,
                mapOf(SearchConstant.GQL.KEY_PARAMS to params)
        )

internal fun MutableList<GraphqlRequest>.addHeadlineAdsRequest(
    requestParams: RequestParams,
    headlineParams: String,
) {
    if (!requestParams.isSkipHeadlineAds()) {
        add(createHeadlineAdsRequest(headlineParams = headlineParams))
    }
}

internal fun createHeadlineAdsRequest(headlineParams: String) =
    GraphqlRequest(
        HeadlineAdsProduct.GQL_QUERY,
        HeadlineAdsModel::class.java,
        mapOf(SearchConstant.GQL.KEY_HEADLINE_PARAMS to headlineParams)
    )
