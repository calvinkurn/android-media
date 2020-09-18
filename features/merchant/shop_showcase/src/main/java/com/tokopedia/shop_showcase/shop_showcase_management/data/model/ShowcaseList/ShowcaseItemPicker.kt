package com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowcaseItemPicker(
        var showcaseId: String = "",
        var showcaseName: String = ""
): Parcelable