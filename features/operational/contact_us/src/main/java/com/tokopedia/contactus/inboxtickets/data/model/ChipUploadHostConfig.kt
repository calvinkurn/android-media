package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName

data class ChipUploadHostConfig(
    @SerializedName("chipUploadHostConfig")
    val chipUploadHostConfig: ChipUploadHostConfig? = null
) {
    data class ChipUploadHostConfig(
        @SerializedName("data")
        val chipUploadHostConfigData: ChipUploadHostConfigData? = null,
        @SerializedName("message_error")
        val messageError: List<String>? = null,
        @SerializedName("status")
        val status: String? = null
    ) {
        data class ChipUploadHostConfigData(
            @SerializedName("generated_host")
            val generatedHost: GeneratedHost? = null
        ) {
            data class GeneratedHost(
                @SerializedName("server_id")
                val serverId: String? = null,
                @SerializedName("token")
                val token: String? = null,
                @SerializedName("upload_secure_host")
                val uploadSecureHost: String? = null,
                @SerializedName("user_id")
                val userId: String? = null
            ) {
                fun getServerID() = serverId.orEmpty()

                fun getSecureHost() = uploadSecureHost.orEmpty()
            }

            fun getHost() = generatedHost ?: GeneratedHost()
        }

        fun getUploadHostConfigData() = chipUploadHostConfigData ?: ChipUploadHostConfigData()

        fun getErrorMessage() = messageError?.get(0).orEmpty()
    }

    fun getUploadHostConfig() = chipUploadHostConfig ?: ChipUploadHostConfig()
}
