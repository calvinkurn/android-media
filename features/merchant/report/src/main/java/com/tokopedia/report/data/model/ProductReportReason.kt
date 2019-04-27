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

        @SerializedName("detail")
        @Expose
        val detail: String = "",

        @SerializedName("value")
        @Expose
        val value: String = ""
): ParentItem<ProductReportReason> {

    override fun getChildList(): List<ProductReportReason> = children

    override val isInitiallyExpanded: Boolean = false

    data class AdditionalField(
            @SerializedName("detail")
            @Expose
            val detail: String = "",

            @SerializedName("key")
            @Expose
            val key: String = "",

            @SerializedName("Max")
            @Expose
            val max: Int = 0,

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("value")
            @Expose
            val value: String = ""
    )

    data class Response(
            @SerializedName("visionProductReportReason")
            @Expose
            val data: List<ProductReportReason> = listOf()
    )
}