package com.tokopedia.product.addedit.description.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SourceTypeRequestParam(
    @SerializedName("Squad")
    @Expose
    val squad: String = "",

    @SerializedName("Usecase")
    @Expose
    val usecase: String = ""
)
