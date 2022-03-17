package com.tokopedia.localizationchooseaddress.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object RefreshTokonowDataQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "RefreshTokonowData"
    )

    override fun getQuery(): String {
        return """
    query RefreshTokonowData(${'$'}tokonowLastUpdate:String!, ${'$'}districtId:String!, ${'$'}latitude:String!, ${'$'}longitude:String!, ${'$'}shopId:String!, ${'$'}warehouseId:String!, ${'$'}serviceType:String!, ${'$'}warehouses:[WarehouseUserPreference!]!){
      RefreshTokonowData(
        tokonowLastUpdate: ${'$'}tokonowLastUpdate,
        districtID: ${'$'}districtId, 
        latitude: ${'$'}latitude, 
        longitude: ${'$'}longitude,
        shopID: ${'$'}shopId, 
        warehouseID: ${'$'}warehouseId, 
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