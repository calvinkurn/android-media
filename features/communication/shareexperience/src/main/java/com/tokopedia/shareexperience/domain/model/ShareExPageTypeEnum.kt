package com.tokopedia.shareexperience.domain.model

import androidx.annotation.Keep

/**
 * Value for Affiliate link
 * Value Int for get Share Properties
 */
@Keep
enum class ShareExPageTypeEnum(val value: String, val valueInt: Int) {
    PDP("pdp", 1),
    SHOP("shop", 2),
    DISCOVERY("discovery", 3),
    FLIGHT("flight", 4),
    CATALOG("catalog", 5),
    TOKONOW("tokonow", 6),
    WISHLIST("wishlist", 7),
    TOKOFOOD("tokofood", 8),
    PLAY("play", 9),
    FEED("feed", 10),
    CATEGORY("category", 11),
    HELP("help", 12);

    companion object {
        fun fromValue(value: String): ShareExPageTypeEnum {
            return values().find { it.value == value } ?: PDP // default PDP
        }
    }
}
