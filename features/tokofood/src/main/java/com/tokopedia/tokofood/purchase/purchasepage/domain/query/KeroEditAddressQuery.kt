package com.tokopedia.tokofood.purchase.purchasepage.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.param.KeroEditAddressParam

object KeroEditAddressQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "KeroEditAddress"

    private val QUERY = """
            mutation $OPERATION_NAME(${'$'}input: KeroAddressInput!) {
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
    fun createRequestParams() =
        HashMap<String, Any>().apply {
            // TODO: Change to real input
            val dummyParam = KeroEditAddressParam(
                123, "", "", "", "", "",
                "", "", "", "", "", "")
            put(INPUT_KEY, dummyParam)
        }

    override fun getOperationNameList(): List<String> =
        listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}