package com.tokopedia.common.topupbills.utils

object CommonTopupBillsGqlMutation {
    val favoriteNumber = """
        mutation favouriteNumber(${'$'}categoryID: Int!){
          recharge_favorite_number(categoryID:${'$'}categoryID) {
            list {
              client_number
              label
              product_id
              operator_id
              category_id
            }
          }
        }
    """.trimIndent()

    val rechargeExpressCheckout = """
        mutation rechargeExpressCheckout(${'$'}cart: RechargeInputExCheckout!) {
          rechargeExpressCheckout(cart:${'$'}cart) {
            data {
              redirect_url
              callback_url_success
              callback_url_failed
              thanks_url
              transaction_id
              query_string
              need_otp
            }
            errors {
              status
              title
            }
          }
        }
    """.trimIndent()
}