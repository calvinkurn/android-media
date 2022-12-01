package com.tokopedia.minicart.common.domain.query

internal object GetProductBundleRecomQuery {
    const val PARAM_WAREHOUSE_ID = "warehouseID"
    const val PARAM_PRODUCT_IDS = "productIDs"
    const val PARAM_EXCLUDE_BUNDLE_IDS = "excludeBundleIDs"
    const val PARAM_QUERY_PARAM = "queryParam"

    const val OPERATION_NAME = "TokonowBundleWidget"
    const val QUERY = """
            query TokonowBundleWidget(
            ${'$'}$PARAM_WAREHOUSE_ID: String!,
            ${'$'}$PARAM_PRODUCT_IDS: [String!]!,             
            ${'$'}$PARAM_EXCLUDE_BUNDLE_IDS: [String!]!,
            ${'$'}$PARAM_QUERY_PARAM: String!){
              TokonowBundleWidget(
                $PARAM_WAREHOUSE_ID:${'$'}$PARAM_WAREHOUSE_ID, 
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
                    }
                  }
                }
              }
            }
        """
}