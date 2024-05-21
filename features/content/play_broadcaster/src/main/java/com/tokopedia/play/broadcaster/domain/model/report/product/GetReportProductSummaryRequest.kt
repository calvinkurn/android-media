package com.tokopedia.play.broadcaster.domain.model.report.product

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.content.common.track.usecase.ContentType

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
            contentType: ContentType,
        ): GetReportProductSummaryRequest {
            return GetReportProductSummaryRequest(
                req = Data(
                    contentId = contentId,
                    contentType = contentType.value,
                )
            )
        }
    }
}
