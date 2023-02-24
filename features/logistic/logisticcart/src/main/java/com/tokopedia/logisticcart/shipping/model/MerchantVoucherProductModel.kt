package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantVoucherProductModel(
    var isMvc: Int = -1,
    var mvcLogo: String = "",
    var mvcErrorMessage: String = ""
) : Parcelable
