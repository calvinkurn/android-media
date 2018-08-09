package com.tokopedia.product.manage.item.video.domain.model.videorecommendation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 25/06/18.
 */

data class VideoRecommendationResult ( @SerializedName("videosearch")
                                       @Expose
                                       var videoSearch: VideoSearch? = null)