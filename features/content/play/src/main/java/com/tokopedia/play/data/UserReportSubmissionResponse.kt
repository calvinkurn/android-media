package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 13/12/21
 */
data class UserReportSubmissionResponse(
    @SerializedName("visionPostReportVideoPlay")
    val submissionReport: Result = Result()
) {
    data class Result(
        @SerializedName("status")
        val status: String = ""
    )
}