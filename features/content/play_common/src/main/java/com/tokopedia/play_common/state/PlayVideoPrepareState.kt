package com.tokopedia.play_common.state

import android.net.Uri
import com.tokopedia.play_common.types.PlayVideoType

/**
 * Created by jegul on 15/01/20
 */
sealed class PlayVideoPrepareState {

    data class Prepared(val uri: Uri, val positionHandle: VideoPositionHandle) : PlayVideoPrepareState()
    data class Unprepared(
            val previousUri: Uri?,
            val previousType: PlayVideoType,
            val lastPosition: Long?,
            val resetState: Boolean
    ) : PlayVideoPrepareState()
}