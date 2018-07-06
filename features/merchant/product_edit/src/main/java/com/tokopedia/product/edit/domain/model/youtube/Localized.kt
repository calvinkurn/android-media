package com.tokopedia.product.edit.domain.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Localized(  @SerializedName("title")
                  @Expose
                  var title: String? = null,
                  @SerializedName("description")
@Expose
var description: String? = null)