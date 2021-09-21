package com.tokopedia.flight.search.data.cloud.single

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by User on 10/26/2017.
 */
@Parcelize
data class Amenity(
        @SerializedName("icon")
        @Expose
        var icon: String = "",
        @SerializedName("label")
        @Expose
        var label: String = "",
        var isDefault: Boolean = false) : Parcelable