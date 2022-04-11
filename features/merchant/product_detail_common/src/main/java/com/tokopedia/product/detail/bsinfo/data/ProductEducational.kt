package com.tokopedia.product.detail.bsinfo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductEducational(
        @SerializedName("pdpGetEducationalBottomsheet")
        @Expose
        val response: ProductEducationalResponse = ProductEducationalResponse()
)

data class ProductEducationalResponse(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("icon")
        @Expose
        val icon: String = "",

        @SerializedName("buttons")
        @Expose
        val educationalButtons: List<EducationalButtons> = listOf()
)

data class EducationalButtons(
        @SerializedName("buttonTitle")
        @Expose
        val buttonTitle: String = "",

        @SerializedName("color")
        @Expose
        val buttonColor: String = "",

        @SerializedName("appLink")
        @Expose
        val buttonApplink: String = "",

        @SerializedName("webLink")
        @Expose
        val buttonWebLink: String = ""
)