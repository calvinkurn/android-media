package com.tokopedia.shop.common.constant

object ShopPageGetHomeTypeQuery {
    private val BASE_QUERY_GET_SHOP_HOME_TYPE = """
            query shopPageGetHomeType(${'$'}shopID: Int!){
              shopPageGetHomeType(shopID: ${'$'}shopID){
                %1s
              }
            }
        """.trimIndent()

    private const val GET_SHOP_HOME_TYPE_QUERY_REQUEST = "shopHomeType"

    private const val GET_SHOP_HOME_LAYOUT_DATA_QUERY_REQUEST = """
        homeLayoutData {
          layoutID
          masterLayoutID
          widgetIDList{
            widgetID
            widgetMasterID
            widgetType
            widgetName
          }
        }
    """


    fun getShopHomeTypeQuery() = String.format(BASE_QUERY_GET_SHOP_HOME_TYPE, GET_SHOP_HOME_TYPE_QUERY_REQUEST)
    fun getShopHomeLayoutDataQuery() = String.format(BASE_QUERY_GET_SHOP_HOME_TYPE, GET_SHOP_HOME_LAYOUT_DATA_QUERY_REQUEST)
}