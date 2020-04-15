package com.tokopedia.product.addedit.detail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WholeSaleInputModel(
        var price: String = "",
        var quantity: String = ""
) : Parcelable