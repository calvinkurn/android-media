package com.tokopedia.common.topupbills.favoritecommon.data

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
    @SerializedName("token")
    val token: String = "",
    @SerializedName("clientNumberHash")
    val clientNumberHash: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("operatorID")
    val operatorId: String = "",
    @SerializedName("productID")
    val productId: String = "",
    @SerializedName("trackingData")
    val trackingData: TopupBillsPersoFavNumberTrackingData = TopupBillsPersoFavNumberTrackingData(),
    @SerializedName("label1")
    val label1: String = "",
    @SerializedName("label2")
    val label2: String = ""
): Parcelable
