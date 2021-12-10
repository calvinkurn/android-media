package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 09/12/21
 */
data class UserReportOptions(
    @SerializedName("category_id")
    val id: String = "",
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
