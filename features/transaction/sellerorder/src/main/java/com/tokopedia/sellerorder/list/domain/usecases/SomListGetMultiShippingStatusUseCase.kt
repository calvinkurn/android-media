package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.mapper.MultiShippingStatusMapper
import com.tokopedia.sellerorder.list.domain.model.SomListGetMultiShippingParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetMultiShippingResponse
import com.tokopedia.sellerorder.list.presentation.models.MultiShippingStatusUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListGetMultiShippingStatusUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: MultiShippingStatusMapper
) : BaseGraphqlUseCase<MultiShippingStatusUiModel>(gqlRepository) {


    override suspend fun executeOnBackground(): MultiShippingStatusUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): MultiShippingStatusUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(MULTI_SHIPPING_STATUS_QUERY, SomListGetMultiShippingResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListGetMultiShippingResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListGetMultiShippingResponse.Data.MpLogisticMultiShippingStatus>()
            return mapper.mapResponseToUiModel(response)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(batchId: String) {
        params = RequestParams.create().apply {
            putObject(SomConsts.PARAM_INPUT, SomListGetMultiShippingParam(batchId))
        }
    }

    companion object {
        private val MULTI_SHIPPING_STATUS_QUERY = """
                mutation MultiShippingStatus(${'$'}input: MPLogisticMultiShippingStatusInput!){
                  mpLogisticMultiShippingStatus(input: ${'$'}input){
                    total_order
                    processed
                    success
                    fail
                    list_fail
                    list_error{
                      order_id
                      message
                    }
                  }
                }
        """.trimIndent()
    }
}