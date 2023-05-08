package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DynamicPriceModel(
    var textLabel: String = ""
) : Parcelable
