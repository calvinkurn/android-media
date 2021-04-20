package com.tokopedia.talk.feature.sellersettings.template.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TemplateMutationResult(
        @SerializedName("success")
        @Expose
        val success: Int = 0
) {
    fun isMutationSuccess(): Boolean {
        return success == 1
    }
}