package com.tokopedia.tokopedianow.searchcategory.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetFeedbackFieldToggle : GqlQueryInterface {

    private const val OPERATION_NAME = "TokonowFeedbackFieldToggle"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query TokonowFeedbackFieldToggle{
              TokonowFeedbackFieldToggle{
                header{
                  process_time
                  error_code
                  messages
                  reason
                }
                data{
                  isActive
                }
              }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
