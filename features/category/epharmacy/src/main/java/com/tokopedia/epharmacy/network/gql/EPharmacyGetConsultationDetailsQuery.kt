package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyGetConsultationDetailsQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "getEpharmacyConsultationDetails"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            query $OPERATION_NAME(${'$'}tokopedia_consultation_id: Int64!) {
              getEpharmacyConsultationDetails(tokopedia_consultation_id: ${'$'}tokopedia_consultation_id) {
                data {
                    consultation_data {
                        prescription {
                            document_url
                        }
                    }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
