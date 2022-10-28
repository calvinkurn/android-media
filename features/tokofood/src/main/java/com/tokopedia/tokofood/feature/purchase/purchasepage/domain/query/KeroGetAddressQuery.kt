package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object KeroGetAddressQuery : GqlQueryInterface {

    private val QUERY = """
            query KeroGetAddressById(${'$'}addr_id: String) {
              kero_get_address(input: {addr_ids: ${'$'}addr_id}) {
                data {
                  addr_id
                  addr_name
                  address_1
                  address_2
                  city
                  district
                  latitude
                  longitude
                  phone
                  postal_code
                  province
                  receiver_name
                }
              }
            }
        """.trimIndent()

    private const val ADDR_ID_KEY = "addr_id"

    @JvmStatic
    fun createRequestParams(addressId: String) =
        HashMap<String, Any>().apply {
            put(ADDR_ID_KEY, addressId)
        }

    override fun getOperationNameList(): List<String> =
        listOf("kero_get_address")

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = "KeroGetAddressById"

}