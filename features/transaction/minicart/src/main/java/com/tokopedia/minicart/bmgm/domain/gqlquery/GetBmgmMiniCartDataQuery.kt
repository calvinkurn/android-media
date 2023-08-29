package com.tokopedia.minicart.bmgm.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 28/08/23.
 */

class GetBmgmMiniCartDataQuery : GqlQueryInterface {

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

    companion object {
        private const val OPERATION_NAME = "mini_cart_v3"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}lang: String, ${'$'}additional_params: CartRevampAdditionalParams) {
              $OPERATION_NAME(lang: ${'$'}lang, additional_params: ${'$'}additional_params) {
                error_message
                status
                data {
                  shopping_summary {
                    total_original_value
                    total_value
                  }
                  available_section {
                    available_group {
                      cart_details {
                        cart_detail_info {
                          cart_detail_type
                          bmgm {
                            offer_id
                            offer_name
                            offer_message
                            total_discount
                            offer_json_data
                            offer_status
                            tier_product {
                              tier_id
                              tier_message
                              tier_discount_text
                              tier_discount_amount
                              price_before_benefit
                              price_after_benefit
                              list_product {
                                product_id
                                warehouse_id
                                quantity
                                price_before_benefit
                                price_after_benefit
                                cart_id
                              }
                            }
                          }
                        }
                        products {
                          cart_id
                          product_id
                          product_quantity
                          product_name
                          product_image {
                            image_src_100_square
                          }
                          warehouse_id
                        }
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()
    }
}