package com.tokopedia.common_epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetEPharmacyPrepareProductsGroupQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "PrepareProductsGroup"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
           mutation $OPERATION_NAME(${'$'}input: prepareProductGroupParam!) {
              prepareProductsGroup(input: ${'$'}input)
              {
                header {
                  process_time
                  error_code
                }
                data {
                  checkout_flow {
                      error_message
                  }
                  attachment_page_ticker_text
                  attachment_page_ticker_logo_url
                  toaster {
                      type
                      message
                  }
                  pap_primary_cta {
                      title
                      redirect_link_apps
                      redirect_link_web
                      state
                  }
                  epharmacy_groups {
                    epharmacy_group_id
                    prescription_source
                    ticker {
                      type_int
                      message
                    }
                    consultation_source {
                      id
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
                        is_closing_hour
                      }
                      status
                      note
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
                        cart_id
                        product_id_str
                        name
                        quantity
                        is_ethical_drug
                        product_image
                        item_weight
                        price
                        product_total_weight_fmt
                        qty_comparison {
                          initial_qty
                          recommend_qty
                        }
                      }
                    }
                    prescription_cta {
                      logo_url
                      title
                      subtitle
                      action_type
                    }
                  }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
