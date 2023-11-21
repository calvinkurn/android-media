package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyGetConsultationOrderDetailsQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "getConsultationOrderDetail"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            query $OPERATION_NAME(${'$'}toko_consultation_id: Int64!, ${'$'}order_uuid: String!, ${'$'}source: String!,  ${'$'}waiting_invoice: Boolean!) {
              getConsultationOrderDetail(toko_consultation_id: ${'$'}toko_consultation_id, order_uuid: ${'$'}order_uuid, source: ${'$'}source, waiting_invoice: ${'$'}waiting_invoice) {
                data {
                  order_id
                  invoice_number
                  invoice_url
                  payment_method
                  payment_amount
                  payment_amount_str
                  item_price
                  item_price_str
                  payment_date
                  order_indicator_color
                  order_status
                  order_status_desc
                  order_expired_date
                  ticker {
                    message
                    type
                    type_int
                    image
                  }
                  consultation_source {
                    id
                    service_name
                    enabler_name
                    enabler_logo_url
                    pwa_link
                    price
                    price_str
                    operating_schedule {
                      daily {
                        open_time
                        close_time
                      }
                      close_days
                      duration
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
                      expiry_date
                    }
                    start_time
                    end_time
                    consultation_status
                  }
                  cta {
                    label
                    variant_color
                    type
                    action_type
                    app_url
                    web_url
                  }
                  tri_dots {
                    label
                    variant_color
                    type
                    action_type
                    app_url
                    web_url
                  }
                  help_button {
                    label
                    caption
                    action_type
                    app_url
                  }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
