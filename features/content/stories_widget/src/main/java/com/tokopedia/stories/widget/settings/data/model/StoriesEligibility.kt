package com.tokopedia.stories.widget.settings.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 3/25/24
 */
data class StoriesEligibility(
    @SerializedName("contentCreatorStoryGetAuthorConfig")
    val data: Response = Response()
) {
    data class Response(
        @SerializedName("isEligibleToCreateAutomaticStory")
        val isEligibleForAuto: Boolean = false,
        @SerializedName("isEligibleToCreateManualStory")
        val isEligibleForManual: Boolean = false,
    )
}
