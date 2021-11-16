package com.tokopedia.checkout.bundle.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CrossSellBottomSheetModel(
        var title: String = "",
        var subtitle: String = ""
) : Parcelable
