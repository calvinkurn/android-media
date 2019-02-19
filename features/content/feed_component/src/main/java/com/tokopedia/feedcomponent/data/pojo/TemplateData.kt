package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.template.Template

/**
 * @author by yfsx on 04/12/18.
 */
data class TemplateData (
        
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("template")
    @Expose
    val template: Template = Template()
)