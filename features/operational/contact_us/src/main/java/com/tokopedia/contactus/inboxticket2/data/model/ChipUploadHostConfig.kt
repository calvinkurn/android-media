package com.tokopedia.contactus.inboxticket2.data.model


import com.google.gson.annotations.SerializedName

data class ChipUploadHostConfig(
        @SerializedName("chipUploadHostConfig")
        val chipUploadHostConfig: ChipUploadHostConfig?
) {
    data class ChipUploadHostConfig(
            @SerializedName("data")
            val chipUploadHostConfigData: ChipUploadHostConfigData?,
            @SerializedName("message_error")
            val messageError: List<String>?,
            @SerializedName("status")
            val status: String?
    ) {
        data class ChipUploadHostConfigData(
                @SerializedName("generated_host")
                val generatedHost: GeneratedHost?
        ) {
            data class GeneratedHost(
                    @SerializedName("server_id")
                    val serverId: String?,
                    @SerializedName("token")
                    val token: String?,
                    @SerializedName("upload_secure_host")
                    val uploadSecureHost: String?,
                    @SerializedName("user_id")
                    val userId: String?
            )
        }
    }
}
