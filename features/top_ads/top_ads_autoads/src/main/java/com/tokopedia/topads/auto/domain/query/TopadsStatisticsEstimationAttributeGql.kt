package com.tokopedia.topads.auto.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.TYPE

object TopadsStatisticsEstimationAttributeGql : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsStatisticsEstimationAttribute"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME(${'$'}$SHOP_ID: String!, ${'$'}$TYPE: Int!, ${'$'}$SOURCE: String!) {
              $OPERATION_NAME(shopID: ${'$'}$SHOP_ID, type: ${'$'}$TYPE, source: ${'$'}$SOURCE) {
                data {
                  type
                  lowClickDivider
                  highClickDivider
                  lowImpMultiplier
                  highImpMultiplier
                }
              }
            }
            """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
