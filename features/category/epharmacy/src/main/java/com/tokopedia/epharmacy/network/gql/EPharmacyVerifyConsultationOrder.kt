package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyVerifyConsultationOrder : GqlQueryInterface {

    private const val OPERATION_NAME = "verifyConsultationOrder"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery(): String = """
        query {
          verifyConsultationOrder(input: {
              toko_consultation_id: "6554231"
            }) {
            header {
              process_time
              error_code
              error_message
            }
            data {
              toko_consultation_id
              is_order_created
              pwa_link
            }
          }
        }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
