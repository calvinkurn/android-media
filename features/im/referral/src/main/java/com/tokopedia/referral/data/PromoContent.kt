package com.tokopedia.referral.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ashwanityagi on 08/11/17.
 */

class PromoContent {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("friend_count")
    @Expose
    var friendCount: String? = null

    @SerializedName("url")
    @Expose
    var shareUrl: String? = null
}
