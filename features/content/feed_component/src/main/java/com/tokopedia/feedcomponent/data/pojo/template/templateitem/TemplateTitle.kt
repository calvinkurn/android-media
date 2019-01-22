package com.tokopedia.feedcomponent.data.pojo.template.templateitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class TemplateTitle (
    @SerializedName("text")
    @Expose
    val text: Boolean = false,
    @SerializedName("textBadge")
    @Expose
    val textBadge: Boolean = false,
    @SerializedName("ctaLink")
    @Expose
    val ctaLink: Boolean = false
)