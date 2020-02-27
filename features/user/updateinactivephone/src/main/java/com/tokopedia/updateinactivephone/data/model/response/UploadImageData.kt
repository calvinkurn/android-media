package com.tokopedia.updateinactivephone.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadImageData (
    /**
     * @return The picObj
     */
    /**
     * @param picObj The pic_obj
     */
    @SerializedName("pic_obj")
    @Expose
    var picObj: String? = "",
    /**
     * @return The picSrc
     */
    /**
     * @param picSrc The pic_src
     */
    @SerializedName("pic_src")
    @Expose
    var picSrc: String? = ""

)
