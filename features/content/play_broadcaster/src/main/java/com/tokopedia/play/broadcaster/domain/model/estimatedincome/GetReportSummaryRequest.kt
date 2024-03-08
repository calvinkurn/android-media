package com.tokopedia.play.broadcaster.domain.model.estimatedincome

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created by Jonathan Darwin on 08 March 2024
 */
data class GetReportSummaryRequest(
    @SerializedName("contentIDs")
    val contentIDs: List<String>,

    @SerializedName("contentType")
    val contentType: String,
) : GqlParam
