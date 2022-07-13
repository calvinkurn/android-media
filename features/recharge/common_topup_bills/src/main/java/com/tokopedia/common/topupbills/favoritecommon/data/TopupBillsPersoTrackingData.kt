package com.tokopedia.common.topupbills.favoritecommon.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsPersoTrackingData(
    @SerializedName("userType")
    val userType: String = ""
): Parcelable