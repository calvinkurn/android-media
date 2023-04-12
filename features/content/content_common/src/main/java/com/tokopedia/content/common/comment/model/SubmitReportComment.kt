package com.tokopedia.content.common.comment.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 08/02/23
 */
data class SubmitReportComment(
    @SerializedName("feedsComplaintSubmitReport")
    val status: Status,
) {
    data class Status(
        @SerializedName("success")
        val success: Boolean = true,
    )
}
