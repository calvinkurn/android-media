package com.tokopedia.updateinactivephone.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadHostModel (
    var isSuccess: Boolean = false,
    var uploadHostData: UploadHostData? = null,
    var errorMessage: String? = "",
    var statusMessage: String? = "",
    var responseCode: Int = 0,
    val isResponseSuccess: Boolean = false

)

data class UploadHostData (
    @SerializedName("generated_host")
    @Expose
    var generatedHost: GeneratedHost
)

data class GeneratedHost (
    @SerializedName("server_id")
    @Expose
    var serverId: Int = 0,

    @SerializedName("upload_host")
    @Expose
    var uploadHost: String? = ""
)
