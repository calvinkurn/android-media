package com.tokopedia.checkout.old.data.model.request.checkout

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EgoldData(
        @SerializedName("is_egold")
        var isEgold: Boolean = false,
        @SerializedName("gold_amount")
        var egoldAmount: Long = 0
) : Parcelable