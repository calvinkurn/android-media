package com.tokopedia.tokopedianow.category.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object AceSearchProduct: GqlQueryInterface {
    override fun getOperationNameList(): List<String> = listOf("ace_search_product_v4")

    override fun getQuery(): String = """
        query aceSearchProductV4(${'$'}params: String!) {
          ace_search_product_v4(params: ${'$'}params){
            header {
              totalData
              totalDataText
              responseCode
              keywordProcess
            }
            data {
              isQuerySafe
              suggestion {
                suggestion
                query
                text
              }
              related {
                relatedKeyword
                position
                otherRelated {
                    keyword
                    url
                    applink
                    product {
                        id
                        name
                        price
                        imageUrl
                        url
                        applink
                        priceStr
                        wishlist
                        ratingAverage
                        stock
                        minOrder
                        maxOrder
                        labelGroups {
                            title
                            position
                            type
                            url
                        }
                        shop {
                            id
                        }
                    }
                }
              }
              products {
                id
                imageUrl300
                name
                price
                priceInt
                categoryBreadcrumb
                discountPercentage
                originalPrice
                childs
                parentId
                maxOrder
                warehouseIdDefault
                ratingAverage
                minOrder
                stock
                source_engine
                boosterList
                warehouseIdDefault
                shop {
                  id
                  name
                  isOfficial
                  isPowerBadge
                }
                labelGroups {
                  url
                  title
                  type
                  position
                }
                labelGroupVariant {
                  title
                  type
                  type_variant
                  hex_color
                }
                wishlist
              }
            }
          }
        }
    """.trimIndent()

    override fun getTopOperationName(): String = "aceSearchProductV4"
}
