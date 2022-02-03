package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.HeadlineAds.HEADLINE_ITEM_VALUE_LOAD_MORE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.TopAdsConstants.SEEN_ADS
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1

class SearchProductLoadMoreGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
): UseCase<SearchProductModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val searchProductParams = requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String?, Any?>
        val params = UrlParamUtils.generateUrlParamString(searchProductParams)
        val headlineAdsParams = com.tokopedia.topads.sdk.utils.TopAdsHeadlineViewParams.createHeadlineParams(
                requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, Any?>,
                HEADLINE_ITEM_VALUE_LOAD_MORE,
                requestParams.parameters[SEEN_ADS] as String)

        val graphqlRequestList = graphqlRequests {
            addAceSearchProductRequest(params)
            addProductAdsRequest(requestParams, params)
            addHeadlineAdsRequest(requestParams, headlineAdsParams)
        }

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(graphqlRequestList)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)
    }
}