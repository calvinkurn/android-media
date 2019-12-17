package com.tokopedia.play.view.type

/**
 * Created by jegul on 16/12/19
 */
enum class PlayVideoType {

    Live,
    VOD,
    Unknown;

    val isLive: Boolean
    get() = this == Live
}