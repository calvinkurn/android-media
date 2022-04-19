package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 04/05/21
 */

data class Picture(
        @SerializedName("url")
        @Expose
        var original: String? = null,
        @SerializedName("url200")
        @Expose
        var thumbnail: String? = null,
        @SerializedName("url100")
        @Expose
        var url100: String? = null
)