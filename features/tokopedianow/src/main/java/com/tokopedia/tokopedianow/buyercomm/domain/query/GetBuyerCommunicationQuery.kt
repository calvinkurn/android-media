package com.tokopedia.tokopedianow.buyercomm.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetBuyerCommunicationQuery: GqlQueryInterface {

    const val PARAM_PAGE = "page"
    const val PARAM_WAREHOUSES = "warehouses"

    private const val OPERATION_NAME = "TokonowGetBuyerCommunication"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(
          ${'$'}$PARAM_PAGE: String!, 
          ${'$'}$PARAM_WAREHOUSES: [WarehousePerService!]!
        ) {
          $OPERATION_NAME(input: {
            $PARAM_PAGE:${'$'}$PARAM_PAGE, 
            $PARAM_WAREHOUSES:${'$'}$PARAM_WAREHOUSES 
          }) 
          {
            header {
              process_time
              messages
              reason
              error_code
            }
            data {
              shopDetails {
                logoURL
                title
              }
              locationDetails {
                status
                operationHour
              }
              shippingDetails {
                hint
                options {
                  name
                  details
                  available
                }
              }
              background {
                color
                animationURL
                imageURL
              }
            }  
          }
        }
    """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
