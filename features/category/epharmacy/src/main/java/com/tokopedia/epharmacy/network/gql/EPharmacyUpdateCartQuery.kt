package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyUpdateCartQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "updateEpharmacyCart"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            mutation $OPERATION_NAME(${'$'}input: EPharmacyUpdateCartParam!) {
                updateEpharmacyCart(input: ${"$"}input) {
                    header{
                      process_time
                      error_code
                      error_message
                    }
                    status
                }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
