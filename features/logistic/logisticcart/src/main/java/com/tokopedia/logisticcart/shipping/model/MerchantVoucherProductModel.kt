package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MerchantVoucherProductModel(
        var isMvc: Boolean = false,
        var mvcLogo: String = "",
        var mvcErrorMessage: String = ""
) : Parcelable