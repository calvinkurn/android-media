package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellOrderSummaryModel(
    var title: String = "",
    var priceWording: String = ""
) : Parcelable
