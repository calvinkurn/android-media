package com.tokopedia.search.result.domain.usecase.getlocalsearchrecommendation

import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.usecase.searchproduct.addAceSearchProductRequest
import com.tokopedia.search.result.domain.usecase.searchproduct.graphqlRequests
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1

class GetLocalSearchRecommendationUseCase(
    private val graphqlUseCase: GraphqlUseCase,
    private val searchProductModelMapper: Func1<GraphqlResponse?, SearchProductModel?>,
    private val reimagineRollence: ReimagineRollence,
): UseCase<SearchProductModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchProductModel> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(
            graphqlRequests {
                addAceSearchProductRequest(
                    reimagineRollence,
                    UrlParamUtils.generateUrlParamString(requestParams.parameters)
                )
            }
        )

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper)
    }
}
