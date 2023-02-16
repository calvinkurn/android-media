package com.tokopedia.digital_checkout.data.request

import android.os.Parcelable
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class DigitalCrossSellData(
    val product: FintechProduct,
    val isSubscription: Boolean,
    val additionalMetadata: String
): Parcelable
