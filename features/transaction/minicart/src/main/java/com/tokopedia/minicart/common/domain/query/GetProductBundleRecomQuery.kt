package com.tokopedia.minicart.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetProductBundleRecomQuery: GqlQueryInterface {

    const val PARAM_WAREHOUSE_ID = "warehouseID"
    const val PARAM_PRODUCT_IDS = "productIDs"
    const val PARAM_EXCLUDE_GROUP_IDS = "excludeGroupIDs"
    const val PARAM_QUERY_PARAM = "queryParam"

    private const val OPERATION_NAME = "TokonowBundleWidget"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query TokonowBundleWidget(            ${'$'}$PARAM_WAREHOUSE_ID            : String!,             ${'$'}$PARAM_PRODUCT_IDS            : [String!]!,             ${'$'}$PARAM_EXCLUDE_GROUP_IDS            : [String!]!,             ${'$'}$PARAM_QUERY_PARAM            : String!){
              TokonowBundleWidget(warehouseID:            ${'$'}$PARAM_WAREHOUSE_ID            , productIDs:            ${'$'}$PARAM_PRODUCT_IDS            , excludeGroupIDs:             ${'$'}$PARAM_EXCLUDE_GROUP_IDS            , queryParam:             ${'$'}$PARAM_QUERY_PARAM            ) {
                header {
                  process_time
                  reason
                  error_code
                }
                data {
                  widgetName
                  widgetData {
                    bundleGroupID
                    bundleName
                    bundleType
                    shopID
                    warehouseID
                    bundleDetails {
                      originalPrice
                      originalPriceRaw
                      displayPrice
                      displayPriceRaw
                      discountPercentage
                      isPO
                      preorderInfo
                      savingAmountWording
                      minOrder
                      minOrderWording
                      isProductsHaveVariant
                    }
                    bundleProducts {
                      productName
                      imageUrl
                      appLink
                      webLink
                    }
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