package com.tokopedia.topads.common.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.interceptor.TopAdsResponseError
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class TopAdsGetShopDepositGraphQLUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase): UseCase<DataDeposit>() {
    override fun createObservable(requestParams: RequestParams): Observable<DataDeposit> {
        val query = requestParams.getString(PARAM_QUERY, "")
        requestParams.clearValue(PARAM_QUERY)

        val graphqlRequest = GraphqlRequest(query, DataDeposit.Response::class.java, requestParams.parameters, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).flatMap { graphqlResponse ->
            val response = graphqlResponse.getData<DataDeposit.Response>(DataDeposit.Response::class.java).dataResponse
            if (response.errors.isNotEmpty()){
                Observable.error(TopAdsResponseError().apply { errors = response.errors }.createException())
            } else {
                Observable.just(response.dataDeposit)
            }
        }
    }

    companion object {
        private val PARAM_QUERY = "query"

        @JvmStatic
        fun createRequestParams(query: String, shopId: String)= RequestParams.create().apply {
                putInt(TopAdsCommonConstant.PARAM_SHOP_ID, shopId.toInt())
                putString(PARAM_QUERY, query)
            }

        @JvmStatic
        fun createGraphqlRequest(query: String, requestParams: RequestParams): GraphqlRequest {
            return GraphqlRequest(query, DataDeposit.Response::class.java, requestParams.parameters, false)
        }
    }
}