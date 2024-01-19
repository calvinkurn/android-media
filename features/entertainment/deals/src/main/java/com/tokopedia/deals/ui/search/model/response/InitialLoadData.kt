package com.tokopedia.deals.ui.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.deals.common.model.response.EventSearch
import com.tokopedia.deals.data.entity.EventChildCategory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InitialLoadData (
    @SerializedName("TravelCollectiveRecentSearches")
        @Expose
        var travelCollectiveRecentSearches: TravelCollectiveRecentSearch = TravelCollectiveRecentSearch(),
    @SerializedName("event_child_category")
        @Expose
        var eventChildCategory: EventChildCategory = EventChildCategory(),
    @SerializedName("event_search")
        @Expose
        var eventSearch: EventSearch = EventSearch()
): Parcelable
