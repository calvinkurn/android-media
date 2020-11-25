package com.tokopedia.sellerappwidget.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.data.model.GetOrderResponse
import com.tokopedia.sellerappwidget.data.model.InputParameterModel
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.domain.mapper.OrderMapper
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/11/20
 */

@AppWidgetScope
class GetOrderUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: OrderMapper
) : BaseUseCase<List<OrderUiModel>>() {

    override suspend fun executeOnBackground(): List<OrderUiModel> {
        val rand = (Math.random() * 100).toInt()
        return if (rand % 2 == 0) {
            emptyList()
        } else {
            dummy()
        }
        /*val gqlRequest = GraphqlRequest(QUERY, GetOrderResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val errors: List<GraphqlError>? = gqlResponse.getError(GetOrderResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetOrderResponse>()
            val orderList = data.orderList.list
            return mapper.mapRemoteModelToUiModel(orderList)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }*/
    }

    private fun dummy(): List<OrderUiModel> {
        val response: GetOrderResponse = Gson().fromJson(DUMMY, GetOrderResponse::class.java)
        return mapper.mapRemoteModelToUiModel(response.orderList.list)
    }

    companion object {
        private const val KEY_INPUT = "input"
        private const val LANG_ID = "id"
        private const val FILTER_STATUS = 999

        /**
         * create params to get order list
         * @param dateFmt should be date with format dd/MM/yyyy
         * @return `RequestParams`
         * */
        fun createParams(dateFmt: String): RequestParams {
            val input = InputParameterModel(
                    batchPage = 0,
                    startDate = dateFmt,
                    endDate = dateFmt,
                    filterStatus = FILTER_STATUS,
                    isBuyerRequestCancel = 0,
                    isMobile = true,
                    lang = LANG_ID,
                    nextOrderId = 0,
                    orderTypeList = emptyList(),
                    page = 1,
                    search = "",
                    shippingList = emptyList(),
                    sortBy = 0,
                    statusList = listOf(Const.OrderStatusId.NEW_ORDER, Const.OrderStatusId.READY_TO_SHIP)
            )
            return RequestParams.create().apply {
                putObject(KEY_INPUT, input)
            }
        }

        private val QUERY = """
            query OrderList(${'$'}input: OrderListArgs!) {
              orderList(input: ${'$'}input) {
                list {
                  order_id
                  deadline_text
                  order_product {
                    product_id
                    product_name
                    picture
                  }
                }
              }
            }
        """.trimIndent()

        private val DUMMY = """
            {
                "orderList": {
                  "list": [
                    {
                      "deadline_text": "1 Hari 23 Jam",
                      "order_id": "636332019",
                      "order_status_id": 400,
                      "order_product": [
                        {
                          "product_id": "1317883553",
                          "product_name": "Product 1",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/16b04166-471e-4170-91b6-1e24f42b534e.jpg"
                        },
                        {
                          "product_id": "1317819595",
                          "product_name": "ole in",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/c989dfca-8d16-4228-acb2-7d8e9064db99.jpg"
                        },
                        {
                          "product_id": "986116148",
                          "product_name": "sendok set",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/6/30/5a4ac224-d983-440c-abad-5af271de88aa.jpg"
                        },
                        {
                          "product_id": "800175154",
                          "product_name": "Kecap Bango Botol",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/4/23/12299749/12299749_2ddafad7-9215-4de4-b1d1-e789d71dbd33_1000_1000"
                        },
                        {
                          "product_id": "516791889",
                          "product_name": "Halo Ini Nama Produk Yang Harus Panjang Banget Supaya Penuh Aja HEHEHE",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2019/7/23/12299749/12299749_a9ba0f7b-b9d0-415f-80e6-9eaa9d73e5d1_700_700"
                        }
                      ]
                    },
                    {
                      "deadline_text": "1 Hari 23 Jam",
                      "order_id": "636332019",
                      "order_status_id": 220,
                      "order_product": [
                        {
                          "product_id": "1317883553",
                          "product_name": "Product 2",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/16b04166-471e-4170-91b6-1e24f42b534e.jpg"
                        }
                      ]
                    },
                    {
                      "deadline_text": "1 Hari 23 Jam",
                      "order_id": "636332019",
                      "order_status_id": 400,
                      "order_product": [
                        {
                          "product_id": "1317883553",
                          "product_name": "Product 3",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/16b04166-471e-4170-91b6-1e24f42b534e.jpg"
                        }
                      ]
                    },
                    {
                      "deadline_text": "1 Hari 23 Jam",
                      "order_id": "636332019",
                      "order_status_id": 220,
                      "order_product": [
                        {
                          "product_id": "1317883553",
                          "product_name": "Product 4",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/16b04166-471e-4170-91b6-1e24f42b534e.jpg"
                        }
                      ]
                    },
                    {
                      "deadline_text": "1 Hari 23 Jam",
                      "order_id": "636332019",
                      "order_status_id": 220,
                      "order_product": [
                        {
                          "product_id": "1317883553",
                          "product_name": "Product 5",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/16b04166-471e-4170-91b6-1e24f42b534e.jpg"
                        }
                      ]
                    },
                    {
                      "deadline_text": "1 Hari 23 Jam",
                      "order_id": "636332019",
                      "order_status_id": 220,
                      "order_product": [
                        {
                          "product_id": "1317883553",
                          "product_name": "Product 6",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/16b04166-471e-4170-91b6-1e24f42b534e.jpg"
                        }
                      ]
                    },
                    {
                      "deadline_text": "1 Hari 23 Jam",
                      "order_id": "636332019",
                      "order_status_id": 400,
                      "order_product": [
                        {
                          "product_id": "1317883553",
                          "product_name": "Product 7",
                          "picture": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2020/11/5/16b04166-471e-4170-91b6-1e24f42b534e.jpg"
                        }
                      ]
                    }
                  ]
                }
              }
        """.trimIndent()
    }
}