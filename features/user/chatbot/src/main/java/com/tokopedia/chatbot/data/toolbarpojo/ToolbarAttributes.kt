package com.tokopedia.chatbot.data.toolbarpojo


import com.google.gson.annotations.SerializedName

data class ToolbarAttributes(
    @SerializedName("agent_type")
    val agentType: String? = "",
    @SerializedName("badge_image")
    val badgeImage: BadgeImage?,
    @SerializedName("profile_image")
    val profileImage: String? = "",
    @SerializedName("profile_image_dark")
    val profileImageDark: String? = "",
    @SerializedName("profile_name")
    val profileName: String?
) {
    data class BadgeImage(
        @SerializedName("dark")
        val dark: String? = "",
        @SerializedName("light")
        val light: String? = ""
    )
}