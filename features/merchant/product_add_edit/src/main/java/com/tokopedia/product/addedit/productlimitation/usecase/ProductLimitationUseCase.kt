package com.tokopedia.product.addedit.productlimitation.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.productlimitation.model.ProductAddRuleResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ProductLimitationUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<ProductAddRuleResponse>(repository) {

    companion object {
        private val query =
                """
                {
                  ProductAddRule {
                    header {
                      reason
                      messages
                      errorCode
                    }
                    data{
                      eligible{
                        value
                        totalProduct
                        limit
                        isUnlimited
                        actionItems
                      }
                    }
                  }
                }
                """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductAddRuleResponse::class.java)
        setRequestParams(RequestParams.create().parameters)
    }
}