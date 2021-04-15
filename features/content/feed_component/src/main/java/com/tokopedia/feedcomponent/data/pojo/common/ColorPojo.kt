package com.tokopedia.feedcomponent.data.pojo.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 22/03/19.
 */
data class ColorPojo (
        @SerializedName("hex")
        @Expose
        val hex:String = "",

        @SerializedName("opacity")
        @Expose
        val opacity:String = "0"
){
}