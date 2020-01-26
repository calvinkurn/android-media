package com.tokopedia.sellerhomedrawer.data.userdata.notifications

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Inbox (

    @SerializedName("talk")
    @Expose
    var inboxTalk: Int = 0,
    @SerializedName("ticket")
    @Expose
    var inboxTicket: Int = 0,
    @SerializedName("review")
    @Expose
    var inboxReview: Int = 0,
    @SerializedName("friend")
    @Expose
    var inboxFriend: Int = 0,
    @SerializedName("wishlist")
    @Expose
    var inboxWishlist: Int = 0,
    @SerializedName("message")
    @Expose
    var inboxMessage: Int = 0,
    @SerializedName("reputation")
    @Expose
    var inboxReputation: Int = 0

)
