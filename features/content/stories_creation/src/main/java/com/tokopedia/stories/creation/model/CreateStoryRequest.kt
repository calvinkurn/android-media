package com.tokopedia.stories.creation.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class CreateStoryRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {
    data class Data(
        @SerializedName("authorID")
        val authorId: String,

        @SerializedName("authorType")
        val authorType: Int,
    )

    companion object {
        fun create(
            authorId: String,
            authorType: Int,
        ) = CreateStoryRequest(
            req = Data(
                authorId = authorId,
                authorType = authorType,
            )
        )
    }
}
