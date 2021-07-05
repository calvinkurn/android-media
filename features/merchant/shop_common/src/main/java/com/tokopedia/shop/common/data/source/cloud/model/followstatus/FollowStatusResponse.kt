package com.tokopedia.shop.common.data.source.cloud.model.followstatus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowStatusResponse(
        @SerializedName("followStatus")
        @Expose
        val followStatus: FollowStatus?
)