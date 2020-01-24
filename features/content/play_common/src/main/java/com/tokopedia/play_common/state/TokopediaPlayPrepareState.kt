package com.tokopedia.play_common.state

import android.net.Uri
import com.tokopedia.play_common.types.TokopediaPlayVideoType

/**
 * Created by jegul on 15/01/20
 */
sealed class TokopediaPlayPrepareState {

    data class Prepared(val uri: Uri) : TokopediaPlayPrepareState()
    data class Unprepared(val previousUri: Uri?, val previousType: TokopediaPlayVideoType, val lastPosition: Long?) : TokopediaPlayPrepareState()
}