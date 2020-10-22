package com.tokopedia.deals.common.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.deals.common.model.response.EventSearch
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchData(
        @SerializedName("event_search")
        @Expose
        var eventSearch: EventSearch = EventSearch()
) : Parcelable