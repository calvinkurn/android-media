package com.tokopedia.play.broadcaster.ui.model.tag

/**
 * Created by jegul on 20/04/21
 */
data class PlayTagUiModel(
    val tags: Set<PlayTagItem>,
    val minTags: Int,
    val maxTags: Int,
)
