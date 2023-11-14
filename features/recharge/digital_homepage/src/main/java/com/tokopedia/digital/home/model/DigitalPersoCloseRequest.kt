package com.tokopedia.digital.home.model

import com.google.gson.annotations.SerializedName

data class DigitalPersoCloseRequest (
   @SerializedName("status")
   val status: Int = 2,
   @SerializedName("type")
   val type: String = "",
   @SerializedName("fav_id")
   val favId: String = ""
)
