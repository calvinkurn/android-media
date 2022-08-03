package com.tokopedia.digital_checkout.data.queries

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery("QueryRechargeCheckout", DigitalCheckoutQuery.QUERY_RECHARGE_CHECKOUT)
object DigitalCheckoutQuery {
    const val QUERY_RECHARGE_CHECKOUT = """
        mutation RechargeCheckout(${'$'}request: RechargeCheckoutRequestV3!) {
          rechargeCheckoutV3(body: ${'$'}request) {
            meta {
              order_id
            }
            data {
              type
              id
              attributes {
                redirect_url
                callback_url_success
                callback_url_failed
                thanks_url
                query_string
                parameter {
                  merchant_code
                  profile_code
                  transaction_id
                  transaction_code
                  transaction_date
                  customer_name
                  customer_email
                  customer_msisdn
                  amount
                  currency
                  items_name
                  items_quantity
                  items_price
                  signature
                  language
                  user_defined_value
                  nid
                  state
                  fee
                  payments_amount
                  payments_name
                  pid
                }
              }
            }
            errors {
              status
              title
            }
          }
        }
    """
}