package com.tokopedia.sellerhomedrawer.data.userdata.notifications

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Chat (

    @SerializedName("unreads")
    @Expose
    var unreads: Int? = 0

)
