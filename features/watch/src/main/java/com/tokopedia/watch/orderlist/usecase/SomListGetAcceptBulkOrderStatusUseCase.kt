package com.tokopedia.watch.orderlist.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.watch.orderlist.mapper.AcceptBulkOrderStatusMapper
import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderStatusUiModel
import com.tokopedia.watch.orderlist.model.SomListGetAcceptBulkOrderStatusResponse
import com.tokopedia.watch.orderlist.param.SomListGetAcceptBulkOrderStatusParam
import javax.inject.Inject

class SomListGetAcceptBulkOrderStatusUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: AcceptBulkOrderStatusMapper
) : BaseGraphqlUseCase<SomListAcceptBulkOrderStatusUiModel>(gqlRepository) {

    companion object {
        const val PARAM_INPUT = "input"
        private val QUERY = """
            query GetMultiAcceptOrderStatus(${'$'}input: MultiAcceptOrderStatusRequest!) {
              get_multi_accept_order_status(input: ${'$'}input) {
                data {
                  total_order
                  success
                  fail
                  multi_origin_invalid_order {
                    order_id
                    invoice_ref_num
                  }
                }
                errors {
                  code
                  status
                  title
                  detail
                }
              }
            }
        """.trimIndent()
    }

    override suspend fun executeOnBackground(): SomListAcceptBulkOrderStatusUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): SomListAcceptBulkOrderStatusUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListGetAcceptBulkOrderStatusResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListGetAcceptBulkOrderStatusResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListGetAcceptBulkOrderStatusResponse.Data>()
            return mapper.mapResponseToUiModel(response.getMultiAcceptOrderStatus)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(param: SomListGetAcceptBulkOrderStatusParam) {
        params = RequestParams.create().apply {
            putObject(PARAM_INPUT, param)
        }
    }
}