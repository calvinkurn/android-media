package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class PriceValidation(
        @SerializedName("is_updated")
        val isUpdated: Boolean = false,
        @SerializedName("message")
        val message: Message = Message(),
        @SerializedName("tracker_data")
        val trackerData: Tracker = Tracker()
)