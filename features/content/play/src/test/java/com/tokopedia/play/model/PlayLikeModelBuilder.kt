package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.LikeSource
import com.tokopedia.play.view.uimodel.recom.PlayLikeInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeParamInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeStatusInfoUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayLikeModelBuilder {

    fun buildParam(
            contentId: String = "123",
            contentType: Int = 1,
            likeType: Int = 2
    ) = PlayLikeParamInfoUiModel(
            contentId = contentId,
            contentType = contentType,
            likeType = likeType
    )

    fun buildStatus(
            totalLike: Long = 1,
            totalLikeFormatted: String = "1",
            isLiked: Boolean = false,
            source: LikeSource = LikeSource.Network
    ) = PlayLikeStatusInfoUiModel(
            totalLike = totalLike,
            totalLikeFormatted = totalLikeFormatted,
            isLiked = isLiked,
            source = source
    )

    fun buildIncompleteData(
            param: PlayLikeParamInfoUiModel = buildParam()
    ) = PlayLikeInfoUiModel.Incomplete(param = param)

    fun buildCompleteData(
            param: PlayLikeParamInfoUiModel = buildParam(),
            status: PlayLikeStatusInfoUiModel = buildStatus()
    ) = PlayLikeInfoUiModel.Complete(
            param = param,
            status = status
    )
}