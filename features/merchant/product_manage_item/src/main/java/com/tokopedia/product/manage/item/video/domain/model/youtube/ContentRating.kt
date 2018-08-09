package com.tokopedia.product.manage.item.video.domain.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentRating ( @SerializedName("ytRating")
                      @Expose
                      var ytRating: String? = null)
