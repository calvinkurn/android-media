package com.tokopedia.feedcomponent.data.pojo.template

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.Body
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.Footer
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.Header
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.Title

/**
 * @author by yfsx on 04/12/18.
 */
data class Cardpost (
    @SerializedName("title")
    @Expose
    val title: Title = Title(),

    @SerializedName("header")
    @Expose
    val header: Header = Header(),

    @SerializedName("body")
    @Expose
    val body: Body = Body(),

    @SerializedName("footer")
    @Expose
    val footer: Footer = Footer()
)