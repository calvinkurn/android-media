package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.*

/**
 * Created by jegul on 09/02/21
 */
class PlayLikeModelBuilder {

    fun buildLikeInfo(
            contentId: String = "123",
            contentType: Int = 1,
            likeType: Int = 2,
            status: PlayLikeStatus = PlayLikeStatus.Unknown,
            source: LikeSource = LikeSource.Network
    ) = PlayLikeInfoUiModel(
            contentId = contentId,
            contentType = contentType,
            likeType = likeType,
            status = status,
            source = source,
    )
}