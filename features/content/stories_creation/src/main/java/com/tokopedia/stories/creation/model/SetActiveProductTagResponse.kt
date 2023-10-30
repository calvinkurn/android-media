package com.tokopedia.stories.creation.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class SetActiveProductTagResponse(
    @SerializedName("contentCreatorStorySetActiveProductTags")
    val data: Data = Data()
) {

    data class Data(
        @SuppressLint("Invalid Data Type")
        @SerializedName("productIDs")
        val productIDs: List<String> = emptyList(),
    )
}
