package com.tokopedia.media.loader.data

enum class FailureType(val value: String) {
    NotFound("Not-Found"),
    Gone("Gone"),
    BadUrl("Bad-URL"),
    Unknown("Unknown");

    companion object {
        private val map = values().associateBy(FailureType::value)

        @JvmStatic
        fun fromString(value: String) = map[value]
    }
}
