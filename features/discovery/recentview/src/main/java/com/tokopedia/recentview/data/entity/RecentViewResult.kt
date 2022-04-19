package com.tokopedia.recentview.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecentViewResult {
    @SerializedName("get_recent_view")
    @Expose
    var item: RecentViewData = RecentViewData()
}