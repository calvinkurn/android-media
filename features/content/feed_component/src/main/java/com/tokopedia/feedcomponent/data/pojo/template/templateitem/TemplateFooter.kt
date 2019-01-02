package com.tokopedia.feedcomponent.data.pojo.template.templateitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class TemplateFooter (

    @SerializedName("like")
    @Expose
    val like: Boolean = false,
    @SerializedName("comment")
    @Expose
    val comment: Boolean = false,
    @SerializedName("share")
    @Expose
    val share: Boolean = false,
    @SerializedName("ctaLink")
    @Expose
    val ctaLink: Boolean = false
)