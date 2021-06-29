package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.mapper.BulkRequestPickupMapper
import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderParam
import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderResponse
import com.tokopedia.sellerorder.list.domain.model.SomListBulkRequestPickupParam
import com.tokopedia.sellerorder.list.domain.model.SomListBulkRequestPickupResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkRequestPickupUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListBulkRequestPickupUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: BulkRequestPickupMapper
) : BaseGraphqlUseCase<SomListBulkRequestPickupUiModel>(gqlRepository) {


    override suspend fun executeOnBackground(): SomListBulkRequestPickupUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): SomListBulkRequestPickupUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(REQUEST_PICK_UP_BULK_QUERY, SomListBulkRequestPickupResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListBulkRequestPickupResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListBulkRequestPickupResponse.Data>()
            return mapper.mapResponseToUiModel(response.mpLogisticBulkRequestPickup)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParams(orderIds: List<String>) {
        params = RequestParams.create().apply {
            putObject(SomConsts.PARAM_INPUT, SomListBulkRequestPickupParam(
                    data = orderIds.map {
                        SomListBulkRequestPickupParam.MPLogisticBulkRequestPickupInputData(
                                orderId = it
                        )
                    }
            ))
        }
    }

    companion object {
        private val REQUEST_PICK_UP_BULK_QUERY = """
              mutation MultiRequestPickup(${'$'}input: MPLogisticBulkRequestPickupInputs!) {
                 mpLogisticBulkRequestPickup(input: ${'$'}input){
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