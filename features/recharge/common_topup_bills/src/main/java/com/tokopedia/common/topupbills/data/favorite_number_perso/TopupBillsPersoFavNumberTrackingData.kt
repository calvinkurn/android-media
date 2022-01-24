package com.tokopedia.common.topupbills.data.favorite_number_perso

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsPersoFavNumberTrackingData(
    @SerializedName("clientNumber")
    val clientNumber: String = "",
    @SerializedName("lastOrderDate")
    val lastOrderDate: String = "",
    @SerializedName("totalTransaction")
    val totalTransaction: String = ""
): Parcelable