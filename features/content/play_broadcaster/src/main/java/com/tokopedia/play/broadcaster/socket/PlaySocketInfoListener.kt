package com.tokopedia.play.broadcaster.socket


/**
 * Created by mzennis on 22/06/20.
 */
interface PlaySocketInfoListener {

    fun onActive() { }

    fun onReceive(data: PlaySocketType)

    fun onReconnect() { }

    fun onClose() { }

    fun onError(throwable: Throwable) { }
}