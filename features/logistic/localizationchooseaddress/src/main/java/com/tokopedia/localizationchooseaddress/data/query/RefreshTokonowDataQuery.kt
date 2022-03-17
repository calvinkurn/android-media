package com.tokopedia.localizationchooseaddress.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object RefreshTokonowDataQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "RefreshTokonowData"
    )

    override fun getQuery(): String {
        return """
    query RefreshTokonowData(${'$'}tokonowLastUpdate:String!, ${'$'}districtID:String!, ${'$'}latitude:String!, ${'$'}longitude:String!, ${'$'}shopID:String!, ${'$'}warehouseID:String!, ${'$'}serviceType:String!, ${'$'}warehouses:[WarehouseUserPreference!]!){
      RefreshTokonowData(
        tokonowLastUpdate: ${'$'}tokonowLastUpdate,
        districtID: ${'$'}districtID, 
        latitude: ${'$'}latitude, 
        longitude: ${'$'}longitude,
        shopID: ${'$'}shopID, 
        warehouseID: ${'$'}warehouseID, 
        serviceType: ${'$'}serviceType, 
        warehouses: ${'$'}warehouses
      ) {
        header {
          process_time
          reason
          error_code
        }
        data {
          tokonowLastUpdate
          warehouseID
          shopID
          serviceType
          warehouses {
            serviceType
            warehouseID
          }
        }
      }
    }
    """.trimIndent()
    }

    override fun getTopOperationName() = "RefreshTokonowData"
}