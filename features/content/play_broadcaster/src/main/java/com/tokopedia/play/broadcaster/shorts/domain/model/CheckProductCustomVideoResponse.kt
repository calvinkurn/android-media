package com.tokopedia.play.broadcaster.shorts.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
data class CheckProductCustomVideoResponse(
    @SerializedName("broadcasterCheckPDPCustomVideo")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("hasVideo")
        val hasVideo: Boolean = false,

        @SerializedName("videoURL")
        val videoUrl: String = "",

        @SerializedName("coverURL")
        val coverUrl: String = "",
    )
}
