package com.tokopedia.kolcomponent.data.pojo.template

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kolcomponent.data.pojo.template.templateitem.Title

/**
 * @author by yfsx on 04/12/18.
 */
data class Cardbanner (

    @SerializedName("title")
    @Expose
    val title: Title = Title()

)