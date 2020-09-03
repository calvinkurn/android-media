package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1

class SearchProductLoadMoreGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>
): UseCase<SearchProductModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        val params = UrlParamUtils.generateUrlParamString(requestParams.parameters)

        val graphqlRequestList = listOf(
                createAceSearchProductRequest(params = params),
                createTopAdsProductRequest(params = params)
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(graphqlRequestList)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)
    }
}