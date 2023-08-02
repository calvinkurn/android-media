package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.list.domain.mapper.OrderListMapper
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.model.SomListOrderListResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomListGetOrderListUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: OrderListMapper
) {

    suspend fun executeOnBackground(params: RequestParams): Triple<String, List<SomListOrderUiModel>, SomListEmptyStateUiModel?> {
        val gqlRequest = GraphqlRequest(
            QUERY,
            SomListOrderListResponse.Data::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))
        val searchKeyword = getSearchKeyword(params)

        val errors = gqlResponse.getError(SomListOrderListResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response =
                gqlResponse.getData<SomListOrderListResponse.Data>(SomListOrderListResponse.Data::class.java)
            return Triple(
                response.orderList.cursorOrderId,
                mapper.mapResponseToUiModel(
                    response.orderList.list,
                    searchKeyword
                ),
                mapper.mapToEmptyState(response.orderList.emptyState)
            )
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    fun composeParams(param: SomListGetOrderListParam): RequestParams =
        RequestParams.create().apply {
            putObject(PARAM_INPUT, param)
        }

    private fun getSearchKeyword(params: RequestParams): String {
        params.parameters[PARAM_INPUT]?.let { input ->
            return if (input is SomListGetOrderListParam) {
                input.search
            } else {
                String.EMPTY
            }
        }
        return String.EMPTY
    }

    companion object {
        val QUERY = """
            query GetOrderList(${'$'}input: OrderListArgs!) {
              orderList(input: ${'$'}input) {
                cursor_order_id
                empty_state {
                  title
                  subtitle
                  image_url
                  cta {
                    cta_text
                    cta_action_type
                    cta_action_value
                  }
                }
                list {
                  order_id
                  status
                  status_color
                  status_indicator_color
                  order_status_id
                  order_resi
                  deadline_text
                  deadline_color
                  deadline_style
                  cancel_request
                  cancel_request_note
                  cancel_request_time
                  cancel_request_origin_note
                  cancel_request_status
                  destination_province
                  courier_name
                  courier_product_name
                  preorder_type
                  buyer_name
                  order_product {
                    product_id
                    product_name
                    picture
                    product_qty
                  }
                  ticker {
                    text
                    type
                    action_text
                    action_key
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
                  have_product_bundle
                  bundle_detail {
                    total_product
                    bundle {
                      bundle_id
                      order_detail {
                        product_id
                        product_name
                        picture
                        product_qty
                      }
                    }
                    non_bundle {
                      product_id
                      product_name
                      picture
                      product_qty
                    }
                  }
                  plus_data {
                    logo_url
                  }
                }
              }
            }
        """.trimIndent()
    }
}
