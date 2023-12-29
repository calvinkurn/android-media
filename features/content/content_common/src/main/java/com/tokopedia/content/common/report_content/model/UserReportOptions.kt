package com.tokopedia.content.common.report_content.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 09/12/21
 */
data class UserReportOptions(
    @SuppressLint("Invalid Data Type")
    @SerializedName("category_id")
    val id: Int = 0,
    @SerializedName("value")
    val value: String = "",
    @SerializedName("detail")
    val detail: String = "",
    @SerializedName("additional_fields")
    val additionalField: List<OptionAdditionalField> = listOf(),
){
    data class Response(
        @SerializedName("visionGetReportVideoReason")
        val data: List<UserReportOptions> = emptyList()
    )

    data class OptionAdditionalField(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("label")
        val label: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("max")
        val max: Int = 0,
        @SerializedName("min")
        val min: Int = 0,
    )
}
