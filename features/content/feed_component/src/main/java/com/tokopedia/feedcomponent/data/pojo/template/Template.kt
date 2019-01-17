package com.tokopedia.feedcomponent.data.pojo.template

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class Template(
        @SerializedName("cardpost")
        @Expose
        val cardpost: Cardpost = Cardpost(),
        @SerializedName("cardbanner")
        @Expose
        val cardbanner: Cardbanner = Cardbanner(),
        @SerializedName("cardrecom")
        @Expose
        val cardrecom: Cardrecom = Cardrecom()
)