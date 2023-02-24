package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.GetMerchantPromoListResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetMerchantPromoListUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<GetMerchantPromoListResponse>(repository) {

    companion object {
        private const val SHOP_ID_PARAM = "shopId"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "GetMerchantPromotionList"
        private val QUERY = """
            query GetMerchantPromotionList(${'$'}shopId: Int!) {
                  merchantPromotionGetPromoList(shop_id: ${'$'}shopId, tab_id: 0) {
                    header {
                      process_time
                      message
                      reason
                      error_code
                    }
                    data {
                      pages {
                        page_id
                        page_name
                        cta_link
                      }
                    }
                  }
            }
        """

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY.trimIndent()
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(shopId: String): GetMerchantPromoListResponse {
        val request = buildRequest(shopId)
        val response = repository.response(listOf(request))
        val errors = response.getError(GetMerchantPromoListResponse::class.java)
        if (errors.isNullOrEmpty()) {
            return response.getSuccessData()
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }

    private fun buildRequest(shopId: String): GraphqlRequest {
        return GraphqlRequest(
            query,
            GetMerchantPromoListResponse::class.java,
            mapOf(SHOP_ID_PARAM to shopId.toLongOrZero())
        )
    }
}
