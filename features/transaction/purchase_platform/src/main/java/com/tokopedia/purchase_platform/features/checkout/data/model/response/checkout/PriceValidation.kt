package com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-12.
 */

data class PriceValidation(
        @SerializedName("is_updated")
        val isUpdated: Boolean = false,

        @SerializedName("message")
        val message: Message? = null,

        @SerializedName("tracker_data")
        val trackerData: Tracker? = null
)