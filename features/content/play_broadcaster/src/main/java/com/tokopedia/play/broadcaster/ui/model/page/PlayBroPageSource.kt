package com.tokopedia.play.broadcaster.ui.model.page

/**
 * Created By : Jonathan Darwin on November 30, 2022
 */
enum class PlayBroPageSource(val value: String) {
    Live("live"),
    Shorts("shorts"),
    Unknown("");

    companion object {
        fun getByValue(value: String): PlayBroPageSource {
            return values().firstOrNull {
                it.value == value
            } ?: Unknown
        }
    }
}

fun PlayBroPageSource?.orUnknown(): PlayBroPageSource = this ?: PlayBroPageSource.Unknown
