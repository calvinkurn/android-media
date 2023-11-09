package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyVerifyConsultationOrder : GqlQueryInterface {

    private const val OPERATION_NAME = "verifyConsultationOrder"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery(): String = """
       query $OPERATION_NAME(${'$'}toko_consultation_id: Int64!,${'$'}source: String) {
              verifyConsultationOrder(toko_consultation_id: ${'$'}toko_consultation_id, source: ${'$'}source) {
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
