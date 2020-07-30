package com.tokopedia.play_common.util


/**
 * Created by mzennis on 2020-01-28.
 */
sealed class PlayConnectionState{
    object UnAvailable: PlayConnectionState()
    object Available: PlayConnectionState()
}