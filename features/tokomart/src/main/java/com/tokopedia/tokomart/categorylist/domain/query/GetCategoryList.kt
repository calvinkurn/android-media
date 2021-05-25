package com.tokopedia.tokomart.categorylist.domain.query

internal object GetCategoryList {

    val QUERY = """
            query TokonowCategoryTree(${'$'}warehouseID:String!, ${'$'}depth:Int!){
                TokonowCategoryTree(warehouseID:${'$'}warehouseID, depth:${'$'}depth){
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
                      applinks
                      imageUrl
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
}