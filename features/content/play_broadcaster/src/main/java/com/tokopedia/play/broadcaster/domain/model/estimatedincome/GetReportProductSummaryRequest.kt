package com.tokopedia.play.broadcaster.domain.model.estimatedincome

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created by Jonathan Darwin on 06 March 2024
 */
data class GetReportProductSummaryRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {

    data class Data(
        @SerializedName("contentID")
        val contentId: String,

        @SerializedName("contentType")
        val contentType: String,
    )
    companion object {
        fun create(
            contentId: String,
            contentType: Type,
        ): GetReportProductSummaryRequest {
            return GetReportProductSummaryRequest(
                req = Data(
                    contentId = contentId,
                    contentType = contentType.value,
                )
            )
        }
    }

    enum class Type(val value: String) {
        Story("story"),
        Play("play"),
    }
}
