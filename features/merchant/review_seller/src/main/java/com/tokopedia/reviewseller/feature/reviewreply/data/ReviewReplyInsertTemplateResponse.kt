package com.tokopedia.reviewseller.feature.reviewreply.data

import com.google.gson.annotations.SerializedName

data class ReviewReplyInsertTemplateResponse(
        @SerializedName("insertResponseTemplate")
        val insertResponseTemplate: InsertResponseTemplate = InsertResponseTemplate()
) {
    data class InsertResponseTemplate(
            @SerializedName("success")
            val isSuccesss: Boolean = false,
            @SerializedName("data")
            val insertTemplateData: InsertTemplateData = InsertTemplateData(),
            @SerializedName("defaultTemplateID")
            val defaultTemplateID: Int? = 0,
            @SerializedName("error")
            val error: String? = ""
    ) {
        data class InsertTemplateData(
                @SerializedName("templateId")
                val templateId: Int? = 0,
                @SerializedName("title")
                val title: String? = "",
                @SerializedName("message")
                val message: String? = "",
                @SerializedName("status")
                val status: Int? = 0
        )
    }
}