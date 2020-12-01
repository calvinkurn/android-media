package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.list.domain.mapper.BulkAcceptOrderMapper
import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderParam
import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListBulkAcceptOrderUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<SomListBulkAcceptOrderResponse.Data>,
        private val mapper: BulkAcceptOrderMapper
) : BaseGraphqlUseCase() {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomListBulkAcceptOrderResponse.Data::class.java)
    }

    suspend fun execute(): SomListBulkAcceptOrderUiModel {
        useCase.setRequestParams(params.parameters)
        return mapper.mapResponseToUiModel(useCase.executeOnBackground())
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