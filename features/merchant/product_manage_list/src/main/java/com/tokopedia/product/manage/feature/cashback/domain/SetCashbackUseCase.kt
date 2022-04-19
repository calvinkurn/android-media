package com.tokopedia.product.manage.feature.cashback.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SetCashbackUseCase @Inject constructor(
        repository: GraphqlRepository) : GraphqlUseCase<SetCashbackResponse>(repository) {

    companion object {
        const val CASHBACK_SUCCESS_CODE = "200"
        const val CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE = "422"
        private const val PARAM_PRODUCT_ID = "ProductIDStr"
        private const val PARAM_CASHBACK = "Cashback"
        private const val PARAM_IS_BY_SYSTEM = "IsBySystem"
        private val query =
            """
            mutation GoldSetProductCashback(${'$'}ProductIDStr: String, ${'$'}Cashback : Int!, ${'$'}IsBySystem : Boolean!){
                GoldSetProductCashback(ProductIDStr: ${'$'}ProductIDStr, Cashback: ${'$'}Cashback, IsBySystem: ${'$'}IsBySystem){
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

    fun setParams(productId: String, cashBack: Int, isBySystem: Boolean) {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_PRODUCT_ID, productId)
        requestParams.putInt(PARAM_CASHBACK, cashBack)
        requestParams.putBoolean(PARAM_IS_BY_SYSTEM, isBySystem)
        setRequestParams(requestParams.parameters)
    }
}