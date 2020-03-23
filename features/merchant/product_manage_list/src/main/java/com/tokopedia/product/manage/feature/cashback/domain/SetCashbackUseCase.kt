package com.tokopedia.product.manage.feature.cashback.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SetCashbackUseCase @Inject constructor(
        repository: GraphqlRepository) : GraphqlUseCase<SetCashbackResponse>(repository) {

    companion object {
        const val CASHBACK_SUCCESS_CODE = "200"
        const val CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE = "422"
        private const val PARAM_PRODUCT_ID = "ProductID"
        private const val PARAM_CASHBACK = "Cashback"
        private const val PARAM_IS_BY_SYSTEM = "IsBySystem"
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

    init {
        setGraphqlQuery(query)
        setTypeClass(SetCashbackResponse::class.java)
    }

    fun setParams(productId: Int, cashBack: Int, isBySystem: Boolean) {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_PRODUCT_ID, productId)
        requestParams.putInt(PARAM_CASHBACK, cashBack)
        requestParams.putBoolean(PARAM_IS_BY_SYSTEM, isBySystem)
        setRequestParams(requestParams.parameters)
    }
}