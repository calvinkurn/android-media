package com.tokopedia.settingbank.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadDocumentResponse(
        @SerializedName("status")
        @Expose
        val status: Int,
        @SerializedName("message")
        @Expose
        val message: String

)
