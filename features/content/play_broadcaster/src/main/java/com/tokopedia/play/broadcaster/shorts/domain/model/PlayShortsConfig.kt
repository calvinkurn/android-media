package com.tokopedia.play.broadcaster.shorts.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on November 17, 2022
 */
data class PlayShortsConfig(
    @SerializedName("draft_short_video")
    val draftShortsId: Int = 0,

    @SerializedName("max_title_length")
    val maxTitleCharacter: Int = 0,

    @SerializedName("max_tagged_product")
    val maxTaggedProduct: Int = 0,

    @SerializedName("short_video_source_id")
    val shortVideoSourceId: String = ""
)
