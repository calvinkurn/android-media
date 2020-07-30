package com.tokopedia.play.view.type

/**
 * Created by jegul on 14/01/20
 */
sealed class PlayRoomEvent {

    data class Freeze(val title: String, val message: String, val btnTitle: String, val btnUrl: String) : PlayRoomEvent()
    data class Banned(val title: String, val message: String, val btnTitle: String) : PlayRoomEvent()

    val isFreeze
        get() = this is Freeze

    val isBanned
        get() = this is Banned
}

