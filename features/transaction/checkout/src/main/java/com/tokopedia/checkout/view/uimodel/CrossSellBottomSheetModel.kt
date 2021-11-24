package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellBottomSheetModel(
        var title: String = "",
        var subtitle: String = ""
) : Parcelable
