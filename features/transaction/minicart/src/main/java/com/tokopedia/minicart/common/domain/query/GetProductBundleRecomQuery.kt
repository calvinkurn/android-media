package com.tokopedia.minicart.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetProductBundleRecomQuery: GqlQueryInterface {

    const val PARAM_WAREHOUSE_ID = "warehouseID"
    const val PARAM_PRODUCT_IDS = "productIDs"
    const val PARAM_EXCLUDE_BUNDLE_IDS = "excludeBundleIDs"
    const val PARAM_QUERY_PARAM = "queryParam"

    private const val OPERATION_NAME = "TokonowProductBundle"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(
               ${'$'}$PARAM_WAREHOUSE_ID : String!,
               ${'$'}$PARAM_PRODUCT_IDS : [String!]!,
               ${'$'}$PARAM_EXCLUDE_BUNDLE_IDS : [String!]!,
               ${'$'}$PARAM_QUERY_PARAM : String!
            ) {
                  $OPERATION_NAME(
                    $PARAM_WAREHOUSE_ID: ${'$'}$PARAM_WAREHOUSE_ID, 
                    $PARAM_PRODUCT_IDS: ${'$'}$PARAM_PRODUCT_IDS, 
                    $PARAM_EXCLUDE_BUNDLE_IDS: ${'$'}$PARAM_EXCLUDE_BUNDLE_IDS,
                    $PARAM_QUERY_PARAM: ${'$'}$PARAM_QUERY_PARAM
                  ) {
                    header {
                      process_time
                      messages
                      reason
                      error_code
                    }
                    data {
                      widgetName
                      widgetData {
                        bundleGroupID
                        bundleName
                        bundleDetails {
                          bundleID
                          originalPrice
                          originalPriceRaw
                          displayPrice
                          displayPriceRaw
                          discountPercentage // float
                          isPO
                          preorderInfo
                          savingAmountWording
                          minOrder
                          minOrderWording
                          isProductsHaveVariant
                        }
                        bundleProducts {
                          productID
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