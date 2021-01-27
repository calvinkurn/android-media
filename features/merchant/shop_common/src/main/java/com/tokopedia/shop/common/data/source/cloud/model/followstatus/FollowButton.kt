package com.tokopedia.shop.common.data.source.cloud.model.followstatus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowButton(
        @SerializedName("buttonLabel")
        @Expose
        val buttonLabel: String?,
        @SerializedName("voucherIconURL")
        @Expose
        val voucherIconURL: String?,
        @SerializedName("coachmarkText")
        @Expose
        val coachmarkText: String?,
)
