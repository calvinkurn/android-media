package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Menu (

    @SerializedName("menuID")
    @Expose
    var menuID: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null

)
