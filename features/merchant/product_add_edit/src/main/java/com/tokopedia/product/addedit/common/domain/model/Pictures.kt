package com.tokopedia.product.addedit.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pictures (
    
    @SerializedName("picID")
    @Expose
    var picID: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("filePath")
    @Expose
    var filePath: String? = null,
    @SerializedName("fileName")
    @Expose
    var fileName: String? = null,
    @SerializedName("width")
    @Expose
    var width: Int? = null,
    @SerializedName("height")
    @Expose
    var height: Int? = null

)
