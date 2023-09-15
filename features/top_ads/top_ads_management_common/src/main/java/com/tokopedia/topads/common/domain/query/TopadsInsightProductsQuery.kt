package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object TopadsInsightProductsQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsInsightProducts"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME(${'$'}shop_id: String!, ${'$'}status: String) {
  $OPERATION_NAME(shop_id: ${'$'}shop_id, status: ${'$'}status) {
    data {
      product {
        product_id
        title
        product_image
        impression_loss
        status
      }
      redirect_url
      total_impression_loss
    }
    errors {
      code
      detail
      object {
        type
        text
      }
      title
    }
  }
}
"""
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
