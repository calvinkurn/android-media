package com.tokopedia.gm.subscribe.membership.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseGetSubscription(
        @SerializedName("goldGetSubscription")
        @Expose
        var goldGetSubscription: GetMembershipData = GetMembershipData()
)