package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 22/04/21
 */
data class SetChannelTagsResponse(
        @SerializedName("broadcasterSetChannelTags")
        val recommendedTags: SetTags = SetTags()
) {

    data class SetTags(
            @SerializedName("success")
            val success: Boolean = false
    )
}