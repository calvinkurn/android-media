package com.tokopedia.media.loader.data

enum class FailureType(val value: String) {
    NotFound("not-found"),
    Gone("gone"),
    BadUrl("bad-url"),
    Unknown("unknown");

    companion object {
        private val map = values().associateBy(FailureType::value)

        @JvmStatic
        fun fromString(value: String) = map[value]
    }
}
