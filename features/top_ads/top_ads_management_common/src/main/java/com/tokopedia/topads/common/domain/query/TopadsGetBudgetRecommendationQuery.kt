package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.topads.common.data.internal.ParamObject

object TopadsGetBudgetRecommendationQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsGetBudgetRecommendation"

    override fun getOperationNameList(): List<String> {
        return listOf()
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME(${'$'}${ParamObject.SHOP_ID}: String!, ${'$'}${ParamObject.REQUEST_TYPE}: String!, ${'$'}${ParamObject.SOURCE}: String!) {
          $OPERATION_NAME(${ParamObject.SHOP_ID}: ${'$'}${ParamObject.SHOP_ID}, ${ParamObject.REQUEST_TYPE}: ${'$'}${ParamObject.REQUEST_TYPE}, ${ParamObject.SOURCE}: ${'$'}${ParamObject.SOURCE}) {
            data {
              minBudgetRecommendation
              maxBudgetRecommendation
              isEligible
            }
            errors {
              code
              detail
              title
            }
            header {
              meta {
                additionalProp1
                additionalProp2
                additionalProp3
              }
              process_time
              total_data
            }
          }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
