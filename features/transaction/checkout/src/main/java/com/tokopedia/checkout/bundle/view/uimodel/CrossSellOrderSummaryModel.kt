package com.tokopedia.checkout.bundle.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellOrderSummaryModel(
        var title: String = "",
        var priceWording: String = ""
) : Parcelable
