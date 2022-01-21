package com.tokopedia.common.topupbills.data.favorite_number_perso

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsPersoFavNumber(
    @SerializedName("items")
    val items: List<TopupBillsPersoFavNumberItem> = listOf(),
    @SerializedName("trackingData")
    val trackingData: TopupBillsPersoTrackingData = TopupBillsPersoTrackingData()
): Parcelable