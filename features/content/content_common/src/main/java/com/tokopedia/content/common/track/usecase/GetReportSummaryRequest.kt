package com.tokopedia.content.common.track.usecase

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created by Jonathan Darwin on 08 March 2024
 */
data class GetReportSummaryRequest(
    @SuppressLint("Invalid Data Type")
    @SerializedName("contentIDs")
    val contentIDs: List<String>,

    @SerializedName("contentType")
    val contentType: String,
) : GqlParam {
    companion object {
        fun create(
            contentId: String,
            contentType: ContentType,
        ): GetReportSummaryRequest {
            return GetReportSummaryRequest(
                contentIDs = listOf(contentId),
                contentType = contentType.value,
            )
        }
    }
}
