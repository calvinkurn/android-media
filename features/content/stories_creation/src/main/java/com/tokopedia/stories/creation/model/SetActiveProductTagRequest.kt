package com.tokopedia.stories.creation.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class SetActiveProductTagRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {

    data class Data(
        @SerializedName("storyID")
        val storyId: String,

        @SuppressLint("Invalid Data Type")
        @SerializedName("productIDs")
        val productIds: List<String>
    )

    companion object {
        fun create(
            storyId: String,
            productIds: List<String>
        ) = SetActiveProductTagRequest(
            req = Data(
                storyId = storyId,
                productIds = productIds,
            )
        )
    }
}
