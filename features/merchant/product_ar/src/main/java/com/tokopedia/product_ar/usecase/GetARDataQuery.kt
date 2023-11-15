package com.tokopedia.product_ar.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetARDataQuery : GqlQueryInterface {

    companion object {
        private const val QUERY_NAME = "pdpGetARData"
        private const val GQL_QUERY = """
            query $QUERY_NAME(${'$'}productID : String, ${'$'}shopID : String,  ${'$'}userLocation: pdpUserLocation) {
                  pdpGetARData(productID: ${'$'}productID, shopID: ${'$'}shopID,  userLocation: ${'$'}userLocation) {
                    provider
                    metadata {
                      shopName
                      categoryID
                      shopType
                      categoryName
                      categoryDetail {
                        id
                        name
                      }
                    }
                    options {
                      psku
                      name
                      productID
                      type
                      providerData
                      price
                      priceFmt
                      slashPriceFmt
                      discPercentage
                      isPriceMasked
                      minOrder
                      campaignInfo {
                        isActive
                        campaignID
                        campaignType
                        campaignTypeName
                        discountPercentage
                        originalPrice
                        discountPrice
                        stock
                        stockSoldPercentage
                        minOrder
                      }
                      stock
                      stockCopy
                      button {
                        text
                        color
                        cart_type
                      }
                      unavailableCopy
                    }
                    optionBgImage
                  }
                }
        """
    }

    override fun getQuery(): String = GQL_QUERY

    override fun getOperationNameList(): List<String> = listOf(QUERY_NAME)

    override fun getTopOperationName(): String = QUERY_NAME
}
