package com.tokopedia.feedcomponent.data.pojo.template

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.Item
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.Title

/**
 * @author by yfsx on 04/12/18.
 */
data class Cardrecom (
    @SerializedName("title")
    @Expose
    val title: Title = Title(),

    @SerializedName("item")
    @Expose
    val item: Item = Item()
)