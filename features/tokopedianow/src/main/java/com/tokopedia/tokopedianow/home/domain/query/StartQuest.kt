package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object StartQuest: GqlQueryInterface {

    const val QUEST_ID_PARAM = "questID"

    private const val OPERATION_NAME = "questStart"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}$QUEST_ID_PARAM: Int){
              $OPERATION_NAME(input:{$QUEST_ID_PARAM: ${'$'}$QUEST_ID_PARAM}) {
                resultStatus {
                  code
                  message
                  status
                }
              }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
