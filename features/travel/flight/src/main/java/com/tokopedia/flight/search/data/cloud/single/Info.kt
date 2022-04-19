package com.tokopedia.flight.search.data.cloud.single

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by furqan on 06/10/2021.
 */
@Parcelize
data class Info(@SerializedName("label")
                @Expose
                var label: String = "",
                @SerializedName("value")
                @Expose
                var value: String = "") : Parcelable