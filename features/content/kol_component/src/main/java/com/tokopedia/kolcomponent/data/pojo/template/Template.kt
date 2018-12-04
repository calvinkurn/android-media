package com.tokopedia.kolcomponent.data.pojo.template

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
class Template {

    @SerializedName("cardpost")
    @Expose
    val cardpost: Cardpost? = null
    @SerializedName("cardbanner")
    @Expose
    val cardbanner: Cardbanner? = null
    @SerializedName("cardrecom")
    @Expose
    val cardrecom: Cardrecom? = null
}