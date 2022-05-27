package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.ProductTopAdsModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SearchProductTopAdsUseCase(
    private val graphqlUseCase: GraphqlUseCase,
): UseCase<TopAdsModel>() {
    override fun createObservable(
        requestParams: RequestParams
    ): Observable<TopAdsModel> {
        val searchProductParams = requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String?, Any?>

        val params = UrlParamUtils.generateUrlParamString(searchProductParams)

        val graphqlRequestList = graphqlRequests {
            addProductAdsRequest(requestParams, params)
        }

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(graphqlRequestList)

        return graphqlUseCase
            .createObservable(RequestParams.EMPTY)
            .map {
                it.getTopAdsModel()
            }
    }

    private fun GraphqlResponse.getTopAdsModel(): TopAdsModel =
        getData<ProductTopAdsModel>(ProductTopAdsModel::class.java)
            ?.topAdsModel
            ?: TopAdsModel()
}