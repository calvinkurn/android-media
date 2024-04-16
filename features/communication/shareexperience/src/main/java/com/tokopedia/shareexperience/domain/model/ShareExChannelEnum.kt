package com.tokopedia.shareexperience.domain.model

/**
 * This is the hardcoded channel data, the purpose is for the static values and default view
 ** App Name for sorting
 ** Label for Short Link & Tracker
 ** Id for Affiliate Link
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
    val id: Long
) {
    WHATSAPP("WhatsApp", "whatsapp", 12),
    TELEGRAM("Telegram", "telegram", 8),
    LINE("Line", "line", 4),
    IG_STORY("IG_Story", "igstory", 17),
    IG_FEED("IG_Feed", "igfeed", 16),
    IG_DM("IG_DM", "igdm", 23),
    FB_STORY("FB_Story", "fbstory", 15),
    FB_FEED("FB_Feed", "fbfeed", 14),
    X_TWITTER("Twitter", "twitter", 10),
    COPY_LINK("", "salinlink", 18),
    SMS("", "sms", 6),
    EMAIL("", "email", 19),
    OTHERS("", "lainnya", 20)
}
