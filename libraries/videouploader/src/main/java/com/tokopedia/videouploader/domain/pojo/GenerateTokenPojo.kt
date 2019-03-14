package com.tokopedia.videouploader.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 13/03/19.
 */
data class GenerateTokenPojo(
        @SerializedName("token")
        @Expose
        val token : String = ""
)