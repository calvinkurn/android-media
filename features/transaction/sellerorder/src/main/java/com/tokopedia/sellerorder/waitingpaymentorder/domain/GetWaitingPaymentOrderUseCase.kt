package com.tokopedia.sellerorder.waitingpaymentorder.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.waitingpaymentorder.domain.mapper.WaitingPaymentOrderResultMapper
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderResponse
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.Paging
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import javax.inject.Inject

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class GetWaitingPaymentOrderUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<WaitingPaymentOrderResponse.Data>,
        private val mapper: WaitingPaymentOrderResultMapper
) {
    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(
        param: WaitingPaymentOrderRequestParam
    ): Pair<Paging, List<WaitingPaymentOrderUiModel>> {
        useCase.setTypeClass(WaitingPaymentOrderResponse.Data::class.java)
        useCase.setRequestParams(generateParam(param))

        return mapper.mapDomainToModelData(useCase.executeOnBackground().waitingPaymentOrder)
    }

    private fun generateParam(param: WaitingPaymentOrderRequestParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }

    companion object {
        private val QUERY = """
            query OrderListWaitingPayment(${'$'}input:OrderListWaitingPaymentRequest!) {
              orderListWaitingPayment(input:${'$'}input) {
                list {
                  order_id
                  buyer_name
                  payment_deadline
                  have_product_bundle
                  products {
                    product_id
                    product_name
                    product_picture
                    product_qty
                    product_price
                  }
                  bundle_detail {
                    product_bundling_icon
                    bundle {
                      bundle_name
                      order_detail {
                        product_id
                        product_name
                        product_picture
                        product_qty
                        product_price
                      }
                    }
                    non_bundle {
                      product_id
                      product_name
                      product_picture
                      product_qty
                      product_price
                    }
                  }
                }
                cursor_payment_deadline
                total_data_per_batch
              }
            }
        """.trimIndent()
    }
}