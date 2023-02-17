package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantVoucherModel(
    var isMvc: Int = 0,
    var mvcTitle: String = "",
    var mvcLogo: String = "",
    var mvcErrorMessage: String = ""
) : Parcelable
