package com.tokopedia.play_common.state

/**
 * Created by jegul on 10/03/20
 */
sealed class VideoPositionHandle {

    data class NotHandled(val lastPosition: Long?) : VideoPositionHandle()
    object Handled : VideoPositionHandle()
}