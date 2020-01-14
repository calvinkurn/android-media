package com.tokopedia.play.view.type

/**
 * Created by jegul on 14/01/20
 */
enum class PlayRoomEvent {

    Freeze,
    Banned;

    val isFreeze: Boolean
        get() = this == Freeze

    val isBanned: Boolean
        get() = this == Banned
}