package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.tokofood.common.domain.param.KeroAddressParamData

object KeroEditAddressQuery : GqlQueryInterface {
    private val QUERY = """
            mutation KeroEditAddress(${'$'}input: KeroAddressInput!) {
                kero_edit_address(input: ${'$'}input) {
                    data {
                      is_success
                      is_state_chosen_address_changed
                      chosen_address {
                        addr_id
                        receiver_name
                        addr_name
                        district
                        city
                        city_name
                        district_name
                        status
                        latitude
                        longitude
                        postal_code
                      }
                      tokonow {
                        shop_id
                        warehouse_id
                        warehouses {
                          warehouse_id
                          service_type
                        }
                        service_type
                      }
                    }
                    status
                    config
                    server_process_time
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
