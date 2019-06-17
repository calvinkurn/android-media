package com.tokopedia.report.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.report.view.util.ParentItem

data class ProductReportReason(
        @SerializedName("category_id")
        @Expose
        val categoryId: Int = -1,

        @SerializedName("child")
        @Expose
        val children: List<ProductReportReason> = listOf(),

        @SerializedName("additional_fields")
        @Expose
        val additionalFields: List<AdditionalField> = listOf(),

        @SerializedName("additional_info")
        @Expose
        val additionalInfo: List<AdditionalInfo> = listOf(),

        @SerializedName("detail")
        @Expose
        val detail: String = "",

        @SerializedName("value")
        @Expose
        val value: String = ""
) {
        @Transient var parentLabel = ""

        val strLabel
                get() = (if (parentLabel.isEmpty()) value else "$parentLabel - $value").toLowerCase()

    data class AdditionalField(
            @SerializedName("detail")
            @Expose
            val detail: String = "",

            @SerializedName("key")
            @Expose
            val key: String = "",

            @SerializedName("max")
            @Expose
            val max: Int = 0,

            @SerializedName("min")
            @Expose
            val min: Int = 0,

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("label")
            @Expose
            val value: String = ""
    )

    data class AdditionalInfo(
            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("value")
            @Expose
            val value: String = ""
    )

    data class Response(
            @SerializedName("visionGetReportProductReason")
            @Expose
            val data: List<ProductReportReason> = listOf()
    )
}