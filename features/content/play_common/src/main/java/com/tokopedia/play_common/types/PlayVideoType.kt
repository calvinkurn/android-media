package com.tokopedia.play_common.types

/**
 * Created by jegul on 23/01/20
 */
enum class PlayVideoType(val value: String) {
    Live("live"),
    VOD("vod"),
    Unknown("unknown");

    val isLive: Boolean
        get() = this == Live

    val isVod: Boolean
        get() = this == VOD
}