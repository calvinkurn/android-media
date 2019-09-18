package com.tokopedia.chatbot.data.toolbarpojo

import com.google.gson.annotations.SerializedName

data class ToolbarAttributes(
        @SerializedName("agent_type")
        val agentType: String?,

        @SerializedName("profile_name")
        val profileName: String?,

        @SerializedName("profile_image")
        val profileImage: String?
)
