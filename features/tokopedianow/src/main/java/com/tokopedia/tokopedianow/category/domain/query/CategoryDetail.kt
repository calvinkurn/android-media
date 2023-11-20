package com.tokopedia.tokopedianow.category.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object CategoryDetail: GqlQueryInterface {
    override fun getOperationNameList(): List<String> = listOf("TokonowCategoryDetail")

    override fun getQuery(): String = """
            query TokonowCategoryDetail(
                ${'$'}categoryID: String!, 
                ${'$'}slug: String!, 
                ${'$'}source: String!, 
                ${'$'}warehouses: [WarehousePerService!]
            ){
              TokonowCategoryDetail(categoryID: ${'$'}categoryID, slug: ${'$'}slug, source: ${'$'}source, warehouses: ${'$'}warehouses) {
                header {
                  process_time
                  messages
                  reason
                  error_code
                }
                data {
                  id
                  name
                  url
                  applinks
                  imageUrl
                  colorObj {
                    hexLight
                    hexDark
                  }
                  child {
                    id
                    name
                    url
                    applinks
                    imageUrl
                    isKyc
                    isAdult
                    minAge
                    colorObj {
                      hexLight
                      hexDark
                    }
                  }
                  recommendation {
                    id
                    name
                    url
                    applinks
                    imageUrl
                    colorObj {
                      hexLight
                      hexDark
                    }
                  }
                  navigation {
                    prev {
                      id
                      name
                      url
                      applinks
                      imageUrl
                    }
                    next {
                      id
                      name
                      url
                      applinks
                      imageUrl
                    }
                  }
                }
              }
            }
        """.trimIndent()

    override fun getTopOperationName(): String = "TokonowCategoryDetail"
}
