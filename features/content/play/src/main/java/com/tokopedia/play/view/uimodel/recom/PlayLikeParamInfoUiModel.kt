package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 21/01/21
 */
sealed class PlayLikeInfoUiModel {

    abstract val param: PlayLikeParamInfoUiModel

    data class Incomplete(override val param: PlayLikeParamInfoUiModel) : PlayLikeInfoUiModel()
    data class Complete(
            override val param: PlayLikeParamInfoUiModel,
            val status: PlayLikeStatusInfoUiModel
    ) : PlayLikeInfoUiModel()
}

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

operator fun PlayLikeParamInfoUiModel.plus(status: PlayLikeStatusInfoUiModel) = PlayLikeInfoUiModel.Complete(param = this, status = status)