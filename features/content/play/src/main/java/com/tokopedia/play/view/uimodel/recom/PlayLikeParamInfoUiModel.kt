package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 21/01/21
 */
data class PlayLikeInfoUiModel(
        val param: PlayLikeParamInfoUiModel,
        val status: PlayLikeStatusInfoUiModel
)

data class PlayLikeParamInfoUiModel(
        val contentId: String,
        val contentType: Int,
        val likeType: Int
)

data class PlayLikeStatusInfoUiModel(
        val totalLike: Int,
        val totalLikeFormatted: String,
        val isLiked: Boolean
)

operator fun PlayLikeParamInfoUiModel.plus(status: PlayLikeStatusInfoUiModel): PlayLikeInfoUiModel = PlayLikeInfoUiModel(param = this, status = status)