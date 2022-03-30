package com.tokopedia.digital_product_detail.data.model.param

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeneralExtraParam (
    var operatorId: String = "",
    var categoryId: String = "",
    var productId: String = "",
    var clientNumber: String = "",
    var menuId: String = ""
): Parcelable