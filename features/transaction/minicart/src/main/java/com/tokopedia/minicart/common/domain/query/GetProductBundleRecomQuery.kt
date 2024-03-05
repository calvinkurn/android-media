package com.tokopedia.minicart.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetProductBundleRecomQuery : GqlQueryInterface {
    const val PARAM_WAREHOUSES = "warehouses"
    const val PARAM_PRODUCT_IDS = "productIDs"
    const val PARAM_EXCLUDE_BUNDLE_IDS = "excludeBundleIDs"
    const val PARAM_QUERY_PARAM = "queryParam"

    private const val OPERATION_NAME = "TokonowBundleWidget"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query TokonowBundleWidget(
            ${'$'}$PARAM_WAREHOUSES: [WarehousePerService!],
            ${'$'}$PARAM_PRODUCT_IDS: [String!]!,             
            ${'$'}$PARAM_EXCLUDE_BUNDLE_IDS: [String!]!,
            ${'$'}$PARAM_QUERY_PARAM: String!){
              TokonowBundleWidget(
                $PARAM_WAREHOUSES:${'$'}$PARAM_WAREHOUSES, 
                $PARAM_PRODUCT_IDS:${'$'}$PARAM_PRODUCT_IDS, 
                $PARAM_EXCLUDE_BUNDLE_IDS: ${'$'}$PARAM_EXCLUDE_BUNDLE_IDS, 
                $PARAM_QUERY_PARAM: ${'$'}$PARAM_QUERY_PARAM
              ) {
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
                      bundleID
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
                      productID
                      productName
                      imageUrl
                      appLink
                      webLink
                      minOrder
                    }
                  }
                }
              }
            }
        """
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
