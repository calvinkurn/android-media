package com.tokopedia.sellerorder.waitingpaymentorder.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.waitingpaymentorder.domain.mapper.WaitingPaymentOrderResultMapper
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderResponse
import javax.inject.Inject
import kotlin.random.Random

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

//    suspend fun execute(param: WaitingPaymentOrderRequestParam): Map<String, Any> {
//        useCase.setTypeClass(WaitingPaymentOrderResponse.Data::class.java)
//        useCase.setRequestParams(generateParam(param))
//
//        return mapper.mapDomainToModelData(useCase.executeOnBackground().waitingPaymentOrder)
//    }

    suspend fun execute(param: WaitingPaymentOrderRequestParam): Map<String, Any> {
        return when (Random.nextInt(1, 3)) {
            1 -> mapper.mapDomainToModelData(generateDummyData(param))
            2 -> throw Exception("Wkwk iseng aja si")
            else -> mapper.mapDomainToModelData(generateEmptyData(param))
        }
    }

    private fun generateEmptyData(param: WaitingPaymentOrderRequestParam): WaitingPaymentOrderResponse.Data.WaitingPaymentOrder {
        return WaitingPaymentOrderResponse.Data.WaitingPaymentOrder(
                totalDataPerBatch = 5,
                cursorPaymentDeadline = 0L,
                paging = WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Paging(
                        currentPage = param.page
                ),
                orders = emptyList()
        )
    }

    private fun generateDummyData(param: WaitingPaymentOrderRequestParam): WaitingPaymentOrderResponse.Data.WaitingPaymentOrder {
        val randomProductCount = (Math.random() * 15).toInt() + 1
        return WaitingPaymentOrderResponse.Data.WaitingPaymentOrder(
                totalDataPerBatch = 5,
                cursorPaymentDeadline = Random.nextLong(),
                paging = WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Paging(
                        currentPage = param.page + 1
                ),
                orders = createDummyOrders(5, randomProductCount)
        )
    }

    private fun createDummyOrders(numOfOrder: Int, randomProductCount: Int): List<WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order> {
        val orders = arrayListOf<WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order>()
        for (i in 0 until numOfOrder) {
            orders.add(
                    WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order(
                            orderId = Random.nextInt().toString(),
                            paymentDeadline = "${(Math.random() * 30.0).toInt() + 1} Sep, 2020",
                            buyerNameAndPlace = "Y******n (Jakarta Selatan)",
                            products = createDummyProducts(randomProductCount)
                    )
            )
        }

        return orders
    }

    private fun createDummyProducts(randomProductCount: Int): List<WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order.Product> {
        val products = arrayListOf<WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order.Product>()
        for (i in 0 until randomProductCount) {
            products.add(
                    WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order.Product(
                            id = Random.nextInt().toString(),
                            name = "Product ${Random.nextInt(1, 100)}",
                            picture = "https://ichef.bbci.co.uk/news/976/cpsprodpb/12A9B/production/_111434467_gettyimages-1143489763.jpg",
                            quantity = Random.nextInt(1, 20),
                            price = "Rp. ${Random.nextInt(1, 10)}.${Random.nextInt(100, 999)}"
                    )
            )
        }

        return products
    }

    private fun generateParam(param: WaitingPaymentOrderRequestParam): Map<String, Any?> {
        return mapOf(SomConsts.PARAM_INPUT to param)
    }

    companion object {
        val QUERY = """
            query OrderListWaitingPayment(${'$'}input:OrderListWaitingPaymentRequest!) {
              orderListWaitingPayment(input:${'$'}input) {
                list {
                  order_id
                  buyer_name
                  payment_deadline
                  products {
                    product_id
                    product_name
                    product_picture
                    product_qty
                    product_price
                  }
                }
                paging {
                  ShowBackButton
                  ShowNextButton
                  CurrentBatchPage
                  CurrentPage
                  NextChangerValue
                  PrevChangerValue
                }
                cursor_payment_deadline
                total_data_per_batch
              }
            }
        """.trimIndent()
    }
}