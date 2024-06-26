package com.tokopedia.digital_checkout.data.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author by jessica on 20/01/21
 */
@Parcelize
data class DigitalCheckoutDataParameter(
    var voucherCode: String = "",
    var cartId: String = "",
    var transactionAmount: Double = 0.0,
    var ipAddress: String = "",
    var userAgent: String = "",
    var accessToken: String = "",
    var walletRefreshToken: String = "",
    var relationType: String = "",
    var relationId: String = "",
    var isNeedOtp: Boolean = false,
    var deviceId: Int = 5,

    var isSubscriptionChecked: Boolean = false,
    var crossSellProducts: HashMap<String, DigitalCrossSellData> = hashMapOf(),
    var userInputPriceValue: Long? = null,

    var isInstantCheckout: Boolean = false,
    var productConsentPayload: String = ""
) : Parcelable
