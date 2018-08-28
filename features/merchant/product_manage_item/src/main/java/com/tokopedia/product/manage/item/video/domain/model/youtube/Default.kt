package com.tokopedia.product.manage.item.video.domain.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Default(@SerializedName("url")
                   @Expose
                   var url: String? = null,
                   @SerializedName("width")
                    @Expose
                    var width: Int = 0,
                    @SerializedName("height")
                    @Expose
                    var height: Int = 0)