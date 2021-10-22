package com.tokopedia.checkout.bundle.data.model.request.checkout.old

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EgoldData(
        @SerializedName("is_egold")
        var isEgold: Boolean = false,
        @SerializedName("gold_amount")
        var egoldAmount: Long = 0
) : Parcelable