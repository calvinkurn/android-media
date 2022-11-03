package com.tokopedia.common_epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetEPharmacyPrepareProductsGroupQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "PrepareProductsGroup"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            mutation $OPERATION_NAME() {
              prepareProductsGroup() 
              {
                header {
                  process_time
                  error_code
                }
                data {
                  attachment_page_ticker_text
                  epharmacy_groups {
                    epharmacy_group_id
                    prescription_source
                    consultation_source {
                      id
                      enabler_name
                      enabler_logo_url
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
                    number_prescription_images
                    prescription_images {
                      prescription_id
                      status
                      reject_reason
                      expired_at
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
                    products_info {
                      shop_id
                      shop_id_str
                      shop_name
                      shop_type
                      shop_location
                      shop_logo_url
                      partner_logo_url
                      products {
                        product_id
                        product_id_str
                        name
                        quantity
                        is_ethical_drug
                        product_image
                        item_weight
                        product_total_weight_fmt
                      }
                    }
                  }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
