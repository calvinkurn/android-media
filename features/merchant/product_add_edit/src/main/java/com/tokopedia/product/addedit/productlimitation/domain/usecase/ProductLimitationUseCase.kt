package com.tokopedia.product.addedit.productlimitation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductAddRuleResponse
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
                        isSuccess
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

    override suspend fun executeOnBackground(): ProductAddRuleResponse {
        val response = super.executeOnBackground()
        val messages = response.productAddRule.header.messages
        val isSuccess = response.productAddRule.data.eligible?.isSuccess ?: true
        if (messages.isEmpty()) {
            if (!isSuccess) {
                throw MessageErrorException(ErrorNetMessage.MESSAGE_ERROR_SERVER)
            }
            return response
        } else {
            throw MessageErrorException(messages.joinToString("\n"))
        }
    }
}