package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class StatusActivity(
    @SerializedName("source")
    @Expose
    val source: String = "",

    @SerializedName("activity")
    @Expose
    val activity: String = "",

    @SerializedName("amount")
    @Expose
    val amount: Int = 0
)
