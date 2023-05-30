package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetProductManageV2 : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsGetProductManageV2"
    private const val PARAM_SHOP_ID = "shopID"
    private const val PARAM_PRODUCT_ID = "productID"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}$PARAM_PRODUCT_ID :String!,${'$'}$PARAM_SHOP_ID:String!){
               	$OPERATION_NAME(type:1, shop_id:${'$'}$PARAM_SHOP_ID,item_id:${'$'}$PARAM_PRODUCT_ID,source: "topads.see_ads_performance"){
            			data {
            			  ad_id
            			  ad_type
            			  is_enable_ad
            			  item_id
            			  item_image
            			  item_name
            			  shop_id
            			  manage_link
            			}
                }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
