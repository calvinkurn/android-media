package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyGetConsultationDetailsQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "getEpharmacyConsultationDetails"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            query $OPERATION_NAME(${"$"}tokopedia_consultation_id: String!) {
              getEpharmacyConsultationDetails(tokopedia_consultation_id: ${"$"}tokopedia_consultation_id) 
              {
                header {
                  process_time
                  error_code
                  error_message
                }
                data {
                    consultation_status
                    consultation_source_id
                    consultation_data {
                        prescription {
                            id
                            type
                            document_url
                            expiry_date
                        }
                        start_time
                        end_time
                    }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
