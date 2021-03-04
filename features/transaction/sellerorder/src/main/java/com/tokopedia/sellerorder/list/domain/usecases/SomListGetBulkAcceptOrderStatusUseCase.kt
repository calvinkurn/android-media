package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.list.domain.mapper.BulkAcceptOrderStatusMapper
import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderResponse
import com.tokopedia.sellerorder.list.domain.model.SomListBulkGetBulkAcceptOrderStatusParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetBulkAcceptOrderStatusResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderStatusUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListGetBulkAcceptOrderStatusUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: BulkAcceptOrderStatusMapper
) : BaseGraphqlUseCase<SomListBulkAcceptOrderStatusUiModel>(gqlRepository) {

    override suspend fun executeOnBackground(): SomListBulkAcceptOrderStatusUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): SomListBulkAcceptOrderStatusUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListGetBulkAcceptOrderStatusResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListGetBulkAcceptOrderStatusResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListGetBulkAcceptOrderStatusResponse.Data>()
            return mapper.mapResponseToUiModel(response.getMultiAcceptOrderStatus)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(param: SomListBulkGetBulkAcceptOrderStatusParam) {
        params = RequestParams.create().apply {
            putObject(PARAM_INPUT, param)
        }
    }

    companion object {
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
}