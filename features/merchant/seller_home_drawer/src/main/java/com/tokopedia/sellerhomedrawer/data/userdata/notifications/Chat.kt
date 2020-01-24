package com.tokopedia.sellerhomedrawer.data.userdata.notifications

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Chat {

    @SerializedName("unreads")
    @Expose
    var unreads: Int? = null

}
