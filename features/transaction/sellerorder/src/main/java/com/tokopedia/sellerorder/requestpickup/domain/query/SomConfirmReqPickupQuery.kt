package com.tokopedia.sellerorder.requestpickup.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object SomConfirmReqPickupQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "MpLogisticPreShipInfo"
    private val SOM_CONFIRM_REQ_QUERY = """
        mutation ${OPERATION_NAME}(${'$'}input: MpLogisticPreShipInfoInputs!) {
          mpLogisticPreShipInfo(input: ${'$'}input) {
            status
            message_error
            data{
              pickup_location{
                title
                address
                phone
              }
              detail{
                title
                invoice
                shippers{
                  name
                  service
                  note
                  courier_image
                  count_text
                  count
                }
                orchestra_partner
              }
              notes{
                title
                list
              }
              schedule_time_day {
                  today {
                    key
                    start
                    end
                  }
                  tomorrow {
                    key
                    start
                    end
                  }
                }
              ticker {
                  text
                  url_text
                  url_detail
                  action_key
                  type
                }
            }
          }
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = SOM_CONFIRM_REQ_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}