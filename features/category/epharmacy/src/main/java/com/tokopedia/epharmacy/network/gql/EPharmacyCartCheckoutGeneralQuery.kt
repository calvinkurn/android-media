package com.tokopedia.epharmacy.network.gql

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object EPharmacyCartCheckoutGeneralQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "checkout_cart_general"

    override fun getOperationNameList() = listOf(OPERATION_NAME)

    override fun getQuery() = """
            mutation $OPERATION_NAME(${'$'}params: CheckoutCartGeneralParams!){
              checkout_cart_general(params: ${'$'}params) {
                header {
                  process_time
                  messages
                  reason
                  error_code
                }
                data {
                  success
                  error
                  error_state
                  message
                  error_metadata
                  data {
                    product_list {
                      id
                      price
                      quantity
                      name
                    }
                    redirect_url
                    callback_url
                    parameter {
                      merchant_code
                      profile_code
                      customer_id
                      customer_name
                      customer_msisdn
                      transaction_id
                      transaction_date
                      gateway_code
                      pid
                      bid
                      nid
                      user_defined_value
                      amount
                      currency
                      language
                      signature
                      device_info {
                        device_name
                        device_version
                      }
                      payment_metadata
                      merchant_type
                      back_url
                    }
                    query_string
                    payment_type
                    oms_data {
                      url
                      merchant_code
                      profile_code
                      transaction_id
                      transaction_date
                      customer_name
                      customer_email
                      customer_msisdn
                      currency
                      items_name
                      items_quantity
                      items_price
                      amount
                      refund_amount
                      signature
                      user_defined_value
                      payment_metadata
                      fee
                      status
                      message
                      hitPG
                    }
                    price_validation {
                      is_updated
                      message {
                        title
                        desc
                        action
                      }
                      tracker_data {
                        product_changes_type
                        campaign_type
                        product_ids
                      }
                    }
                    query_url
                  }
                }
              }
            }
    """.trimIndent()

    override fun getTopOperationName() = OPERATION_NAME
}
