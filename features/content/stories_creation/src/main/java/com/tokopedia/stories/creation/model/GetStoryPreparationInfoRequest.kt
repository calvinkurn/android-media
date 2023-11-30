package com.tokopedia.stories.creation.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class GetStoryPreparationInfoRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {

    data class Data(
        @SerializedName("author")
        val author: Author,
    )

    data class Author(
        @SerializedName("ID")
        val authorId: String,

        @SerializedName("type")
        val authorType: Int,
    )

    companion object {
        fun create(
            authorId: String,
            authorType: Int,
        ) = GetStoryPreparationInfoRequest(
            req = Data(
                author = Author(
                    authorId = authorId,
                    authorType = authorType,
                )
            )
        )
    }
}
