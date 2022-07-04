package com.tokopedia.tokopedianow.recipebookmark.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetRecipeBookmarksQuery: GqlQueryInterface {

    const val PARAM_USER_ID = "userID"
    const val PARAM_WAREHOUSE_ID = "warehouseID"
    const val PARAM_PAGE = "page"
    const val PARAM_LIMIT = "limit"

    private const val OPERATION_NAME = "TokonowGetRecipeBookmarks"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(
               ${'$'}$PARAM_USER_ID : String!,
               ${'$'}$PARAM_WAREHOUSE_ID : String!,
               ${'$'}$PARAM_PAGE : Int!,
               ${'$'}$PARAM_LIMIT : Int!
            ) {
               $OPERATION_NAME(input: 
               {
                 $PARAM_USER_ID: ${'$'}$PARAM_USER_ID,
                 $PARAM_WAREHOUSE_ID: ${'$'}$PARAM_WAREHOUSE_ID,
                 $PARAM_PAGE: ${'$'}$PARAM_PAGE,
                 $PARAM_LIMIT: ${'$'}$PARAM_LIMIT
               }) {
                	header {
                    process_time
                    reason
                    status_code
                  }
                	metadata {
                    hasNext
                    total
                  }
                	data {
                    userId
                    recipes {
                    	title
                      status
                      recipeDetailURL
                      instruction
                      portion
                      durationMinute
                      healthyScore
                      category {
                        id
                        name
                      }
                      tags {
                        id
                        name
                      }
                      products {
                        id
                        quantity
                        price
                        stock
                      }
                      ingredients {
                        id
                        name
                        quantity
                        unit
                        image {
                          id
                          urlOriginal
                          urlThumbnail
                          fileName
                          filePath
                          width
                          height
                        }
                      }
                      images {
                        id
                        urlOriginal
                        urlThumbnail
                        fileName
                        filePath
                        width
                        height
                      }
                      customVideos {
                        url
                        fileName
                      }
                      videos {
                        url
                        type
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