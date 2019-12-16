package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.TradeinParams
import com.tokopedia.product.detail.data.model.TradeinResponse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetTradeinInfoUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<TradeinResponse>() {

    companion object {
        fun createParams(tradeinParams: TradeinParams): RequestParams =
                RequestParams.create().apply {
                    putObject(ProductDetailCommonConstant.PARAMS, tradeinParams)
                }
    }

    var params: RequestParams = RequestParams.EMPTY
    val request by lazy {
        GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_TRADE_IN], TradeinResponse::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): TradeinResponse {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequests(listOf(request))

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(TradeinResponse::class.java) ?: listOf()
        val data = gqlResponse.getData<TradeinResponse>(TradeinResponse::class.java)

        if (data == null || error.isNotEmpty()) {
            return TradeinResponse()
        }

        return data
    }

}