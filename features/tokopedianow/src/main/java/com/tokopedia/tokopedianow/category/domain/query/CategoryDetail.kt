package com.tokopedia.tokopedianow.category.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object CategoryDetail: GqlQueryInterface {

    override fun getOperationNameList(): List<String> = listOf("TokonowCategoryDetail")

    override fun getTopOperationName(): String = "TokonowCategoryDetail"

    override fun getQuery(): String {
        return """
            query TokonowCategoryDetail(${'$'}categoryID: String!, ${'$'}slug: String!, ${'$'}warehouseID: String!){
              TokonowCategoryDetail(categoryID: ${'$'}categoryID, slug: ${'$'}slug, warehouseID: ${'$'}warehouseID) {
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
    }
}
