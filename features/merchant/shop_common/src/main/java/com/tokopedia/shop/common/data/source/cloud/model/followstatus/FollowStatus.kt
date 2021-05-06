package com.tokopedia.shop.common.data.source.cloud.model.followstatus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowStatus(
        @SerializedName("status")
        @Expose
        val status: Status?,
        @SerializedName("followButton")
        @Expose
        val followButton: FollowButton?,
        @SerializedName("error")
        @Expose
        val error: Error?,
)