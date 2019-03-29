package com.tokopedia.gm.subscribe.membership.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseSetSubscription(
        @SerializedName("goldSetSubscription")
        @Expose
        var goldSetSubscription: SetMembershipData = SetMembershipData()
)