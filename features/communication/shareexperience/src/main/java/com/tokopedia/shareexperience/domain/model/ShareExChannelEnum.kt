package com.tokopedia.shareexperience.domain.model

/**
 * This is the hardcoded channel data, the purpose is for the static values and default view
 ** App Name for sorting
 ** Label for Short Link
 ** Id for Affiliate Link
 ** Tracker name for trackers only
 *
 *  affiliate id
 *    "others":   0,
 *    "facebook": 1,
 *    "goplay":   2,
 *    "instagram": 3,
 *    "line":     4,
 *    "linkedin": 5,
 *    "sms":      6,
 *    "signal":   7,
 *    "telegram": 8,
 *    "tiktok":   9,
 *    "twitter":  10,
 *    "website":  11,
 *    "whatsapp": 12,
 *    "youtube":  13,
 *    "fbfeed":   14,
 *    "fbstory":  15,
 *    "igfeed":   16,
 *    "igstory":  17,
 *    "salinlink": 18,
 *    "email":    19,
 *    "lainnya":  20,
 *    "play":     21,
 *    "video":    22, // not used
 * 	  "igdm" :    23,
 * 	  "tokopediablog" : 24, // not used
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
    IG_DM("IG_DM", "igdm", 23, "Instagram Message"),
    FB_STORY("FB_Story", "fbstory", 15, "Facebook Story"),
    FB_FEED("FB_Feed", "fbfeed", 14, "Facebook NewsFeed"),
    X_TWITTER("Twitter", "twitter", 10, "Twitter"),
    COPY_LINK("", "salinlink", 18, "Salin Link"),
    SMS("", "sms", 6, "SMS"),
    EMAIL("", "email", 19, "Email"),
    OTHERS("", "lainnya", 20, "Lainnya")
}
