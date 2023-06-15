package com.tokopedia.notifications.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReminderPromptAppDataObj (
    @Expose
    @SerializedName("first_shown")
    var firstShown : Long = 0,
    @Expose
    @SerializedName("last_shown")
    var lastShown : Long = 0,
    @Expose
    @SerializedName("current_count")
    var currentCount : Int = 0
    )

