package com.tokopedia.digital_product_detail.data.model.param

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
    val pgCategoryIDs: List<Int>
) :Parcelable