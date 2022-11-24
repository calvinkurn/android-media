package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyInitiateConsultationQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "initiateConsultation"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            mutation $OPERATION_NAME(${'$'}input: initiateConsultationParam!) {
              initiateConsultation(input: ${"$"}input) 
              {
                header {
                  process_time
                  error_code
                }
                data {
                    toko_consultation_id
                    consultation_source {
                        id
                        enabler_name
                        pwa_link
                        operating_schedule {
                            daily {
                                open_time
                                close_time
                            }
                            close_days
                        }
                        status
                    }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
