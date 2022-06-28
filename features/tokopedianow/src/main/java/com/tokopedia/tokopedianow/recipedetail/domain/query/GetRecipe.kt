package com.tokopedia.tokopedianow.recipedetail.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetRecipe: GqlQueryInterface {

    const val PARAM_RECIPE_ID = "recipe_id"
    const val PARAM_WAREHOUSE_ID = "warehouse_id"

    private const val OPERATION_NAME = "TokonowGetRecipe"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    //TODO: Waiting for BE final contract
    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}${PARAM_RECIPE_ID}: String!, ${'$'}${PARAM_WAREHOUSE_ID}: String!) {
            $OPERATION_NAME(recipe_id:${'$'}${PARAM_RECIPE_ID}, warehouse_id:${'$'}${PARAM_WAREHOUSE_ID}}) {
                
            }
       }
       """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}