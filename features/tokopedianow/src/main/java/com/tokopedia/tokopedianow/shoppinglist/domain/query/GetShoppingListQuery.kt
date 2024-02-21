package com.tokopedia.tokopedianow.shoppinglist.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetShoppingListQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "TokonowGetShoppingList"

    const val PARAM_WAREHOUSES = "warehouses"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query TokonowGetShoppingList(${'$'}warehouses: [WarehousePerService!]!) {
              TokonowGetShoppingList(
                queryParam:"",
                source: "",
                warehouses: ${'$'}warehouses
              ) {
                header {
                  process_time
                  messages
                  reason
                  error_code
                }
                data {
                  metadata {
                    queryParam
                    inStockTotalData
                    inStockSelectedTotalData
                    inStockSelectedTotalPrice
                    inStockSelectedTotalPriceFmt
                    oosTotalData
                  }
                  listAvailableItem {
                    id
                    name
                    imageUrl
                    applink
                    url
                    price
                    priceInt
                    discountPercentage
                    originalPrice
                    parentProductID
                    minOrder
                    maxOrder
                    stock
                    warehouseID
                    shop{
                      id
                    }
                    labelGroup{
                      title
                      type
                      position
                      url
                    }
                    labelGroupVariant{
                      title
                      type
                      typeVariant
                      hexColor
                    }
                    isSelected
                  }
                  listUnavailableItem {
                    id
                    name
                    imageUrl
                    applink
                    url
                    price
                    priceInt
                    discountPercentage
                    originalPrice
                    parentProductID
                    minOrder
                    maxOrder
                    stock
                    warehouseID
                    shop{
                      id
                    }
                    labelGroup{
                      title
                      type
                      position
                      url
                    }
                    labelGroupVariant{
                      title
                      type
                      typeVariant
                      hexColor
                    }
                    isSelected
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
