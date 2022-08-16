package com.tokopedia.tokopedianow.recipebookmark.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object RemoveRecipeBookmarkQuery: GqlQueryInterface {

    const val PARAM_RECIPE_ID = "recipeID"

    private const val OPERATION_NAME = "TokonowRemoveRecipeBookmark"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            mutation $OPERATION_NAME(
               ${'$'}$PARAM_RECIPE_ID : String!
            ) {
               $OPERATION_NAME(input: 
               {
                 $PARAM_RECIPE_ID: ${'$'}$PARAM_RECIPE_ID
               }) {
                   header {
                      success
                      statusCode
                      message
                      processTime
                   }
               }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}