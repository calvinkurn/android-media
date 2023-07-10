package com.tokopedia.tokopedianow.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetCategoryListQuery: GqlQueryInterface {
    override fun getOperationNameList(): List<String> = listOf("TokonowCategoryTree")

    override fun getQuery(): String = """
            query TokonowCategoryTree(${'$'}warehouses:[WarehousePerService!], ${'$'}depth:Int!){
                TokonowCategoryTree(warehouses:${'$'}warehouses, depth:${'$'}depth){
                    header{
                        process_time
                        messages
                        reason
                        error_code
                    }
                    data{
                      id
                      name
                      url
                      isAdult
                      applinks
                      imageUrl
                      color
                      child {
                        id
                        name
                        url
                        applinks
                        imageUrl
                        parentID
                      }
                    }
                  }
                }
        """.trimIndent()

    override fun getTopOperationName(): String = "TokonowCategoryTree"
}
