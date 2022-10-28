package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.tokofood.common.domain.param.KeroAddressParamData

object KeroEditAddressQuery : GqlQueryInterface {
    private val QUERY = """
            mutation KeroEditAddress(${'$'}input: KeroAddressInput!) {
              kero_edit_address(input: ${'$'}input) {
                data {
                  addr_id
                  is_success
                }
              }
            }
        """.trimIndent()

    private const val INPUT_KEY = "input"

    @JvmStatic
    fun createRequestParams(addressParam: KeroAddressParamData) =
        HashMap<String, Any>().apply {
            put(INPUT_KEY, addressParam)
        }

    override fun getOperationNameList(): List<String> =
        listOf("kero_edit_address")

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = "KeroEditAddress"

}