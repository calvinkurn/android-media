package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyAtcQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "cart_general_add_to_cart_instant"
    override fun getOperationNameList() = listOf(OPERATION_NAME)
    override fun getQuery() = """
            mutation $OPERATION_NAME(${'$'}params: CartGeneralAddToCartInstantParams!){
              cart_general_add_to_cart_instant(params: ${'$'}params) {
                header {
                  process_time
                  messages
                  reason
                  error_code
                }
                data {
                  success
                  message
                  data {
                    shopping_summary {
                      business_breakdown {
                        total_bill_fmt
                        product {
                          title
                          total_price_fmt
                        }
                      }
                    }
                    business_data {
                      business_id
                      success
                      message
                      custom_response
                      cart_groups {
                        cart_group_id
                        carts {
                          cart_id
                          success
                          product_id
                          quantity
                          metadata
                          custom_response
                          shop_id
                          price
                        }
                      }
                    }
                  }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
