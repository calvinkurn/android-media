package com.tokopedia.digital_checkout.data.request

/**
 * @author by jessica on 20/01/21
 */
class DigitalCheckoutDataParameter (
        var voucherCode: String? = null,
        var cartId: String? = null,
        var transactionAmount: Long = 0,
        var ipAddress: String? = null,
        var userAgent: String? = null,
        var accessToken: String? = null,
        var walletRefreshToken: String? = null,
        var relationType: String? = null,
        var relationId: String? = null,
        var isNeedOtp: Boolean = false,

        var isSubscriptionChecked: Boolean = false,
        var isFintechProductChecked: Boolean = false
)