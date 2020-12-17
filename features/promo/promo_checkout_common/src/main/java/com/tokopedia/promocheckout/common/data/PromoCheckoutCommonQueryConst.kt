package com.tokopedia.promocheckout.common.data

/**
 * @author by furqan on 08/10/2020
 */
object PromoCheckoutCommonQueryConst {
    val QUERY_FLIGHT_CANCEL_VOUCHER = """
        query flightCancelVoucher {
            status
            flightCancelVoucher {
              type
              attributes {
                success
              }
            }
        }
    """.trimIndent()

    val QUERY_FLIGHT_CHECK_VOUCHER = """
        query flightVoucher(${'$'}cartID: String!, ${'$'}voucherCode: String!) {
            status
            flightVoucher(cartID:${'$'}cartID, voucherCode:${'$'}voucherCode) {
                voucherCode
                UserID
                DiscountAmount
                DiscountAmountPlain
                CashbackAmount
                CashbackAmountPlain
                DiscountedPrice
                DiscountedPricePlain
                Message
                VoucherAmount
                TitleDescription
                IsCoupon
            }
        }
    """.trimIndent()
}