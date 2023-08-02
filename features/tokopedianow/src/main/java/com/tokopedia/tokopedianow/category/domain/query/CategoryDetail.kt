package com.tokopedia.tokopedianow.category.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object CategoryDetail: GqlQueryInterface {
    override fun getOperationNameList(): List<String> = listOf("TokonowCategoryDetail")

    override fun getQuery(): String = """
            query TokonowCategoryDetail(
                ${'$'}categoryID: String!, 
                ${'$'}slug: String!, 
                ${'$'}source: String!, 
                ${'$'}warehouseID: String!
            ){
              TokonowCategoryDetail(categoryID: ${'$'}categoryID, slug: ${'$'}slug, source: ${'$'}source, warehouseID: ${'$'}warehouseID) {
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
                  color
                  child {
                    id
                    name
                    url
                    applinks
                    imageUrl
                    isKyc
                    isAdult
                    minAge
                    color
                  }
                  recommendation {
                    id
                    name
                    url
                    applinks
                    imageUrl
                    color
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
