package com.tokopedia.sellerorder.list.domain.list

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class SomGetOrderListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomListOrder.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(param: SomListOrderParam): Result<SomListOrder.Data.OrderList> {
        useCase.setTypeClass(SomListOrder.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return try {
            val orderList = useCase.executeOnBackground().orderList
            Success(orderList)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(param: SomListOrderParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
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
                  order_status_id
                  order_resi
                  order_date
                  order_label {
                    flag_name
                    flag_color
                    flag_background
                  }
                  buyer_name
                  deadline_text
                  deadline_color
                  order_product {
                    picture
                    product_name
                  }
                  cancel_request
                  cancel_request_note
                  cancel_request_time
                  cancel_request_origin_note
                  ticker_info {
                    text
                    type
                    action_text
                    action_key
                    action_url
                  }
                }
              }
            }
        """.trimIndent()
    }
}