package com.tokopedia.topupbills.telco.view.model

/**
 * Created by nabillasabbaha on 07/05/19.
 */
class DigitalPromo(
        val id: String,
        val description: String,
        val promoCode: String,
        var voucherCodeCopied: Boolean = false,
        val url: String
)