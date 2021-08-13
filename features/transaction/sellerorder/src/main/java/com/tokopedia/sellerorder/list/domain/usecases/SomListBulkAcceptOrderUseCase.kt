package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.list.domain.mapper.BulkAcceptOrderMapper
import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderParam
import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListBulkAcceptOrderUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: BulkAcceptOrderMapper
) : BaseGraphqlUseCase<SomListBulkAcceptOrderUiModel>(gqlRepository) {

    override suspend fun executeOnBackground(): SomListBulkAcceptOrderUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): SomListBulkAcceptOrderUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListBulkAcceptOrderResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListBulkAcceptOrderResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListBulkAcceptOrderResponse.Data>()
            return mapper.mapResponseToUiModel(response)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(orderIds: List<String>, userId: String) {
        params = RequestParams.create().apply {
            putObject(PARAM_INPUT, SomListBulkAcceptOrderParam(
                    orderIds = orderIds.joinToString(","),
                    userId = userId
            ))
        }
    }

    companion object {
        private val QUERY = """
            mutation MultiAcceptOrder(${'$'}input:MultiAcceptOrderRequest!) {
                multi_accept_order(input:${'$'}input){
                    data {
                      message,
                      batch_id,
                      total_order,
                      redis_key {
                        progress,
                        success,
                        fail,
                        purchase_protection_imei_invalid,
                        purchase_protection_imei_invalid_product,
                        multi_origin_invalid,
                        multi_origin_invalid_order
                      }
                    },
                    errors {
                      code,
                      status,
                      title,
                      detail
                    }
                }
            }
        """.trimIndent()
    }
}