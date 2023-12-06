package com.tokopedia.creation.common.upload.model.dto.stories

import com.google.gson.annotations.SerializedName
import com.tokopedia.creation.common.upload.model.stories.StoriesStatus
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
data class StoriesUpdateStoryRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {

    data class Data(
        @SerializedName("storyID")
        val storyId: String,

        @SerializedName("activeMediaID")
        val activeMediaId: String,

        @SerializedName("status")
        val status: Int,
    )

    companion object {
        fun create(
            storyId: String,
            activeMediaId: String,
            status: StoriesStatus,
        ) = StoriesUpdateStoryRequest(
            req = Data(
                storyId = storyId,
                activeMediaId = activeMediaId,
                status = status.value,
            )
        )
    }
}
