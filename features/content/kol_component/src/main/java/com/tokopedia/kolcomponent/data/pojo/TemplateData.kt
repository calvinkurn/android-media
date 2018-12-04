package com.tokopedia.kolcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kolcomponent.data.pojo.template.Template

/**
 * @author by yfsx on 04/12/18.
 */
class TemplateData {
    @SerializedName("id")
    @Expose
    val id: Int = 0
    @SerializedName("type")
    @Expose
    val type: String? = null
    @SerializedName("template")
    @Expose
    val template: Template? = Template()

}