package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("user_id")
    @Expose
    val userId: Long = 0L,

    @SerializedName("full_name")
    @Expose
    val fullName: String = "",

    @SerializedName("user_email")
    @Expose
    val userEmail: String = "",

    @SerializedName("user_status")
    @Expose
    val userStatus: Int = 0,

    @SerializedName("user_url")
    @Expose
    val userUrl: String = "",

    @SerializedName("UserLabel")
    @Expose
    val userLabel: String = "",

    @SerializedName("user_profile_pict")
    @Expose
    val userProfilePict: String = "",

    @SerializedName("user_reputation")
    @Expose
    val userReputation: UserReputation = UserReputation()
)