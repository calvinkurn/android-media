package com.tokopedia.tokopedianow.shoppinglist.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object SaveShoppingListStateQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "TokonowSaveShoppingListState"

    const val PARAM_SOURCE = "source"
    const val PARAM_ACTION_LIST = "actionList"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            mutation $OPERATION_NAME(${'$'}$PARAM_SOURCE: String!, ${'$'}$PARAM_ACTION_LIST: [TokonowShoppingListAction!]!){
              $OPERATION_NAME(
                source: ${'$'}$PARAM_SOURCE,
                actionList: ${'$'}$PARAM_ACTION_LIST
              )
              {
                header {
                  process_time
                  messages
                  reason
                  error_code
                }
              }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
