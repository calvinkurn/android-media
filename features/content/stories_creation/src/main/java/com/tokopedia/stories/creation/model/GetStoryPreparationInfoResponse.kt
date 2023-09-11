package com.tokopedia.stories.creation.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class GetStoryPreparationInfoResponse(
    @SerializedName("contentCreatorStoryGetPreparationInfo")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("config")
        val config: String = ""
    )
}
