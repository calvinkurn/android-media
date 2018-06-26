package com.tokopedia.product.edit.model

/**
 * Created by hendry on 25/06/18.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideoRecommendationData {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null

}