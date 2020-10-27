package com.tokopedia.updateinactivephone.revamp.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadHostDataModel (
        @SerializedName("data")
        var data: UploadHostData = UploadHostData()
) {
    data class UploadHostData (
            @SerializedName("generated_host")
            @Expose
            var generatedHost: GeneratedHost = GeneratedHost()
    )

    data class GeneratedHost (
            @SerializedName("server_id")
            @Expose
            var serverId: Int = 0,

            @SerializedName("upload_host")
            @Expose
            var uploadHost: String = ""
    )
}
