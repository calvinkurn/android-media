package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyInitiateConsultationQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "initiateConsultation"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            mutation $OPERATION_NAME(${'$'}input: initiateConsultationParam!) {
              initiateConsultation(input: ${"$"}input) {
                header {
                    error_code
                }
                data {
                    consultation_source {
                        id
                        enabler_name
                        pwa_link
                        status
                    }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
