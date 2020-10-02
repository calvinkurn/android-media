package com.tokopedia.shop.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowcaseItemPicker(
        var showcaseId: String = "",
        var showcaseName: String = ""
): Parcelable