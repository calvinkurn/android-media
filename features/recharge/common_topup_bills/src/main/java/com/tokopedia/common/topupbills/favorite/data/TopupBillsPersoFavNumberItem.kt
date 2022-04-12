package com.tokopedia.common.topupbills.favorite.data

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
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("trackingData")
    val trackingData: TopupBillsPersoFavNumberTrackingData = TopupBillsPersoFavNumberTrackingData()
): Parcelable