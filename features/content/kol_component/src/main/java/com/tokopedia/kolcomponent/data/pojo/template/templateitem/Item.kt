package com.tokopedia.kolcomponent.data.pojo.template.templateitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
class Item {
    @SerializedName("ctaLink")
    @Expose
    val ctaLink: Boolean = false
}