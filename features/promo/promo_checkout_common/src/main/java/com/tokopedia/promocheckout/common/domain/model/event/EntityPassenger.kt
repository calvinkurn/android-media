package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EntityPassenger(
        @SerializedName("element_type")
        @Expose
        val elementType: String = "",
        @SerializedName("error_message")
        @Expose
        val errorMessage: String = "",
        @SerializedName("help_text")
        @Expose
        val helpText: String = "",
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("required")
        @Expose
        val required: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("validator_regex")
        @Expose
        val validatorRegex: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
)