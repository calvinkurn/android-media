package com.tokopedia.digital_checkout.data.request

import android.os.Parcelable
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import kotlinx.android.parcel.Parcelize

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

        var isSubscriptionChecked: Boolean = false,
        var fintechProducts: HashMap<String, FintechProduct> = hashMapOf(),
        var userInputPriceValue: Long? = null
) : Parcelable