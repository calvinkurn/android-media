package com.tokopedia.notifications.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReminderPromptFreqRemoteConfObj (
    @Expose
    @SerializedName("page_name")
    val pageName : String? = "",
    @Expose
    @SerializedName("freq")
    val freq : Int = 0,
    @Expose
    @SerializedName("persistence_duration")
    val persistenceDuration: Float = 0f, //in days
    @Expose
    @SerializedName("cool_off_duration")
    val coolOffDuration: Float = 0f,
    @Expose
    @SerializedName("reset")
    val reset: Boolean = false
)
