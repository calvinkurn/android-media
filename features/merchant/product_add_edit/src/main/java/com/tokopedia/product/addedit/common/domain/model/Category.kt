package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category (

    @SerializedName("id")
    @Expose
    var id: String? = null

)
