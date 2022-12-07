package com.tokopedia.play.analytic.like

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.recom.PlayLikeStatus

/**
 * Created by jegul on 23/08/21
 */
interface PlayLikeAnalytic {

    fun clickLike(
        channelId: String,
        channelType: PlayChannelType,
        channelName: String,
        likeStatus: PlayLikeStatus,
    )
}