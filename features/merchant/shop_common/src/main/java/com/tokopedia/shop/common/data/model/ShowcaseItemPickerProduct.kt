package com.tokopedia.shop.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowcaseItemPickerProduct(
        var productId: String = "",
        var productName: String = ""
): Parcelable