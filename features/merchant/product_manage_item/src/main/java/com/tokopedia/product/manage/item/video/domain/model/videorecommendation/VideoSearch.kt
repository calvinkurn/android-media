package com.tokopedia.product.manage.item.domain.model.videorecommendation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 25/06/18.
 */

data class VideoSearch ( @SerializedName("data")
                    @Expose
                    var data: List<VideoRecommendationData>? = null,
                    @SerializedName("error")
                    @Expose
                    var error: Any? = null)
