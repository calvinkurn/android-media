package com.tokopedia.play.broadcaster.shorts.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on November 17, 2022
 */
data class PlayShortsConfig(
    /** TODO: adjust SerializedName() */
    @SerializedName("")
    val shortsId: String = "",

    @SerializedName("")
    val maxTitleCharacter: Int = 24,
)
