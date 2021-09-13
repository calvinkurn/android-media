package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserData {
    @SerializedName("user_id")
    @Expose
    var userId = 0

    @SerializedName("full_name")
    @Expose
    var fullName: String? = null

    @SerializedName("user_email")
    @Expose
    var userEmail: String? = null

    @SerializedName("user_status")
    @Expose
    var userStatus = 0

    @SerializedName("user_url")
    @Expose
    var userUrl: String? = null

    @SerializedName("UserLabel")
    @Expose
    var userLabel: String? = null

    @SerializedName("user_profile_pict")
    @Expose
    var userProfilePict: String? = null

    @SerializedName("user_reputation")
    @Expose
    var userReputation: UserReputation? = null
}