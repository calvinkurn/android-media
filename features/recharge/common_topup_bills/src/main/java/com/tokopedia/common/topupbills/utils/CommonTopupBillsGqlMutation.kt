package com.tokopedia.common.topupbills.utils

object CommonTopupBillsGqlMutation {
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

    val updateSeamlessFavoriteNumber = """
        mutation updateFavorite(${'$'}updateRequest: favoriteDetailUpdateRequest!) {
          updateFavoriteDetail(updateRequest:${'$'}updateRequest) {
            clientNumber
            hashedClientNumber
            operatorID
            productID
            categoryID
            label
            lastUpdated
            lastOrderDate
            totalTransaction
            subscribed
            wishlist
          }
        }
    """.trimIndent()
}
