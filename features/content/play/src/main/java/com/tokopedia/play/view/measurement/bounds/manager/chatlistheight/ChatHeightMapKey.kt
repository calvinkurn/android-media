package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/09/20
 */
data class ChatHeightMapKey(
        val videoOrientation: VideoOrientation,
        val isChatMode: Boolean
)