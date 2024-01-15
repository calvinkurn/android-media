package com.tokopedia.shareexperience.domain.model

/**
 * App Name for sorting
 * Label for Short Link
 * Id for Affiliate Link, 0 means not defined (others but not lainnya)
 */
enum class ShareExChannelEnum(
    val appName: String,
    val label: String,
    val id: Int
) {
    WHATSAPP("WhatsApp", "whatsapp", 12),
    TELEGRAM("Telegram", "telegram", 8),
    LINE("Line", "line", 4),
    IG_STORY("IG_Story", "igstory", 17),
    IG_FEED("IG_Feed", "igfeed", 16),
    IG_DM("IG_DM", "igmessage", 0),
    FB_STORY("FB_Story", "fbstory", 15),
    FB_FEED("FB_Feed", "fbfeed", 14),
    X_TWITTER("Twitter", "twitter", 10),
    COPY_LINK("", "salinlink", 18),
    SMS("", "sms", 6),
    EMAIL("", "email", 19),
    OTHERS("", "lainnya", 20)
}
