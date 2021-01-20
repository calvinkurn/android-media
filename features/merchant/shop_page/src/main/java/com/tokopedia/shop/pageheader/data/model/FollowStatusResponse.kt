package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowStatusResponse(
        @SerializedName("followStatus")
        @Expose
        val followStatus: FollowStatus?
)

data class FollowStatus(
        @SerializedName("status")
        @Expose
        val status: Status?,
        @SerializedName("followButton")
        @Expose
        val followButton: FollowButton?,
        @SerializedName("toaster")
        @Expose
        val toaster: Toaster?,
        @SerializedName("error")
        @Expose
        val error: Error?,
)

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

data class Toaster(
        @SerializedName("toasterText")
        @Expose
        val toasterText: String?,
        @SerializedName("buttonLabel")
        @Expose
        val buttonLabel: String?,
        @SerializedName("url")
        @Expose
        val url: String?,
        @SerializedName("appLink")
        @Expose
        val appLink: String?,
)

data class Error(
        @SerializedName("message")
        @Expose
        val message: String?
)