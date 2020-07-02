package com.tokopedia.youtube_common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentRating ( @SerializedName("ytRating")
                      @Expose
                      var ytRating: String? = null)
