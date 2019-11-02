package com.tokopedia.gamification.util

import java.util.regex.Pattern

class HexValidator {

    companion object {
        private const val HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"

        @JvmStatic
        fun validate(hex: String?): Boolean {
            if (hex == null)
                return false
            val pattern = Pattern.compile(HEX_PATTERN)
            val matcher = pattern.matcher(hex)
            return matcher.matches()
        }
    }
}