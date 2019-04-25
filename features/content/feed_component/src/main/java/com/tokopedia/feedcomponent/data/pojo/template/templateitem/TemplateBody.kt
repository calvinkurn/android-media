package com.tokopedia.feedcomponent.data.pojo.template.templateitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class TemplateBody (

    @SerializedName("media")
    @Expose
    val media: Boolean = false,
    @SerializedName("caption")
    @Expose
    val caption: Boolean = false,
    @SerializedName("postTag")
    @Expose
    val postTag: Boolean = false,
    @SerializedName("mediaGridButton")
    @Expose
    val mediaGridButton: Boolean = false
)