package com.tokopedia.kolcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
class Included {

    @SerializedName("template")
    @Expose
    val template: List<TemplateData>? = null
}