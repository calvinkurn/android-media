package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 21/01/21
 */
enum class LikeSource {

    Network,
    UserAction,
    Storage
}

data class PlayLikeInfoUiModel(
    val contentId: String = "",
    val contentType: Int = 0,
    val likeType: Int = 0,
    val status: PlayLikeStatus = PlayLikeStatus.Unknown,
    val source: LikeSource = LikeSource.Storage,
    val likeBubbleConfig: PlayLikeBubbleConfig = PlayLikeBubbleConfig(),
)

data class PlayLikeBubbleConfig(
    val bubbleMap: Map<String, List<Int>> = emptyMap(),
)

enum class PlayLikeStatus {

    Unknown,
    Liked,
    NotLiked
}