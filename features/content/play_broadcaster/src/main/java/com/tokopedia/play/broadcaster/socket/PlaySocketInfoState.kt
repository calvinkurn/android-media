package com.tokopedia.play.broadcaster.socket


/**
 * Created by mzennis on 22/06/20.
 */
sealed class PlaySocketInfoState {

    object Active : PlaySocketInfoState()

    object ReConnect : PlaySocketInfoState()

    object Close : PlaySocketInfoState()

    data class Error(val throwable: Throwable) : PlaySocketInfoState()
}