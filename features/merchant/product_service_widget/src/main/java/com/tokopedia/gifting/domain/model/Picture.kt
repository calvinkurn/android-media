package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Picture {
    @SerializedName("FilePath")
    @Expose
    var filePath: String? = null

    @SerializedName("FileName")
    @Expose
    var fileName: String? = null

    @SerializedName("URL")
    @Expose
    var url: String? = null

    @SerializedName("URL100")
    @Expose
    var url100: String? = null

    @SerializedName("URL200")
    @Expose
    var url200: String? = null
}