package com.tokopedia.atc_common.domain.usecase.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object UpdateCartCounterQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf("")

    override fun getQuery() = """
          mutation update_cart_counter {
              update_cart_counter() {
                    count
              }
          }
    """.trimIndent()

    override fun getTopOperationName() = ""
}
