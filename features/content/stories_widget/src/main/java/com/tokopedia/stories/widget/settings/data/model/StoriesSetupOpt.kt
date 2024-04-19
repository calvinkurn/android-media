package com.tokopedia.stories.widget.settings.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 3/25/24
 */
data class StoriesSetupOpt(
    @SerializedName("contentCreatorStorySetAuthorOptions")
    val response: Response = Response()
) {
    data class Response(
        @SerializedName("success")
        val success: Boolean = false,
    )
}
