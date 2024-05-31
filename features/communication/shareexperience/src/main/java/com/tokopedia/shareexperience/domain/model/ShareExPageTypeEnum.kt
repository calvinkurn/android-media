package com.tokopedia.shareexperience.domain.model

import androidx.annotation.Keep

/**
 * Value for Affiliate link
 * Value Int for get Share Properties
 */
@Keep
enum class ShareExPageTypeEnum(val value: String, val valueInt: Int) {
    OTHERS("others", 0),
    PDP("pdp", 1),
    REVIEW("review", 2),
    SHOP("shop", 3),
    DISCOVERY("discovery", 4),
    ORDER_DETAIL("pdp", 6),
    GOPAYLATER_REFERRAL("gopaylater referral", 7),
    THANK_YOU_PRODUCT("pdp", 8);

    companion object {

        fun fromValue(value: String): ShareExPageTypeEnum {
            return values().find { it.value == value } ?: OTHERS // default
        }

        fun fromValueInt(valueInt: Int): ShareExPageTypeEnum {
            return values().find { it.valueInt == valueInt } ?: OTHERS // default
        }
    }
}
