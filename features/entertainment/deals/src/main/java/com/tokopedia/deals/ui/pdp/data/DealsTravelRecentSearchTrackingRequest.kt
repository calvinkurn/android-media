package com.tokopedia.deals.ui.pdp.data

import com.google.gson.annotations.SerializedName

data class DealsTravelRecentSearchTrackingRequest(
    @SerializedName("message")
    val message: com.tokopedia.deals.ui.pdp.data.DealsTravelMessage = com.tokopedia.deals.ui.pdp.data.DealsTravelMessage(),
    @SerializedName("service")
    val service: String = "",
    @SerializedName("travel_recent_search")
    val travelRecentSearch: com.tokopedia.deals.ui.pdp.data.TravelRecentSearch = com.tokopedia.deals.ui.pdp.data.TravelRecentSearch()
)

data class DealsTravelMessage(
    @SerializedName("user_id")
    val userId: Long = 0
)

data class TravelRecentSearch(
    @SerializedName("data_type")
    val dataType: String = "",
    @SerializedName("recent_data")
    val recentData: com.tokopedia.deals.ui.pdp.data.RecentData = com.tokopedia.deals.ui.pdp.data.RecentData()
)

data class RecentData(
    @SerializedName("entertainment")
    val entertainment: com.tokopedia.deals.ui.pdp.data.Entertainment = com.tokopedia.deals.ui.pdp.data.Entertainment()
)

data class Entertainment(
    @SerializedName("app_url")
    val appUrl: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("price")
    val price: String = "",
    @SerializedName("price_prefix")
    val pricePrefix: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("value")
    val value: String = ""
)
