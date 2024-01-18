package com.tokopedia.shareexperience.data.util

import androidx.annotation.Keep

@Keep
enum class ShareExPageTypeEnum(val value: Int) {
    PDP(1),
    SHOP(2),
    DISCOVERY(3);

    companion object {
        fun fromValue(value: Int): ShareExPageTypeEnum {
            return values().find { it.value == value } ?: PDP // default PDP
        }
    }
}
