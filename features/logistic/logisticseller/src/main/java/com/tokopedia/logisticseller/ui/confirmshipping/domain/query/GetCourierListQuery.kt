package com.tokopedia.logisticseller.ui.confirmshipping.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetCourierListQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "mpLogisticGetEditShippingForm"
    private val GET_COURIER_LIST_QUERY = """
            query $OPERATION_NAME(${'$'}deliveryIdentifier: String!){
              $OPERATION_NAME(DeliveryIdentifier:${'$'}deliveryIdentifier){
                data{
                  shipment{
                    shipping_max_add_fee
                    shipment_id
                    shipment_available
                    shipment_image
                    shipment_name
                    shipment_package{
                      desc
                      active
                      name
                      sp_id
                    }
                  }
                  ticker_unification_params{
                    page
                    target{
                      type
                      values
                    }
                    template{
                      contents{
                        key
                        value
                      }
                    }
                  }
                }
                status
                server_process_time
              }
            }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = GET_COURIER_LIST_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}
