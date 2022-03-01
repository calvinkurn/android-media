package com.tokopedia.common.topupbills.data.requests

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteNumberParam(
    @SerializedName("channelName")
    @Expose
    val channelName: String,
    @SerializedName("clientNumbers")
    @Expose
    val clientNumbers: List<String>,
    @SerializedName("dgCategoryIDs")
    @Expose
    val dgCategoryIDs: List<Int>,
    @SerializedName("pgCategoryIDs")
    @Expose
    val pgCategoryIDs: List<Int>,
    @SerializedName("dgOperatorIDs")
    @Expose
    val dgOperatorIds: List<Int>
) :Parcelable