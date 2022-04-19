package com.tokopedia.topupbills.telco.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 16/05/19.
 */
@Parcelize
data class TelcoProductPromo(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("bonus_text")
        @Expose
        val bonusText: String = "",
        @SerializedName("new_price")
        @Expose
        val newPrice: String = "",
        @SerializedName("new_price_plain")
        @Expose
        val newPricePlain: Int = 0,
        @SerializedName("value_text")
        @Expose
        val valueText: String = "")
    : Parcelable