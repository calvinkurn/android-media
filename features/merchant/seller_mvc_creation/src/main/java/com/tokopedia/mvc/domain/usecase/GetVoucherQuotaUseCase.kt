package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.mapper.GetVoucherQuotaMapper
import com.tokopedia.mvc.data.response.GetVoucherQuotaResponse
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetVoucherQuotaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetVoucherQuotaMapper,
    private val userSession: UserSessionInterface
): GraphqlUseCase<VoucherCreationQuota>(repository) {

    companion object {
        private const val SHOP_ID_KEY = "shopId"
        private const val OPERATION_NAME = "MerchantPromotionGetQuotaUsage"
        private const val QUERY = """
            query MerchantPromotionGetQuotaUsage(${'$'}shopId: Int!) {
              MerchantPromotionGetQuotaUsage(shop_id: ${'$'}shopId) {
                header {
                  process_time
                  message
                  reason
                  error_code
                }
                data {
                  quota {
                    used
                    remaining
                    total
                  }
                  status_source
                  sources {
                    name
                    used
                    remaining
                    total
                    expired
                  }
                  ticker {
                    title
                  }
                  cta_text
                  cta_link
                }
              }
            }
        """
    }

    private val query = object: GqlQueryInterface {
        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY.trimIndent()
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(): VoucherCreationQuota {
        val shopId = userSession.shopId.toLongOrZero()
        val request = buildRequest(shopId)
        val response = repository.response(listOf(request))
        val errors = response.getError(GetVoucherQuotaResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getSuccessData<GetVoucherQuotaResponse>()
            return mapper.mapRemoteModelToUiModel(data.merchantPromotionGetQuotaUsage)
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }

    private fun buildRequest(param: Long): GraphqlRequest {
        return GraphqlRequest(
            query,
            GetVoucherQuotaResponse::class.java,
            mapOf(SHOP_ID_KEY to param)
        )
    }
}
