package com.tokopedia.profilecompletion.addbod.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

data class AddBodData(
        @SerializedName("isSuccess")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("birthDateMessage")
        @Expose
        var birthDateMessage: String = "",
        @SerializedName("completionScore")
        @Expose
        var completionScore: Int = 0
)