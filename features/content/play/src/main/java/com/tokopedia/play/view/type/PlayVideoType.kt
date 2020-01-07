package com.tokopedia.play.view.type

/**
 * Created by jegul on 16/12/19
 */
enum class PlayVideoType(val value: String) {
    Live("live"),
    VOD("vod"),
    Unknown("unknown");

    val isLive: Boolean
    get() = this == Live
}