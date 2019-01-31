package com.tokopedia.feedcomponent.data.pojo.template

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateHeader
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle

/**
 * @author by yfsx on 04/12/18.
 */
data class Cardpost (
        @SerializedName("title")
    @Expose
    val title: TemplateTitle = TemplateTitle(),

        @SerializedName("header")
    @Expose
    val header: TemplateHeader = TemplateHeader(),

        @SerializedName("body")
    @Expose
    val body: TemplateBody = TemplateBody(),

        @SerializedName("footer")
    @Expose
    val footer: TemplateFooter = TemplateFooter()
)