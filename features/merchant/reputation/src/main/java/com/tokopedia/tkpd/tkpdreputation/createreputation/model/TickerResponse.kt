package com.tokopedia.tkpd.tkpdreputation.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerResponse(
     @SerializedName("title")
     @Expose
     val title: String = "",

     @SerializedName("subtitle")
     @Expose
     val subtitle: String = ""
)