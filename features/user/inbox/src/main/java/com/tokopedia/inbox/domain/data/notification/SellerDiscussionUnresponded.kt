package com.tokopedia.inbox.domain.data.notification

import com.google.gson.annotations.SerializedName

class SellerDiscussionUnresponded(
        @SerializedName("total_int")
        var totalInt: Int = 0
)