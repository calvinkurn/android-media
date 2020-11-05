package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.list.domain.mapper.BulkAcceptOrderStatusMapper
import com.tokopedia.sellerorder.list.domain.model.SomListBulkGetBulkAcceptOrderStatusParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetBulkAcceptOrderStatusResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderStatusUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListGetBulkAcceptOrderStatusUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<SomListGetBulkAcceptOrderStatusResponse.Data>,
        private val mapper: BulkAcceptOrderStatusMapper
) : BaseGraphqlUseCase() {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomListGetBulkAcceptOrderStatusResponse.Data::class.java)
    }

    suspend fun execute(): SomListBulkAcceptOrderStatusUiModel {
        useCase.setRequestParams(params.parameters)
        return mapper.mapResponseToUiModel(useCase.executeOnBackground().getMultiAcceptOrderStatus)
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