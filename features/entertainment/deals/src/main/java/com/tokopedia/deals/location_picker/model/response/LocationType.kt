package com.tokopedia.deals.location_picker.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class LocationType(
        @SerializedName("id")
        @Expose
        var id: String = "0",

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("display_name")
        @Expose
        var displayName: String = "",

        @SerializedName("search_radius")
        @Expose
        var searchRadius: String = "",

        @SerializedName("icon")
        @Expose
        var icon: String = "",

        @SerializedName("type_id")
        @Expose
        var typeId: String = "0",

        @SerializedName("status")
        @Expose
        var status: String = "",

        @SerializedName("custom_text_1")
        @Expose
        var customText: String = ""
): Parcelable