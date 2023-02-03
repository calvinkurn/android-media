package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.GetProductAutoMigratedStatusResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductAutoMigratedStatusUseCase @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<GetProductAutoMigratedStatusResponse>(repository) {

    companion object {
        const val PARAM_PRODUCT_ID = "product_id"
        private val query =
            """
                query isProductAutomigrated(${'$'}product_id: String) {
                    isProductAutomigrated(product_id: ${'$'}product_id) {
                      productMigrateStatus{
                       is_automigrated
                     }
                  }
                }
            """.trimIndent()
    }

    private val requestParams = RequestParams.create()

    init {
        setGraphqlQuery(query)
        setTypeClass(GetProductAutoMigratedStatusResponse::class.java)
    }

    fun setRequestParam(productId: String) {
        requestParams.putObject(PARAM_PRODUCT_ID, productId)
        setRequestParams(requestParams.parameters)
    }
}
