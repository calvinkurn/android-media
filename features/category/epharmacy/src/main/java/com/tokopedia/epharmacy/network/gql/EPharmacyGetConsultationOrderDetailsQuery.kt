package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyGetConsultationOrderDetailsQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "getConsultationOrderDetail"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            query $OPERATION_NAME(${'$'}toko_consultation_id: String!, ${'$'}order_uuid: String!, ${'$'}source: String!) {
              getConsultationOrderDetail(toko_consultation_id: ${'$'}toko_consultation_id, order_uuid: ${'$'}toko_consultation_id, source: ${'$'}source) {
                header {
                  process_time
                  error_code
                  error_message
                }
                data {
                  order_id
                  invoice_number
                  payment_method
                  payment_amount
                  payment_amount_str
                  order_status
                  order_status_desc
                  ticker {
                    message
                    type
                  }
                  consultation_source {
                    id
                    enabler_name
                    enabler_logo_url
                    pwa_link
                    price
                    operating_schedule {
                      daily {
                        open_time
                        close_time
                      }
                      close_days
                      duration
                      is_closing_hour
                    }
                  }
                  consultation_data {
                    toko_consultation_id
                    partner_consultation_id
                    consultation_string
                    medical_recommendation {
                      product_id
                      product_name
                      quantity
                    }
                    doctor_details {
                      name
                      specialties
                    }
                    prescription {
                      id
                      type
                      document_url
                    }
                    start_time
                    end_time
                    consultation_status
                  }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
