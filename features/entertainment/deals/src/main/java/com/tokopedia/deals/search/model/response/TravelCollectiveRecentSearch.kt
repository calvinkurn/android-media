package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelCollectiveRecentSearch (
        @SerializedName("items")
        @Expose
        var items: List<Item> = arrayListOf(),

        @SerializedName("meta")
        @Expose
        var meta: Meta = Meta()
): Parcelable