package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.mapper.BulkConfirmShippingMapper
import com.tokopedia.sellerorder.list.domain.model.SomListBulkConfirmShippingParam
import com.tokopedia.sellerorder.list.domain.model.SomListBulkConfirmShippingResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkConfirmShippingUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListBulkConfirmShippingUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: BulkConfirmShippingMapper
) : BaseGraphqlUseCase<SomListBulkConfirmShippingUiModel>(gqlRepository) {


    override suspend fun executeOnBackground(): SomListBulkConfirmShippingUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): SomListBulkConfirmShippingUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(CONFIRM_SHIPPING_BULK_QUERY, SomListBulkConfirmShippingResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListBulkConfirmShippingResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListBulkConfirmShippingResponse.Data>()
            return mapper.mapResponseToUiModel(response.mpLogisticBulkConfirmShipping)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(orderIds: List<String>, shippingRefList: List<String>) {
        params = RequestParams.create().apply {
            putObject(SomConsts.PARAM_INPUT, SomListBulkConfirmShippingParam(
                    data = orderIds.mapIndexed { index, orderId ->
                        SomListBulkConfirmShippingParam.BulkConfirmShippingInputData(
                                orderId = orderId,
                                shippingRef = shippingRefList.getOrNull(index).orEmpty()
                        )
                    }
            ))
        }
    }

    companion object {
        private val CONFIRM_SHIPPING_BULK_QUERY = """
              mutation MultiConfirmShipping(${'$'}input: MPLogisticBulkConfirmShippingInputs!) {
                 mpLogisticBulkConfirmShipping(input: ${'$'}input){
                    status
                    message
                    job_id
                    total_on_process
                    errors {
                      order_id
                      message
                    }
                  }
                }
        """.trimIndent()
    }
}