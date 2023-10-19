package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object TopAdsCreateHeadlineAdsQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsManageHeadlineAd"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """mutation $OPERATION_NAME(${'$'}input:topadsManageHeadlineAdInput!){
  $OPERATION_NAME(input:${'$'}input) {
    data {
      id
      resourceURL
    }
    errors{
      code
      title
      detail
    }
  }
}
""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
