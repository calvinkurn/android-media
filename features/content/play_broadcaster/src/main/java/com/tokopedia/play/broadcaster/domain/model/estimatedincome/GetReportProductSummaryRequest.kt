package com.tokopedia.play.broadcaster.domain.model.estimatedincome

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created by Jonathan Darwin on 06 March 2024
 */
data class GetReportProductSummaryRequest(
    @SerializedName("contentID")
    val contentId: String,

    @SerializedName("contentType")
    val contentType: String,
) : GqlParam
