package com.tokopedia.shareexperience.domain.model.channel

/**
 * ID for sorting
 * Label for Short Link
 */
enum class ShareExChannelEnum(
    val id: String,
    val label: String
) {
    WHATSAPP("WhatsApp", "whatsapp"),
    TELEGRAM("Telegram", "telegram"),
    LINE("Line", "line"),
    IG_STORY("IG_Story", "igstory"),
    IG_FEED("IG_Feed", "igfeed"),
    IG_DM("IG_DM", "igmessage"),
    FB_STORY("FB_Story", "fbstory"),
    FB_FEED("FB_Feed", "fbfeed"),
    X_TWITTER("Twitter", "twitter"),
    COPY_LINK("", "salinlink"),
    SMS("", "sms"),
    EMAIL("", "email"),
    OTHERS("", "lainnya")
}
