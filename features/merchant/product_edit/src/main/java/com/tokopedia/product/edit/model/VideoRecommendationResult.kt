package com.tokopedia.product.edit.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 25/06/18.
 */

class VideoRecommendationResult {

    @SerializedName("videosearch")
    @Expose
    var videoSearch: VideoSearch? = null

}
