package com.tokopedia.common_digital.common.presentation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 01/07/19.
 */
class RechargePushEventRecommendationResponseEntity {

    @SerializedName("Message")
    @Expose
    val message: String? = null

    @SerializedName("IsError")
    @Expose
    val isError = false
}