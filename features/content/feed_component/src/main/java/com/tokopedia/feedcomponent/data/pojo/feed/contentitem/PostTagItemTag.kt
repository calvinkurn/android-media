package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.common.ColorPojo

/**
 * @author by yfsx on 22/03/19.
 */
data class PostTagItemTag(
        @SerializedName("linkType")
        @Expose
        val linkType:String = "",
        @SerializedName("text")
        @Expose
        val text:String = "",
        @SerializedName("textColor")
        @Expose
        val textColor:ColorPojo = ColorPojo(),
        @SerializedName("bgColor")
        @Expose
        val bgColor:ColorPojo = ColorPojo()
) {}