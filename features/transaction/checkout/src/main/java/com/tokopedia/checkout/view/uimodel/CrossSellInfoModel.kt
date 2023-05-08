package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellInfoModel(
    var title: String = "",
    var subtitle: String = "",
    var tooltipText: String = "",
    var iconUrl: String = ""
) : Parcelable
