package com.tokopedia.product.edit.domain.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentRating ( @SerializedName("ytRating")
                      @Expose
                      var ytRating: String? = null)
