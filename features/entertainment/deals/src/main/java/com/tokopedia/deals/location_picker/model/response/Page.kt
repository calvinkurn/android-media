package com.tokopedia.deals.location_picker.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Page (
        @SerializedName("next_page")
        @Expose
        var nextPage: String = "",

        @SerializedName("prev_page")
        @Expose
        var prevPage: String = ""
): Parcelable