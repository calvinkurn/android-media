package com.tokopedia.shop.common.data.source.cloud.model.followstatus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(
        @SerializedName("userIsFollowing")
        @Expose
        val userIsFollowing: Boolean?,
        @SerializedName("userNeverFollow")
        @Expose
        val userNeverFollow: Boolean?,
        @SerializedName("userFirstFollow")
        @Expose
        val userFirstFollow: Boolean?,
)