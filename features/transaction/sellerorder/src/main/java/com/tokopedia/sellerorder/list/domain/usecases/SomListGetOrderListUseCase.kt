package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.list.domain.mapper.OrderListMapper
import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.model.SomListOrderListResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListGetOrderListUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: OrderListMapper
) : BaseGraphqlUseCase<Pair<Int, List<SomListOrderUiModel>>>(gqlRepository) {

    private fun getSearchKeyword(): String {
        params.parameters[PARAM_INPUT]?.let { input ->
            return if (input is SomListGetOrderListParam) {
                input.search
            } else {
                ""
            }
        }
        return ""
    }

    override suspend fun executeOnBackground(): Pair<Int, List<SomListOrderUiModel>> {
        val gqlRequest = GraphqlRequest(QUERY, SomListOrderListResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val searchKeyword = getSearchKeyword()

        val errors = gqlResponse.getError(SomListOrderListResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListOrderListResponse.Data>()
            return response.orderList.cursorOrderId to mapper.mapResponseToUiModel(response.orderList.list, searchKeyword)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun setParam(param: SomListGetOrderListParam) {
        params = RequestParams.create().apply {
            putObject(PARAM_INPUT, param)
        }
    }

    companion object {
        val QUERY = """
            query OrderList(${'$'}input: OrderListArgs!) {
              orderList(input: ${'$'}input) {
                cursor_order_id
                list {
                  order_id
                  status
                  status_color
                  status_indicator_color
                  order_status_id
                  order_resi
                  deadline_text
                  deadline_color
                  cancel_request
                  cancel_request_note
                  cancel_request_time
                  cancel_request_origin_note
                  destination_province
                  courier_name
                  courier_product_name
                  preorder_type
                  buyer_name
                  order_product {
                    product_id
                    product_name
                    picture
                  }
                  ticker_info {
                    text
                    type
                    action_text
                    action_key
                    action_url
                  }
                  button {
                    key
                    display_name
                    type
                    url
                    popup {
                      title
                      body
                      actionButton {
                        key
                        displayName
                        color
                        type
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }
}