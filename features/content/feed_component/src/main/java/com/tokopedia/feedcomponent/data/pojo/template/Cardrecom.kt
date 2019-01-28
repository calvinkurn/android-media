package com.tokopedia.feedcomponent.data.pojo.template

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateItem
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle

/**
 * @author by yfsx on 04/12/18.
 */
data class Cardrecom (
        @SerializedName("title")
    @Expose
    val title: TemplateTitle = TemplateTitle(),

        @SerializedName("item")
    @Expose
    val item: TemplateItem = TemplateItem()
)