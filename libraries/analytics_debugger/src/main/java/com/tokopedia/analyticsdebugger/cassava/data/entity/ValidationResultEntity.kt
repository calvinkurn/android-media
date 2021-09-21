package com.tokopedia.analyticsdebugger.cassava.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 11/04/2021
 */
data class ValidationResultEntity(
        @SerializedName("status")
        @Expose
        val status: Boolean = false
)