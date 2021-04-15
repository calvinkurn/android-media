package com.tokopedia.digital_checkout.data

/**
 * @author by jessica on 29/01/21
 */
object DigitalCheckoutConst {

    object RequestBodyParams {
        const val REQUEST_BODY_CHECKOUT_TYPE = "checkout"
        const val REQUEST_BODY_OTP_CART_TYPE = "cart"
    }

    object SummaryInfo {
        const val STRING_SUBTOTAL_TAGIHAN = "Subtotal Tagihan"
        const val STRING_KODE_PROMO = "Kode Promo"

        const val SUMMARY_TOTAL_PAYMENT_POSITION = 0
        const val SUMMARY_PROMO_CODE_POSITION = 1
    }
}