package com.tokopedia.common.topupbills.data.favorite_number_perso

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsPersoFavNumberItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("mediaURL")
    val mediaUrl: String = "",
    @SerializedName("title")
    val clientName: String = "",
    @SerializedName("subtitle")
    val clientNumber: String = "",
    @SerializedName("trackingData")
    val trackingData: TopupBillsPersoFavNumberTrackingData = TopupBillsPersoFavNumberTrackingData()
): Parcelable