package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-17.
 */
data class SetOnboardingStatusData(
        @SerializedName("success")
        @Expose
        val isSuccess: Boolean
)