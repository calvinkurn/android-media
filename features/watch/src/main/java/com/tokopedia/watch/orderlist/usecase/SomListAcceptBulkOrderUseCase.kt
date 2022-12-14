package com.tokopedia.watch.orderlist.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.watch.orderlist.mapper.AcceptBulkOrderMapper
import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderResponse
import com.tokopedia.watch.orderlist.model.AcceptBulkOrderModel
import com.tokopedia.watch.orderlist.param.SomListAcceptBulkOrderParam
import javax.inject.Inject

class SomListAcceptBulkOrderUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: AcceptBulkOrderMapper
) : BaseGraphqlUseCase<AcceptBulkOrderModel>(gqlRepository) {

    companion object {
        const val PARAM_INPUT = "input"
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

    override suspend fun executeOnBackground(): AcceptBulkOrderModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): AcceptBulkOrderModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListAcceptBulkOrderResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListAcceptBulkOrderResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListAcceptBulkOrderResponse.Data>()
            return mapper.mapResponseToUiModel(response)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(orderIds: List<String>, userId: String) {
        params = RequestParams.create().apply {
            putObject(PARAM_INPUT, SomListAcceptBulkOrderParam(
                    orderIds = orderIds.joinToString(","),
                    userId = userId
            ))
        }
    }
}