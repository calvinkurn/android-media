package com.tokopedia.stories.creation.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class SetActiveProductTagResponse(
    @SerializedName("contentCreatorSetActiveProductTag")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("productIDs")
        val productIDs: List<String> = emptyList(),
    )
}
