package com.tokopedia.stories.creation.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on October 09, 2023
 */
data class GetStoryProductDetailsRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {

    data class Data(
        @SerializedName("storyID")
        val storyId: String
    )

    companion object {
        fun create(
            storyId: String
        ) = GetStoryProductDetailsRequest(
            req = Data(
                storyId = storyId
            )
        )
    }
}

