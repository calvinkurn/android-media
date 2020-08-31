package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubConfig(
        @SerializedName("category_id")
        @Expose
        val categoryId: Int = 0,
        @SerializedName("child_category_ids")
        @Expose
        val childCategoryIds: String = "",
        @SerializedName("expiry_date")
        @Expose
        val expiryDate: String = "",
        @SerializedName("name")
        @Expose
        val name: String = ""
)