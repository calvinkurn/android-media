package com.tokopedia.shareexperience.domain.model

/**
 * App Name for sorting
 * Label for Short Link
 * Id for Affiliate Link, 0 means not defined (others but not lainnya)
 * Tracker name for trackers only
 */
enum class ShareExChannelEnum(
    val appName: String,
    val label: String,
    val id: Long,
    val trackerName: String
) {
    WHATSAPP("WhatsApp", "whatsapp", 12, "WhatsApp"),
    TELEGRAM("Telegram", "telegram", 8, "Telegram"),
    LINE("Line", "line", 4, "Line"),
    IG_STORY("IG_Story", "igstory", 17, "Instagram Story"),
    IG_FEED("IG_Feed", "igfeed", 16, "Instagram Feed"),
    IG_DM("IG_DM", "igmessage", 0, "Instagram Message"),
    FB_STORY("FB_Story", "fbstory", 15, "Facebook Story"),
    FB_FEED("FB_Feed", "fbfeed", 14, "Facebook NewsFeed"),
    X_TWITTER("Twitter", "twitter", 10, "Twitter"),
    COPY_LINK("", "salinlink", 18, "Salin Link"),
    SMS("", "sms", 6, "SMS"),
    EMAIL("", "email", 19, "Email"),
    OTHERS("", "lainnya", 20, "Lainnya")
}
