package com.tokopedia.digital_checkout.data.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 20/01/21
 */
@Parcelize
data class DigitalCheckoutDataParameter(
        var voucherCode: String? = null,
        var cartId: String? = null,
        var transactionAmount: Double = 0.0,
        var ipAddress: String? = null,
        var userAgent: String? = null,
        var accessToken: String? = null,
        var walletRefreshToken: String? = null,
        var relationType: String? = null,
        var relationId: String? = null,
        var isNeedOtp: Boolean = false,

        var isSubscriptionChecked: Boolean = false,
        var isFintechProductChecked: Boolean = false
) : Parcelable