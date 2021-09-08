package com.tokopedia.play.view.uimodel.recom.multiplelikes

/**
 * Created by jegul on 08/09/21
 */
data class PlayMultipleLikesConfig(
    val bubbles: List<PlayLikeBubbleUiModel> = emptyList(),
)

data class PlayLikeBubbleUiModel(
    val icon: String,
    val bgColor: String,
)