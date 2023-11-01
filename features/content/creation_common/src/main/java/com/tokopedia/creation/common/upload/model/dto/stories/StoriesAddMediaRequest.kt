package com.tokopedia.creation.common.upload.model.dto.stories

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
data class StoriesAddMediaRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {

    data class Data(
        @SerializedName("storyID")
        val storyId: String,

        @SerializedName("type")
        val type: Int,

        @SerializedName("videoURL")
        val videoURL: String,

        @SerializedName("imageUploadID")
        val imageUploadID: String,

        @SerializedName("requestID")
        val requestID: String,

        @SerializedName("status")
        val status: Int,

        @SerializedName("orientation")
        val orientation: Int,
    )

    enum class Status(val value: Int) {
        Hidden(0),
        Active(1)
    }

    enum class Orientation(val value: Int) {
        Potrait(0),
        Landscape(1),
    }

    companion object {
        fun create(
            storyId: String,
            type: Int,
            videoURL: String,
            imageUploadID: String,
            requestID: String,
            status: Status,
            orientation: Orientation,
        ) = StoriesAddMediaRequest(
            req = Data(
                storyId = storyId,
                type = type,
                videoURL = videoURL,
                imageUploadID = imageUploadID,
                requestID = requestID,
                status = status.value,
                orientation = orientation.value,
            )
        )
    }
}
