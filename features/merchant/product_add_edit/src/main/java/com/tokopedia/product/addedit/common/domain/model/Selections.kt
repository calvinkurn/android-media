package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Selections (

    @SerializedName("variantID")
    @Expose
    var variantID: String? = null,
    @SerializedName("unitID")
    @Expose
    var unitID: String? = null,
    @SerializedName("options")
    @Expose
    var options: Options? = null

)
