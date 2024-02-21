package com.tokopedia.atc_common.domain.usecase.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object AddToCartMultiQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf("")

    override fun getQuery() = """
        mutation add_to_cart_multi(${'$'}param: [AddToCartMultiParam], ${'$'}chosen_address: ChosenAddressParam) {
          add_to_cart_multi(param: ${'$'}param, chosen_address: ${'$'}chosen_address) {
            error_message
            status
            data{
              success
              messages
              data {
                cart_id
                product_id
                quantity
                notes
                shop_id
              }
            }
          }
        }
    """.trimIndent()

    override fun getTopOperationName() = ""
}
