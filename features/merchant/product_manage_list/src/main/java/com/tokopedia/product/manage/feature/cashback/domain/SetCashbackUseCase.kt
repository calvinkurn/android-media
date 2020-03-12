package com.tokopedia.product.manage.feature.cashback.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SetCashbackUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase) : UseCase<SetCashbackResponse>() {

    companion object {
        const val CASHBACK_SUCCESS_CODE = "200"
        const val CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE = "422"
        const val PARAM_PRODUCT_ID = "ProductID"
        const val PARAM_CASHBACK = "Cashback"
        const val PARAM_IS_BY_SYSTEM = "IsBySystem"

        @JvmStatic
        fun createRequestParams(productId: Int, cashBack: Int, isBySystem: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(PARAM_PRODUCT_ID, productId)
            requestParams.putInt(PARAM_CASHBACK, cashBack)
            requestParams.putBoolean(PARAM_IS_BY_SYSTEM, isBySystem)
            return requestParams
        }

        private val query =
            """
            mutation GoldSetProductCashback(${'$'}ProductID : Int!, ${'$'}Cashback : Int!, ${'$'}IsBySystem : Boolean!){
                GoldSetProductCashback(ProductID: ${'$'}ProductID, Cashback: ${'$'}Cashback, IsBySystem: ${'$'}IsBySystem){
                    header {
                        process_time
                        message
                        reason
                        error_code
                    }
                }
            }
            """.trimIndent()


    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SetCashbackResponse {
        val gqlRequest = GraphqlRequest(query, SetCashbackResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<SetCashbackResponse>(SetCashbackResponse::class.java)
        }
    }
}