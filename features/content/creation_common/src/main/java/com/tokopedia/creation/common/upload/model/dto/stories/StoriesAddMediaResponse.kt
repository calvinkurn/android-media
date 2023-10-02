package com.tokopedia.creation.common.upload.model.dto.stories

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
data class StoriesAddMediaResponse(
    @SerializedName("contentCreatorStoryAddMedia")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("mediaID")
        val mediaId: String = ""
    )
}
